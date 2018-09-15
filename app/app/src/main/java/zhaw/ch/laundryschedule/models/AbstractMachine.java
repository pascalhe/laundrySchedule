package zhaw.ch.laundryschedule.models;

public abstract class AbstractMachine extends AbstractBaseModel {

    private String name;
    private String capacity;

    public AbstractMachine(){
        super();
    }

    public AbstractMachine(String _id, String _name, String _capacity){
        super(_id);
        name = _name;
        capacity = _capacity;
    }
    public AbstractMachine(String _name, String _capacity){
        super();
        name = _name;
        capacity = _capacity;
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

}
