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
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * Responsible for the control of the form to add a controller. Provides necessary text field and combo boxes to fill out
 * and choose appropriate data. Sets up save/back buttons. Eliminates the text fields to a particular limit
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public class AddCustomerController {

    @FXML
    private TextField nameTextField, addressTextField, postalCodeTextField, phoneNumberTextField, customerIdTextField;
    @FXML
    private ComboBox<FirstLevelDiv> divisionComboBox;
    @FXML
    private ComboBox<Country> countryComboBox;
    @FXML
    private Button saveButton, cancelButton, allCustomersButton;

    private Stage stage;
    private Parent scene;
    private Integer customerId;

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
     * Populates the given customer data into the form, if the customer isn't null
     * @param customer customer which data is populated into the form
     */
    public void populateCustomerData(Customer customer) {
        if (customer != null) {
            customerId = customer.getCustomerId();
            customerIdTextField.setText(Integer.toString(customer.getCustomerId()));
            nameTextField.setText(customer.getName());
            addressTextField.setText(customer.getAddress());
            postalCodeTextField.setText(customer.getPostalCode());
            phoneNumberTextField.setText(customer.getPhoneNumber());
            divisionComboBox.setValue(customer.getDivision());
            countryComboBox.setValue(customer.getDivision().getCountry());
        }
    }

    /**
     * Saves the information from the form into the DB and local cache. Validates that the fields are not not empty. Prompts
     * an alert if some text fields are empty
     * @return true if the customer has been saved successfully, false otherwise
     */
    private boolean saveCustomer(){
        String customerName = nameTextField.getText();
        String address = addressTextField.getText();
        String postalCode = postalCodeTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        FirstLevelDiv div = divisionComboBox.getSelectionModel().getSelectedItem();
        try {
            Customer customer = new Customer(customerId,
                    customerName,
                    address,
                    postalCode,
                    phoneNumber,
                    div.getDivisionId());
            CustomerDao customerDao = new CustomerDao();
            if (customerId == 0){
                customerDao.add(customer);
            } else {
                customerDao.update(customer);
            }
            return true;
        } catch (IllegalArgumentException ex){
            var alert = new Alert(Alert.AlertType.INFORMATION, "Make sure no fields are empty");
            alert.setTitle("Incomplete Form");
            alert.setResizable(false);
            alert.show();
            return false;
        }
    }

    /**
     * Called to initialize a controller after its root element has been completely processed
     * Sets up the limitations on the length of the text fields, coutnry and division combo boxe, as well as
     * save/cancel buttons.
     */
    @FXML
    public void initialize() {
        customerId = 0;
        nameTextField.lengthProperty().addListener(changeListener -> {
            if (nameTextField.getText().length() > 30) {
                nameTextField.setText(nameTextField.getText().substring(0, 30));
            }
        });
        addressTextField.lengthProperty().addListener(changeListener -> {
            if (addressTextField.getText().length() > 35) {
                addressTextField.setText(addressTextField.getText().substring(0, 35));
            }
        });

        postalCodeTextField.lengthProperty().addListener(changeListener -> {
            if (postalCodeTextField.getText().length() > 10) {
                postalCodeTextField.setText(postalCodeTextField.getText().substring(0, 10));
            }
        });

        phoneNumberTextField.lengthProperty().addListener(changeListener -> {
            if (phoneNumberTextField.getText().length() > 15) {
                phoneNumberTextField.setText(phoneNumberTextField.getText().substring(0, 15));
            }
        });

        setCountryComboBox();
        countryComboBox.getSelectionModel().selectFirst();
        setFirstLevelDivComboBox();
        divisionComboBox.getSelectionModel().selectFirst();
        setSaveButton();
        setCancelButton();
        setAllCustomersButton();
    }

    /**
     * Sets up country combo box with all the countries from the DB/local cache and functionality to filter the division
     * combo box
     */
    private void setCountryComboBox() {
        ObservableList<Country> countryList = FXCollections.observableArrayList();
        Collection<Country> countryCollection = DBCache.getCountryHashMap().values();
        countryList.addAll(countryCollection);
        countryComboBox.setItems(countryList);
        countryComboBox.setOnAction(event -> {
            setFirstLevelDivComboBox();
            divisionComboBox.getSelectionModel().selectFirst();
        });
    }

    /**
     * Sets up division combo box with all the divisions from the DB/local cache
     */
    private void setFirstLevelDivComboBox() {
        ObservableList<FirstLevelDiv> firstLevelDivList = FXCollections.observableArrayList();
        Collection<FirstLevelDiv> firstLevelDivCollection = DBCache.getFirstLevelDivHashMap().values();
        Iterator<FirstLevelDiv> iterator = firstLevelDivCollection.iterator();
        while (iterator.hasNext()){
            FirstLevelDiv firstLevelDiv = iterator.next();
            if (firstLevelDiv.getCountry().getName().equals(countryComboBox.getSelectionModel().getSelectedItem().getName())){
                firstLevelDivList.add(iterator.next());
            }
        }
        divisionComboBox.setItems(firstLevelDivList);
    }

    /**
     * Sets up save button by calling save customers and changing the scene to the customers list
     */
    private void setSaveButton() {
        saveButton.setOnAction(event -> {
            if (saveCustomer()) {
                stage = getStage("../view/CustomersList.fxml", event);
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
     * Sets up all customers button by changing the scene to the customer list menu
     */
    private void setAllCustomersButton() {
        allCustomersButton.setOnAction(event -> {
            stage = getStage("../view/CustomersList.fxml", event);
            stage.show();
            setWindowPosition();
        });
    }
}
