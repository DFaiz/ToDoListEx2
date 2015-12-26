package il.ac.shenkar.david.todolistex2;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David on 28-Nov-15.
 */
public class Task implements Serializable {

    private long taskId;
    private String description;
    private Boolean completed = false;
    private Date dueDate;
    private Boolean hasDate = false;
    private Priority task_priority;
    private Boolean toDelete = false;
    private Category task_catg;
    private Task_Status task_sts;
    private Locations tsk_location;

    public Task ()
    {}

    public Task(int id,String description)
    {
        super();
        this.taskId=id;
        this.description = description;
        dueDate = null;
        task_priority = Priority.NORMAL;
        task_sts = Task_Status.WAITING;
        tsk_location = Locations.Meeting_Room;
        task_catg = Category.GENERAL;
    }

    public Task(String description)
    {
        super();
        this.description = description;
        dueDate = null;
        task_priority = Priority.NORMAL;
        task_sts=Task_Status.WAITING;
        tsk_location = Locations.Meeting_Room;
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

    public Category getTask_catg() {
        return task_catg;
    }

    public void setTask_catg(Category task_catg) {
        this.task_catg = task_catg;
    }

    public Task_Status getTask_sts() {
        return task_sts;
    }

    public void setTask_sts(Task_Status task_sts) {
        this.task_sts = task_sts;
    }

    public Locations getTsk_location() {
        return tsk_location;
    }

    public void setTsk_location(Locations tsk_location) {
        this.tsk_location = tsk_location;
    }
}
