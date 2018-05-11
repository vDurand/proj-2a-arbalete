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
public class ArbaleteDeChasse extends Arbalete{
    
    public ArbaleteDeChasse() {
        _name = "Arbalète de chasse";
        _nbCran = 2;
        _vitesseInitial = new double[_nbCran];
        _vitesseInitial[0] = 150;
        _vitesseInitial[1] = 200;
        _rayonDispersion = 0.02;
        _cranCourant = 0;
        afficheArba();
    }
    
}
