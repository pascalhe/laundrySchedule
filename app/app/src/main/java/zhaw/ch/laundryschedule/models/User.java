package zhaw.ch.laundryschedule.models;

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

    public User(String id, String firstName, String lastName, String email, String userName, String password, Location location){
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.location = location;
    }

    public User(String firstName, String lastName, String email, String userName, String password, Location location){
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.location = location;
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
