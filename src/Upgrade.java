/*
Upgrade
This class is designed to hold information about upgrades in Deadwood. In the text based version
the area is not used

Dominic Danis 11/2/2021
*/

import java.util.ArrayList;

public class Upgrade {
    private int rank;
    private String currency;
    private int curAmount;
    private ArrayList<Integer> area;

    //constructors
    public Upgrade(int level, String cur, ArrayList<Integer> dims, int amt){
        rank = level;
        currency = cur;
        area = dims;
        curAmount = amt;
    }
    public Upgrade(){}

    //setters
    public String getCurrency(){
        return currency;
    }
    public int getCurAmount(){
        return curAmount;
    }
    public int getRank(){
        return rank;
    }
}
