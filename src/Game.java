/*
Game

This class contains all of the data related to Deadwood. It controls the flow of the game, ends days, resets days,
initializes the game and calculates final standings.

Dominic Danis 11/2/2021
Last Edit: 11/30/2021
*/

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Game {
    private ArrayList<Player> playerOrder = new ArrayList<Player>(); 
    private static Game current;
    private int days;
    private ArrayList<Scenes> remainingScenes;
    private ArrayList<Location> boardLocations;
    private String[][] colorArray = new String[][]{
    {"blue", "cyan", "green", "orange", "pink", "red", "violet", "white"},
    {"b1.png","c1.png","g1.png","o1.png","p1.png","r1.png","v1.png","w1.png"},
    {"b2.png","c2.png","g2.png","o2.png","p2.png","r2.png","v2.png","w2.png"},
    {"b3.png","c3.png","g3.png","o3.png","p3.png","r3.png","v3.png","w3.png"},
    {"b4.png","c4.png","g4.png","o4.png","p4.png","r4.png","v4.png","w4.png"},
    {"b5.png","c5.png","g5.png","o5.png","p5.png","r5.png","v5.png","w5.png"},
    {"b6.png","c6.png","g6.png","o6.png","p6.png","r6.png","v6.png","w6.png"}};

    //allows only 1 creation of game object
    public static Game createGame(ArrayList<Scenes> gameScenes, ArrayList<Location> gameLocations){
        if(current == null){
            current = new Game(gameScenes, gameLocations);
        }
        return current;
    }
    
    //Constructor
    private Game(ArrayList<Scenes> gameScenes, ArrayList<Location> gameLocations){
        this.remainingScenes = gameScenes;
        this.boardLocations = gameLocations;
    }

    public void initializeGame(int players){
        Location trailer = findTrailer();
        Collections.shuffle(remainingScenes);
        for(int i = 0; i<players; i++){                                     //set players initialization based on number
            if(players==3 || players == 4 || players == 2){
                Player initPlayer = new Player(trailer, 0, 0, 1, colorArray[0][i], getColorColumn(colorArray,colorArray[0][i] ));
                playerOrder.add(initPlayer);
            }
            else if(players==5){
                Player initPlayer = new Player(trailer, 2, 0, 1, colorArray[0][i], getColorColumn(colorArray,colorArray[0][i] ));
                playerOrder.add(initPlayer);
            }
            else if(players==6){
                Player initPlayer = new Player(trailer, 4, 0, 1, colorArray[0][i], getColorColumn(colorArray,colorArray[0][i] ));
                playerOrder.add(initPlayer);
            }
            else if(players==7 || players==8){
                Player initPlayer = new Player(trailer, 0, 0, 2, colorArray[0][i], getColorColumn(colorArray,colorArray[0][i] ));
                playerOrder.add(initPlayer);
            }
            else{}
        }
        if(players==2 || players==3){
            days = 3;
        }
        else{
            days = 4;
        }
        playerOrder.get(0).setOptions();
        placeScenes();

    }
    private Location findTrailer(){
        for(int i = 0; i<boardLocations.size(); i++){
            if(boardLocations.get(i).getSetName().equals("trailer")){
                return boardLocations.get(i);
            }
        }
        return null;
    }
    public void placeScenes(){
        for(int i=0; i<boardLocations.size();i++){
            if(!boardLocations.get(i).getSetName().equals("trailer") && !boardLocations.get(i).getSetName().equals("office")){
                boardLocations.get(i).setScene(remainingScenes.get(0));
                remainingScenes.remove(0);
            }
        }
        //GameController.getController().setCardLocations(this.boardLocations);
    }
    public void endOfTurn(){
        GameController controller = GameController.getController();
        playerOrder.add(playerOrder.get(0));                        //set the next player to their turn
        playerOrder.remove(0);
        if(dayIsOver()){                                            //check for end of day
            days--;
            if(days>0){
                endDay();
                resetDay();
            }
            else{
                try {
                    GameController.getController().switchToEnd();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                calcStandings();
                gameOverController.getController().display(playerOrder);
            }
        }
        else{
            controller.clearGameComm();
        }
        playerOrder.get(0).setOptions();
        controller.clearTurnMenu();
        controller.updateTurnOption(playerOrder.get(0).getOpts());
        controller.switchPlayerView(playerOrder.get(0));
        
    }
    private void endDay(){
        for(int i=0;i<playerOrder.size();i++){
            playerOrder.get(i).setLocation(boardLocations.get(10));             //set back to trailers
            playerOrder.get(i).setRole(null);                                   //remove all roles
        }
        GameController.getController().resetDay(days);
    }
    private void resetDay(){
        placeScenes();
        for(int i=0; i<boardLocations.size();i++){
            boardLocations.get(i).resetCounter();
        }
    }
    private void calcStandings(){
        for(int i=0;i<playerOrder.size();i++){
            playerOrder.get(i).addFinalStanding((playerOrder.get(i).getCredits()+playerOrder.get(i).getMoney()+(playerOrder.get(i).getRank()*5)));
        }
        for(int i=0;i<playerOrder.size();i++){
            int j=i;
            while(j>0 && playerOrder.get(j).getStanding()> playerOrder.get(j-1).getStanding()){
                Player temp = playerOrder.get(j-1);
                playerOrder.set(j-1, playerOrder.get(j));
                playerOrder.set(j, temp);
                j--;
            }
        }
    }

    private boolean dayIsOver(){
        boolean over;
        int counter = 0;
        for(int i=0; i<boardLocations.size();i++){
            if(boardLocations.get(i).hasScene()){
                counter++;
            }
            else{}
        }
        if(counter==1){
            over = true;
        }
        else{
            over=false;
        }
        return over;
    }

    private String[] getColorColumn(String[][]images, String color){
        int column = 0;
        for(int j=0;j<images[0].length;j++){
            if(images[0][j].equals(color)){
                column = j;
            }
        }
        String[] returnCol = new String[6];
        for(int i=1;i<7;i++){
            returnCol[i-1] = images[i][column];
        }
        return returnCol;
    }

    //mutators
    public void setDays(int dayNum){
        days = dayNum;
    }
    public void setPlayerOrder(Player next){
        this.playerOrder.add(next);
    }
    //getters
    public String[][] getColorArray(){
        return colorArray;
    }
    public ArrayList<Location> getLocations(){
        return boardLocations;
    }
    public ArrayList<Player> getPlayers(){
        return playerOrder;
    }
    public ArrayList<Scenes> getScenes(){
        return remainingScenes;
    }
    public int getDay(){
        return days;
    }
    public static Game getGame(){
        return current;
    }
}
