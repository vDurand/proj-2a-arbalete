package player;
import outils.Fleche;
import java.util.ArrayList;


public class Joueur {
    private String _pseudo ;
    private  double _scoreVolee ;
    private ArrayList<Fleche> _fleches ;
	
    public Joueur(String pseudo) {
        _pseudo = pseudo ;
        _fleches = new ArrayList<>();
    }	

    public String getPseudo() {
        return _pseudo;
    }

    public void setPseudo(String _pseudo) {
        this._pseudo = _pseudo;
    }
	
    public void addFleche(Fleche e) {
        _fleches.add(e);
    }
	
    public int getScore() {
        int score = 0;
        for (int k=0; k<=_fleches.size() ; k++) {
            score += _fleches.get(k).getValeur();
        }
        return score;
    }
    
    public ArrayList<Fleche> getHistorique() {
        return _fleches;
    }

    public void setScore(int score) {
        _scoreVolee = score;
    }

}
