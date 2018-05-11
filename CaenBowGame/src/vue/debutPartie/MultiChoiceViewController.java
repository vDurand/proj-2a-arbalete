/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue.debutPartie;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Calvin
 */
public class MultiChoiceViewController implements Initializable {

    @FXML
    private Button createBTN;
    @FXML
    private Button joinBTN;
    @FXML
    private ImageView caenBowLogo;
    @FXML
    private Button btnBack;
    
    private static Stage _stage;
    private AnchorPane accueilView;
    private FXMLLoader loader;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        createBTN.setLayoutY(_stage.getHeight()/2+100);
        createBTN.setLayoutX((_stage.getWidth()/2)-406);
        joinBTN.setLayoutY(_stage.getHeight()/2+100);
        joinBTN.setLayoutX((_stage.getWidth()/2)+200);
        caenBowLogo.setLayoutX(_stage.getWidth()/2-117);
        caenBowLogo.setLayoutY((_stage.getHeight()/2)-250);
        btnBack.setLayoutX(_stage.getWidth()/2-406);
        btnBack.setLayoutY((_stage.getHeight()/2)+200);
    }    
    
    public void createMultiGame(){
        MultiConfigViewController multiConf = new MultiConfigViewController();
        multiConf.lancerMultiConfig(_stage);
    }
    
    public void joinMultiGame(){
        MultiJoinViewController multiJoin = new MultiJoinViewController();
        multiJoin.startMultiJoin(_stage);
    }
    
    public void goBack() {
        MultiSoloSelectingViewController selectingMenu = new MultiSoloSelectingViewController();
        selectingMenu.lancerAccueilView(_stage);
    }
    
    public void startMultiChoice(Stage stage) {
        try {
            // chargement de la fenetre d'accueil.
            _stage = stage;
            loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("MultiChoiceView.fxml"));
            accueilView = (AnchorPane) loader.load();

            Scene scene = new Scene(accueilView);
            scene.getStylesheets().addAll(this.getClass().getResource("styleSelecting.css").toExternalForm());
            
            
            _stage.setScene(scene);

        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
}
