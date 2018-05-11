package modele;

import server.*;
import java.util.* ;
import outils.Fleche;
import player.Joueur;

public class PartieMulti extends Partie {
    private final Connexion _connexion ; 
    private ArrayList<Joueur> _adversaires ;
    private ArrayList<Joueur> _classement;
    
	
    public PartieMulti(Joueur joueur, int nbVolee, Connexion c) {
        _connexion = c;
        _joueur = joueur;
        _nbVolee = nbVolee;
        _adversaires = new ArrayList<Joueur>();
    }
    
    @Override
    public ArrayList<Joueur> getClassement(){
        return _classement;
    }
    
    public boolean isEmptyClassement(){
        if(_classement == null)
            return true;
        return false;
    }
    
    public void setClassement(ArrayList<Joueur> c){
        _classement = c;
    }
	
    
    public void sendShot(int score, double x, double y) {
	_connexion.sendShot(score,x,y);	
    }
	
	
    @Override
    public int nbJoueurs() {
        return 1+_adversaires.size();
    }
    
    public Joueur findJoueur(String pseudo) {
    for(Joueur j : _adversaires) {
        if(j.getPseudo().equals(pseudo)) {
            return j;
            }
        }
    return null;
    }
    
    @Override
    public double getDistanceCible(){
        return _distanceCible;
    }
    
    public void addArrowToAdversary(String pseudo,double x,double y,int score){
        System.out.println("new arrow from network");
        Joueur j = findJoueur(pseudo);
        j.addFleche(new Fleche(x,y,score));
    }
    
    public void addNewAdversary(String pseudo){
        System.out.println("new adversary from network");
        _adversaires.add(new Joueur(pseudo));
    }
    
    public void addAdversary(Joueur j){
        _adversaires.add(j);
    }
    
    public void updateRound(double distance,double x,double y){
        _distanceCible = distance;
        _vitesseVentX = x;
        _vitesseVentZ = y;
        System.out.println("DISTANCE="+_distanceCible);
    }
    
    public void removeAdversary(String pseudo){
        _adversaires.remove(findJoueur(pseudo));
    }

    public Joueur getAdversaire(int _numeroJoueurCourant) {
        return _adversaires.get(_numeroJoueurCourant);
    }
   
    
}
