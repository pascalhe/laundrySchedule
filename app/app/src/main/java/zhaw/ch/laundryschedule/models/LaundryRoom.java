package zhaw.ch.laundryschedule.models;

public class LaundryRoom extends AbstractBaseModel {

    private String name;
    private Location location;

    public LaundryRoom(){
        super();
        location = new Location();
    }

    public LaundryRoom(String _name){
        name = _name;
    }

    public LaundryRoom(String _id, String _name){
        super(_id);
        name = _name;
    }

    public void setName(String _name) {
        name = _name;
    }

    public String getName() {
        return name;
    }

    public void setLocation(Location _location) {
        location = _location;
    }

    public Location getLocation() {
        return location;
    }
}
