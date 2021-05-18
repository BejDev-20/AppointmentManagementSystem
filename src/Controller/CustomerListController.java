package Controller;

import DAO.CustomerDao;
import DAO.DBCache;
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
import model.Customer;
import model.FirstLevelDiv;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

/**
 * Responsible for the control of the customer list view. Sets up all the button controls and provides functionality
 * for adding, updating, and deleting customers.
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public class CustomerListController {

    @FXML
    private Button backButton, addNewCustomerButton, updateCustomerButton, deleteCustomerButton;
    @FXML
    private TableView<Customer> customerTableView;
    @FXML
    private TableColumn<Customer, String> customerNameColumn, addressColumn, postalCodeColumn, phoneNumberColumn;
    @FXML
    private TableColumn<Customer, FirstLevelDiv> divisionIdColumn;

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
            // throws an exception for the .load() method if it is not possible to load the hierarchy from the FXML doc
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
     * Fills the customer table with data from all the customers from the DB
     */
    private void fillCustomerTable(){
        ObservableList<Customer> customersList = FXCollections.observableArrayList();
        Collection<Customer> customersCollection = DBCache.getInstance().getCustomerHashMap().values();
        customersList.addAll(customersCollection);
        customerTableView.setItems(customersList);
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        divisionIdColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
    }

    /**
     * Called to initialize a controller after its root element has been completely processed
     * Sets up add/delete/back buttons and fills up the customer table
     */
    @FXML
    public void initialize(){
        setAddNewCustomerButton();
        setDeleteCustomerButton();
        setBackButton();
        fillCustomerTable();
    }

    /**
     * Sets up functionality for the add new customer button by switching the scene to the add customer
     */
    private void setAddNewCustomerButton(){
        addNewCustomerButton.setOnAction(event -> {
            stage = getStage("../view/AddCustomer.fxml", event);
            stage.show();
            setWindowPosition();
        });
    }

    /**
     * Sets up the functionality for the update button, changes the scene to the add customer populating selected
     * customer data
     * @param event event that triggered the actions
     */
    @FXML
    private void onActionUpdate(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/AddCustomer.fxml"));
            loader.load();
            AddCustomerController addCustomerController = loader.getController();
            if (!(customerTableView.getSelectionModel().isEmpty())){
                addCustomerController.populateCustomerData(customerTableView.getSelectionModel().getSelectedItem());
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up delete button and prompts confirmation alert as well as informational one in case the customer cannot
     * be deleted due to appointments associated with that customer
     */
    private void setDeleteCustomerButton() {
        deleteCustomerButton.setOnAction(event -> {
            if (!(customerTableView.getSelectionModel().isEmpty())) {
                Alert deleteConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete the customer?");
                deleteConfirmation.setTitle("Confirmation");
                deleteConfirmation.setResizable(false);
                Optional<ButtonType> result = deleteConfirmation.showAndWait();
                if (result.isPresent() & result.get() == ButtonType.OK) {
                    Customer customer = customerTableView.getSelectionModel().getSelectedItem();
                    if (hasAppointments(customer)){
                        var alert = new Alert(Alert.AlertType.ERROR, "Customers who have appointments cannot be deleted");
                        alert.setTitle("Delete Error");
                        alert.setResizable(false);
                        alert.show();
                    } else {
                        CustomerDao customerDao = new CustomerDao();
                        customerDao.delete(customerTableView.getSelectionModel().getSelectedItem());
                        fillCustomerTable();
                        var alert = new Alert(Alert.AlertType.INFORMATION, "Customer " + customer.getName() +
                                " has been deleted.");
                        alert.setTitle("Deleted");
                        alert.setResizable(false);
                        alert.show();
                    }
                }
            }
        });
    }

    /**
     * Checks if the customer has any appointments associated with it
     * @param customer customer to check if any appointments are associated with it
     * @return true if customer has appoiintments associated, false otherwise
     */
    private boolean hasAppointments(Customer customer){
        boolean hasApps = false;
        Collection<Appointment> appointments = DBCache.getAppointmentHashMap().values();
        for (Appointment appointment : appointments) {
            if (appointment.getCustomer().equals(customer)) {
                hasApps = true;
                break;
            }
        }
        return hasApps;
    }

    /**
     * Sets up back button to switch the scene to the Main menu
     */
    private void setBackButton(){
        backButton.setOnAction(event -> {
            stage = getStage("../view/MainMenu.fxml", event);
            stage.show();
            setWindowPosition();
        });
    }
}
