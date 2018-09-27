package zhaw.ch.laundryschedule.models;

/**
 * Represent a washing machine in the app
 */
public class WashingMachine extends AbstractMachine {

    public WashingMachine() {
        super();
    }

    public WashingMachine(String _name, String _capacity, String _locationDocId){
        super(_name, _capacity, _locationDocId);
    }
}
