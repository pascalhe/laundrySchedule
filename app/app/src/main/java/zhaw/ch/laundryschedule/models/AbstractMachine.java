package zhaw.ch.laundryschedule.models;

public abstract class AbstractMachine extends AbstractBaseModel {

    private String name;
    private String capacity;

    AbstractMachine() {
        super();
    }

    AbstractMachine(String _name, String _capacity) {
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
