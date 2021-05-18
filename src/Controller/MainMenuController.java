package Controller;

import DAO.DBCache;
import DAO.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.Appointment;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Iterator;

import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * Responsible for the control of the main menu. Sets up the functionality to switch to other screens and access the
 * reports
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public class MainMenuController {
    @FXML
    private Button addCustomerButton, customersButton, appointmentsButton, addAppointmentButton, byTypeAndMonthButton;
    @FXML
    private Button divisionCountryButton;
    @FXML
    private Label upcomingAppointmentLabel;

    private Stage stage;
    private Parent scene;

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
     * Calculates and sets the position of the current scene to show it on the desktop
     */
    private void setWindowPosition(){
        double x = (Screen.getPrimary().getBounds().getWidth() - scene.getBoundsInParent().getWidth())/2;
        double y = (Screen.getPrimary().getBounds().getHeight() - scene.getBoundsInParent().getHeight())/2;
        stage.setX(x);
        stage.setY(y);
        stage.setResizable(false);
    }

    /**
     * Called to initialize the controller after its root element has been completely processed
     * Sets up customers, appointments, add customer, and add appointment buttons
     */
    @FXML
    public void initialize(){

        Collection<Appointment> allAppointments = DBCache.getAppointmentHashMap().values();
        for (Appointment appointment : allAppointments) {
            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();
            LocalTime appTime = appointment.getStartTime().toLocalTime();
            if (appointment.getUser().equals(DBCache.getInstance().getUser()) &&
                    appointment.getStartTime().toLocalDate().equals(currentDate) &&
                    appTime.isAfter(currentTime) &&
                    MINUTES.between(appTime, currentTime) <= 15
            ) {
                upcomingAppointmentLabel.setText("You have an upcoming appointment: \n"
                        + appointment.getAppointmentId() + "\t"
                        + appointment.getStartTime().getMonth() + "-"
                        + appointment.getStartTime().getDayOfMonth() + "-"
                        + appointment.getStartTime().getYear() + "\tat\t" +
                        DateTimeFormatter.ofPattern("HH:mm").format(appTime));
            }
        }

        addCustomerButton.setOnAction(event -> {
            stage = getStage("../view/AddCustomer.fxml", event);
            stage.show();
            setWindowPosition();
        });

        customersButton.setOnAction(event -> {
            stage = getStage("../view/CustomersList.fxml", event);
            stage.show();
            setWindowPosition();
        });

        appointmentsButton.setOnAction(event -> {
            stage = getStage("../view/AppointmentsList.fxml", event);
            stage.show();
            setWindowPosition();
        });

        addAppointmentButton.setOnAction(event -> {
            stage = getStage("../view/AddAppointment.fxml", event);
            stage.show();
            setWindowPosition();
        });

        byTypeAndMonthButton.setOnAction(event -> {
            stage = getStage("../view/AppointmentsTypeMonth.fxml", event);
            stage.show();
            setWindowPosition();
        });

        divisionCountryButton.setOnAction(event -> {
            stage = getStage("../view/CustomersDivisionCountry.fxml", event);
            stage.show();
            setWindowPosition();
        });
    }
}
