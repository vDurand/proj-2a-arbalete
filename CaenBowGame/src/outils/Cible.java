/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outils;

/**
 *
 * @author moraine
 */
public class Cible {
    
    private final double _xCentre;
    private final double _yCentre;
    private final double _rayon;
    //private final double _distance;
    
    public Cible(double xCentre, double yCentre, double rayon){
        _xCentre = xCentre; //Horizontal
        _yCentre = yCentre; //vertical
        _rayon = rayon;
    }
    
    public double getRayon() {
        return _rayon;
    }
    
    public int getValeur(double x, double y){
        double distanceC;
        double score;
        distanceC = (x-_xCentre)*(x-_xCentre)+(y-_yCentre)*(y-_yCentre);
        if(_rayon*_rayon-distanceC < 0){
            return 0;
        }
        score = ((_rayon*_rayon)-distanceC)/(_rayon*_rayon)*10;
        if(score%1==0){
            return (int) score;
        }
        score = score +1;
        return (int) score;
    }
    
}
