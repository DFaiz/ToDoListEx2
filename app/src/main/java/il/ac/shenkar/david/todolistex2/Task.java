package il.ac.shenkar.david.todolistex2;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

enum Priority {
    LOW,MEDIUM,HIGH
}

/**
 * Created by David on 28-Nov-15.
 */
public class Task implements Serializable {

    private long taskId;
    private String description;
    private Boolean completed = false;
    private Boolean hasLocation = false;
    private Boolean hasDate = false;
    private MapCords location;
    private Date dueDate;
    private Priority task_priority;
    private Boolean toDelete = false;

    public Task(int id,String description)
    {
        super();
        this.taskId=id;
        this.description = description;
        dueDate = null;
        location = null;
        task_priority = Priority.LOW;
    }

    public Task(String description)
    {
        super();
        this.description = description;
        dueDate = null;
        location = null;
        task_priority = Priority.LOW;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String d)
    {
        description = d;
    }

    public Boolean getCompleted() {
        return completed;
    }
    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public Priority getPriority() {
        return task_priority;
    }

    public void setPriority(Priority priority) {
        this.task_priority = priority;
    }


    public MapCords getLocation() {
        return location;
    }

    public void setLocation(MapCords location) {
        this.location = location;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate)
    {
        this.dueDate = dueDate;
    }

    public void setDueDate(String dueDate)
    {
        try
        {
            if(dueDate==null || dueDate=="")
            {
                setHasDate(false);
                return;
            }
            Date myDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dueDate);
            setDueDate(myDate);
            setHasDate(true);
        }catch(Exception e){};
    }


    public Boolean getHasDate() {
        return hasDate;
    }

    public void setHasDate(Boolean hasDate) {
        this.hasDate = hasDate;
    }


    public Boolean getToDelete() {
        return toDelete;
    }

    public void setToDelete(Boolean toDelete) {
        this.toDelete = toDelete;
    }

    public Boolean getHasLocation() {
        return hasLocation;
    }

    public void setHasLocation(Boolean hasLocation) {
        this.hasLocation = hasLocation;
    }
}
