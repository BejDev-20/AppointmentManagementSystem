package DAO;

import javafx.collections.ObservableList;
import model.*;
import java.util.HashMap;

/**
 * Responsible for keeping the local cache of all the entries in the customer, appointment, user, firstleveldiv,
 * country and contact tables. Implemented using Singleton to ensure only one instance of cache exists. Offers getters
 * for all the hashmaps with entries per object and update methods for all the maps
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public class DBCache {

    /* user that is currently logged in */
    private User currentUser;
    /* map of all the customers */
    private static HashMap<Integer, Customer> customerHashMap = new HashMap<>();
    /* map of all the appointments */
    private static HashMap<Integer, Appointment> appointmentHashMap = new HashMap<>();
    /* map of all the users */
    private static HashMap<Integer, User> userHashMap = new HashMap<>();
    /* map of all the firstleveldivisions */
    private static HashMap<Integer, FirstLevelDiv> firstLevelDivHashMap = new HashMap<>();
    /* map of all the countries */
    private static HashMap<Integer, Country> countryHashMap = new HashMap<>();
    /* map of all the contacts */
    private static HashMap<Integer, Contact> contactHashMap = new HashMap<>();
    /* instance of the cache */
    private static DBCache single_instance = null;

    /**
     * Private constructor to prevent creation of a second instance
     */
    private DBCache(){
        currentUser = null;
    }

    /**
     * Retrieves the instance of the cache, creates a new one if the instance doesn't exist
     * @return instance of the cache
     */
    public static DBCache getInstance(){
        if (single_instance == null){
            single_instance = new DBCache();
            updateAllCache();
        }
        return single_instance;
    }

    /**
     * Sets the user to the given one
     * @param user user to set the current user to
     */
    public void setUser(User user){
        this.currentUser = user;
    }

    /**
     * Retrieves current user
     * @return current user
     */
    public User getUser(){
        return currentUser;
    }

    /**
     * Updates all of the hashmaps maintained by the cache
     */
    public static void updateAllCache(){
        updateContacts();
        updateCustomers();
        updateUsers();
        updateCountries();
        updateFirstLevelDiv();
        updateAppointments();
    }

    /**
     * Updates the appointment hashmap by pulling the information from the database
     */
    public static void updateAppointments() {
        AppointmentDao appointmentDao = new AppointmentDao();
        ObservableList<Appointment> appointments = appointmentDao.getAll();
        appointmentHashMap.clear();
        for (Appointment appointment : appointments) {
            appointmentHashMap.put(appointment.getAppointmentId(), appointment);
        }
    }

    /**
     * Updates the firstleveldivision hashmap by pulling the information from the database
     */
    public static void updateFirstLevelDiv() {
        FirstLevelDivDao firstLevelDivDao = new FirstLevelDivDao();
        ObservableList<FirstLevelDiv> firstLevelDivs = firstLevelDivDao.getAll();
        firstLevelDivHashMap.clear();
        for (FirstLevelDiv firstLevelDiv : firstLevelDivs) {
            firstLevelDivHashMap.put(firstLevelDiv.getDivisionId(), firstLevelDiv);
        }
    }

    /**
     * Updates the countries hashmap by pulling the information from the database
     */
    public static void updateCountries() {
        CountryDao countryDao = new CountryDao();
        ObservableList<Country> countries = countryDao.getAll();
        countryHashMap.clear();
        for (Country country : countries) {
            countryHashMap.put(country.getCountryId(), country);
        }
    }

    /**
     * Updates the users hashmap by pulling the information from the database
     */
    public static void updateUsers() {
        UsersDao usersDao = new UsersDao();
        ObservableList<User> users = usersDao.getAll();
        userHashMap.clear();
        for (User user : users) {
            userHashMap.put(user.getUserId(), user);
        }
    }

    /**
     * Updates the customers hashmap by pulling the information from the database
     */
    public static void updateCustomers(){
        CustomerDao customerDao = new CustomerDao();
        ObservableList<Customer> customers = customerDao.getAll();
        customerHashMap.clear();
        for (Customer customer : customers) {
            customerHashMap.put(customer.getCustomerId(), customer);
        }
    }

    /**
     * Updates the contacts hashmap by pulling the information from the database
     */
    public static void updateContacts(){
        ContactDao contactDao = new ContactDao();
        ObservableList<Contact> contacts = contactDao.getAll();
        contactHashMap.clear();
        for (Contact contact : contacts) {
            contactHashMap.put(contact.getContactId(), contact);
        }
    }

    /**
     * Retrieves an appointment by the ID
     * @param id id of the appointment to be retrieved
     * @return appointment with the id passed into the method
     */
    public static Appointment getAppById(int id){
        Appointment app = appointmentHashMap.get(id);
        if (app == null){
            AppointmentDao appointmentDao = new AppointmentDao();
            app = appointmentDao.getById(id);
        }
        return app;
    }

    /**
     * Retrieves an cusotmer by the ID
     * @param id id of the cusotmer to be retrieved
     * @return cusotmer with the id passed into the method
     */
    public static Customer getCustomerById(int id){
        Customer customer = customerHashMap.get(id);
        if (customer == null){
            CustomerDao customerDao = new CustomerDao();
            customer = customerDao.getById(id);
        }
        return customer;
    }

    /**
     * Retrieves an user by the ID
     * @param id id of the user to be retrieved
     * @return user with the id passed into the method
     */
    public static User getUserById(int id){
        User user = userHashMap.get(id);
        if (user == null){
            UsersDao usersDao = new UsersDao();
            user = usersDao.getById(id);
        }
        return user;
    }

    /**
     * Retrieves a country by the ID
     * @param id id of the country to be retrieved
     * @return country with the id passed into the method
     */
    public static Country getCountryById(int id){
        Country country = countryHashMap.get(id);
        if (country == null){
            CountryDao countryDao = new CountryDao();
            country = countryDao.getById(id);
        }
        return country;
    }

    /**
     * Retrieves a firstLevelDiv by the ID
     * @param id id of the firstLevelDiv to be retrieved
     * @return firstLevelDiv with the id passed into the method
     */
    public static FirstLevelDiv getFirstLevelDivById(int id){
        FirstLevelDiv firstLevelDiv = firstLevelDivHashMap.get(id);
        if (firstLevelDiv == null){
            FirstLevelDivDao firstLevelDivDao = new FirstLevelDivDao();
            firstLevelDiv = firstLevelDivDao.getById(id);
        }
        return firstLevelDiv;
    }

    /**
     * Retrieves a contact by the ID
     * @param id id of the contact to be retrieved
     * @return contact with the id passed into the method
     */
    public static Contact getContactById(int id){
        Contact contact = contactHashMap.get(id);
        if (contact == null){
            ContactDao contactDao = new ContactDao();
            contact = contactDao.getById(id);
        }
        return contact;
    }

    /**
     * Retrieves customer's hashmap with all the cusotmers records
     * @return customer's hashmap
     */
    public static HashMap<Integer, Customer> getCustomerHashMap() {
        return customerHashMap;
    }

    /**
     * Retrieves appointment's hashmap with all the appointments records
     * @return appointment's hashmap
     */
    public static HashMap<Integer, Appointment> getAppointmentHashMap() {
        return appointmentHashMap;
    }

    /**
     * Retrieves user's hashmap with all the users records
     * @return user's hashmap
     */
    public static HashMap<Integer, User> getUserHashMap() {
        return userHashMap;
    }

    /**
     * Retrieves country's hashmap with all the countries records
     * @return country's hashmap
     */
    public static HashMap<Integer, Country> getCountryHashMap(){
        return countryHashMap;
    }

    /**
     * Retrieves firstleveldiv's hashmap with all the firstleveldivs records
     * @return firstleveldiv's hashmap
     */
    public static HashMap<Integer, FirstLevelDiv> getFirstLevelDivHashMap() {
        return firstLevelDivHashMap;
    }

    /**
     * Retrieves contact's hashmap with all the contacts records
     * @return contact's hashmap
     */
    public static HashMap<Integer, Contact> getContactHashMap() {
        return contactHashMap;
    }
}
