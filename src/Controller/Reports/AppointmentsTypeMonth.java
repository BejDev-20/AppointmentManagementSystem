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

import java.io.IOException;
import java.sql.*;
import java.time.Month;

/**
 * Responsible for generating and displaying the report on all the appointments grouped by the type and month
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public class AppointmentsTypeMonth {

    @FXML
    private Button backButton;
    @FXML
    private TableView<TypeAndMonth> typeAndMonthTableView;
    @FXML
    private TableColumn<TypeAndMonth, Integer> numberOfAppointmentsColumn;
    @FXML
    private TableColumn<TypeAndMonth, Month> monthColumn;
    @FXML
    private TableColumn<TypeAndMonth, String> typeColumn;

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
     */
    @FXML
    public void initialize(){
        ObservableList<TypeAndMonth> allData = FXCollections.observableArrayList();
        try{
            String sql = "SELECT count(Appointment_ID) as Count,  MONTH(Start) as Month, Type FROM WJ07OFg.appointments " +
                         "GROUP BY Month, Type;";
            PreparedStatement ps = DBConnection.getConn().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int count = rs.getInt("Count");
                int month = rs.getInt("Month");
                String type = rs.getString("Type");
                TypeAndMonth oneRow = new TypeAndMonth(count, Month.of(month), type);
                allData.add(oneRow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        typeAndMonthTableView.setItems(allData);
        numberOfAppointmentsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfAppointments"));
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        backButton. setOnAction(event -> {
            stage = getStage("/view/MainMenu.fxml", event);
            stage.show();
            setWindowPosition();
        });
        }

    }

