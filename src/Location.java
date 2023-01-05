/*
Location

This class contains all of the locations in Deadwood. Notice that it's fields contain instances of our other classes.
Locations are initialized upon startup by ParseXML. 

Dominic Danis Created 11/3/2021
Last edit 11/30/2021
*/

import java.util.ArrayList;

public class Location {
    private Scenes currentScene;
    private ArrayList<String> neighbors;
    private String setName;
    private int counter;
    private int defaultCounter;
    private ArrayList<Role> roles = new ArrayList<Role>();
    private ArrayList<Player> occupants = new ArrayList<Player>();
    private ArrayList<Integer> area;
    private ArrayList<Take> takes;
    private ArrayList<Upgrade> upgrades;

    //constructors
    public Location(ArrayList<String> adj, String name, ArrayList<Take> shots, ArrayList<Role> parts, ArrayList<Integer> dims){
        neighbors = adj;
        setName = name;
        takes = shots;
        roles = parts;
        area = dims;
        counter = takes.size();
        defaultCounter = counter;
    }
    public Location(ArrayList<String> adj, String name, ArrayList<Integer> dims){
        neighbors = adj;
        setName = name;
        area = dims;
    }
    public Location(String name, ArrayList<String> adj, ArrayList<Integer> dims, ArrayList<Upgrade> ups){
        setName = name;
        neighbors = adj;
        area = dims;
        upgrades = ups;
    }


    //getters
    public ArrayList<Role> getRoles(){
        return roles;
    }
    public ArrayList<Player> getOccupants(){
        return occupants;
    }
    public ArrayList<String> getNeighbors(){
        return neighbors;
    }
    public int getCounter(){
        return counter;
    }
    public Scenes getScene(){
        return currentScene;
    }
    public String getSetName(){
        return this.setName;
    }
    public ArrayList<Upgrade> getUpgrades(){
        return upgrades;
    }
    public boolean hasScene(){
        boolean retVar;
        if(this.currentScene!=null){
            retVar = true;
        }
        else{
            retVar = false;
        }
        return retVar;
    }
    public ArrayList<Integer> getArea(){
        return this.area;
    }
    public ArrayList<Take> getTakes(){
        return takes;
    }
    //mutators
    public void addOccupants(Player current){
        occupants.add(current);
    }
    public void addSceneRoles(Scenes curSce){
        currentScene = curSce;
        for(int i=0;i<curSce.getRoles().size();i++){
            roles.add(curSce.getRoles().get(i));
        }
    }
    public void setScene(Scenes current){
        this.currentScene = current;
        if(currentScene!=null){
            for(int i=0;i<currentScene.getRoles().size();i++){
                roles.add(currentScene.getRoles().get(i));
            }
        }
    }
    public void removeScene(){
        if(currentScene!=null){
            for(int i=0;i<roles.size();i++){
                for(int j=0;j<currentScene.getRoles().size();j++){
                    if(roles.get(i).equals(currentScene.getRoles().get(j))){
                        roles.remove(i);
                    }
                }
            }
        }
    }
    public void downCounter(){
        counter--;
    }
    public void resetCounter(){
        counter = defaultCounter;
    }

    public boolean validateMove(Location old, Location updated){
        boolean isAdj = false;
        for(int i=0; i<old.neighbors.size(); i++){
            if(old.neighbors.get(i).equals(updated.setName)){
                isAdj = true;
            }
        }
        return isAdj;
    }
    public boolean validateRehearsal(int chips){
        boolean isValid = true;
        if(chips>=this.currentScene.getBudget()){
            isValid = false;
        }
        return isValid;
    }
    public ArrayList<Role> generateRoles(Player current){
        ArrayList<Role> validRoles = new ArrayList<Role>();
        for(int i =0; i<roles.size();i++){
            if(roles.get(i).getRank()<=current.getRank() && roles.get(i).getAvail()){
                validRoles.add(roles.get(i));
            }
        }
        return validRoles;
    }
    public boolean availRoles(Player current){
        for(int i=0; i<roles.size();i++){
            if(roles.get(i).getAvail() && (roles.get(i).getRank()<=current.getRank() && currentScene!=null)){
                return true;
            }
        }
        return false;
    }
}
