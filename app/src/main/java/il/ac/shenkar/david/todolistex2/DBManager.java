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
import android.util.Log;

/**
 * Created by David on 05-Dec-15.
 */
public class DBManager extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 28;

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
            + TaskItem.COLUMN_NAME_PARSE_TASK_ID+ " TEXT,"
            + TaskItem.COLUMN_NAME_DESCRIPTION+ " TEXT NOT NULL, "
            + TaskItem.COLUMN_NAME_DUE_DATE+ " TEXT NOT NULL, "
            + TaskItem.COLUMN_NAME_PRIORITY+ " INTEGER NOT NULL, "
            + TaskItem.COLUMN_NAME_LOCATION+ " INTEGER NOT NULL, "
            + TaskItem.COLUMN_NAME_CATEGORY+ " INTEGER NOT NULL, "
            + TaskItem.COLUMN_NAME_STATUS+ " INTEGER NOT NULL, "
            + TaskItem.COLUMN_NAME_EMPLOYEE+ " TEXT NOT NULL, "
            + TaskItem.COLUMN_NAME_TEAM+ " TEXT NOT NULL "
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

        values.put(TaskItem.COLUMN_NAME_PARSE_TASK_ID,"");
        values.put(TaskItem.COLUMN_NAME_DESCRIPTION, task.getDescription());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        values.put(TaskItem.COLUMN_NAME_DUE_DATE, sdf.format(task.getDueDate()));
        values.put(TaskItem.COLUMN_NAME_PRIORITY, task.getPriority().ordinal());
        //Log.w("loc", "task.getTsk_location().ordinal()" + task.getTsk_location().ordinal());
        values.put(TaskItem.COLUMN_NAME_LOCATION, task.getTsk_location());
        //Log.w("loc", "task.getTsk_location().ordinal()" + task.getTsk_location().ordinal());
        values.put(TaskItem.COLUMN_NAME_CATEGORY, task.getTask_catg().ordinal());
        values.put(TaskItem.COLUMN_NAME_STATUS, task.getTask_sts().ordinal());
        values.put(TaskItem.COLUMN_NAME_EMPLOYEE, task.getEmp_name());
        values.put(TaskItem.COLUMN_NAME_TEAM, Globals.team_name);

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
        String selectQuery = "SELECT  * FROM " + TaskItem.TABLE_NAME + " ORDER BY " + TaskItem.COLUMN_NAME_DUE_DATE + " DESC ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task tsktsk = new Task();

                int id = cursor.getInt(cursor.getColumnIndex(TaskItem.COLUMN_NAME_TASK_ID));
                tsktsk.setTaskId(id);
                String desc = cursor.getString(cursor.getColumnIndex(TaskItem.COLUMN_NAME_PARSE_TASK_ID));
                tsktsk.setParse_task_id(desc);
                desc = cursor.getString(cursor.getColumnIndex(TaskItem.COLUMN_NAME_DESCRIPTION));
                tsktsk.setDescription(desc);

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
                if(desc==Location.Meeting_Room.toString())
                    tsktsk.setTsk_location(Location.Meeting_Room.ordinal());

                if(desc==Location.Office_245.toString())
                    tsktsk.setTsk_location(Location.Office_245.ordinal());

                if(desc==Location.Lobby.toString())
                    tsktsk.setTsk_location(Location.Lobby.ordinal());

                if(desc==Location.NOC.toString())
                    tsktsk.setTsk_location(Location.NOC.ordinal());

                if(desc==Location.VPsoffice.toString())
                    tsktsk.setTsk_location(Location.VPsoffice.ordinal());

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
                    tsktsk.setTask_sts(Task_Status.INPROGESS);
                if (id==2)
                    tsktsk.setTask_sts(Task_Status.DONE);

                desc = (cursor.getString(cursor.getColumnIndex(TaskItem.COLUMN_NAME_EMPLOYEE)));
                tsktsk.setEmp_name(desc);

                taskList.add(tsktsk);
            } while (cursor.moveToNext());
        }

        db.close();
        // return list
        return taskList;
    }

    public List<Task> getSortedTasks (Sorting srt_code)
    {
        List<Task> taskList = new ArrayList<Task>();
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        // Select sorted tasks Query
        if(srt_code.ordinal()>=Sorting.WAITING.ordinal())
        {
            selectQuery = "SELECT  * FROM " + TaskItem.TABLE_NAME + " WHERE " + TaskItem.COLUMN_NAME_STATUS + " =?";
            if(srt_code==Sorting.WAITING){
                cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(Task_Status.WAITING.ordinal())},null);}
            if(srt_code==Sorting.INPROGESS){
                cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(Task_Status.INPROGESS.ordinal())},null);}
            if(srt_code==Sorting.DONE){
                cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(Task_Status.DONE.ordinal())},null);
            }

        }
        else
        {
            if(srt_code.ordinal()==Sorting.PRIORITY.ordinal())
            {
                selectQuery = "SELECT  * FROM " + TaskItem.TABLE_NAME + " ORDER BY " + TaskItem.COLUMN_NAME_PRIORITY + " DESC ";
                cursor = db.rawQuery(selectQuery,null);
            }
        }

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task tsktsk = new Task();

                int id = cursor.getInt(cursor.getColumnIndex(TaskItem.COLUMN_NAME_TASK_ID));
                tsktsk.setTaskId(id);
                String desc = cursor.getString(cursor.getColumnIndex(TaskItem.COLUMN_NAME_PARSE_TASK_ID));
                tsktsk.setParse_task_id(desc);
                desc = cursor.getString(cursor.getColumnIndex(TaskItem.COLUMN_NAME_DESCRIPTION));
                tsktsk.setDescription(desc);

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
                if(desc==Location.Meeting_Room.toString())
                    tsktsk.setTsk_location(Location.Meeting_Room.ordinal());

                if(desc==Location.Office_245.toString())
                    tsktsk.setTsk_location(Location.Office_245.ordinal());

                if(desc==Location.Lobby.toString())
                    tsktsk.setTsk_location(Location.Lobby.ordinal());

                if(desc==Location.NOC.toString())
                    tsktsk.setTsk_location(Location.NOC.ordinal());

                if(desc==Location.VPsoffice.toString())
                    tsktsk.setTsk_location(Location.VPsoffice.ordinal());

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
                    tsktsk.setTask_sts(Task_Status.INPROGESS);
                if (id==2)
                    tsktsk.setTask_sts(Task_Status.DONE);

                desc = (cursor.getString(cursor.getColumnIndex(TaskItem.COLUMN_NAME_EMPLOYEE)));
                tsktsk.setEmp_name(desc);

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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        values.put(TaskItem.COLUMN_NAME_DUE_DATE, sdf.format(task.getDueDate()));
        values.put(TaskItem.COLUMN_NAME_PRIORITY, task.getPriority().ordinal());
        //values.put(TaskItem.COLUMN_NAME_LOCATION, task.getTsk_location().ordinal());
        values.put(TaskItem.COLUMN_NAME_LOCATION, task.getTsk_location());
        values.put(TaskItem.COLUMN_NAME_CATEGORY, task.getTask_catg().ordinal());
        values.put(TaskItem.COLUMN_NAME_STATUS, task.getTask_sts().ordinal());
        values.put(TaskItem.COLUMN_NAME_EMPLOYEE, task.getEmp_name());

        // updating row
        db.update(TaskItem.TABLE_NAME, values, TaskItem.COLUMN_NAME_TASK_ID + " = ?", new String[] { String.valueOf(task.getTaskId()) });
    }

    public void updateParseID (Task tsktsk)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskItem.COLUMN_NAME_PARSE_TASK_ID,tsktsk.getParse_task_id());
        // updating row
        db.update(TaskItem.TABLE_NAME, values, TaskItem.COLUMN_NAME_TASK_ID + " = ?", new String[]{String.valueOf(tsktsk.getTaskId())});
    }

    public void deleteTask (Task task)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TaskItem.TABLE_NAME, TaskItem.COLUMN_NAME_TASK_ID + " = ?",
                new String[]{String.valueOf(task.getTaskId())});
        db.close();
    }

    public boolean ifTaskExists (String parseTaskID)
    {
        // Select Task Query
        String selectQuery = "SELECT * FROM " + TaskItem.TABLE_NAME + " WHERE " + TaskItem.COLUMN_NAME_PARSE_TASK_ID + " =?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(parseTaskID)}, null);
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public void clearDB ()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TaskItem.TABLE_NAME);
        onCreate(db);
    }
}
