package model;

import DAO.DBCache;

/**
 * Represent a division that consists of division ID, its name and country. Provides getters and setters for all the fields.
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public class FirstLevelDiv {

    /** division's ID */
    private int divisionId;
    /** division's name */
    private String name;
    /** division's country */
    private Country country;

    /**
     * Creates division with the given parameters, checks if any of the parameters are null or empty
     * @param divisionId division's ID
     * @param name division's name
     * @param countryId division's country
     */
    public FirstLevelDiv(int divisionId, String name, int countryId) {
        if (name != null) name = name.trim();
        checkForNull(name);
        this.divisionId = divisionId;
        this.name = name;
        this.country = DBCache.getCountryById(countryId);
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
     * Retrieves division's ID
     * @return division's ID
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Sets the division's ID to the given one
     * @param divisionId division's ID to set it to
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * Retrieves division's name
     * @return division's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the division's name to the given one
     * @param name division's name to set it to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves division's country
     * @return division's country
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Sets the division's country to the given one
     * @param country division's country to set it to
     */
    public void setCountry(Country country) {
        this.country = country;
    }

    /**
     * Checks if this division is equal to another
     * @param o object to compare the division to
     * @return true is equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FirstLevelDiv that = (FirstLevelDiv) o;
        return country.equals(that.country) &&
                name.equals(that.name);
    }

    /**
     * Retrieves a String representation of the division
     * @return a String representation of the division
     */
    @Override
    public String toString() {
        return name;
    }
}
