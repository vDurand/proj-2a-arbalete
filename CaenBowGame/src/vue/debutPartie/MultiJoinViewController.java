/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue.debutPartie;

import vue.interfaceJeu.FXMLDocumentController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import player.Joueur;
import modele.PartieMulti;
import server.*;

/**
 * FXML Controller class
 *
 * @author Calvin
 */
public class MultiJoinViewController implements Initializable {
    @FXML
    private Button btnBack;
    @FXML
    private Button btnValider;
    @FXML
    private Button btnRefresh;  
    @FXML
    private Button btnJoin; 
    @FXML
    private ImageView imgWood;
    @FXML
    private Text textTop;
    @FXML
    private Text textAlertPseudo;
    @FXML
    private TableView gameTable;
    @FXML
    private TableColumn columnName;
    @FXML
    private TableColumn columnPlayers;
    @FXML
    private TextField pseudoInput;
    @FXML
    private Text pseudoLabel;
    
    private static Stage _stage;
    private Scene _scene;
    private Client _client;
    private PartieDisponible selectedPartie;
    
    private final ObservableList<PartieDisponible> data = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        _client = new Client(false);
        
        imgWood.setLayoutY(_stage.getHeight()/2-250);
        imgWood.setLayoutX(_stage.getWidth()/2-205);
        textTop.setLayoutY(_stage.getHeight()/2-217);
        textTop.setLayoutX(_stage.getWidth()/2-50);
        btnValider.setLayoutY(_stage.getHeight()/2+170);
        btnValider.setLayoutX(_stage.getWidth()/2+35);
        btnJoin.setLayoutY(_stage.getHeight()/2+170);
        btnJoin.setLayoutX(_stage.getWidth()/2+35);
        btnBack.setLayoutY(_stage.getHeight()/2+175);
        btnBack.setLayoutX(_stage.getWidth()/2-150);
        gameTable.setLayoutY(_stage.getHeight()/2-170);
        gameTable.setLayoutX(_stage.getWidth()/2-135);
        btnRefresh.setLayoutY(_stage.getHeight()/2-220);
        btnRefresh.setLayoutX(_stage.getWidth()/2+100);
        
        pseudoInput.setLayoutY(_stage.getHeight()/2-160);
        pseudoInput.setLayoutX(_stage.getWidth()/2-130);
        pseudoLabel.setLayoutY(_stage.getHeight()/2-180);
        pseudoLabel.setLayoutX(_stage.getWidth()/2-130);
        textAlertPseudo.setLayoutY(_stage.getHeight()/2-120);
        textAlertPseudo.setLayoutX(_stage.getWidth()/2-130);
        pseudoInput.setVisible(false);
        pseudoLabel.setVisible(false);
        btnJoin.setVisible(false);
        textAlertPseudo.setVisible(false);
        
        btnValider.setDisable(true);
        
        if(_client.hasPartie()){
            ArrayList<PartieDisponible> partie =  _client.getAvailableGame();
            for(int i = 0; i < partie.size(); i++){
                data.add(partie.get(i));
            }
        }
        
        columnName.setCellValueFactory(
                new PropertyValueFactory<PartieDisponible, String>("name"));
        columnPlayers.setCellValueFactory(
                new PropertyValueFactory<PartieDisponible, String>("nbPlayer"));
        gameTable.setItems(data);
    }    
    
    public void goBack() {
        MultiChoiceViewController selectingMenu = new MultiChoiceViewController();
        selectingMenu.startMultiChoice(_stage);
    }
    
    public void selectPartie() {
        btnValider.setDisable(false);
        PartieDisponible p = (PartieDisponible) gameTable.getSelectionModel().getSelectedItem();
    }
    
    public void goRefresh() {
        _client = new Client(false);
        btnValider.setDisable(true);
        data.clear();
        if(_client.hasPartie()){
            ArrayList<PartieDisponible> partie =  _client.getAvailableGame();
            for(int i = 0; i < partie.size(); i++){
                data.add(partie.get(i));
            }
        }
    }
    
    public void joinMultiGame() {
        PartieDisponible p = (PartieDisponible) gameTable.getSelectionModel().getSelectedItem();
        _client.joinPartie(p.getPort());
        btnBack.setVisible(false);
        btnValider.setVisible(false);
        btnRefresh.setVisible(false);
        gameTable.setVisible(false);
        columnName.setVisible(false);
        columnPlayers.setVisible(false);
        pseudoInput.setVisible(true);
        pseudoLabel.setVisible(true);
        btnJoin.setVisible(true);
        btnJoin.setDisable(true);
        
        pseudoInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Pattern pattern = Pattern.compile("\\s");
                Matcher matcher = pattern.matcher(newValue);
                if (matcher.find()) {
                    pseudoInput.setText(newValue.replaceAll("[\\s]", "_"));
                }
                if (!newValue.matches("")) {
                    btnJoin.setDisable(false);
                    textAlertPseudo.setVisible(false);
                }
                else{
                    btnJoin.setDisable(true);
                }
            }
        });
    }
    
    public void startMultiGame(){
        _client.getConnexion().setLogin(pseudoInput.getText());
        boolean available = _client.getConnexion().checkAvailability(this);
        if(available){
            int nbRound = _client.getConnexion().getMaxRound();
            Joueur j = new Joueur(pseudoInput.getText());
            PartieMulti partie = new PartieMulti(j, nbRound, _client.getConnexion());
            _client.getConnexion().setPartie(partie);
            
            _client.getConnexion().joinGame();
            
            double targetDist = _client.getConnexion().getCurrentRoundTargetDistance();
            double windX = _client.getConnexion().getCurrentRoundWindX();
            double windY = _client.getConnexion().getCurrentRoundWindY();
            partie.setDistanceCible(targetDist);
            partie.setVitesseVent(windX, windY);

            FXMLDocumentController docController = new FXMLDocumentController();
            docController.lancerDocController(_stage, partie);
        }
    }
    
    public void notifyPseudoTaken(){
        textAlertPseudo.setVisible(true);
        btnJoin.setDisable(true);
    }
    
    public void startMultiJoin(Stage stage) {
        try {
            _stage = stage;
            // chargement de la fenetre de Configuration.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("MultiJoinView.fxml"));
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
