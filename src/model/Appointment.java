package model;

import DAO.DBCache;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Represent an appointment that consists of appointment ID, title of the appointment, its title, description, location,
 * type, start and end time, as well as customer, user, and contact associated with the appointment. It also keeps track
 * of the local business hours for the business location. Provides getters and setters for all the fields.
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public class Appointment {

    /** opening business hours */
    private static final LocalTime START_HOUR = LocalTime.of(8, 0);
    /** closing business hours */
    private static final LocalTime END_HOUR = LocalTime.of(22, 0);
    /** appointment ID */
    private Integer appointmentId;
    /** appointment's title, description, location, type */
    private String title, description, location, type;
    /** appointment's start and end times */
    private LocalDateTime startTime, endTime;
    /** customer for the appointment */
    private Customer customer;
    /** user who created/updated appointment */
    private User user;
    /** contact tied to the appointment */
    private Contact contact;

    /**
     * Creates appointment with the given parameters, checks if any of the parameters are null or empty
     * @param appointmentId appointment's ID
     * @param title appointment's title
     * @param description appointment's description
     * @param location appointment's location
     * @param type appointment's type
     * @param startTime appointment's start time
     * @param endTime appointment's end time
     * @param customerId customer ID of the customer for the appointment
     * @param userId user ID of the user who created/updated the appointment
     * @param contactId contact ID of the contact tied to the appointment
     */
    public Appointment(int appointmentId, String title, String description, String location, String type,
                       LocalDateTime startTime, LocalDateTime endTime, int customerId, int userId, int contactId) {
        if (title != null) title = title.trim();
        if (description != null) description = description.trim();
        if (location != null) location = location.trim();
        if (type != null) type = type.trim();

        checkForNull(title);
        checkForNull(description);
        checkForNull(location);
        checkForNull(type);

        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customer = DBCache.getCustomerHashMap().get(customerId);
        this.user = DBCache.getUserHashMap().get(userId);
        this.contact = DBCache.getContactHashMap().get(contactId);
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
     * Retrieves appointment's title
     * @return appointment's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets appointment's title to the given one
     * @param title title to set the appointment's title to
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retrieves appointment's description
     * @return appointment's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets appointment's description to the given one
     * @param description description to set the appointment's description to
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves opening business hours
     * @return opening business hours
     */
    public static LocalTime getSTART_HOUR() {
        return START_HOUR;
    }

    /**
     * Retrieves closing business hours
     * @return closing business hours
     */
    public static LocalTime getEND_HOUR() {
        return END_HOUR;
    }

    /**
     * Retrieves appointment's location
     * @return appointment's location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets appointment's location to the given one
     * @param location location to set the appointment's location to
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Retrieves appointment's type
     * @return appointment's type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets appointment's type to the given one
     * @param type type to set the appointment's type to
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retrieves appointment's start time
     * @return appointment's start time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets appointment's startTime to the given one
     * @param startTime startTime to set the appointment's startTime to
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Retrieves appointment's ID
     * @return appointment's ID
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Sets appointment's ID to the given one
     * @param appointmentId ID to set the appointment's ID to
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Retrieves appointment's end time
     * @return appointment's end time
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets appointment's endTime to the given one
     * @param endTime endTime to set the appointment's endTime to
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Retrieves customer the appointment is associated with
     * @return appointment's customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Sets customer associated with the appointment to the given one
     * @param customer customer to set the appointment's customer to
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Retrieves user the appointment is associated with
     * @return appointment's user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets user associated with the appointment to the given one
     * @param user user to set the appointment's user to
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Retrieves contact the appointment is associated with
     * @return appointment's contact
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * Sets contact associated with the appointment to the given one
     * @param contact contact to set the appointment's contact to
     */
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    /**
     * Checks if this appointment is equal to another
     * @param o object to compare the appointment to
     * @return true is equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return customer.equals(that.customer) &&
                user.equals(that.user) &&
                contact.equals(that.contact) &&
                title.equals(that.title) &&
                description.equals(that.description) &&
                location.equals(that.location) &&
                type.equals(that.type) &&
                startTime.equals(that.startTime) &&
                endTime.equals(that.endTime);
    }

    /**
     * Retrieves a String representation of the appointment
     * @return a String representation of the appointment
     */
    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", type='" + type + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", customer=" + customer +
                ", user=" + user +
                ", contact=" + contact +
                '}';
    }
}
