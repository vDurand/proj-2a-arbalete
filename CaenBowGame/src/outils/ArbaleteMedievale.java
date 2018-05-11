/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outils;

import outils.Arbalete;

/**
 *
 * @author moraine
 */
public class ArbaleteMedievale extends Arbalete {
    
    public ArbaleteMedievale() {
        _name = "Arbalète médiévale";
        _nbCran = 1;
        _vitesseInitial = new double[_nbCran];
        _vitesseInitial[0] = 102;
        _rayonDispersion = 0.1;
        _cranCourant = 0;
        afficheArba();
    }
    
}
