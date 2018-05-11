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
public class ArbaleteAPoulie extends Arbalete {

    public ArbaleteAPoulie() {
        _name = "Arbalète à poulie";
        _nbCran = 3;
        _vitesseInitial = new double[_nbCran];
        _vitesseInitial[0] = 150;
        _vitesseInitial[1] = 200;
        _vitesseInitial[2] = 250;
        _rayonDispersion = 0.01;
        _cranCourant = 0;
        afficheArba();
    }
    
}
