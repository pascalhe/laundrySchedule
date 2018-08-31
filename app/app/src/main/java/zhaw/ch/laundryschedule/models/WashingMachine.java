package zhaw.ch.laundryschedule.models;

public class WashingMachine extends AbstractMachine {

    public WashingMachine(){
        super();
    }

    public WashingMachine(String _id, String _name, double _capacity){
        super(_id, _name, _capacity);
    }
}
