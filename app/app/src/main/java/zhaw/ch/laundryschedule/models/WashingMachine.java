package zhaw.ch.laundryschedule.models;

public class WashingMachine extends AbstractMachine {

    public WashingMachine() {
        super();
    }

    public WashingMachine(String _name, String _capacity, String _locationDocId){
        super(_name, _capacity, _locationDocId);
    }
}
