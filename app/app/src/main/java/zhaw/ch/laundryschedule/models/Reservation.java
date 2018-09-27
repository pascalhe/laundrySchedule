package zhaw.ch.laundryschedule.models;

import java.util.Date;

/**
 * Represent a reservation in the app
 */
public class Reservation extends AbstractBaseModel {

    private Date from;
    private Date to;
    private String userDocId;
    private String washingMachineDocId;

    public Reservation(){
        super();
    }

    public Reservation(int id, Date from, Date to, String userDocId, String washingMachineDocId){
        super();
        this.from = from;
        this.to = to;
        this.userDocId = userDocId;
        this.washingMachineDocId = washingMachineDocId;
    }

    public Reservation(Date from, Date to, String userDocId, String washingMachineDocId){
        this.from = from;
        this.to = to;
        this.userDocId = userDocId;
        this.washingMachineDocId = washingMachineDocId;
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

    public void setWashingMachineDocId(String washingMachineDocId){
        this.washingMachineDocId = washingMachineDocId;
    }

    public String getWashingMachineDocId(){
        return washingMachineDocId;
    }
}
