package model;

/**
 * Represents a country that consists of country ID and name.
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public class Country {
    /** ID of the country */
    private int countryId;
    /** name of the country */
    private String name;

    /**
     * Creating the country using given ID and name
     * @param countryId ID of the country
     * @param name name of the country
     */
    public Country(int countryId, String name) {
        checkForNull(name);
        this.countryId = countryId;
        this.name = name;
    }

    /**
     * Checks if the given text is null or doesn't have any characters. Checks if any of the parameters are null or empty
     * @param text text to check
     */
    private void checkForNull(String text) {
        if (text == null || text.equals("")){
            throw new IllegalArgumentException();
        }
    }

    /**
     * Retrieves the ID of the country
     * @return country ID
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Sets country's ID to the given one
     * @param countryId ID to set the country ID to
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * Retrieves the name of the country
     * @return country's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets country's name to the given one
     * @param name name to set the country's name to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Checks if this country is equal to another
     * @param o object to compare the country to
     * @return true is equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return name.equals(country.name);
    }

    /**
     * Retrieves a String representation of the country
     * @return a String representation of the country
     */
    @Override
    public String toString() {
        return name;
    }
}
