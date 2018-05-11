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
public abstract class Arbalete {
    
    protected String _name;
    protected int _cranCourant; 
    
    protected int _nbCran; /* de 1 à 3 */
    protected double _vitesseInitial[]; /* Varie de 100 m/s à 300 m/s */
    protected double _rayonDispersion; /* de 0.00 à 0.10 m */
    protected String _description;
    
    protected void afficheArba(){
        
        String nbetoilePuis = "*";
        double quartilePuis = (double)(250-100)/4;
        double borneInf = 100 + quartilePuis;
        while (_vitesseInitial[_nbCran-1]>=borneInf) {
            borneInf+=quartilePuis;
            nbetoilePuis += "*";
        }
        
        String nbetoilePrec = "*";
        double prec = 1 - 10*_rayonDispersion;
        double quartilePrec = 0.2;
        double borneInf2 = quartilePrec;
        while (prec>=borneInf2) {
            borneInf2+=quartilePrec;
            nbetoilePrec += "*";
        }
        
        
        _description = _name + "\n\tPuissance : " + nbetoilePuis
                + "\n\tPrécision : " + nbetoilePrec
                + "\n\tNombre de Cran : " + _nbCran;
    }
    
    public int getNbCran(){
        return _nbCran;
    }
    
    public void setCran(int cran){
        _cranCourant = cran;
    }
    
    public double getPuissance(){
        return _vitesseInitial[_cranCourant];
    }
    
    public String getDescription() {
        return _description;
    }
    
    public double getDispersion() {
        return _rayonDispersion;
    }
    
}
