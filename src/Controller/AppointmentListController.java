package Controller;

import DAO.AppointmentDao;
import DAO.DBCache;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import static DAO.DBCache.*;

/**
 * Responsible for the control of the appointment list view. Sets up all the button controls and provides functionality
 * for adding, updating, and deleting appointments.
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public class AppointmentListController {

    @FXML
    private TableView<Appointment> appointmentsTableView;
    @FXML
    private TableColumn<Appointment, String> titleColumn, descriptionColumn, locationColumn, contactColumn, typeColumn;
    @FXML
    private TableColumn<Appointment, String> startTimeColumn, endTimeColumn, customerColumn, userColumn;
    @FXML
    private TableColumn<Appointment, Integer> idColumn;
    @FXML
    private Button backButton, addNewAppointmentButton, updateAppointmentButton, deleteAppointmentButton;
    @FXML
    private ComboBox<Contact> contactNameComboBox;
    @FXML
    private RadioButton weekRadioButton, monthRadioButton, allRadioButton;

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
     * Fills the appointment table with data from all the appointments filtering by the contact and week/month/all radio
     * button selected.
     */
    private void fillAppointmentTable() {
        ObservableList<Appointment> appointmentsList = filterAppointments();
        appointmentsTableView.setItems(appointmentsList);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));

        customerColumn.setCellValueFactory(data -> {
            if (data.getValue() instanceof Appointment){
                return new SimpleStringProperty(data.getValue().getCustomer().getName());
            } else {
                throw new RuntimeException("Unknown type.");
            }
        });

        contactColumn.setCellValueFactory(data -> {
            if (data.getValue() instanceof Appointment){
                return new SimpleStringProperty((String) data.getValue().getContact().getContactName());
            } else {
                throw new RuntimeException("Unknown type.");
            }
        });

        userColumn.setCellValueFactory(data -> {
            if (data.getValue() instanceof Appointment){
                return new SimpleStringProperty((String) data.getValue().getUser().getName());
            } else {
                throw new RuntimeException("Unknown type.");
            }
        });
    }

    /**
     * Filters all available appointments by the radio button (week/month/all)
     * @return filtered list of appointments by week/month/all
     */
    private ObservableList<Appointment> filterAppointments(){
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
        Collection<Appointment> appointmentsCollection = getInstance().getAppointmentHashMap().values();
        for (Appointment app : appointmentsCollection) {
            LocalDate date = app.getStartTime().toLocalDate();
            if (weekRadioButton.isSelected() && date.getYear() == LocalDate.now().getYear() &&
                    date.getDayOfYear() - LocalDate.now().getDayOfYear() <= 7 &&
                    date.getDayOfYear() - LocalDate.now().getDayOfYear() >= 0 &&
                    app.getContact().equals(contactNameComboBox.getSelectionModel().getSelectedItem())) {
                appointmentsList.add(app);
            } else if (monthRadioButton.isSelected() && date.getYear() == LocalDate.now().getYear() &&
                    LocalDate.now().getMonth().equals(date.getMonth()) &&
                    app.getContact().equals(contactNameComboBox.getSelectionModel().getSelectedItem())) {
                appointmentsList.add(app);
            } else if (allRadioButton.isSelected() &&
                    app.getContact().equals(contactNameComboBox.getSelectionModel().getSelectedItem())) {
                appointmentsList.add(app);
            }
        }
        return appointmentsList;
    }

    /**
     * Called to initialize a controller after its root element has been completely processed
     * Sets up combo box, radio buttons, add/delete/back buttons and fills up the appointment table
     */
    @FXML
    public void initialize(){
        setContactComboBox();
        setRadioButtons();
        setAddNewAppointmentButton();
        setDeleteAppointmentButton();
        setBackButton();
        fillAppointmentTable();
    }

    /**
     * Sets up the contact combo box, filling it up with all contacts available from the DB, selects first and prompts
     * filtering of the appointment table
     */
    private void setContactComboBox() {
        ObservableList<Contact> contactsList = FXCollections.observableArrayList();
        Collection<Contact> contactsCollection = DBCache.getInstance().getContactHashMap().values();
        contactsList.addAll(contactsCollection);
        contactNameComboBox.setItems(contactsList);
        contactNameComboBox.getSelectionModel().selectFirst();
        contactNameComboBox.setOnAction(comboBox -> fillAppointmentTable());
    }

    /**
     * Sets up week/month/all radio buttons and prompts filtering
     */
    private void setRadioButtons() {
        weekRadioButton.setOnAction(event -> fillAppointmentTable());
        monthRadioButton.setOnAction(event -> fillAppointmentTable());
        allRadioButton.setOnAction(event -> fillAppointmentTable());
    }

    /**
     * Sets up functionality for the add new appointment button by switching the scene to the add appointment
     */
    private void setAddNewAppointmentButton() {
        addNewAppointmentButton.setOnAction(event -> {
            stage = getStage("../view/AddAppointment.fxml", event);
            stage.show();
            setWindowPosition();
        });
    }

    /**
     * Sets up delete button and prompts confirmation alert for deleting the appointment
     */
    private void setDeleteAppointmentButton() {
        deleteAppointmentButton.setOnAction(event -> {
            if (!(appointmentsTableView.getSelectionModel().isEmpty())) {
                Alert deleteConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete the customer?");
                deleteConfirmation.setTitle("Confirmation");
                deleteConfirmation.setResizable(false);
                Optional<ButtonType> result = deleteConfirmation.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    AppointmentDao appointmentDao = new AppointmentDao();
                    Appointment appointment = appointmentsTableView.getSelectionModel().getSelectedItem();
                    appointmentDao.delete(appointment);
                    fillAppointmentTable();
                    var alert = new Alert(Alert.AlertType.INFORMATION, "Appointment " + appointment.getType() +
                                          " has been deleted.");
                    alert.setTitle("Deleted");
                    alert.setResizable(false);
                    alert.show();
                    }
                }
        });
    }

    /**
     * Sets up back button to switch the scene to the Main menu
     */
    private void setBackButton() {
        backButton.setOnAction(event -> {
            stage = getStage("../view/MainMenu.fxml", event);
            stage.show();
            setWindowPosition();
        });
    }

    /**
     * Sets up the functionality for the update button, changes the scene to the add appointment populating selected
     * appointment data
     * @param event event that triggered the actions
     */
    @FXML
    void onActionUpdate(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/AddAppointment.fxml"));
            loader.load();
            AddAppointmentController addAppointmentController = loader.getController();
            if (!(appointmentsTableView.getSelectionModel().isEmpty())){
                addAppointmentController.populateAppointmentData(appointmentsTableView.getSelectionModel().getSelectedItem());
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
