/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue.debutPartie;

import com.jfoenix.controls.JFXComboBox;
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
public class MultiSoloSelectingViewController implements Initializable {
    
    @FXML
    private Button soloSelectionBtn;
    @FXML
    private Button multiSelectionBtn;
    @FXML
    private ImageView caenBowLogo;
    
    private static Stage _stage;
    private AnchorPane accueilView;
    private FXMLLoader loader;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        soloSelectionBtn.setLayoutY(_stage.getHeight()/2+100);
        soloSelectionBtn.setLayoutX((_stage.getWidth()/2)-406);
        multiSelectionBtn.setLayoutY(_stage.getHeight()/2+100);
        multiSelectionBtn.setLayoutX((_stage.getWidth()/2)+200);
        caenBowLogo.setLayoutX(_stage.getWidth()/2-117);
        caenBowLogo.setLayoutY((_stage.getHeight()/2)-250);
    }    
    
    public void startSoloGame(){
        SoloConfigViewController soloConf = new SoloConfigViewController();
        soloConf.lancerSoloConfig(_stage);
    }
    
    public void startMultiSelection(){
        MultiChoiceViewController multiChoice = new MultiChoiceViewController();
        multiChoice.startMultiChoice(_stage);
    }
    
    public void lancerAccueilView(Stage stage) {
        try {
            // chargement de la fenetre d'accueil.
            _stage = stage;
            loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("MultiSoloSelectingView.fxml"));
            accueilView = (AnchorPane) loader.load();

            Scene scene = new Scene(accueilView);
            scene.getStylesheets().addAll(this.getClass().getResource("styleSelecting.css").toExternalForm());
            
            
            _stage.setScene(scene);

        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
