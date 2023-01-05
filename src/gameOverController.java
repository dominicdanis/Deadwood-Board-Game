/*
Game Over Controller

This class controls the ending game screen. It prints game results
and provides functionality to close program upon button press

Dominic Danis Created 11/30/2021
*/

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class gameOverController implements Initializable{
    
    @FXML Label finalStandings;
    @FXML Button closeButton;
    @FXML AnchorPane bigPane;
    static public gameOverController current;

    public void display(ArrayList<Player> standings){
        String endingText = "";
        for(int i=0; i<standings.size();i++){
            endingText += " " + Integer.toString(i+1) + " " + standings.get(i).getName();
        }
        finalStandings.setText(endingText);
    }
    public static gameOverController getController(){
        return current;
    }

    public void closeProgram(ActionEvent event)throws IOException{
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        current = this;
        bigPane.setStyle("-fx-background-color: #F6BE8F");
    }
}
