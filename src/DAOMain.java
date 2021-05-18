import DAO.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.sql.SQLException;

/**
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public class DAOMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        primaryStage.setTitle("Sasquatch Consulting");
        primaryStage.setScene(new Scene(root));
        double x = (Screen.getPrimary().getBounds().getWidth() - root.getBoundsInParent().getWidth())/2;
        double y = (Screen.getPrimary().getBounds().getHeight() - root.getBoundsInParent().getHeight())/2;
        primaryStage.setX(x);
        primaryStage.setY(y);
        primaryStage.setResizable(false);
        primaryStage.show();
    }



    public static void main(String[] args) throws SQLException {
        DBCache cache = DBCache.getInstance();
        launch(args);
        DBConnection.closeConnection();
    }
}
