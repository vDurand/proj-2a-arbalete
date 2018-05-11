/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outils;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.tan;

/**
 *
 * @author moraine
 */
public class Trajectoire {
    private final double _f = 0.2;
    private final double _constGravitation = 9.81;
    private int _nbFleche;
    
    
    public Trajectoire(){
        _nbFleche = 10;
    }

    public double[] position(double angleV, double angleH, double vitInit, double dist, double vitesseVentX, double vitesseVentZ, double dispersion){
        double position[] = new double[2]; //position[0] -> axe horizontale et position[1] -> axe verticale
        double temps;
        double x = vitInit * sin(angleV) * cos(angleH);
        double y = vitInit * sin(angleV) * sin(angleH);
        double z = vitInit * cos(angleV);
        
        if(x!=0){
            temps = dist / (x - _f * vitesseVentX);
        }else{
            temps = dist / vitInit;
        }
        
        position[0] = (- _f * vitesseVentZ * temps + y * temps) * (-1+Math.random()*2) * dispersion; 
        position[1] = -0.5 * _constGravitation / (vitInit * vitInit) * dist * dist * (1 - tan(angleV) * tan(angleV)) + dist * tan(angleV) - _f * vitesseVentX * temps;

        _nbFleche = _nbFleche - 1;
        return position;
    }
    
}
