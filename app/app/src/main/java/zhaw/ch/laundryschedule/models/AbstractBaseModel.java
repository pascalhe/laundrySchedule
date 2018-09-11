package zhaw.ch.laundryschedule.models;

public abstract class AbstractBaseModel {
    private String id;
    private String documentKey;

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

    public String getDocumentKey() {
        return documentKey;
    }

    public void setDocumentKey(String documentKey) {
        this.documentKey = documentKey;
    }
}
