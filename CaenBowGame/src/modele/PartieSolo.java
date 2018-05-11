package modele;

import java.util.ArrayList;
import player.Joueur;

public class PartieSolo extends Partie {
    
    public PartieSolo(Joueur joueur, int nbVolee, int forceVent, int distance) {
        _joueur = joueur;
        _nbVolee = nbVolee;
        _forceVent = forceVent;
        _dist = distance;
        initialiseVent(forceVent);
        initialiseDistance(distance);
    }
    
    public void majVent(){
        initialiseVent(_forceVent);
    }
      
    public void majDistance(){
        initialiseDistance(_dist);
    }
    
    protected void initialiseVent(int forceVent){
        if(forceVent==0){
            _vitesseVentX = 0;
            _vitesseVentZ = 0;
        }
        else if(forceVent==1){
            _vitesseVentX = 1 + Math.random() * 14;
            _vitesseVentZ = 1 + Math.random() * 14;
        }
        else if(forceVent==2){
            _vitesseVentX = 16 + Math.random() * 29;
            _vitesseVentZ = 16 + Math.random() * 29;
        }
        else{
            _vitesseVentX = 46 + Math.random() * 24;
            _vitesseVentZ = 46 + Math.random() * 24;
        }
    }

    protected void initialiseDistance(int distance) {
        if(distance==0){
            _distanceCible = 10 + Math.random() * 35;
        }
        else if(distance==1){
            _distanceCible = 51 + Math.random() * 74;
        }
        else{
            _distanceCible = 126 + Math.random() * 74;
        }
    }
    
    @Override
    public int nbJoueurs(){
        return 1;
    }
    
    @Override
    public ArrayList<Joueur> getClassement(){
        ArrayList<Joueur> resultats = new ArrayList<>();
        resultats.add(_joueur);
        return resultats;
    }
    
}
