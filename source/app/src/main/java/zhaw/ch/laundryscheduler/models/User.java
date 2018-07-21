package zhaw.ch.laundryscheduler.models;

public class User extends AbstractBaseModel{
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String password;
    private Location location;

    public User(){
        super();
        location = new Location();
    }

    public User(String _id, String _firstName, String _lastName, String _email, String _userName, String _password, Location _location){
        super(_id);
        firstName = _firstName;
        lastName = _lastName;
        email = _email;
        userName = _userName;
        password = _password;
        location = _location;
    }

    public User(String _firstName, String _lastName, String _email, String _userName, String _password, Location _location){
        super();
        firstName = _firstName;
        lastName = _lastName;
        email = _email;
        userName = _userName;
        password = _password;
        location = _location;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String _firstName){
        firstName = _firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String _lastName){
        lastName = _lastName;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String _email){
        email = _email;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String _userName){
        userName = _userName;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String _passwort){
        password = password;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location _location) {
        location = _location;
    }
}
