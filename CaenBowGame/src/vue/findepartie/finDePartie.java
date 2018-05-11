
package vue.findepartie;

import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javafx.application.Application;
import static javafx.fxml.FXMLLoader.load;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Guaquiere
 */
public class finDePartie extends Application {
    
    private static final Logger LOG = getLogger(finDePartie.class.getName());
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = load(getClass().getResource("finDePartie.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }
    
}
