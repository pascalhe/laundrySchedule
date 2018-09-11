package zhaw.ch.laundryschedule.models;

public class WashingMachine extends AbstractMachine {

    public WashingMachine(){
        super();
    }

    public WashingMachine(String _id, String _name, String _capacity){
        super(_id, _name, _capacity);
    }
    public WashingMachine(String _name, String _capacity){
        super(_name, _capacity);
    }
}
