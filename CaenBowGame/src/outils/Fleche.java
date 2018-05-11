package outils;

public class Fleche {
	
	private final double _x ;
	private final double _y ;
	private final int _valeur ;

	public Fleche(double x , double y,int score){
            _x=x;
            _y=y;
            _valeur=score;
	}
        
        public int getValeur(){
		return _valeur ; 
	}
        
        public double getX(){
            return _x;
        }
        
        public double getY(){
            return _y;
        }

}
