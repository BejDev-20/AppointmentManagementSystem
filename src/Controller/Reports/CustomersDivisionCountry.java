package Controller.Reports;

import DAO.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.Country;
import model.FirstLevelDiv;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;

/**
 * Responsible for generating and displaying the report on all the customers grouped by their location (division and
 * country)
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public class CustomersDivisionCountry {

    @FXML
    private Button backButton;
    @FXML
    private TableView<DivisionCountry> divisionAndCountryTableView;
    @FXML
    private TableColumn<DivisionCountry, Integer> numberOfCustomersColumn;
    @FXML
    private TableColumn<DivisionCountry, FirstLevelDiv> divisionColumn;
    @FXML
    private TableColumn<DivisionCountry, Country> countryColumn;

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
     * Called to initialize a controller after its root element has been completely processed
     * Sets up table view, retrieves data for types of appointments for each month, as well as functionality for the
     * back button
     * */
    @FXML
    public void initialize(){
        ObservableList<DivisionCountry> allData = FXCollections.observableArrayList();
        try{
            String sql = "SELECT COUNT(Customer_ID) as Count, Division_ID FROM customers GROUP BY Division_ID;";
            PreparedStatement ps = DBConnection.getConn().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int count = rs.getInt("Count");
                int division = rs.getInt("Division_ID");
                DivisionCountry oneRow = new DivisionCountry(count, division);
                allData.add(oneRow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        divisionAndCountryTableView.setItems(allData);
        numberOfCustomersColumn.setCellValueFactory(new PropertyValueFactory<>("customerCount"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("firstLevelDivision"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        backButton.setOnAction(event -> {
            stage = getStage("/view/MainMenu.fxml", event);
            stage.show();
            setWindowPosition();
        });
        }

    }

