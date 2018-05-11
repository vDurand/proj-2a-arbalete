/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue.findepartie;

import static java.lang.Math.min;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import static javafx.application.Platform.exit;
import javafx.beans.property.ReadOnlyObjectWrapper;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import outils.Fleche;
import player.Joueur;
import modele.Partie;
import static java.lang.Math.min;
import static java.util.logging.Logger.getLogger;
import static javafx.collections.FXCollections.observableArrayList;
import modele.PartieMulti;

/**
 *
 * @author Guaquiere
 */
public class FinDePartieController implements Initializable {

    private static final Logger LOG = getLogger(FinDePartieController.class.getName());

    private Joueur _joueur;
    private Partie _partie;
    private int _nbJoueurs;
    private int _nbVolee;
    private ArrayList<Joueur> _resultat;
    @FXML private AnchorPane ecran;
    @FXML private TableView<ObservableList<String>> tabRecap;
    @FXML private Label vainqueur;
    
    
    private String getNomVainqueur(){
        return _resultat.get(0).getPseudo();
    }

    /**
     *
     */
    public void buttonQuit(){
        exit();
    }
   
       
    private ObservableList<String> ajoutLigne(int i){
           ObservableList<String> score = observableArrayList();
              score.clear();
             score.add(_resultat.get(i).getPseudo());
            List<Fleche> tirs = _resultat.get(i).getHistorique();
            int valeur;
            int total = 0;
            for(int j = 0; j < tirs.size(); j++){
                valeur = tirs.get(j).getValeur();
                total += valeur;
                score.add(Integer.toString(valeur));
            }
            score.add(Integer.toString(total));
            return score;
       }
       
    
    
     public void initFinPartie(Partie partie) {
         _partie = partie;
         assert tabRecap != null : "fx:id=\"tableView\" was not injected: check your FXML file 'tableview.fxml'.";
         _nbVolee = _partie.getNbVolee();
        _nbJoueurs = _partie.nbJoueurs();
        _resultat = _partie.getClassement();
        if(_partie instanceof PartieMulti){
            PartieMulti partieTmp = (PartieMulti) _partie;
            for(int i = 0; i < _resultat.size();i++){
                Joueur joueurTmp = partieTmp.findJoueur(_resultat.get(i).getPseudo());
                if(joueurTmp == null){
                    _resultat.set(i, _partie.getJoueur());
                }
                else{
                    _resultat.set(i, joueurTmp);
                }
            }
        }
        
        if(_resultat == null){
            System.out.println("resulta empty");
        }
        else{
            for(int i = 0; i < _resultat.size(); i++)
                System.out.println("-->REsult:"+_resultat.get(i).getPseudo());
        }
        
        
        vainqueur.setText( getNomVainqueur() + " remporte la partie !");
         
        //tabRecap.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        double tailleMax = Screen.getPrimary().getBounds().getWidth() - 200;
        double tailleTab = 50 + 270*_nbVolee + 50;
        
        tabRecap.setPrefWidth(min(tailleMax,tailleTab));
        tabRecap.setFixedCellSize(30);
        
        final int finalIdx = 0;
        TableColumn<ObservableList<String>, String> nomJoueurs = new TableColumn("Joueur");        
        nomJoueurs.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx)));
        nomJoueurs.setMinWidth(50);
        tabRecap.getColumns().add(nomJoueurs);
        int idx = 0;
        for(int i = 0; i < _nbVolee; i++){
            TableColumn columnVolee = new TableColumn("Volee " + Integer.toString(i+1));
            tabRecap.getColumns().add(columnVolee);
            for(int j = 1; j <= 10; j++){
                idx++;
                final int finalIdx2 = idx;
                TableColumn<ObservableList<String>, String> column = new TableColumn<>(Integer.toString(j));
                column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx2)));
                column.setPrefWidth(25);
                columnVolee.getColumns().add(column);
            }
        }
        idx++;
        TableColumn<ObservableList<String>, String> total = new TableColumn("Total");
        nomJoueurs.setMinWidth(50);      
        final int finalIdx3 = idx;
         System.out.println("-->NB JOUEUR = "+_nbJoueurs);
        total.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx3)));
        tabRecap.getColumns().add(total);
        
        /*
             ObservableList<String> score1 = observableArrayList();
              score1.clear();
             score1.add("Thibaut");
                for (int k = 0; k < 10*_nbVolee; k++) {
                score1.add(Integer.toString(k));
                }
            score1.add(Integer.toString(11));
            */
            
            ObservableList<ObservableList<String>> res = observableArrayList();
            for (int i = 0; i < _nbJoueurs; i++){
                res.add(ajoutLigne(i));
            }
            //res.addAll(score1,ajoutLigne(),ajoutLigne());
            tabRecap.setItems(res);
        //}
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
    }   

   
    
}