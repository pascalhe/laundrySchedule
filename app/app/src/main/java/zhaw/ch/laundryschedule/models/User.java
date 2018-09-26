package zhaw.ch.laundryschedule.models;

/**
 * Represent a a user in the app.
 */
public class User extends AbstractBaseModel {
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String locationDocId;

    public User() {
        super();
    }

    public User(String firstName, String lastName, String email, String userName, String locationDocId) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.locationDocId = locationDocId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String _firstName) {
        firstName = _firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String _lastName) {
        lastName = _lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String _email) {
        email = _email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String _userName) {
        userName = _userName;
    }

    public String getLocationDocId() {
        return locationDocId;
    }

    public void setLocationDocId(String locationDocId) {
        this.locationDocId = locationDocId;
    }
}
