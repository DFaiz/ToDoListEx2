package il.ac.shenkar.david.todolistex2;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David on 28-Nov-15.
 */
public class Task implements Serializable {

    private long taskId;
    private String parse_task_id;
    private String description;
    private Date dueDate;
    private Priority task_priority;
    private Boolean toDelete = false;
    private Category task_catg;
    private Task_Status task_sts;
    //private Location tsk_location;
    private int tsk_location;
    private String emp_name;

    public Task ()
    {}

    public Task(int id,String description)
    {
        super();
        this.taskId=id;
        this.description = description;
        dueDate = null;
        parse_task_id = null;
        task_priority = Priority.NORMAL;
        task_sts = Task_Status.WAITING;
        task_catg = Category.GENERAL;
        emp_name = null;
    }

    public Task(String description)
    {
        super();
        this.description = description;
        dueDate = null;
        task_priority = Priority.NORMAL;
        task_sts=Task_Status.WAITING;
        task_catg = Category.GENERAL;
        this.tsk_location = -1;
        emp_name = null;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String d)
    {
        description = d;
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
                return;
            }
            Date myDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dueDate);
            setDueDate(myDate);
        }catch(Exception e){};
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

    public int getTsk_location() {
        return tsk_location;
    }

    public void setTsk_location(int new_tsk_location) {
        tsk_location = new_tsk_location;
    }

    public String getParse_task_id() {
        return parse_task_id;
    }

    public void setParse_task_id(String parse_task_id) {
        this.parse_task_id = parse_task_id;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }
}
