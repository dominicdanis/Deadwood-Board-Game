/*
Scene

This class is designed to store information about a scene. It contains a method to
validate a roll when a player chooses to act

Dominic Danis 11/2/2021
*/

import java.util.ArrayList;

public class Scenes {
    private String sceneName;
    private ArrayList<Role> sceneRoles;
    private int budget;
    private boolean isFlipped;
    private int sceneNum;
    private String description;
    private String image;

    //constructor
    public Scenes(int num, String desc, String name, String pic, int budg, ArrayList<Role> roles){
        sceneNum = num;
        description = desc;
        sceneName = name;
        image = pic;
        budget = budg;
        sceneRoles = roles;
    }

    public boolean validateRoll(int roll){
        if(roll>=budget){
            return true;
        }
        else{
            return false;
        }
    }

    //getters
    public int getBudget(){
        return this.budget;
    }
    public boolean getFlipped(){
        return isFlipped;
    }
    public ArrayList<Role> getRoles(){
        return sceneRoles;
    }
    public int getNum(){
        return sceneNum;
    }
    public String getImage(){
        return image;
    }
}
