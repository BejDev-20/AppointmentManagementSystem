package Controller;
import DAO.DBCache;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.*;
import java.time.format.TextStyle;
import java.util.*;

/**
 * Responsible for the control of the login form. Provides necessary text field for username and password.
 * Sets up login/exit buttons.
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public class LoginController {

    @FXML
    private TextField usernameTextField, passwordTextField;
    @FXML
    private Button loginButton, exitButton;
    @FXML
    private Label localeLabel;

    private Stage stage;
    private Parent scene;
    File logFile;

    /**
     * Retrieves the stage from the given path and event
     * @param FXMLPath path of the FXML document to set up the next scene
     * @param event that triggers the action
     * @return the stage from the given path and event
     */
    private Stage getStage(String FXMLPath, ActionEvent event){
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        try {
            scene = FXMLLoader.load(getClass().getResource(FXMLPath));
            stage.setScene(new Scene(scene));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stage;
    }

    /**
     * Sets the login screen to the middle of the user's display
     */
    private void setWindowPosition(){
        double x = (Screen.getPrimary().getBounds().getWidth() - scene.getBoundsInParent().getWidth())/2;
        double y = (Screen.getPrimary().getBounds().getHeight() - scene.getBoundsInParent().getHeight())/2;
        stage.setX(x);
        stage.setY(y);
        stage.setResizable(false);
    }

    /**
     * Called to initialize a controller after its root element has been completely processed
     * Sets up login and exit buttons, pops alert if username or password isn't correct
     */
    @FXML
    public void initialize() {
        logFile = new File("login_activity.txt");
        setUpLocalization();
        String zoneID = ZoneId.systemDefault().toString();
        localeLabel.setText(zoneID.substring(zoneID.indexOf('/')+1));
        loginButton.setOnAction(event -> {
            try {
                String username = usernameTextField.getText();
                String password = passwordTextField.getText();
                User user = new User(0, username, password);
                if (searchForUser(user) != null) {
                    stage = getStage("/view/MainMenu.fxml", event);
                    stage.show();
                    setWindowPosition();
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException ex) {
                showAlert();
            }
        });

        exitButton.setOnAction(event -> System.exit(0));
    }

    /**
     * Launches the alert if the user notifying the user that incorrect password/username was used. Localizes the
     * messages to French if needed.
     */
    private void showAlert() {
        var alert = new Alert(Alert.AlertType.ERROR, "Incorrect user name or password");
        alert.setTitle("Login Error");
        try {
            ResourceBundle rb = ResourceBundle.getBundle("Nat", Locale.getDefault());
            if (Locale.getDefault().getLanguage().contains("fr")){
                alert.setContentText(rb.getString("Username") + " " + rb.getString("or") + " " +
                        rb.getString("Password").toLowerCase() + " " + rb.getString("Incorrect"));
                alert.setTitle(rb.getString("Login") + " " + rb.getString("Error"));
            }
        } catch(MissingResourceException ex){}
        alert.setResizable(false);
        alert.show();
    }

    private void setUpLocalization() {
        try {
            ObservableList<String> languages = FXCollections.observableArrayList();
            languages.add("EN");
            languages.add("FR");
            ResourceBundle rb = ResourceBundle.getBundle("Nat", Locale.getDefault());
            if (Locale.getDefault().getLanguage().equals("fr")) {
                usernameTextField.setPromptText(rb.getString("Username"));
                passwordTextField.setPromptText(rb.getString("Password"));
                loginButton.setText(rb.getString("Password"));
                exitButton.setText(rb.getString("Exit"));
            }
        } catch(MissingResourceException ex){}
    }

    /**
     * Verifies the information from the form (username/password) to the list of existing users. Returns the user
     * if found, null otherwise
     * @param user user that needs to be verified
     * @return the user if found, null if no matching user exists
     */
    private User searchForUser(User user) {
        DBCache dbCache = DBCache.getInstance();
        HashMap<Integer, User> users = dbCache.getUserHashMap();
        users.put(1, new User(1,"test", "test"));
        User userToReturn = null;
        for(User oneUser : users.values()){
            if(oneUser.equals(user)){
                DBCache.getInstance().setUser(oneUser);
                userToReturn = oneUser;
            }
        }
        try {
            LocalDateTime dateTime = LocalDateTime.now();
            ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
            ZonedDateTime localZDT = ZonedDateTime.of(dateTime.toLocalDate(), dateTime.toLocalTime(), localZoneId);
            ZoneId utcZoneId = ZoneId.of("UTC");
            Instant localToGMTInstant = localZDT.toInstant();
            ZonedDateTime localToUtc = localZDT.withZoneSameInstant(utcZoneId);

            FileWriter myWriter = new FileWriter(logFile, true);
            myWriter.append(localToUtc.toLocalDate() + "\t" + localToUtc.toLocalTime() + "\t\t" + user.getName() + "\t");
            if (userToReturn != null){
                myWriter.append("Successful login\n");
            } else {
                myWriter.append("Failed login\n");
            }
            myWriter.close();
        } catch (IOException ex){}
        return userToReturn;
    }


}
