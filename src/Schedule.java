import java.util.Date;

public class Schedule {
    private Date date;
    private Date time;
    private User user;
    private Billboard billboard;
    public Schedule(User user, Billboard billboard, Date date, Date time){
        this.billboard= billboard;
        this.date = date;
        this.time = time;
        this.user = user;
    }

    public Date getBillboardDate(){return date;}
    public Date getBillboardTime(){return time;}
    public void setDate(Date date){this.date = date;}
    public void setTime(Date time){this.time = time;}
    public void setBillboard(Billboard billboard){this.billboard = billboard;}
    public Date getDate(){return date;}
    public Date getTime(){return time;}
}
