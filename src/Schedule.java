import java.io.Serializable;
import java.util.Date;

public class Schedule implements Serializable {
    private Date date;
    private Date time;
    private User user;
    private Billboard billboard;
    private Date duration = Server.parseTime("00:00:00");
    private Date recurTime= Server.parseTime("00:00:00");
    public Schedule(User user, Billboard billboard, Date date, Date time, Date recurTime,Date duration){
        this.billboard= billboard;
        this.date = date;
        this.time = time;
        this.user = user;
        if(duration!=null){
        this.duration= duration;}
        if(recurTime!=null){
        this.recurTime =recurTime;}
    }
    public Date getDuration(){return duration;}
    public Date getRecurTime(){return recurTime;}
    public Date getDate(){return date;}
    public Date getTime(){return time;}
    public void setDate(Date date){this.date = date;}
    public void setTime(Date time){this.time = time;}
    public void setBillboard(Billboard billboard){this.billboard = billboard;}
    public void setDuration(Date duration){this.duration = duration;
    }
    public void setRecurTime(Date recurTime){this.duration = recurTime;}
    public String getAuthor(){
        return user.getUserName();
    }
    public String getBillboardName(){
        return billboard.getBillboardName();
    }
}
