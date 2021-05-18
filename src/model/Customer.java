package model;

import DAO.DBCache;

/**
 * Represent a customer that consists of customer ID, its name, address, postal code, phone number, and division.
 * Provides getters and setters for all the fields.
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public class Customer {

    /** customer's ID */
    private int customerId;
    /** customer's name, address, postalCode, and phoneNumber */
    private String name, address, postalCode, phoneNumber;
    /** customer's division */
    private FirstLevelDiv division;

    /**
     * Creates customer with the given parameters, checks if any of the parameters are null or empty
     * @param customerId customer's ID
     * @param name customer's name
     * @param address customer's address
     * @param postalCode customer's postal code
     * @param phoneNumber customer's phone number
     * @param divisionId customer's division's ID
     */
    public Customer(Integer customerId, String name, String address, String postalCode, String phoneNumber,
                    Integer divisionId) {
        if (name != null) name = name.trim();
        if (address != null) address = address.trim();
        if (postalCode != null) postalCode = postalCode.trim();
        if (phoneNumber != null) phoneNumber = phoneNumber.trim();

        checkForNull(name);
        checkForNull(address);
        checkForNull(postalCode);
        checkForNull(phoneNumber);

        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.division = DBCache.getFirstLevelDivById(divisionId);
    }

    /**
     * Checks if the given text is null or doesn't have any characters
     * @param text text to check
     */
    private void checkForNull(String text) {
        if (text == null || text.equals("")){
            throw new IllegalArgumentException();
        }
    }

    /**
     * Retrieves customer's ID
     * @return customer's ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets customer's ID to the given one
     * @param customerId customer ID to set to
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Retrieves customer's name
     * @return customer's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets customer's name to the given one
     * @param name customer name to set to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves customer's address
     * @return customer's address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets customer's address to the given one
     * @param address customer address to set to
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Retrieves customer's postalCode
     * @return customer's postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets customer's postalCode to the given one
     * @param postalCode customer postalCode to set to
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Retrieves customer's phoneNumber
     * @return customer's phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets customer's phoneNumber to the given one
     * @param phoneNumber customer phoneNumber to set to
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Retrieves customer's division
     * @return customer's division
     */
    public FirstLevelDiv getDivision(){
        return division;
    }

    /**
     * Sets customer's division to the given one
     * @param division customer division to set to
     */
    public void setDivisionId(FirstLevelDiv division){
        this.division = division;
    }

    /**
     * Checks if this customer is equal to another
     * @param o object to compare the customer to
     * @return true is equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return phoneNumber.equals(customer.phoneNumber) &&
                name.equals(customer.name) &&
                address.equals(customer.address) &&
                postalCode.equals(customer.postalCode);
    }

    /**
     * Retrieves a String representation of the customer
     * @return a String representation of the customer
     */
    @Override
    public String toString() {
        return name;
    }
}