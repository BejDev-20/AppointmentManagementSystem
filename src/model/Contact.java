package model;

/**
 * Represent a contact that consists of contact ID, its name and email. Provides getters and setters for all the fields.
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public class Contact {

    /** contact ID */
    private Integer contactId;
    /** name and email of the contact */
    private String contactName, contactEmail;

    /**
     * Creates contact with the given parameters, checks if any of the parameters are null or empty
     * @param contactID contact ID
     * @param contactName name of the contact
     * @param contactEmail email of the contact
     */
    public Contact(Integer contactID, String contactName, String contactEmail){
        checkForNull(contactName);
        checkForNull(contactEmail);

        this.contactId = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
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
     * Retrieves contact's ID
     * @return contact's ID
     */
    public Integer getContactId() {
        return contactId;
    }

    /**
     * Retrieves contact's name
     * @return contact's name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Sets contact's name to the given one
     * @param contactName name of the contact
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Retrieves contact's email
     * @return contact's email
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * Sets contact's email to the given one
     * @param contactEmail email of the contact
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /**
     * Checks if this contact is equal to another
     * @param o object to compare the contact to
     * @return true is equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return contactName.equals(contact.contactName) &&
                contactEmail.equals(contact.contactEmail);
    }

    /**
     * Retrieves a String representation of the contact
     * @return a String representation of the contact
     */
    @Override
    public String toString() {
        return contactName;
    }
}
