package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import java.sql.*;

/**
 * Represents a DAO for contacts table providing the functionality to get all contacts from the table,
 * add, update, or delete a contact as well as retrieve one by the ID.
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public class ContactDao implements DAO<Contact> {

    /**
     * Retrieves all the contacts from the database and saves each into the local cache
     * @return list of all the contacts retrieved
     */
    @Override
    public ObservableList<Contact> getAll() {
        ObservableList<Contact> allContacts = FXCollections.observableArrayList();
        try{
            String sql = "SELECT Contact_ID, Contact_Name, Email FROM contacts";
            PreparedStatement ps = DBConnection.getConn().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                Contact contact = new Contact(contactId, contactName, email);
                allContacts.add(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allContacts;
    }

    /**
     * Retrieves an contact by the ID
     * @param id id of the contact to be retrieved
     * @return contact with the id passed into the method
     */
    @Override
    public Contact getById(int id) {
        Contact contact = null;
        try{
            String sql = "SELECT Contact_ID, Contact_Name, Email FROM contacts WHERE Contact_ID = " + id;
            PreparedStatement ps = DBConnection.getConn().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                if(id == rs.getInt("Contact_ID")) {
                    int contactId = rs.getInt("Contact_ID");
                    String contactName = rs.getString("Contact_Name");
                    String email = rs.getString("Email");
                    contact = new Contact(contactId, contactName, email);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (contact != null) DBCache.getInstance().updateContacts();
        return contact;
    }

    /**
     * Adds the given contact to the database as well as cache
     * @param item contact to be added to the database and cache
     * @return true if the contact is added
     */
    @Override
    public boolean add(Contact item) {
        try {
            String sql = "INSERT INTO contacts VALUES(NULL, ?, ?)";
            PreparedStatement psti = DBConnection.getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            psti.setString(1, item.getContactName());
            psti.setString(2, item.getContactEmail());
            psti.execute();
            DBCache.getInstance().updateContacts();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Updates the given contact in the database as well as cache
     * @param item contact to be updated in the database and cache
     * @return true if the contact is updated
     */
    @Override
    public boolean update(Contact item) {
        try {
            String sql = "UPDATE contacts SET Contact_Name = ?, Email = ? WHERE Country_ID = ? ;";
            PreparedStatement psti = DBConnection.getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            psti.setString(1, item.getContactName());
            psti.setString(2, item.getContactEmail());
            psti.execute();
            DBCache.getInstance().updateContacts();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Deletes the given contact from the database and cache
     * @param item contact to be deleted
     * @return true if the contact is deleted
     */
    @Override
    public boolean delete(Contact item) {
        try {
            String sql = "DELETE FROM contacts WHERE Contact_ID = ? ;";
            PreparedStatement psti = DBConnection.getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            psti.setInt(1, item.getContactId());
            psti.execute();
            DBCache.getInstance().updateContacts();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }
}
