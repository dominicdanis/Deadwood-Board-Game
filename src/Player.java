/*
Player

This class contains attributes about each player in Deadwood. Players are created upon a user input for amount of players. This class contains functions for each action a player may take on their turn, as well as a few helper functions

Dominic Danis 11/2/2021
Last Edit: 11/30/2021
*/

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Player {
    private Location CurrentLocation;
    private int credits;
    private int money;
    private int rank;
    private Role currentRole;
    private int finalStanding;
    private int chips;
    private String name;
    private ArrayList<String> turnOptions = new ArrayList<String>();
    private String[] imageSet = new String[6];

    //constructor
    public Player(Location init, int cred, int cash, int level, String color, String[] images){
        this.CurrentLocation = init;
        credits = cred;
        money = cash;
        rank = level;
        currentRole = null;
        name = color;
        chips = 0;
        imageSet = images;
    }
   
    //turn functions
    public ArrayList<String> getValidLocations(){
        ArrayList<String> returnLocations = new ArrayList<String>();
        for(int i=0;i<CurrentLocation.getNeighbors().size();i++){
            returnLocations.add(CurrentLocation.getNeighbors().get(i));
        }
        return returnLocations;
    }
    public void finishMove(Location choice){
        for(int i=0; i<CurrentLocation.getOccupants().size();i++){
            if(CurrentLocation.getOccupants().get(i).equals(this)){
                CurrentLocation.getOccupants().remove(i);
            }
        }
        CurrentLocation = choice;
        if(CurrentLocation.hasScene()){
            if(!CurrentLocation.getScene().getFlipped()){
                GameController.getController().flipCard(CurrentLocation);
            }
        }
        CurrentLocation.addOccupants(this);
        for(int i =0; i< turnOptions.size();i++){
            if(turnOptions.get(i).equals("move")){
                turnOptions.remove(i);
            }
        }
        for(int i =0; i< turnOptions.size();i++){
            if(turnOptions.get(i).equals("take role")){
                turnOptions.remove(i);
            }
        }
        for(int i = 0; i<turnOptions.size(); i++){
            if(turnOptions.get(i).equals("upgrade")){
                turnOptions.remove(i);
            }  
        }   
        if(CurrentLocation.availRoles(this)){                           //set options for next turn
            turnOptions.add("take role");
        }
        if(CurrentLocation.getSetName().equals("office")){
            turnOptions.add("upgrade");
        }
        GameController.getController().updatePlayerPosition(this);
        GameController.getController().updateTurnOption(this.turnOptions);
    }
    public void updateRoles(String selection){
        ArrayList<Role> validRoles = CurrentLocation.generateRoles(this);
        for(int i =0; i<validRoles.size();i++){                                 //give role
            if(validRoles.get(i).getRoleName().equals(selection)){
                setRole(validRoles.get(i));
                currentRole.setAvailable(false);
            }
        }
        GameController.getController().updatePlayerRole(this);
        turnOptions.clear();
        turnOptions.add("end turn");
        GameController.getController().updateTurnOption(this.turnOptions);
    }
    public void act(){
        GameController controller = GameController.getController();
        int roll = roll();
        boolean validRoll = CurrentLocation.getScene().validateRoll(roll+ chips);     //is acting successful?
        controller.updateRollLabel(validRoll, roll, currentRole);
        if(validRoll){                                                                  //if successful payout
            if(currentRole.getCardStatus()){
                CurrentLocation.downCounter();
                addCredits(2);
            }
            else{
                CurrentLocation.downCounter();
                addCredits(1);
                addMoney(1);
            }
        }
        else if(!validRoll){                                                            //not successful pay off card
            if(!currentRole.getCardStatus()){
                addMoney(1);
            }
        }
        controller.updateTakes(CurrentLocation.getCounter(), CurrentLocation);
        if(CurrentLocation.getCounter()==0){                                            //when scene ends pay players
            controller.endOfSceneMessage();
            controller.clearLocationCard(CurrentLocation);
            ArrayList<Integer> payments= new ArrayList<Integer>();
            for(int i=0;i<CurrentLocation.getScene().getBudget();i++){                  //populate payment amounts
                payments.add(roll());
            }
            PaymentManager manager = new PaymentManager();
            manager.payPlayers(CurrentLocation, payments);
            for(int i=0; i<CurrentLocation.getOccupants().size();i++){                  //reset player and location        
                CurrentLocation.getOccupants().get(i).setRole(null);
                CurrentLocation.getOccupants().get(i).setChips(0);;
            }
            CurrentLocation.removeScene();
            GameController.getController().wrapScene(CurrentLocation);
            CurrentLocation.setScene(null);
        }
        turnOptions.clear();
        turnOptions.add("end turn");
        controller.updateTurnOption(turnOptions);
        controller.switchPlayerView(this);
    }
    public void rehearse(){
        addChip();
        GameController.getController().updateRehearseLabel(chips, CurrentLocation.getScene().getBudget());
        turnOptions.clear();
        turnOptions.add("end turn");
        GameController.getController().updateTurnOption(this.turnOptions);
        GameController.getController().switchPlayerView(this);
    }
    public void upgrade(ArrayList<String> choices){

        PaymentManager pay = new PaymentManager();
        GameController controller = GameController.getController();
        boolean isValid = pay.validatePurchase(this, choices, CurrentLocation.getUpgrades());       //validate purchase
        if(!isValid){
            GameController.getController().sendPurchaseFail();
            return;
        }
        Upgrade chosen = new Upgrade();                 
        for(int i=0;i<CurrentLocation.getUpgrades().size();i++){
            if(CurrentLocation.getUpgrades().get(i).getCurrency().equals(choices.get(1)) && CurrentLocation.getUpgrades().get(i).getRank()==Integer.parseInt(choices.get(0))){
                chosen = CurrentLocation.getUpgrades().get(i);
            }
        }
        if(chosen.getCurrency().equals("dollar")){                                                //charge player
            takeMoney(chosen.getCurAmount());
        }
        else{
            takeCredits(chosen.getCurAmount());
        }
        setRank(chosen.getRank());                                                                //award upgrade
        for(int i=0; i<turnOptions.size();i++){
            if(turnOptions.get(i).equals("upgrade")){
                turnOptions.remove(i);
            }
        }
        controller.sendPurcahseSuccess();
        controller.updateTurnOption(turnOptions);
        controller.updatePlayerRankings(this);
        controller.clearUpdateMenu();
        controller.switchPlayerView(this);
    }
    public int roll(){
        return ThreadLocalRandom.current().nextInt(1,7);
    }


    //mutators
    public void addFinalStanding(int add){
        finalStanding+=add;
    }
    public void setOptions(){
        turnOptions.clear();
        turnOptions.add("end turn");
        turnOptions.add("show info");
        turnOptions.add("location");
        if(currentRole!=null){
            turnOptions.add("act");
            if(CurrentLocation.validateRehearsal(chips)){
                turnOptions.add("rehearse");
            }
        }
        if(CurrentLocation.getSetName().equals("office")){
            turnOptions.add("upgrade");
        }
        if(currentRole==null){
            turnOptions.add("move");
        }
        if(CurrentLocation.availRoles(this) && currentRole==null){
            turnOptions.add("take role");
        }
    }
    public void takeMoney(int take){
        money-=take;
    }
    public void takeCredits(int take){
        credits-=take;
    }
    public void setLocation(Location moved){
        CurrentLocation = moved;
    }
    public void addChip(){
        chips++;
    }
    public void setRole(Role set){
        currentRole = set;
    }
    public void setRank(int set){
        rank = set;
    }
    public void addMoney(int add){
        money+=add;
    }
    public void addCredits(int add){
        credits += add;
    }
    public void setChips(int num){
        chips = num;
    }

    //getters
    public ArrayList<String> getOpts(){
        return turnOptions;
    }
    public int getChips(){
        return this.chips;
    }
    public Location getLocation(){
        return this.CurrentLocation;
    }
    public int getCredits(){
        return this.credits;
    }
    public int getMoney(){
        return this.money;
    }
    public int getRank(){
        return this.rank;
    }
    public Role getRole(){
        return this.currentRole;
    }
    public String getName(){
        return this.name;
    }
    public int getStanding(){
        return finalStanding;
    }
}
