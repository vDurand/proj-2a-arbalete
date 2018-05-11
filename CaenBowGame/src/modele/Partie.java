package modele;
import java.util.ArrayList;
import player.Joueur;

public abstract class Partie {
    protected double _vitesseVentX = 0;
    protected double _vitesseVentZ = 0;
    protected double _distanceCible = 0;
    protected Joueur _joueur;
    protected int _nbVolee;
    protected int _forceVent;
    protected int _dist;
   
    
    abstract public int nbJoueurs();
    
    abstract public ArrayList<Joueur> getClassement();
    
  
    
    public void setVitesseVent(double vx, double vz){
        _vitesseVentX = vx;
        _vitesseVentZ = vz;
    }
    
    public double getVitesseVentX(){
        return _vitesseVentX;
    }
    
    public double getVitesseVentZ(){
        return _vitesseVentZ;
    }
    
    public void setDistanceCible(double distance){
        _distanceCible = distance;
    }
   
    
    public Joueur getJoueur() {
        return _joueur ;
    }
    
    
    public int getNbVolee(){
        return _nbVolee;
    }
/*
    public void sendShot(int i, int i0, int i1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendShot(int score, double posX, double posY) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
*/
    public double getDistanceCible() {
        return _distanceCible;
    }
    
    
       
}
