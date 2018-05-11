/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;


import vue.debutPartie.MultiSoloSelectingViewController;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author jean-paul
 */
public class Main extends Application {

    private Stage _stage;
    private MultiSoloSelectingViewController accueil;

    @Override
    public void start(Stage stage) throws Exception {
        Screen screen = Screen.getPrimary();
        _stage = stage;
        _stage.setTitle("CaenBow - Game");
        _stage.getIcons().add(new Image(getClass().getResourceAsStream("inconbis.png")));
        _stage.setX(screen.getVisualBounds().getMinX());
        _stage.setY(screen.getVisualBounds().getMinY());
        _stage.setWidth(screen.getVisualBounds().getWidth());
        _stage.setHeight(screen.getVisualBounds().getHeight());
        accueil = new MultiSoloSelectingViewController();
        accueil.lancerAccueilView(_stage);
        _stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
