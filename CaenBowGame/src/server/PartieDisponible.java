/**
 * @Author Valentin Durand - ENSICAEN
 * @Project CaenBowClient
 * @Package client
 * @Class Client
 * @ Jan 4, 2017 1:37:04 PM
 */
package server;

public class PartieDisponible {
    int port;
    String name;
    String nbPlayer;

    public PartieDisponible(int port, String name, int nbPlayer) {
        this.port = port;
        this.name = name;
        this.nbPlayer = nbPlayer+"/4";
    }

    public String getName() {
        return name;
    }

    public String getNbPlayer() {
        return nbPlayer;
    }

    public int getPort() {
        return port;
    }
    
    
}
