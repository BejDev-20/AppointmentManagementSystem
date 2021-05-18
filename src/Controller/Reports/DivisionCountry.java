package Controller.Reports;

import DAO.DBCache;
import model.Country;
import model.FirstLevelDiv;

/**
 * Represents one row of customers data grouped by division and country of the location of the customer. Consists of
 * number of customers, division and country
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public class DivisionCountry {

    private Integer customerCount;
    private FirstLevelDiv firstLevelDivision;
    private Country country;

    /**
     * Creates the row with the given number of customers, firstLevelDivision and country
     * @param customerCount number of customers in the given division and country
     * @param firstLevelDivision country in which the customers reside
     */
    public DivisionCountry(int customerCount, int firstLevelDivision) {
        this.customerCount = customerCount;
        this.firstLevelDivision = DBCache.getFirstLevelDivById(firstLevelDivision);
        this.country = this.firstLevelDivision.getCountry();
    }

    /**
     * Retrieves the number of customers
     * @return number of customers
     */
    public int getCustomerCount() {
        return customerCount;
    }

    /**
     * Retrieves the firstLevelDivision
     * @return firstLevelDivision
     */
    public FirstLevelDiv getFirstLevelDivision() {
        return firstLevelDivision;
    }

    /**
     * Retrieves the country
     * @return country
     */
    public Country getCountry() {
        return country;
    }
}
