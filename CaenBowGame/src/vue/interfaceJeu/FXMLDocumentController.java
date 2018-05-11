package vue.interfaceJeu;

import outils.Trajectoire;
import outils.Cible;
import outils.ArbaleteMedievale;
import outils.ArbaleteAPoulie;
import outils.ArbaleteDeChasse;
import outils.Arbalete;
import outils.Fleche;
import modele.Partie;
import modele.PartieMulti;
import modele.PartieSolo;
import vue.findepartie.FinDePartieController;
import vue.findepartie.finDePartie;
import java.io.IOException;
import player.*;

import static java.lang.Math.atan;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import static java.lang.Math.round;


/**
 *
 * @author jorand
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML private AnchorPane terrain;
    @FXML private AnchorPane adversaire;
    
    @FXML private ImageView crossbow;
    @FXML private ImageView profilArba;
    @FXML private ImageView cibleReel;
    @FXML private ImageView boussole;
    
    @FXML private Label forceVent;
    @FXML private Label Angles;
    @FXML private Label ArbaChasse;
    @FXML private Label ArbaPoulie;
    @FXML private Label ArbaMedievale;
    @FXML private Label Resultat;
    @FXML private Label Volee;
    @FXML private Label FlecheRestante;
    @FXML private Label DistCible;
    @FXML private Label pseudo;
    
    
    @FXML private RadioButton Cran1;
    @FXML private RadioButton Cran2;
    @FXML private RadioButton Cran3;
    
    private double _angleX;
    private double _angleY;
    private double _distanceCible;
    private int _score = 0;
    private Cible _cible;
    
    private int _PositionHBoxPremierImpact;
    private int _PositionHBoxPremierImpactAdv;
    private int _nbFlechesRestantes = 10;
    private int _voleeCourante = 1;
    
    
    private Partie _partie;
    private Joueur _joueurVisible;
    private Trajectoire _traject;
    private Arbalete _arbalete;
    private int _numeroJoueurCourant = 0;
    
    private ArbaleteMedievale _arbaleteMedievale;
    private ArbaleteAPoulie _arbaleteAPoulie;
    private ArbaleteDeChasse _arbaleteDeChasse;
    
    
    
    /**
     * Mise à jour de la force du vent
     * @param FX : résultante horizontale
     * @param FY : résultante verticale
     */
    public void updateWind(double FX, double FY) {
        double angle;
        double norme = (double)round(100*sqrt(FX*FX + FY*FY))/100;
        
        if (FX!=0) {
            angle = atan(FY/FX);
            angle = toDegrees(angle);
        } else {
            angle = 90;
        }
        
        angle = angle-90;
        boussole.setRotate(angle);
        forceVent.setText(norme + " m/s");
    }
    
    
    /**
     * Mise à jour de la distance à la cible
     * @param dist : distance Joueur - cible
     */
    public void updateDistance(double dist) {
        _distanceCible = dist;
        
        double coeff[] = new double[2];
        coeff = TailleFctDistance(10,200,200,12);
        double taille = coeff[0]*dist + coeff[1];
        
        cibleReel.setFitWidth(taille);
        cibleReel.setFitHeight(taille);
    }
    
    
    
    
    
    /**
     * recherche fct(distance) = taille sous forme a*dist+b
     * @param x1
     * @param fx1
     * @param x2
     * @param fx2
     * @return a et b de la fonction fct(dist)
        */
    private double[] TailleFctDistance(double x1, double fx1, double x2, double fx2) {
        double coeff[] = new double[2];
        coeff[0] = (fx2-fx1)/(x2-x1);
        coeff[1] = fx1 - x1*coeff[0];
        return coeff;
    }
    
    
    /**
     * Initialisation des descriptions des Arbalètes sur la vue
     */
    private void initLabel() {
        
        ArbaMedievale.setText(_arbaleteMedievale.getDescription());
        ArbaChasse.setText(_arbaleteDeChasse.getDescription());
        ArbaPoulie.setText(_arbaleteAPoulie.getDescription());
        
        Volee.setText("volée : " + _voleeCourante + "/" + _partie.getNbVolee());
        FlecheRestante.setText("Reste " + _nbFlechesRestantes + "/10");
        DistCible.setText("Distance de la cible : " + (int) _distanceCible +"m");
        
        pseudo.setText(_joueurVisible.getPseudo());
        pseudo.setAlignment(Pos.CENTER);
    }
    
    
    /**
     * Accorde le nombre de cran de l'arbalète avec le nombre de radioButton
     */
    private void updateNbCran() {
        Cran1.setVisible( (_arbalete.getNbCran()>=1) );
        Cran2.setVisible( (_arbalete.getNbCran()>=2) );
        Cran3.setVisible( (_arbalete.getNbCran()>=3) );
        Cran1.setSelected(true);
    }
    
    
    /**
     * Permet d'ajouter une image d'impact à une hbox
     * @param h : hbox défini
     * @param adv : savoir si impact de l'adversaire
     */
    private void setImageImpact(HBox h, boolean adv) {
        double taille;
        if (adv) {
            double coeff[] = new double[2];
            coeff = TailleFctDistance(10,10,200,1);
            taille = coeff[0]*10 + coeff[1];
        }
        else {  
            taille = 10;
        }
        
        ImageView iv = new ImageView(new Image("vue/img/impact.png"));
        iv.setFitWidth(taille);
        iv.setFitHeight(taille);
        h.getChildren().add(iv);
        h.setAlignment(Pos.CENTER);
    }
    
    
    /**
     * Afficher impact Position posX, posY
     * @param h : hbox contenant l'image
     * @param posX : position de l'impact suivant X en m
     * @param posY : position de l'impact suivant Y en m
     */
    public void marqueImpact(HBox h, double posX, double posY) {
        double coeff_image_iv = 10.0/11.0;
        
        posX = posX*(cibleReel.getFitWidth()*coeff_image_iv) / _cible.getRayon();
        posY = posY*(cibleReel.getFitHeight()*coeff_image_iv) / _cible.getRayon();
        
        if (posX>=250) {
            posX = 250;
        }
        else if (posX<=-250) {
            posX = -250;
        }
        
        if (posY>=250) {
            posY = 250;
        }
        else if (posY<=-250) {
            posY = -250;
        }
        
        AnchorPane.setLeftAnchor(h,posX);
        AnchorPane.setBottomAnchor(h,posY);
        h.setVisible(true);
    }
    
    
    /**
     * Permet d'initialiser chaque HBox contenant l'image d'un impact
     */
    private void initImpact() {
        _PositionHBoxPremierImpact = terrain.getChildren().size();
        _PositionHBoxPremierImpactAdv = adversaire.getChildren().size();
        
        for (int k=0; k<10; k++) {
            HBox h = new HBox();
            setImageImpact(h, false);
            terrain.getChildren().add(h);
            
            HBox hadv = new HBox();
            setImageImpact(hadv, true);
            adversaire.getChildren().add(hadv);
        }
        initAllImpact();
        initAllImpactAdv();
    }
    
    
    /**
     * initialise chaque impact sur la cibledu joueur
     */
    private void initAllImpact() {
        for (int k=0; k<10; k++) {
            AnchorPane.setLeftAnchor(terrain.getChildren().get(_PositionHBoxPremierImpact + k), 0.0);
            AnchorPane.setBottomAnchor(terrain.getChildren().get(_PositionHBoxPremierImpact + k), 0.0);
            AnchorPane.setRightAnchor(terrain.getChildren().get(_PositionHBoxPremierImpact + k), 0.0);
            AnchorPane.setTopAnchor(terrain.getChildren().get(_PositionHBoxPremierImpact + k), 0.0);
    
            terrain.getChildren().get(_PositionHBoxPremierImpact + k).setVisible(false);
        }
    }
    
    
    /**
     * initialise chaque impact sur la cible de l'adversaire
     */
    private void initAllImpactAdv() {
        for (int k=0; k<10; k++) {
            AnchorPane.setLeftAnchor(adversaire.getChildren().get(_PositionHBoxPremierImpactAdv + k),0.0);
            AnchorPane.setBottomAnchor(adversaire.getChildren().get(_PositionHBoxPremierImpactAdv + k),0.0);
            AnchorPane.setRightAnchor(adversaire.getChildren().get(_PositionHBoxPremierImpactAdv + k),0.0);
            AnchorPane.setTopAnchor(adversaire.getChildren().get(_PositionHBoxPremierImpactAdv + k),25.0);
            
            adversaire.getChildren().get(_PositionHBoxPremierImpactAdv + k).setVisible(false);
        }
        
    }
    
    
    /**
     * Affichage cible joueur suivant
     * @param e 
     */
    @FXML public void JoueurSuivant(InputEvent e) {
        initAllImpactAdv();
        
        int nbjoueur = _partie.nbJoueurs();
        
        if (nbjoueur!=1) {
            if (_numeroJoueurCourant==nbjoueur) {
                _joueurVisible = _partie.getJoueur();
                _numeroJoueurCourant = 0 ;
            }
            else {
                _joueurVisible = ((PartieMulti) _partie).getAdversaire(_numeroJoueurCourant);
                _numeroJoueurCourant++;
            }
        }
        pseudo.setText(_partie.getJoueur().getPseudo());
        
        afficheImpactAdv();
    } 
    
    
    /**
     * Affichage cible joueur precedent
     * @param e 
     */
    @FXML public void JoueurPrecedent(InputEvent e) {
        initAllImpactAdv();
        
        int nbjoueur = _partie.nbJoueurs();
        
        if (nbjoueur != 1) {
            if (_numeroJoueurCourant == 0) {
                _joueurVisible = _partie.getJoueur();
                _numeroJoueurCourant = nbjoueur ;
            }
            else {
                _joueurVisible = ((PartieMulti) _partie).getAdversaire(_numeroJoueurCourant);
                _numeroJoueurCourant--;
            }
        }
        else {
            _joueurVisible = _partie.getJoueur();
        }
        
        pseudo.setText( _joueurVisible.getPseudo());
        afficheImpactAdv();
    }
    
    
    /**
     * Affichage impacts de l'adversaire
     */
    private void afficheImpactAdv() {
        ArrayList<Fleche> tab = _joueurVisible.getHistorique();
        int size = (tab.size())%10;
        int voleeParcourue = tab.size()/10;
        
        for (int k=0; k<size; k++) {
            marqueImpact((HBox) adversaire.getChildren().get(_PositionHBoxPremierImpactAdv+k), tab.get(10*voleeParcourue + k).getX(), tab.get(10*voleeParcourue + k).getY());
        }
    }
    
    
    
    /**
     * Tirer un carreau d'arbalète
     * @param e : clic de la souris
     */
    @FXML public void onMouseClickedShoot(InputEvent e) {
        
        if (_voleeCourante == _partie.getNbVolee() && _nbFlechesRestantes==0) {
            lancerFinDePartie();
            return;
        }
        if (_nbFlechesRestantes == 0) {
            if (_partie instanceof PartieSolo) {
                ((PartieSolo)_partie).majVent();
                ((PartieSolo)_partie).majDistance();
            }
            updateWind(_partie.getVitesseVentX(),_partie.getVitesseVentZ());
            updateDistance(_partie.getDistanceCible());
            
            initAllImpactAdv();
            _nbFlechesRestantes = 10;
            _voleeCourante++;
            Volee.setText("volée : " + _voleeCourante + "/" + _partie.getNbVolee());
            initAllImpact();
            return;
        }
        
        int score = _score;
        double pos[] = new double[2];
        pos = _traject.position(toRadians(_angleY)/5.0, toRadians(_angleX)/5.0, _arbalete.getPuissance(),_distanceCible, _partie.getVitesseVentX()/50.0, _partie.getVitesseVentZ()/50.0, _arbalete.getDispersion());
        _score = (int) (_score + _cible.getValeur(pos[0],pos[1]));
        
        
        score = _score-score;
        if (score==0) {
            Resultat.setText("MANQUÉ");
            
            if(_partie instanceof PartieMulti) {
                ((PartieMulti)_partie).sendShot(0, 0, 0);
            }
        } 
        else {
            Resultat.setText("+ " + score);
            double posX = pos[0]*cibleReel.getFitWidth() / _cible.getRayon();
            double posY = pos[1]*cibleReel.getFitHeight() / _cible.getRayon();
            marqueImpact((HBox) terrain.getChildren().get(_PositionHBoxPremierImpact + 10 - _nbFlechesRestantes), pos[0], pos[1]);
            if(_partie instanceof PartieMulti) {
                ((PartieMulti)_partie).sendShot(score, posX, posY);
            }
        }
        
        _nbFlechesRestantes--;
        FlecheRestante.setText("Reste " + _nbFlechesRestantes + "/10");
         
        Fleche f = new Fleche(pos[0], pos[1], score);
        _partie.getJoueur().addFleche(f);
        
        afficheImpactAdv();
        updateWind(_partie.getVitesseVentX(),_partie.getVitesseVentZ());
    }
    
    
    /**
     * Définition de la trajectoire du carreau
     * @param e : déplacement de la souris
     */
    @FXML public void onMouseMoved(MouseEvent e) {
        double width = terrain.getWidth();
        double height = terrain.getHeight();
        _angleX = 180*e.getX()/width -90;
        _angleY = 180*e.getY()/height -90;
        
        
        if (_angleX>=60) {
            _angleX = 60;
        }
        else if (_angleX<=-60) {
            _angleX = -60;
        }
        
        if (_angleY>=45) {
            _angleY = 45;
        }
        else if (_angleY<=-45) {
            _angleY = -45;
        }
        
        crossbow.setRotate(_angleX);
        profilArba.setRotate(_angleY+10);
        
        _angleY = -_angleY;
        Angles.setText("Angle horizontal : " + round(1000.0*_angleX)/1000.0 + "°"
                + "\nAngle vertical : " + round(1000.0*_angleY)/1000.0 +"°");
        
        
    }
    
    
    
    /**
     * Quitter la partie
     * @param e : clic de la souris sur le bouton Quitter
     */
    @FXML public void QuitterPartie(InputEvent e) {
        Platform.exit();
    }
    
    
    
    /**
     * Choisir l'arbalète à Poulie
     * @param e : clic souris
     */
    @FXML public void ChangeForArbaletePoulie(InputEvent e) {
        Image image = new Image("vue/img/arbpoulie.png");
        crossbow.setImage(image);
        _arbalete = _arbaleteAPoulie;
        updateNbCran();
    }
    
    
    /**
     * Choisir l'arbalète Médievale
     * @param e : clic souris
     */
    @FXML public void ChangeForArbaleteMedieval(InputEvent e) {
        Image image = new Image("vue/img/arbalete-genoise-plan.png");
        crossbow.setImage(image);
        _arbalete = _arbaleteMedievale;
        updateNbCran();
    }
    
    
    /**
     * Choisir l'arbalète de chasse
     * @param e : clic souris
     */
    @FXML public void ChangeForArbaleteChasse(InputEvent e) {
        Image image = new Image("vue/img/arbchasse.png");
        crossbow.setImage(image);
        _arbalete = _arbaleteDeChasse;
        updateNbCran();
    }
    
    
    /**
     * Changement du cran de l'arbalète
     * @param e : choix sur radioButton
     */
    @FXML public void ChangementCran(InputEvent e) {
        if (Cran1.isSelected()) {
            _arbalete.setCran(0);
        }
        else if (Cran2.isSelected()) {
            _arbalete.setCran(1);
        }
        else if (Cran3.isSelected()) {
            _arbalete.setCran(2);
        }
    }

    
    
    /**
     * Lancer la fin de la Partie
     */
    public void lancerFinDePartie(){
        try {
            if(_partie instanceof PartieSolo){
                _partie.getClassement();
    }
            else{
                //TODO partie multi
            }
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(finDePartie.class.getResource("finDePartie.fxml"));
            AnchorPane ecranFinal = (AnchorPane) loader.load();
            Scene scene = new Scene(ecranFinal);
            stage .setScene(scene);
            stage.setFullScreen(true);
            stage.show();
            FinDePartieController controller = loader.<FinDePartieController>getController();
            controller.initFinPartie(_partie);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    /**
     * Initialise la vue
     * @param partie 
     */
    public void initJeu(Partie partie){
        _partie = partie;
        _arbaleteMedievale = new ArbaleteMedievale();
        _arbaleteDeChasse = new ArbaleteDeChasse();
        _arbaleteAPoulie = new ArbaleteAPoulie();
        _arbalete = _arbaleteMedievale;        
        _cible = new Cible(0,0,0.6);
        _traject = new Trajectoire();
        _joueurVisible = _partie.getJoueur();
    
        
        updateWind(_partie.getVitesseVentX(),_partie.getVitesseVentZ());
        updateDistance(_partie.getDistanceCible());
        initImpact();
        initLabel();
        
        
        updateNbCran();
        
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Resultat.setText("");
                
        ToggleGroup selectCran = new ToggleGroup();
        Cran1.setToggleGroup(selectCran);
        Cran2.setToggleGroup(selectCran);
        Cran3.setToggleGroup(selectCran);
        
    }
    
    
    
    public void lancerDocController(Stage stage, Partie partie) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FXMLDocument.fxml"));
            
            AnchorPane configurationView = (AnchorPane) loader.load();
            

            Scene scene = new Scene(configurationView);
            
            stage.setScene(scene);
            stage.show();
            
            FXMLDocumentController ctrl = loader.getController();
            ctrl.initJeu(partie);

        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

  

}
