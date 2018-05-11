/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue.debutPartie;

import vue.interfaceJeu.FXMLDocumentController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
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
import modele.PartieMulti;
import player.*;
import server.*;

/**
 * FXML Controller class
 *
 * @author Calvin
 */
public class MultiConfigViewController implements Initializable {

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
    private TextField pseudoInput;
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
    private Text pseudoLabel;
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
    @FXML
    private TextField gameNameInput;
    @FXML
    private Text gameNameLabel;
    
    private static Stage _stage;
    private Scene _scene;
    private Client _client;
    /**
     * Initializes the controller class.
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
        roundInput.setLayoutY(_stage.getHeight()/2-60);
        roundInput.setLayoutX(_stage.getWidth()/2-130);
        targetSlider.setLayoutY(_stage.getHeight()/2+40);
        targetSlider.setLayoutX(_stage.getWidth()/2-130);
        windSlider.setLayoutY(_stage.getHeight()/2+140);
        windSlider.setLayoutX(_stage.getWidth()/2-130);
        
        target0.setLayoutY(_stage.getHeight()/2+70);
        target0.setLayoutX(_stage.getWidth()/2-140);
        target1.setLayoutY(_stage.getHeight()/2+70);
        target1.setLayoutX(_stage.getWidth()/2-27);
        target2.setLayoutY(_stage.getHeight()/2+70);
        target2.setLayoutX(_stage.getWidth()/2+90);
        nbRoundLabel.setLayoutY(_stage.getHeight()/2-70);
        nbRoundLabel.setLayoutX(_stage.getWidth()/2-130);
        targetLabel.setLayoutY(_stage.getHeight()/2+20);
        targetLabel.setLayoutX(_stage.getWidth()/2-130);
        windLabel.setLayoutY(_stage.getHeight()/2+120);
        windLabel.setLayoutX(_stage.getWidth()/2-130);
        wind0.setLayoutY(_stage.getHeight()/2+170);
        wind0.setLayoutX(_stage.getWidth()/2-130);
        wind1.setLayoutY(_stage.getHeight()/2+170);
        wind1.setLayoutX(_stage.getWidth()/2-60);
        wind2.setLayoutY(_stage.getHeight()/2+170);
        wind2.setLayoutX(_stage.getWidth()/2+12);
        wind3.setLayoutY(_stage.getHeight()/2+170);
        wind3.setLayoutX(_stage.getWidth()/2+100);
        
        pseudoInput.setLayoutY(_stage.getHeight()/2-130);
        pseudoInput.setLayoutX(_stage.getWidth()/2-130);
        pseudoLabel.setLayoutY(_stage.getHeight()/2-140);
        pseudoLabel.setLayoutX(_stage.getWidth()/2-130);
        
        gameNameInput.setLayoutY(_stage.getHeight()/2-180);
        gameNameInput.setLayoutX(_stage.getWidth()/2-130);
        gameNameLabel.setLayoutY(_stage.getHeight()/2-190);
        gameNameLabel.setLayoutX(_stage.getWidth()/2-130);
        
        btnValider.setDisable(true);
        _client = new Client(true);

        roundInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    roundInput.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(Integer.parseInt(newValue) < 1){
                    roundInput.setText("1");
                }
                if(Integer.parseInt(newValue) > 25){
                    roundInput.setText("25");
                }
            }
        });
        
        pseudoInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Pattern pattern = Pattern.compile("\\s");
                Matcher matcher = pattern.matcher(newValue);
                if (matcher.find()) {
                    pseudoInput.setText(newValue.replaceAll("[\\s]", "_"));
                }
                if (!newValue.matches("")) {
                    btnValider.setDisable(false);
                }
                else{
                    btnValider.setDisable(true);
                }
            }
        });
    }    
    
    public void goBack() {
        System.err.println(">>>"+Integer.parseInt(roundInput.getText()));
        System.err.println(">>>"+(int) targetSlider.getValue());
        MultiChoiceViewController selectingMenu = new MultiChoiceViewController();
        selectingMenu.startMultiChoice(_stage);
    }
    
    
    public void startMultiGame() {
        Connexion c = _client.getConnexion();
        int nbRound = Integer.parseInt(roundInput.getText());
        String pseudo = pseudoInput.getText();
        String gameName = gameNameInput.getText();
        int windLevel = (int) windSlider.getValue();
        int targetDistance = (int) targetSlider.getValue();
        c.createPartie(pseudo, gameName, nbRound, windLevel, targetDistance);
        double targetDist = c.getCurrentRoundTargetDistance();
        double windX = c.getCurrentRoundWindX();
        double windY = c.getCurrentRoundWindY();
        Joueur j = new Joueur(pseudo);
        PartieMulti partie = new PartieMulti(j, nbRound, c);
        c.setPartie(partie);
        partie.setDistanceCible(targetDist);
        partie.setVitesseVent(windX, windY);
        
        FXMLDocumentController docController = new FXMLDocumentController();
        docController.lancerDocController(_stage, partie);
    }
    
    public void lancerMultiConfig(Stage stage) {
        try {
            _stage = stage;
            // chargement de la fenetre de Configuration.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("MultiConfigView.fxml"));
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
