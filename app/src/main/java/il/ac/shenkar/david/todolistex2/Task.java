package il.ac.shenkar.david.todolistex2;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by David on 28-Nov-15.
 */
public class Task {

    private int ID;
    private String name;
    private String description;
    private Calendar taskDate; // timestamp for task
    private double latitude; // geo location
    private double longitude; // geo location

    public Task(int id, String name, String description, double lat, double lng)
    {
        super();
        this.ID = id;
        this.name = name;
        this.description = description;
        taskDate = null;
        this.latitude = lat;
        this.longitude = lng;
    }

    public int getId()
    {
        return ID;
    }

    public void setId(int id) {
        this.ID = id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String n)
    {
        name = n;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String d)
    {
        description = d;
    }

    public void setDate(String date, String time)
    {
        String tmpDate[];
        String tmpTime[];
        tmpDate = date.split("/");
        tmpTime = time.split(":");
        taskDate = new GregorianCalendar(Integer.parseInt(tmpDate[2]), Integer.parseInt(tmpDate[1]) - 1 , Integer.parseInt(tmpDate[0]), Integer.parseInt(tmpTime[0]), Integer.parseInt(tmpTime[1]));
    }

    public Calendar getTaskDate()
    {
        return taskDate;
    }

    public void setTaskDate(Calendar taskDate)
    {
        this.taskDate = taskDate;
    }

    public double getLat() {
        return latitude;
    }

    public void setLat(double lati)
    {
        this.latitude = lati;
    }

    public double getLng() {
        return longitude;
    }

    public void setLng(double longi)
    {
        this.longitude = longi;
    }
}
