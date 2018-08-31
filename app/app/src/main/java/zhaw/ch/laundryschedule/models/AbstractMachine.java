package zhaw.ch.laundryschedule.models;

public abstract class AbstractMachine extends AbstractBaseModel {

    private String name;
    private double capacity;

    public AbstractMachine(){
        super();
    }

    public AbstractMachine(String _id, String _name, double _capacity){
        super(_id);
        name = _name;
        capacity = _capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }
}
