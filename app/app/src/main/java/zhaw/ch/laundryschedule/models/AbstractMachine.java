package zhaw.ch.laundryschedule.models;

public abstract class AbstractMachine extends AbstractBaseModel {

    private String name;
    private String capacity;
    private String locationDocId;

    AbstractMachine() {
        super();
    }

    AbstractMachine(String _name, String _capacity, String locationDocId) {
        super();
        name = _name;
        capacity = _capacity;
        this.locationDocId = locationDocId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getLocationDocId() {
        return locationDocId;
    }

    public void setLocationDocId(String locationDocId) {
        this.locationDocId = locationDocId;
    }
}
