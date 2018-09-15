package zhaw.ch.laundryschedule.models;

import java.util.Date;

public class Reservation extends AbstractBaseModel {

    private Date from;
    private Date to;
    private String userDocId;

    public Reservation(int id, Date from, Date to, String userDocId){
        super();
        this.from = from;
        this.to = to;
        this.userDocId = userDocId;
    }

    public Reservation(Date from, Date to, String userDocId){
        this.from = from;
        this.to = to;
        this.userDocId = userDocId;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getUserDocId(){
        return userDocId;
    }

    public void setUserDocId(String userDocId){
        this.userDocId = userDocId;
    }
}
