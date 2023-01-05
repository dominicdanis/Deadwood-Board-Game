/*
Take

This class is designed to hold information about Takes in Deadwood. In the text based version this class is
not used

Dominic Danis 11/2/2021
*/

import java.util.ArrayList;

public class Take {
    private int takeNum;
    private ArrayList<Integer> dimensions;

    //constructor
    public Take(int take, ArrayList<Integer> dims){
        takeNum = take;
        dimensions = dims;
    }

    public ArrayList<Integer> getDims(){
        return dimensions;
    }
}
