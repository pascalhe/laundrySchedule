package zhaw.ch.laundryschedule.models;

public abstract class AbstractBaseModel {

    private String documentKey;

    AbstractBaseModel() {
    }


    public String getDocumentKey() {
        return documentKey;
    }

    public void setDocumentKey(String documentKey) {
        this.documentKey = documentKey;
    }
}
