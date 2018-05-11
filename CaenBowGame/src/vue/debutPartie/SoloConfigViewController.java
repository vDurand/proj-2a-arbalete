/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue.debutPartie;

import vue.interfaceJeu.InterfaceJeu;
import vue.interfaceJeu.FXMLDocumentController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import player.Joueur;
import modele.Partie;
import modele.PartieSolo;

/**
 * FXML Controller class
 *
 * @author Calvin
 */
public class SoloConfigViewController implements Initializable {

    @FXML
    private Button btnBack;
    @FXML
    private Button btnValider;
    @FXML
    private ImageView imgWood;
    @FXML
    private Text textTop;
    @FXML
    private TextField roundInput;
    @FXML
    private Slider targetSlider;
    @FXML
    private Slider windSlider;
    @FXML
    private Text target0;
    @FXML
    private Text target1;
    @FXML
    private Text target2;
    @FXML
    private Text nbRoundLabel;
    @FXML
    private Text targetLabel;
    @FXML
    private Text windLabel;
    @FXML
    private Text wind0;
    @FXML
    private Text wind1;
    @FXML
    private Text wind2;
    @FXML
    private Text wind3;
    
    private static Stage _stage;
    private Scene _scene;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imgWood.setLayoutY(_stage.getHeight()/2-250);
        imgWood.setLayoutX(_stage.getWidth()/2-205);
        textTop.setLayoutY(_stage.getHeight()/2-215);
        textTop.setLayoutX(_stage.getWidth()/2-45);
        btnValider.setLayoutY(_stage.getHeight()/2+170);
        btnValider.setLayoutX(_stage.getWidth()/2+35);
        btnBack.setLayoutY(_stage.getHeight()/2+175);
        btnBack.setLayoutX(_stage.getWidth()/2-150);
        roundInput.setLayoutY(_stage.getHeight()/2-100);
        roundInput.setLayoutX(_stage.getWidth()/2-130);
        targetSlider.setLayoutY(_stage.getHeight()/2);
        targetSlider.setLayoutX(_stage.getWidth()/2-130);
        windSlider.setLayoutY(_stage.getHeight()/2+100);
        windSlider.setLayoutX(_stage.getWidth()/2-130);
        
        target0.setLayoutY(_stage.getHeight()/2+30);
        target0.setLayoutX(_stage.getWidth()/2-140);
        target1.setLayoutY(_stage.getHeight()/2+30);
        target1.setLayoutX(_stage.getWidth()/2-27);
        target2.setLayoutY(_stage.getHeight()/2+30);
        target2.setLayoutX(_stage.getWidth()/2+90);
        nbRoundLabel.setLayoutY(_stage.getHeight()/2-120);
        nbRoundLabel.setLayoutX(_stage.getWidth()/2-130);
        targetLabel.setLayoutY(_stage.getHeight()/2-20);
        targetLabel.setLayoutX(_stage.getWidth()/2-130);
        windLabel.setLayoutY(_stage.getHeight()/2+80);
        windLabel.setLayoutX(_stage.getWidth()/2-130);
        wind0.setLayoutY(_stage.getHeight()/2+130);
        wind0.setLayoutX(_stage.getWidth()/2-130);
        wind1.setLayoutY(_stage.getHeight()/2+130);
        wind1.setLayoutX(_stage.getWidth()/2-60);
        wind2.setLayoutY(_stage.getHeight()/2+130);
        wind2.setLayoutX(_stage.getWidth()/2+12);
        wind3.setLayoutY(_stage.getHeight()/2+130);
        wind3.setLayoutX(_stage.getWidth()/2+100);

        roundInput.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                roundInput.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if(Integer.parseInt(newValue) < 1){
                roundInput.setText("1");
            }
            if(Integer.parseInt(newValue) > 25){
                roundInput.setText("25");
            }
        });
    }    
    
    public void goBack() {
        MultiSoloSelectingViewController selectingMenu = new MultiSoloSelectingViewController();
        selectingMenu.lancerAccueilView(_stage);
    }
    
    
    public void startSoloGame() {
        Joueur joueur = new Joueur("Joueur");
        int vent = (int) windSlider.getValue();
        int distance = (int) targetSlider.getValue();
        int nbVolee = Integer.parseInt(roundInput.getText());
        
        Partie s = new PartieSolo(joueur, nbVolee, vent, distance);
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(InterfaceJeu.class.getResource("FXMLDocument.fxml"));
            AnchorPane configurationView = (AnchorPane) loader.load();

            _scene = new Scene(configurationView);
            
            _stage.setScene(_scene);
            _stage.show();
            FXMLDocumentController controller = loader.<FXMLDocumentController>getController();
            controller.initJeu(s);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void lancerSoloConfig(Stage stage) {
        try {
            _stage = stage;
            // chargement de la fenetre de Configuration.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("SoloConfigView.fxml"));
            AnchorPane configurationView = (AnchorPane) loader.load();

            _scene = new Scene(configurationView);
            _scene.getStylesheets().addAll(this.getClass().getResource("styleSelecting.css").toExternalForm());

            _stage.setScene(_scene);
            _stage.show();
            

        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
}
