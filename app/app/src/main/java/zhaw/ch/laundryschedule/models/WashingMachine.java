package zhaw.ch.laundryschedule.models;

public class WashingMachine extends AbstractMachine {

    private String locationDocId;
    public WashingMachine(){
        super();
    }

    public WashingMachine(String _id, String _name, String _capacity, String locationDocId){
        super(_id, _name, _capacity);
        this.locationDocId = locationDocId;
    }
    public WashingMachine(String _name, String _capacity, String locationDocId){
        super(_name, _capacity);
        this.locationDocId = locationDocId;
    }

    public String getLocationDocId() {
        return locationDocId;
    }

    public void setLocationDocId(String locationDocId) {
        this.locationDocId = locationDocId;
    }
}
