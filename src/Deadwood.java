/*
Deadwood

This class contains the main method for playing Deadwood. This is an acting board game implemented in
a text based version. Rules and game explination can be found in Deadwood-Free-Edition-Rules.pdf

Dominic Danis 11/2/2021
Last Edit: 11/30/2021
*/

import org.w3c.dom.Document;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.util.ArrayList;

public class Deadwood extends Application{

    public static Game current;
    public static void main(String[] args){
        Document doc = null;
        ParseXML parsing = new ParseXML();
        ArrayList<Location> gameLocations = new ArrayList<Location>();
        ArrayList<Scenes> gameScenes = new ArrayList<Scenes>();
        try{
           doc = parsing.getDocFromFile("cards.xml");
           //returns arraylist with all scenes, with subclasses implemented
           gameScenes = parsing.readCardData(doc);
           doc = parsing.getDocFromFile("board.xml");
           //returns arraylist with all Locations, with subclasses implemented
           gameLocations = parsing.readBoardData(doc);

        }catch (Exception e){
           System.out.println("Error = "+e);
        }
        //play the game
        current = Game.createGame(gameScenes,gameLocations);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("gameBegin.fxml"));
        primaryStage.setTitle("Deadwood");
        Image image = new Image("images/DeadwoodCover.png");
        Scene currentScene = new Scene(root, image.getWidth(), image.getHeight()+150); //With space for player menu
        primaryStage.setScene(currentScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
