package zhaw.ch.laundryscheduler.models;

public abstract class AbstractBaseModel {
    private String id;

    public AbstractBaseModel(){
        id = new String();
    }
    public AbstractBaseModel(String _id){
        id = _id;
    }

    public String getId(){
        return id;
    }

    public void setId(String _id){
        id = _id;
    }
}
