package zhaw.ch.laundryschedule.models;

import java.util.Date;

public class Reservation extends AbstractBaseModel {

    private Date from;
    private Date to;
    private String userDocId;
    private String waschingMachineDocId;

    public Reservation(){
        super();
    }

    public Reservation(int id, Date from, Date to, String userDocId, String waschingMachineDocId){
        super();
        this.from = from;
        this.to = to;
        this.userDocId = userDocId;
        this.waschingMachineDocId = waschingMachineDocId;
    }

    public Reservation(Date from, Date to, String userDocId, String waschingMachineDocId){
        this.from = from;
        this.to = to;
        this.userDocId = userDocId;
        this.waschingMachineDocId = waschingMachineDocId;
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
        this.waschingMachineDocId = washingMachineDocId;
    }

    public String getWashingMachineDocId(){
        return waschingMachineDocId;
    }
}
