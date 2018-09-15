package zhaw.ch.laundryschedule.models;

public class WashingMachine extends AbstractBaseModel{

    private String name;
    private String capacity;
    private String locationDocId;

    public WashingMachine(){
        super();
    }

    public WashingMachine(String id, String name, String capacity, String locationDocId){
        super(id);
        this.name = name;
        this.capacity = capacity;
        this.locationDocId = locationDocId;
    }
    public WashingMachine(String name, String capacity, String locationDocId){
        this.name = name;
        this.capacity = capacity;
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
