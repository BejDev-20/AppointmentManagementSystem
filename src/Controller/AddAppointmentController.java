package Controller;

import DAO.AppointmentDao;
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
import model.Appointment;
import model.Contact;
import model.Customer;
import java.io.IOException;
import java.time.*;
import java.util.*;

/**
 * Responsible for the control of the form to add an appointment. Provides necessary text field and combo boxes to fill out
 * and choose appropriate data. Sets up save/back buttons. Eliminates the text fields to a particular limit
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public class AddAppointmentController {

    @FXML
    private Button saveButton, cancelButton, allAppointmentsButton;
    @FXML
    private TextField appointmentTextField, titleTextField, typeTextField, locationTextField;
    @FXML
    private ComboBox<Contact> contactComboBox;
    @FXML
    private ComboBox<LocalTime> startTimeComboBox;
    @FXML
    private ComboBox<LocalTime> endTimeComboBox;
    @FXML
    private ComboBox<Customer> customersComboBox;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private DatePicker datePicker;

    private Stage stage;
    private Parent scene;
    private Integer appointmentId;

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
     * Populates the given appointment data into the form, if the appointment isn't null
     * @param appointment appointment which data is populated into the form
     */
    public void populateAppointmentData(Appointment appointment){
        if (appointment != null) {
            appointmentId = appointment.getAppointmentId();
            datePicker.setValue(appointment.getStartTime().toLocalDate());
            startTimeComboBox.setValue(appointment.getStartTime().toLocalTime());
            endTimeComboBox.setValue(appointment.getEndTime().toLocalTime());
            appointmentTextField.setText(Integer.toString(appointment.getAppointmentId()));
            contactComboBox.setValue(appointment.getContact());
            titleTextField.setText(appointment.getTitle());
            typeTextField.setText(appointment.getType());
            locationTextField.setText(appointment.getLocation());
            descriptionTextArea.setText(appointment.getDescription());
        }
    }

    /**
     * Saves the information from the form into the DB and local cache. Validates that the fields are not not empty. Prompts
     * an alert if some text fields are empty
     * @return true if the appointment has been saved successfully, false otherwise
     */
    private boolean saveAppointment(){
        try {
            LocalDate date = datePicker.getValue();
            LocalTime startTime = startTimeComboBox.getValue();
            LocalTime endTime = endTimeComboBox.getValue();
            LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
            LocalDateTime endDateTime = LocalDateTime.of(date, endTime);
            int customerId = customersComboBox.getSelectionModel().getSelectedItem().getCustomerId();
            int userId = DBCache.getInstance().getUser().getUserId();
            boolean overlaps = checkOverlap(startDateTime, endDateTime, customersComboBox.getSelectionModel().getSelectedItem());
            if (overlaps){
                throw new NullPointerException();
            }
            Appointment app = new Appointment(appointmentId,
                    titleTextField.getText(),
                    descriptionTextArea.getText(),
                    locationTextField.getText(),
                    typeTextField.getText(),
                    startDateTime,
                    endDateTime,
                    customerId,
                    userId,
                    contactComboBox.getSelectionModel().getSelectedItem().getContactId());
            AppointmentDao appointmentDao = new AppointmentDao();
            if (appointmentId == 0){
                appointmentDao.add(app);
            } else {
                appointmentDao.update(app);
            }
            return true;
        } catch (IllegalArgumentException ex){
            var alert = new Alert(Alert.AlertType.INFORMATION, "Make sure no fields are empty");
            alert.setTitle("Incomplete Form");
            alert.setResizable(false);
            alert.show();
        } catch (NullPointerException ex){
            var alert = new Alert(Alert.AlertType.INFORMATION, "Looks like the new appointment overlaps an existing one ");
            alert.setTitle("Overlap appointment");
            alert.setResizable(false);
            alert.show();
        }
        return false;
    }

    private boolean checkOverlap(LocalDateTime startDateTime, LocalDateTime endDateTime, Customer customer) {
        Collection<Appointment> allAppointments = DBCache.getAppointmentHashMap().values();
        Iterator<Appointment> iterator = allAppointments.iterator();
        LocalTime[][] appArray = new LocalTime[56][2];
        int index = 0;
        while(iterator.hasNext()){
            Appointment appointment = iterator.next();
            if (appointment.getCustomer().equals(customer) &&
                    appointment.getStartTime().toLocalDate().equals(startDateTime.toLocalDate())){
                appArray[index][0] = appointment.getStartTime().toLocalTime();
                appArray[index][1] = appointment.getEndTime().toLocalTime();
                index++;
            }
        }
        LocalTime startTime = startDateTime.toLocalTime();
        LocalTime endTime = endDateTime.toLocalTime();
        for (int idx = 0; idx < index; idx++){
            LocalTime tempStart = appArray[idx][0];
            LocalTime tempEnd = appArray[idx][1];
            if (startTime.equals(tempStart) || (startTime.isAfter(tempStart) && startTime.isBefore(tempEnd))
                    || endTime.equals(tempEnd) || (endTime.isBefore(tempEnd) && endTime.isAfter(tempStart)) ||
                    tempStart.equals(startTime) || (tempStart.isAfter(startTime) && tempStart.isBefore(endTime))
                    || tempEnd.equals(endTime) || (tempEnd.isBefore(endTime) && tempEnd.isAfter(startTime))){
                return true;
            }
        }
        return false;
    }

    /**
     * Called to initialize a controller after its root element has been completely processed
     * Sets up the limitations on the length of the text fields, contact, time, and customer combo boxes, as well as
     * save/cancel buttons.
     */
    @FXML
    public void initialize(){
        titleTextField.lengthProperty().addListener(changeListener -> {
            if (titleTextField.getText().length() > 20) {
                titleTextField.setText(titleTextField.getText().substring(0, 20));
            }
        });

        typeTextField.lengthProperty().addListener(changeListener -> {
            if (typeTextField.getText().length() > 20) {
                typeTextField.setText(typeTextField.getText().substring(0, 20));
            }
        });

        locationTextField.lengthProperty().addListener(changeListener -> {
            if (locationTextField.getText().length() > 20) {
                locationTextField.setText(locationTextField.getText().substring(0, 20));
            }
        });

        descriptionTextArea.lengthProperty().addListener(changeListener -> {
            if (descriptionTextArea.getText().length() > 250) {
                descriptionTextArea.setText(descriptionTextArea.getText().substring(0, 250));
            }
        });

        setContactComboBox();
        setTimeComboBoxes();
        datePicker.setValue(LocalDate.now());
        setCustomerComboBox();
        setSaveButton();
        setCancelButton();
        setAllAppointmentsButton();
        appointmentId = 0;
    }

    /**
     * Sets up contact combo box with all the contacts from the DB/local cache
     */
    private void setContactComboBox() {
        ObservableList<Contact> contactsList = FXCollections.observableArrayList();
        Collection<Contact> contactsCollection = DBCache.getInstance().getContactHashMap().values();
        contactsList.addAll(contactsCollection);
        contactComboBox.setItems(contactsList);
        contactComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Sets up time combo box with times from the the start of the business hours to the end of it.
     */
    private void setTimeComboBoxes() {
        ObservableList<LocalTime> startTimes = setUpTime(Appointment.getSTART_HOUR(), Appointment.getEND_HOUR());
        ObservableList<LocalTime> endTimes = setUpTime(Appointment.getSTART_HOUR().plusMinutes(15), Appointment.getEND_HOUR().plusMinutes(30));

        startTimeComboBox.setItems(startTimes);
        endTimeComboBox.setItems(endTimes);
        startTimeComboBox.getSelectionModel().selectFirst();
        endTimeComboBox.getSelectionModel().selectFirst();

        startTimeComboBox.setOnAction(event ->{
            if (startTimeComboBox.getSelectionModel().getSelectedIndex() > endTimeComboBox.getSelectionModel().getSelectedIndex()) {
                endTimeComboBox.setValue(startTimeComboBox.getValue().plusMinutes(15));
            }
        });

        endTimeComboBox.setOnAction(event ->{
            if (endTimeComboBox.getSelectionModel().getSelectedIndex() < startTimeComboBox.getSelectionModel().getSelectedIndex()) {
                startTimeComboBox.setValue(endTimeComboBox.getValue().minusMinutes(15));
            }
        });
    }


    /**
     * Creates a list of all the times an appointment can be scheduled for, from opening to closing of the business
     * hours incrementing by 15 mins.
     * @param start start time of the business hours
     * @param end end time of the business hours
     * @return the list of all generated times
     */
    private ObservableList<LocalTime> setUpTime(LocalTime start, LocalTime end){
        ObservableList<LocalTime> times = FXCollections.observableArrayList();
        while (start.isBefore(end.minusMinutes(29))){
            LocalDate date = LocalDate.now();
            ZoneId estZoneId = ZoneId.of("America/Thunder_Bay");
            ZonedDateTime estZDT = ZonedDateTime.of(date, start, estZoneId);
            ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
            Instant estToGMTInstant = estZDT.toInstant();
            ZonedDateTime estToLocal = estZDT.withZoneSameInstant(localZoneId);
            LocalTime time = LocalTime.of(estToLocal.getHour(), estToLocal.getMinute());
            times.add(time);
            start = start.plusMinutes(15);
        }
        return times;
    }

    /**
     * Set us customer combo box with all the customers from the DB/local cache
     */
    private void setCustomerComboBox() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        Collection<Customer> customerCollection = DBCache.getInstance().getCustomerHashMap().values();
        customerList.addAll(customerCollection);
        customersComboBox.setItems(customerList);
        customersComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Sets up save button by calling save appointments and changing the scene to the appointment list
     */
    private void setSaveButton() {
        saveButton.setOnAction(event -> {
            if (saveAppointment()) {
                stage = getStage("../view/AppointmentsList.fxml", event);
                stage.show();
                setWindowPosition();
            }
        });
    }

    /**
     * Sets up cancel button by changing the scene to the main menu
     */
    private void setCancelButton() {
        cancelButton.setOnAction(event -> {
            stage = getStage("../view/MainMenu.fxml", event);
            stage.show();
            setWindowPosition();
        });
    }

    /**
     * Sets up all appointments button by changing the scene to the appointment list menu
     */
    private void setAllAppointmentsButton() {
        allAppointmentsButton.setOnAction(event -> {
            stage = getStage("../view/AppointmentsList.fxml", event);
            stage.show();
            setWindowPosition();
        });
    }
}
