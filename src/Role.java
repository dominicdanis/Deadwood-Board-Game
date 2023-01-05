/*
Role

This class contains attributes about roles in deadwood. Instances of role are initialized by ParseXML upon startup.

Dominic Danis 11/2/2021
*/

import java.util.ArrayList;

public class Role {
    private int requiredRank;
    private String roleName;
    private boolean onCard;
    private boolean isAvailable;
    private ArrayList<Integer> dimensions;
    private String line;

    //constructor
    public Role(String name, int rank, ArrayList<Integer> dim, String pLine){
        this.roleName = name;
        this.requiredRank = rank;
        this.dimensions = dim;
        this.line = pLine;
        this.isAvailable = true;
    }
    public Role(String name, int rank, ArrayList<Integer> dim, String pLine, Boolean card){
        this.roleName = name;
        this.requiredRank = rank;
        this.dimensions = dim;
        this.line = pLine;
        this.isAvailable = true;
        this.onCard = card;
    }

    //setters
    public void setAvailable(boolean set){
        isAvailable = set;
    }

    //getters
    public boolean getAvail(){
        return isAvailable;
    }
    public int getRank(){
        return requiredRank;
    }
    public boolean getCardStatus(){
        return onCard;
    }
    public String getRoleName(){
        return this.roleName;
    }
    public String getLine(){
        return this.line;
    }
    public ArrayList<Integer> getDimensions(){
        return dimensions;
    }
}
