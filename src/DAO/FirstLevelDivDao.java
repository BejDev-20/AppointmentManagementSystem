package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FirstLevelDiv;
import java.sql.*;
import java.time.LocalDateTime;
import static DAO.DBCache.getInstance;

/**
 * Represents a DAO for firstLevelDivs table providing the functionality to get all firstLevelDivs from the table,
 * add, update, or delete an appointment as well as retrieve one by the ID.
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public class FirstLevelDivDao implements DAO<FirstLevelDiv> {

    /**
     * Retrieves all the firstLevelDivs from the database and saves each into the local cache
     * @return list of all the firstLevelDivs retrieved
     */
    @Override
    public ObservableList<FirstLevelDiv> getAll() {
        ObservableList<FirstLevelDiv> allFirstLevelDiv = FXCollections.observableArrayList();
        try{
            String sql = "SELECT Division_ID, Division, COUNTRY_ID FROM first_level_divisions";
            PreparedStatement ps = DBConnection.getConn().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int divisionId = rs.getInt("Division_ID");
                String division = rs.getString("Division");
                int countryId = rs.getInt("COUNTRY_ID");
                FirstLevelDiv firstLevelDiv = new FirstLevelDiv(divisionId, division, countryId);
                allFirstLevelDiv.add(firstLevelDiv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allFirstLevelDiv;
    }

    /**
     * Retrieves a firstLevelDiv by the ID
     * @param id id of the firstLevelDiv to be retrieved
     * @return firstLevelDiv with the id passed into the method
     */
    @Override
    public FirstLevelDiv getById(int id) {
        FirstLevelDiv firstLevelDiv = null;
        try{
            String sql = "SELECT Division_ID, Division, COUNTRY_ID FROM first_level_divisions WHERE Division_ID = " + id;
            PreparedStatement ps = DBConnection.getConn().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                if(id == rs.getInt("Division_ID")) {
                    int divisionId = rs.getInt("Division_ID");
                    String divisionName = rs.getString("Division");
                    int countryId = rs.getInt("COUNTRY_ID");
                    firstLevelDiv = new FirstLevelDiv(divisionId, divisionName, countryId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (firstLevelDiv != null) DBCache.getInstance().updateFirstLevelDiv();
        return firstLevelDiv;
    }

    /**
     * Adds the given firstLevelDiv to the database as well as cache
     * @param item firstLevelDiv to be added to the database and cache
     * @return true if the firstLevelDiv is added
     */
    @Override
    public boolean add(FirstLevelDiv item) {
        try {
            String sql = "INSERT INTO first_level_divisions VALUES(NULL, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psti = DBConnection.getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            psti.setString(1, item.getName());
            psti.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            psti.setString(3, getInstance().getUser().getName());
            psti.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            psti.setString(5, getInstance().getUser().getName());
            psti.setInt(6, item.getCountry().getCountryId());
            psti.execute();
            DBCache.getInstance().updateFirstLevelDiv();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Updates the given firstLevelDiv in the database as well as cache
     * @param item firstLevelDiv to be updated in the database and cache
     * @return true if the firstLevelDiv is updated
     */
    @Override
    public boolean update(FirstLevelDiv item) {
        try {
            String sql = "UPDATE first_level_divisions SET Division = ?, Last_Update = ?, Last_Updated_By = ?, " +
                    "COUNTRY_ID=? WHERE Country_ID = ? ;";
            PreparedStatement psti = DBConnection.getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            psti.setString(1, item.getName());
            psti.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            psti.setString(3, getInstance().getUser().getName());
            psti.setInt(4, item.getCountry().getCountryId());
            psti.execute();
            DBCache.getInstance().updateFirstLevelDiv();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Deletes the given firstLevelDiv from the database and cache
     * @param item firstLevelDiv to be deleted
     * @return true if the firstLevelDiv is deleted
     */
    @Override
    public boolean delete(FirstLevelDiv item) {
        try {
            String sql = "DELETE FROM first_level_divisions WHERE Division_ID = ? ;";
            PreparedStatement psti = DBConnection.getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            psti.setInt(1, item.getDivisionId());
            psti.execute();
            DBCache.getInstance().updateFirstLevelDiv();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }
}
