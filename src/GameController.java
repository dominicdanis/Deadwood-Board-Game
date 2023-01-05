/*
Game Controller

This class contains the main controller for updating and initializing the
FXML of the board game gameplay displays. It has a static instance of itself 
that is accesed and used for updating display components

Dominic Danis Created 11/30/2021
*/

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameController implements Initializable{
    static GameController controller;
    //Dice Player Pieces
    @FXML ImageView blue,cyan,green,orange,pink,violet,white,red;
    ArrayList<ImageView> playerDice;
    //Move Menu
    @FXML Button moveButton,actButton,rehearseButton,takeRoleButton,upgradeButton,endTurnButton;
    ArrayList<Button> turnOptions;
    //set Holders
    @FXML ImageView churchArea,hotelArea,mainStreetArea,jailArea,generalStoreArea,ranchArea,bankArea,saloonArea,secretHideoutArea,trainStationArea;
    ArrayList<ImageView> areaHolders;
    //Take Areas
    @FXML ImageView churchTake1,churchTake2,hotelTake1,hotelTake2,hotelTake3,mainTake1,mainTake2,mainTake3,jailTake1,generalTake1,generalTake2,ranchTake1,ranchTake2,bankTake1,saloonTake1,saloonTake2,secretTake1,secretTake2,secretTake3,trainTake1,trainTake2,trainTake3;
    ArrayList<ImageView> churchTakes, hotelTakes, mainTakes,jailTakes,generalTakes,ranchTakes,bankTakes,saloonTakes,secretTakes,trainTakes;
    //Move related items
    @FXML Button confirmButton;
    @FXML ChoiceBox<String> moveChoice;
    //Take role items
    @FXML ChoiceBox<String> roleChoice;
    @FXML Button confirmRole;
    //Game Information
    @FXML Label currentDayLabel;
    //Table Data
    @FXML Label rehearsePoints,moneyInTable,creditsInTable;
    @FXML ImageView activePieceTable;
    //Upgrading
    @FXML Button creditBuy, dollarBuy, cancelPurchase;
    @FXML ChoiceBox<String> upgradeChoices;
    //Game Communications
    @FXML Label gameCommunications;
    //Board Holder
    @FXML Pane boardHolder;
    //Ratio
    double ratio;
 
    //Runs upon startup
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = this;
        populateTakeList();
        populateDiceList();
        populateButtonList();
        populateAreaList();
        createHolders(Game.getGame().getLocations());
        displayActive();
        resetPlayerPos();
        initUpgradeChoice();
        initGameView();
    }

    //Getter for other classes - Singleton
    public static GameController getController(){
        return controller;
    }

    //Startup List Makers
    private void populateTakeList(){
        churchTakes = new ArrayList<>(Arrays.asList(churchTake2,churchTake1));
        hotelTakes = new ArrayList<>(Arrays.asList(hotelTake3,hotelTake2,hotelTake1));
        mainTakes = new ArrayList<>(Arrays.asList(mainTake3,mainTake2,mainTake1));
        jailTakes = new ArrayList<>(Arrays.asList(jailTake1));
        generalTakes = new ArrayList<>(Arrays.asList(generalTake2,generalTake1));
        ranchTakes = new ArrayList<>(Arrays.asList(ranchTake2,ranchTake1));
        bankTakes = new ArrayList<>(Arrays.asList(bankTake1));
        saloonTakes = new ArrayList<>(Arrays.asList(saloonTake2,saloonTake1));
        secretTakes = new ArrayList<>(Arrays.asList(secretTake3,secretTake2,secretTake1));
        trainTakes = new ArrayList<>(Arrays.asList(trainTake3,trainTake2,trainTake1));
    }
    private void createHolders(ArrayList<Location> locations){
        for(int i=0; i<locations.size();i++){
            if(locations.get(i).getSetName().equals("Train Station")){
                setHolderAreas(locations.get(i), trainStationArea);
                setTakeArea(locations.get(i).getTakes(),trainTakes);
            }
            else if(locations.get(i).getSetName().equals("Secret Hideout")){
                setHolderAreas(locations.get(i), secretHideoutArea);
                setTakeArea(locations.get(i).getTakes(),secretTakes);
                secretTake1.setVisible(true);
            }
            else if(locations.get(i).getSetName().equals("Church")){
                setHolderAreas(locations.get(i), churchArea);
                setTakeArea(locations.get(i).getTakes(),churchTakes);
            }
            else if(locations.get(i).getSetName().equals("Hotel")){
                setHolderAreas(locations.get(i), hotelArea);
                setTakeArea(locations.get(i).getTakes(),hotelTakes);
            }
            else if(locations.get(i).getSetName().equals("Main Street")){
                setHolderAreas(locations.get(i), mainStreetArea);
                setTakeArea(locations.get(i).getTakes(),mainTakes);
            }
            else if(locations.get(i).getSetName().equals("Jail")){
                setHolderAreas(locations.get(i), jailArea);
                setTakeArea(locations.get(i).getTakes(),jailTakes);
            }
            else if(locations.get(i).getSetName().equals("General Store")){
                setHolderAreas(locations.get(i), generalStoreArea);
                setTakeArea(locations.get(i).getTakes(),generalTakes);
            }
            else if(locations.get(i).getSetName().equals("Ranch")){
                setHolderAreas(locations.get(i), ranchArea);
                setTakeArea(locations.get(i).getTakes(),ranchTakes);
            }
            else if(locations.get(i).getSetName().equals("Bank")){
                setHolderAreas(locations.get(i), bankArea);
                setTakeArea(locations.get(i).getTakes(),bankTakes);
            }
            else if(locations.get(i).getSetName().equals("Saloon")){
                setHolderAreas(locations.get(i), saloonArea);
                setTakeArea(locations.get(i).getTakes(),saloonTakes);
            }
            else{}
        }
    }
    private void populateDiceList(){
        playerDice = new ArrayList<>(
            Arrays.asList(blue, cyan, green, orange, pink, violet, white, red)
        );
    }
    private void populateButtonList(){
        turnOptions = new ArrayList<>(
            Arrays.asList(moveButton,actButton,rehearseButton,takeRoleButton,upgradeButton, endTurnButton)
        );
    }
    private void populateAreaList(){
        areaHolders = new ArrayList<>(
            Arrays.asList(churchArea,hotelArea,mainStreetArea,jailArea,generalStoreArea,ranchArea,bankArea,saloonArea,secretHideoutArea,trainStationArea)
        );
    }

    //Startup Initializers
    private void resetPlayerPos(){
        double xPos = 995;
        double yPos = 285;
        for(int i=0; i<playerDice.size();i++){
            playerDice.get(i).setLayoutX(xPos);
            playerDice.get(i).setLayoutY(yPos);
            xPos +=45;
            if(i==3){                       //Make all dice fit
                yPos+=50;
                xPos=995;
            }
        }
    }
    private void displayActive(){
        int players = Deadwood.current.getPlayers().size();
        for(int i=0;i<playerDice.size();i++){
            if(i<players){
                playerDice.get(i).setVisible(true);
            }
            else{
                playerDice.get(i).setVisible(false);
            }
        }
        for(int i=0;i<playerDice.size();i++){
            if(!playerDice.get(i).isVisible()){
                playerDice.remove(i);
                i--;
            }
        }
    }
    private void setTakeArea(ArrayList<Take> takes, ArrayList<ImageView> takeBox){
        for(int i=0;i<takes.size();i++){
            takeBox.get(i).setLayoutX(takes.get(i).getDims().get(0));
            takeBox.get(i).setLayoutY(takes.get(i).getDims().get(1));
            takeBox.get(i).setFitHeight(takes.get(i).getDims().get(2));
            takeBox.get(i).setFitWidth(takes.get(i).getDims().get(3));
        }
    }
    private void setHolderAreas(Location place, ImageView view){
        ArrayList<Integer> area = place.getArea();
        view.setLayoutX(area.get(0));
        view.setLayoutY(area.get(1));
    }
    private void initUpgradeChoice(){
        upgradeChoices.getItems().addAll("2","3","4","5","6");
        upgradeChoices.setValue("2");
    }
    private void initGameView(){
        //First player turn option
        updateTurnOption(Deadwood.current.getPlayers().get(0).getOpts());
        updateCurrentDay(Deadwood.current.getDay());
        //Display proper dice images
        for(int i=0;i<Deadwood.current.getPlayers().size();i++){
            updatePlayerRankings(Deadwood.current.getPlayers().get(i));
        }
        //First player stats
        switchPlayerView(Deadwood.current.getPlayers().get(0));
        Image background = new Image("images/board.jpg");
        //Styling for board image
        BackgroundImage image = new BackgroundImage(background,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,null,new BackgroundSize(background.getWidth(), background.getHeight(), false, false, false, false));
        boardHolder.setBackground(new Background(image));
        for(int i=0;i<secretTakes.size();i++){
            secretTakes.get(i).setVisible(false);
        }

    }

    //Associates - find the FXML element from an Object
    private ArrayList<ImageView> associateTakes(Location place){
        switch(place.getSetName()){
            case "Train Station":
                return trainTakes;
            case "Jail":
                return jailTakes;
            case "General Store":
                return generalTakes;
            case "Secret Hideout":
                return secretTakes;
            case "Church":
                return churchTakes;
            case "Ranch":
                return ranchTakes;
            case "Bank":
                return bankTakes;
            case "Hotel":
                return hotelTakes;
            case "Saloon":
                return saloonTakes;
            case "Main Street":
                return mainTakes;
            default:
                return null;
        }
    }
    private ImageView associateLocation(Location current){
        switch(current.getSetName()){
            case "Train Station":
                return trainStationArea;
            case "Jail":
                return jailArea;
            case "General Store":
                return generalStoreArea;
            case "Secret Hideout":
                return secretHideoutArea;
            case "Church":
                return churchArea;
            case "Ranch":
                return ranchArea;
            case "Bank":
                return bankArea;
            case "Hotel":
                return hotelArea;
            case "Saloon":
                return saloonArea;
            case "Main Street":
                return mainStreetArea;
            default:
                return null;
        }
    }
    private ImageView associatePlayer(Player current){
        switch(current.getName()){
            case "blue":
                return blue;
            case "cyan":
                return cyan;
            case "green":
                return green;
            case "orange":
                return orange;
            case "pink":
                return pink; 
            case "violet":
                return violet;
            case "white":
                return white;
            case "red":
                return red;
            default:
                return null;
        }
    }

    //Bigger functions
    public void resetDay(int day){
        dayOverMessage();
        updateCurrentDay(day);
        resetPlayerPos();
        resetCardImages();
    }

    //Update Display Information
    public void updateTakes(int takeNum, Location place){
        ArrayList<ImageView> takeList = associateTakes(place);
        takeNum = takeList.size()-takeNum;
        for(int i=0;i <takeList.size();i++){
            if(i==takeNum-1){
                takeList.get(i).setVisible(true);
            }
        }
    }
    public void updateTurnOption(ArrayList<String> opts){
        for(int i=0; i<opts.size();i++){
            if(opts.get(i).equals("move")){
                turnOptions.get(0).setVisible(true);
            }
            else if(opts.get(i).equals("take role")){
                turnOptions.get(3).setVisible(true);
            }
            else if(opts.get(i).equals("end turn")){
                turnOptions.get(5).setVisible(true);
            }
            else if(opts.get(i).equals("upgrade")){
                turnOptions.get(4).setVisible(true);
            }
            else if(opts.get(i).equals("act")){
                turnOptions.get(1).setVisible(true);
            }
            else if(opts.get(i).equals("rehearse")){
                turnOptions.get(2).setVisible(true);
            }
            else{}
        }
    }
    public void updatePlayerPosition(Player moved){
        String color = moved.getName();
        ArrayList<Integer> dims = moved.getLocation().getArea(); 
        switch(color){
            case "blue":
                blue.setLayoutX(dims.get(0));
                blue.setLayoutY(dims.get(1)+90);
                blue.toFront();
                break;
            case "cyan":
                cyan.setLayoutX(dims.get(0)+40);
                cyan.setLayoutY(dims.get(1)+90);
                cyan.toFront();
                break;
            case "green":
                green.setLayoutX(dims.get(0)+80);
                green.setLayoutY(dims.get(1)+90);
                green.toFront();
                break;
            case "orange":
                orange.setLayoutX(dims.get(0)+120);
                orange.setLayoutY(dims.get(1)+90);
                orange.toFront();
                break;
            case "pink":
                pink.setLayoutX(dims.get(0));
                pink.setLayoutY(dims.get(1)+45);
                pink.toFront();
                break;  
            case "violet":
                violet.setLayoutX(dims.get(0)+40);
                violet.setLayoutY(dims.get(1)+45);
                violet.toFront();
                break;  
            case "white":
                white.setLayoutX(dims.get(0)+80);
                white.setLayoutY(dims.get(1)+45);
                white.toFront();
                break;
            case "red":
                red.setLayoutX(dims.get(0)+120);
                red.setLayoutY(dims.get(1)+45);
                red.toFront();
                break;
            default:
                break;
        }
    }
    public void updatePlayerRole(Player updated){
        ImageView piece = associatePlayer(updated);
        ArrayList<Integer> area = updated.getRole().getDimensions();
        if(updated.getRole().getCardStatus()){
            piece.setLayoutX(area.get(0) + updated.getLocation().getArea().get(0));
            piece.setLayoutY(area.get(1) + updated.getLocation().getArea().get(1));
        }
        else{
            piece.setLayoutX(area.get(0));
            piece.setLayoutY(area.get(1));
        }
    }
    public void updateRollLabel(boolean success, int roll, Role part){
        if(success){
            if(part.getCardStatus()){
                gameCommunications.setText("Success" + " you rolled a " + roll + ". You have been paid 2 credits");
            }
            else{
                gameCommunications.setText("Success" + " you rolled a " + roll + ". You have been paid 1 credit and 1 dollar");
            }
        }
        else{
            if(part.getCardStatus()){
                gameCommunications.setText("Failure" + " you rolled a " + roll);
            }
            else{
                gameCommunications.setText("Failure" + " you rolled a " + roll + ". You have been paid 1 dollar");
            }
        }
        
    }
    public void updateRehearseLabel(int current, int max){
        gameCommunications.setText("You now have " + current + " chips. The maximum for this scene is " + max);
    }
    public void flipCard(Location flip){
        ImageView card = associateLocation(flip);
        Image picture = new Image("images/" + flip.getScene().getImage());
        card.setImage(picture);
        card.setVisible(true);
    }
    public void wrapScene(Location current){
        ArrayList<ImageView> shots = associateTakes(current);
        ImageView scene = associateLocation(current);
        for(int i=0;i<shots.size();i++){
            shots.get(i).setVisible(false);
        }
        Image image = new Image("images/CardBack.jpg");
        scene.setImage(image);
    }
    public void updateCurrentDay(int day){
        currentDayLabel.setText("Remaining days: " + day);
    }
    public void switchPlayerView(Player active){
        ImageView dice = associatePlayer(active);
        activePieceTable.setImage(dice.getImage());
        rehearsePoints.setText(Integer.toString(active.getChips()));
        moneyInTable.setText(Integer.toString(active.getMoney()));
        creditsInTable.setText(Integer.toString(active.getCredits()));
    }
    public void updatePlayerRankings(Player active){
        ImageView player = associatePlayer(active);
        String color = player.getId().substring(0,1);
        Image dice = new Image("images/" + color + Integer.toString(active.getRank()) + ".png");
        player.setImage(dice);
    }
    public void endOfSceneMessage(){
        gameCommunications.setText("Its a wrap! the last shot has been completed and actor payouts are being calculated...");
    }
    public void addPayment(Player paid, int amount){
        String currentText = gameCommunications.getText();
        gameCommunications.setText(currentText + " " + paid.getName() + " has been paid " + amount + " dollars");
    }
    public void sendPurcahseSuccess(){
        gameCommunications.setText("Success! You have purchased your new rank");
    }
    public void sendPurchaseFail(){
        gameCommunications.setText("You did not have enough the purcahse the requested rank. Try again for anoter rank or end your turn");
    }
    public void clearUpdateMenu(){
        upgradeChoices.setVisible(false);
        creditBuy.setVisible(false);
        dollarBuy.setVisible(false);
        cancelPurchase.setVisible(false);
    }
    public void clearTurnMenu(){
        for(int i=0;i<turnOptions.size();i++){
            turnOptions.get(i).setVisible(false);
        }
    }
    public void clearGameComm(){
        gameCommunications.setText("");
    }
    public void clearLocationCard(Location ended){
        ImageView place = associateLocation(ended);
        place.setVisible(false);
    }
    public void noBonus(){
        gameCommunications.setText(gameCommunications.getText() + " There was no one on the card. There are no bonuses");
    }
    private void dayOverMessage(){
        gameCommunications.setText("The last scene has wrapped. The day is now over. Players have been returned to the trailers and new scene cards have been dealt.");
        gameCommunications.setVisible(true);
    }
    private void resetCardImages(){
        for(int i=0; i<areaHolders.size();i++){
            areaHolders.get(i).setImage(new Image("images/Cardback.jpg"));
            areaHolders.get(i).setVisible(true);
        }
    }

    //In Game Button Controls
    public void startMove(ActionEvent event)throws IOException{
        ArrayList<String> locations = Game.getGame().getPlayers().get(0).getValidLocations();
        moveChoice.getItems().clear();
        moveChoice.setVisible(true);
        confirmButton.setVisible(true);
        for(int i=0;i<locations.size();i++){
            moveChoice.getItems().addAll(locations.get(i));
        }
        for(int i=0;i<turnOptions.size();i++){
            turnOptions.get(i).setVisible(false);
        }
        moveChoice.setValue(locations.get(0));
    }   
    public void submitMove(ActionEvent event)throws IOException{
        String choice = moveChoice.getValue();
        Location moveTo = null;
        for(int i=0;i<Game.getGame().getLocations().size();i++){
            if(Game.getGame().getLocations().get(i).getSetName().equals(choice)){
                moveTo = Game.getGame().getLocations().get(i);
            }
        }
        Deadwood.current.getPlayers().get(0).finishMove(moveTo);
        moveChoice.setVisible(false);
        confirmButton.setVisible(false);
    }
    public void endTurn(ActionEvent event)throws IOException{
        Game.getGame().endOfTurn();
    }
    public void startTakeRole(ActionEvent event)throws IOException{
        for(int i=0;i<turnOptions.size();i++){
            turnOptions.get(i).setVisible(false);
        }
        roleChoice.getItems().clear();
        confirmRole.setVisible(true);
        roleChoice.setVisible(true);
        Player current = Game.getGame().getPlayers().get(0);
        ArrayList<Role> roles = current.getLocation().generateRoles(current);
        for(int i=0;i<roles.size();i++){
            roleChoice.getItems().addAll(roles.get(i).getRoleName());
        }
        roleChoice.setValue(roles.get(0).getRoleName());
    }
    public void confirmRole(ActionEvent event)throws IOException{
        String choice = roleChoice.getValue();
        Game.getGame().getPlayers().get(0).updateRoles(choice);
        roleChoice.setVisible(false);
        confirmRole.setVisible(false);
    }
    public void startAct(ActionEvent event)throws IOException{
        for(int i=0;i<turnOptions.size();i++){
            turnOptions.get(i).setVisible(false);
        }
        Player current = Game.getGame().getPlayers().get(0);
        current.act();
    }
    public void rehearse(ActionEvent event)throws IOException{
        for(int i=0;i<turnOptions.size();i++){
            turnOptions.get(i).setVisible(false);
        }
        Game.getGame().getPlayers().get(0).rehearse();
    }
    public void upgradeClick(ActionEvent event)throws IOException{
        upgradeChoices.setVisible(true);
        creditBuy.setVisible(true);
        dollarBuy.setVisible(true);
        cancelPurchase.setVisible(true);
        for(int i=0;i<turnOptions.size();i++){
            turnOptions.get(i).setVisible(false);
        }
    }
    public void buyWithDollar(ActionEvent event)throws IOException{
        ArrayList<String> choices = new ArrayList<>();
        choices.add(upgradeChoices.getValue());
        choices.add("dollar");
        Game.getGame().getPlayers().get(0).upgrade(choices);
    }
    public void buyWithCredit(ActionEvent event)throws IOException{
        ArrayList<String> choices = new ArrayList<>();
        choices.add(upgradeChoices.getValue());
        choices.add("credit");
        Game.getGame().getPlayers().get(0).upgrade(choices);
    }
    public void cancelPurchase(ActionEvent event)throws IOException{
        upgradeChoices.setVisible(false);
        creditBuy.setVisible(false);
        dollarBuy.setVisible(false);
        cancelPurchase.setVisible(false);
        for(int i=0;i<turnOptions.size();i++){
            turnOptions.get(i).setVisible(true);
        }
    }

    //Close the window
    public void switchToEnd() throws IOException{
        Stage stage = (Stage)confirmButton.getScene().getWindow();
        stage.close();
        Parent endParent = FXMLLoader.load(getClass().getResource("gameOver.fxml"));
        Scene gameScene = new Scene(endParent,500,500);
        Stage window = new Stage();
        window.setScene(gameScene);
        window.setResizable(false);
        window.show();
    }

}
