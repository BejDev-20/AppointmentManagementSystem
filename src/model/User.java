package model;

/**
 * Represent a user that consists of user ID, its name and password. Provides getters and setters for all the fields.
 * @author Iulia Bejsovec
 * @version 01/2021
 */
public class User {

    /** user's ID */
    private int userId;
    /** user's name, password*/
    private String name, password;

    /**
     * Creates user with the given parameters, checks if any of the parameters are null or empty
     * @param userId user's ID
     * @param name user's name
     * @param password user's password
     */
    public User(int userId, String name, String password) {
        if (name != null) name = name.trim();
        if (password != null) password = password.trim();

        checkForNull(name);
        checkForNull(password);

        this.userId = userId;
        this.name = name;
        this.password = password;
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
     * Retrieves user's ID
     * @return user's ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user's ID to the given one
     * @param userId user's ID to set it to
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Retrieves user's name
     * @return user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's name to the given one
     * @param name user's name to set it to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves user's password
     * @return user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password to the given one
     * @param password user's password to set it to
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Checks if this user is equal to another
     * @param o object to compare the user to
     * @return true is equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name) &&
                password.equals(user.password);
    }

    /**
     * Retrieves a String representation of the user
     * @return a String representation of the user
     */
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
