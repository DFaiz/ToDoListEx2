package il.ac.shenkar.david.todolistex2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import il.ac.shenkar.david.todolistex2.TaskContract.TaskItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by David on 05-Dec-15.
 */
public class DBManager extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 10;

    private static final String DATABASE_NAME = "tasks_app";
    private static DBManager instance;

    public DBManager(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBManager getInstance (Context context)
    {
        if (instance == null) {
            instance = new DBManager(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
       final String SQL_CREATE_TASKS_TABLE = "CREATE TABLE "
            + TaskItem.TABLE_NAME + " ( "
            + TaskItem.COLUMN_NAME_TASK_ID+ " INTEGER PRIMARY KEY autoincrement,"
            + TaskItem.COLUMN_NAME_DESCRIPTION+ " TEXT NOT NULL, "
            + TaskItem.COLUMN_NAME_DATE_ENABLED+ " INTEGER NOT NULL, "
            + TaskItem.COLUMN_NAME_DUE_DATE+ " TEXT NOT NULL, "
            + TaskItem.COLUMN_NAME_PRIORITY+ " INTEGER NOT NULL, "
            + TaskItem.COLUMN_NAME_COMPLETED+ " INTEGER, "
            + TaskItem.COLUMN_NAME_LOCATION+ " INTEGER NOT NULL, "
            + TaskItem.COLUMN_NAME_CATEGORY+ " INTEGER NOT NULL, "
            + TaskItem.COLUMN_NAME_STATUS+ " INTEGER NOT NULL "
            + " )";
        db.execSQL(SQL_CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TaskItem.TABLE_NAME);
        onCreate(db);
    }

    public long addTask(Task task)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TaskItem.COLUMN_NAME_DESCRIPTION, task.getDescription());
        values.put(TaskItem.COLUMN_NAME_DATE_ENABLED, task.getHasDate());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        values.put(TaskItem.COLUMN_NAME_DUE_DATE, sdf.format(task.getDueDate()));
        values.put(TaskItem.COLUMN_NAME_PRIORITY, task.getPriority().ordinal());
        int myInt = (task.getCompleted()) ? 1 : 0;
        values.put(TaskItem.COLUMN_NAME_COMPLETED, myInt);
        values.put(TaskItem.COLUMN_NAME_LOCATION, task.getTsk_location().ordinal());
        values.put(TaskItem.COLUMN_NAME_CATEGORY, task.getTask_catg().ordinal());
        values.put(TaskItem.COLUMN_NAME_STATUS, task.getTask_sts().ordinal());

        // Inserting Row
        long newTaskID = db.insert(TaskItem.TABLE_NAME, null, values);
        //Closing database connection
        db.close();
       return  newTaskID;
    }

    public List<Task> getAllTasks ()
    {
        List<Task> taskList = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TaskItem.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task tsktsk = new Task();

                int id = cursor.getInt(cursor.getColumnIndex(TaskItem.COLUMN_NAME_TASK_ID));
                tsktsk.setTaskId(id);
                String desc = cursor.getString(cursor.getColumnIndex(TaskItem.COLUMN_NAME_DESCRIPTION));
                tsktsk.setDescription(desc);
                id =  cursor.getInt(cursor.getColumnIndex(TaskItem.COLUMN_NAME_COMPLETED));
                if(id==1)
                    tsktsk.setCompleted(true);
                else
                    tsktsk.setCompleted(false);

                tsktsk.setHasDate(true);

                desc = cursor.getString(cursor.getColumnIndex(TaskItem.COLUMN_NAME_DUE_DATE));
                tsktsk.setDueDate(desc);

                id =  cursor.getInt(cursor.getColumnIndex(TaskItem.COLUMN_NAME_PRIORITY));
                if(id==0)
                    tsktsk.setPriority(Priority.LOW);
                if(id==1)
                    tsktsk.setPriority(Priority.NORMAL);
                if (id==2)
                    tsktsk.setPriority(Priority.URGENT);

                desc = cursor.getString(cursor.getColumnIndex(TaskItem.COLUMN_NAME_LOCATION));
                if(desc==Locations.Meeting_Room.toString())
                    tsktsk.setTsk_location(Locations.Meeting_Room);

                if(desc==Locations.Office_245.toString())
                    tsktsk.setTsk_location(Locations.Office_245);

                if(desc==Locations.Lobby.toString())
                    tsktsk.setTsk_location(Locations.Lobby);

                if(desc==Locations.NOC.toString())
                    tsktsk.setTsk_location(Locations.NOC);

                if(desc==Locations.VPsoffice.toString())
                    tsktsk.setTsk_location(Locations.VPsoffice);

                id =  cursor.getInt(cursor.getColumnIndex(TaskItem.COLUMN_NAME_CATEGORY));
                if(id==0)
                    tsktsk.setTask_catg(Category.CLEANING);
                if(id==1)
                    tsktsk.setTask_catg(Category.ELECTRICITY);
                if (id==2)
                    tsktsk.setTask_catg(Category.COMPUTERS);
                if (id==3)
                    tsktsk.setTask_catg(Category.GENERAL);
                if (id==4)
                    tsktsk.setTask_catg(Category.OTHER);

                id =  cursor.getInt(cursor.getColumnIndex(TaskItem.COLUMN_NAME_STATUS));
                if(id==0)
                    tsktsk.setTask_sts(Task_Status.WAITING);
                if(id==1)
                    tsktsk.setTask_sts(Task_Status.INPROCESS);
                if (id==2)
                    tsktsk.setTask_sts(Task_Status.DONE);

                taskList.add(tsktsk);
            } while (cursor.moveToNext());
        }

        db.close();
        // return list
        return taskList;
    }

    public void updateTask (Task task)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TaskItem.COLUMN_NAME_DESCRIPTION, task.getDescription());
        values.put(TaskItem.COLUMN_NAME_DATE_ENABLED, task.getHasDate());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        values.put(TaskItem.COLUMN_NAME_DUE_DATE, sdf.format(task.getDueDate()));
        values.put(TaskItem.COLUMN_NAME_PRIORITY, task.getPriority().ordinal());
        int myInt = (task.getCompleted()) ? 1 : 0;
        values.put(TaskItem.COLUMN_NAME_COMPLETED, myInt);
        values.put(TaskItem.COLUMN_NAME_LOCATION, task.getTsk_location().ordinal());
        values.put(TaskItem.COLUMN_NAME_CATEGORY, task.getTask_catg().ordinal());
        values.put(TaskItem.COLUMN_NAME_STATUS, task.getTask_sts().ordinal());

        // updating row
        db.update(TaskItem.TABLE_NAME, values, TaskItem.COLUMN_NAME_TASK_ID + " = ?", new String[] { String.valueOf(task.getTaskId()) });
    }

    public void deleteTask (Task task)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TaskItem.TABLE_NAME, TaskItem.COLUMN_NAME_TASK_ID + " = ?",
                new String[] { String.valueOf(task.getTaskId()) });
        db.close();
    }
}
