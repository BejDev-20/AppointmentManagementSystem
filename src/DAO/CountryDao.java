package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import java.sql.*;
import java.time.LocalDateTime;
import static DAO.DBCache.getInstance;

/**
 * Represents a DAO for country table providing the functionality to get all countries from the table,
 * add, update, or delete a country as well as retrieve one by the ID.
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public class CountryDao implements DAO<Country> {

    /**
     * Retrieves all the countries from the database and saves each into the local cache
     * @return list of all the countries retrieved
     */
    @Override
    public ObservableList<Country> getAll() {
        ObservableList<Country> allCountries = FXCollections.observableArrayList();
        try{
            String sql = "SELECT Country_ID, Country FROM countries";
            PreparedStatement ps = DBConnection.getConn().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int countryId = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Country country = new Country(countryId, countryName);
                allCountries.add(country);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCountries;
    }

    /**
     * Retrieves a country by the ID
     * @param id id of the country to be retrieved
     * @return country with the id passed into the method
     */
    @Override
    public Country getById(int id) {
        Country country = null;
        try{
            String sql = "SELECT Country_ID, Country FROM countries WHERE Country_ID = " + id;
            PreparedStatement ps = DBConnection.getConn().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                if(id == rs.getInt("Country_ID")) {
                    int countryId = rs.getInt("Country_ID");
                    String countryName = rs.getString("Country");
                    country = new Country(countryId, countryName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (country != null) DBCache.getInstance().updateCountries();
        return country;
    }

    /**
     * Adds the given country to the database as well as cache
     * @param item country to be added to the database and cache
     * @return true if the country is added
     */
    @Override
    public boolean add(Country item) {
        try {
            String sql = "INSERT INTO countries VALUES(NULL, ?, ?, ?, ?, ?)";
            PreparedStatement psti = DBConnection.getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            psti.setString(1, item.getName());
            psti.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            psti.setString(3, getInstance().getUser().getName());
            psti.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            psti.setString(5, getInstance().getUser().getName());
            psti.execute();
            DBCache.getInstance().updateCustomers();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Updates the given country in the database as well as cache
     * @param item country to be updated in the database and cache
     * @return true if the country is updated
     */
    @Override
    public boolean update(Country item) {
        try {
            String sql = "UPDATE countries SET Country = ?, Last_Update = ?, Last_Updated_By = ? " +
                         "WHERE Country_ID = ? ;";
            PreparedStatement psti = DBConnection.getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            psti.setString(1, item.getName());
            psti.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            psti.setString(3, getInstance().getUser().getName());
            psti.execute();
            DBCache.getInstance().updateCountries();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Deletes the given country from the database and cache
     * @param item country to be deleted
     * @return true if the country is deleted
     */
    @Override
    public boolean delete(Country item) {
        try {
            String sql = "DELETE FROM countries WHERE Country_ID = ? ;";
            PreparedStatement psti = DBConnection.getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            psti.setInt(1, item.getCountryId());
            psti.execute();
            DBCache.getInstance().updateCountries();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }
}
