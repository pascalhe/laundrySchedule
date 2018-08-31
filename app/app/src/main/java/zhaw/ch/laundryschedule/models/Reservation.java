package zhaw.ch.laundryschedule.models;

import java.util.Date;

public class Reservation extends AbstractBaseModel {

    private Date from;
    private Date to;

    public Reservation(){
        super();
    }

    public Reservation(String _id, Date _from, Date _to){
        super(_id);
        from = _from;
        to = _to;
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



}
