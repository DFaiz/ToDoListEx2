package il.ac.shenkar.david.todolistex2;

import android.provider.BaseColumns;

/**
 * Created by David on 05-Dec-15.
 */
public final class TaskContract
{
    public TaskContract(){}

    public static abstract class TaskItem implements BaseColumns {
        public static final String TABLE_NAME = "TASKS";
        public static final String COLUMN_NAME_TASK_ID = "TASKID";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_COMPLETED = "completed";
        public static final String COLUMN_NAME_PRIORITY = "priority";
        public static final String COLUMN_NAME_DATE_ENABLED = "dateEnable";
        public static final String COLUMN_NAME_DUE_DATE = "dueDate";
        public static final String COLUMN_NAME_LOCATION_ENABLED = "locEnable";
    }

    public static abstract class TaskLocation implements BaseColumns {
        public static final String TABLE_NAME = "Task_Locations";
        public static final String COLUMN_NAME_TASK_ID = "TASKID";
        public static final String COLUMN_NAME_LOCATION_LAT = "LATITUDE";
        public static final String COLUMN_NAME_LOCATION_LON = "LONGITUDE";
    }
}
