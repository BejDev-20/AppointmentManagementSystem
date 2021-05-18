package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import java.sql.*;
import java.time.LocalDateTime;
import static DAO.DBCache.getInstance;

/**
 * Represents a DAO for users table providing the functionality to get all users from the table,
 * add, update, or delete an user as well as retrieve one by the ID.
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public class UsersDao implements DAO<User> {

    /**
     * Retrieves all the users from the database and saves each into the local cache
     * @return list of all the users retrieved
     */
    @Override
    public ObservableList<User> getAll() {
        ObservableList<User> allUsers = FXCollections.observableArrayList();
        try{
            String sql = "SELECT User_ID, User_Name, Password FROM users";
            PreparedStatement ps = DBConnection.getConn().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                User user = new User(userId, userName, password);
                allUsers.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allUsers;
    }

    /**
     * Retrieves an user by the ID
     * @param id id of the user to be retrieved
     * @return user with the id passed into the method
     */
    @Override
    public User getById(int id) {
        User user = null;
        try{
            String sql = "SELECT User_ID, User_Name, Password FROM users WHERE User_ID = " + id;
            PreparedStatement ps = DBConnection.getConn().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                if(id == rs.getInt("User_ID")) {
                    int userId = rs.getInt("User_ID");
                    String userName = rs.getString("User_Name");
                    String userPassword = rs.getString("Password");
                    user = new User(userId, userName, userPassword);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (user != null) DBCache.getInstance().updateUsers();
        return user;
    }

    /**
     * Adds the given user to the database as well as cache
     * @param item user to be added to the database and cache
     * @return true if the user is added
     */
    @Override
    public boolean add(User item) {
        try {
            String sql = "INSERT INTO users VALUES(NULL, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psti = DBConnection.getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            psti.setString(1, item.getName());
            psti.setString(2, item.getPassword());
            psti.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psti.setString(4, getInstance().getUser().getName());
            psti.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            psti.setString(6, getInstance().getUser().getName());
            psti.execute();
            DBCache.getInstance().updateUsers();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Updates the given user in the database as well as cache
     * @param item user to be updated in the database and cache
     * @return true if the user is updated
     */
    @Override
    public boolean update(User item) {
        try {
            String sql = "UPDATE users SET User_Name = ?, Password = ?, Last_Update = ?, Last_Updated_By = ? " +
                    "WHERE User_ID = ? ;";
            PreparedStatement psti = DBConnection.getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            psti.setString(1, item.getName());
            psti.setString(2, item.getPassword());
            psti.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psti.setString(4, getInstance().getUser().getName());
            psti.execute();
            DBCache.getInstance().updateUsers();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Deletes the given user from the database and cache
     * @param item user to be deleted
     * @return true if the user is deleted
     */
    @Override
    public boolean delete(User item) {
        try {
            String sql = "DELETE FROM users WHERE User_ID = ? ;";
            PreparedStatement psti = DBConnection.getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            psti.setInt(1, item.getUserId());
            psti.execute();
            DBCache.getInstance().updateUsers();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }
}
