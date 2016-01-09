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
        public static final String COLUMN_NAME_TASK_ID = "taskid";
        public static final String COLUMN_NAME_PARSE_TASK_ID = "parse_taskid";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_COMPLETED = "completed";
        public static final String COLUMN_NAME_DUE_DATE = "dueDate";
        public static final String COLUMN_NAME_PRIORITY = "priority";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_CATEGORY = "tsk_category";
        public static final String COLUMN_NAME_STATUS = "tsk_status";
    }
}
