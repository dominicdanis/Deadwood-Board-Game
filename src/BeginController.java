/*
Begin Controller
This class controls the FXML for the starting window and will launch the game
when user has chosen the amount of players they would like

Dominic Danis Created 11/30/2021
*/


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//we may want many controllers for different things

public class BeginController implements Initializable{
    
    @FXML HBox buttonBox;
    @FXML VBox selectBox;
    @FXML AnchorPane lowerHalf;
    @FXML ImageView gameImage;
    @FXML SplitPane bigPane;


    public void startGame(ActionEvent event)throws IOException{
        Button clicked = (Button)event.getSource();
        int players = 0;
        try {
            players = Integer.parseInt(clicked.getText());
        } catch (Exception e) {
            System.exit(0);
        }
        Deadwood.current.initializeGame(players);
        Parent gameBoardParent = FXMLLoader.load(getClass().getResource("gameDisplay.fxml"));
        Scene gameScene = new Scene(gameBoardParent,1518.6666259765625,1017.3333129882812);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(gameScene);
        window.setResizable(false);
        window.setMaximized(true);
        window.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bigPane.setStyle("-fx-background-color: #F6BE8F");
    }


}
