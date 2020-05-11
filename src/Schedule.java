import java.time.Duration;
import java.util.Date;

public class Schedule {
    private Date date;
    private Date time;
    private User user;
    private Billboard billboard;
    private Date duration;
    private Date recurTime;
    public Schedule(User user, Billboard billboard, Date date, Date time, Date recurTime,Date duration){
        this.billboard= billboard;
        this.date = date;
        this.time = time;
        this.user = user;
        this.duration= duration;
        this.recurTime =recurTime;
    }

    public Date getBillboardDate(){return date;}
    public Date getBillboardTime(){return time;}
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
}
