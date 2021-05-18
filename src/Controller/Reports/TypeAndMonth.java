package Controller.Reports;

import java.time.Month;

/**
 * Represents one row of appointment data grouped by type of the appointment in a given month. Consists of number of
 * appointments, type and month
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public class TypeAndMonth {

    private Integer numberOfAppointments;
    private Month month;
    private String type;

    /**
     * Creates the row with the given number of appointments, month and type
     * @param numberOfAppointments number of appointments of the given type in a month
     * @param month month for which
     * @param type type of the appointment
     */
    public TypeAndMonth(int numberOfAppointments, Month month, String type) {
        this.numberOfAppointments = numberOfAppointments;
        this.month = month;
        this.type = type;
    }

    /**
     * Retrieves the number of appointments
     * @return number of appointments
     */
    public int getNumberOfAppointments() {
        return numberOfAppointments;
    }

    /**
     * Retrieves the month in which there is a number of appointments of certain type
     * @return month in which there is a number of appointments of certain type
     */
    public Month getMonth() {
        return month;
    }

    /**
     * Retrieves the type of appointments
     * @return type of the appointmentsAppointmentsTypeMonth
     */
    public String getType() {
        return type;
    }
}
