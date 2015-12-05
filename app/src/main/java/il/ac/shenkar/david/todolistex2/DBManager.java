package il.ac.shenkar.david.todolistex2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import il.ac.shenkar.david.todolistex2.TaskContract.TaskItem;
import il.ac.shenkar.david.todolistex2.TaskContract.TaskLocation;

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
    private static final int DATABASE_VERSION = 2;

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
                + TaskItem.TABLE_NAME + " ("
                + TaskItem.COLUMN_NAME_TASK_ID+ " INTEGER PRIMARY KEY,"
                + TaskItem.COLUMN_NAME_DESCRIPTION+ " TEXT NOT NULL, "
                + TaskItem.COLUMN_NAME_PRIORITY+ " INTEGER, "
                + TaskItem.COLUMN_NAME_DATE_ENABLED+ " TINYINT, "
                + TaskItem.COLUMN_NAME_DUE_DATE+ " TEXT, "
                + TaskItem.COLUMN_NAME_COMPLETED+ " TINYINT, "
                + TaskItem.COLUMN_NAME_LOCATION_ENABLED+ " TINYINT "
                +")";
        db.execSQL(SQL_CREATE_TASKS_TABLE);

        final String SQL_CREATE_LOCATIONS_TABLE = "CREATE TABLE "
                + TaskLocation.TABLE_NAME + " ("
                + TaskLocation.COLUMN_NAME_TASK_ID+ " INTEGER PRIMARY KEY,"
                + TaskLocation.COLUMN_NAME_LOCATION_LAT+ " REAL, "
                + TaskLocation.COLUMN_NAME_LOCATION_LON+ " REAL, "
                +")";
        db.execSQL(SQL_CREATE_LOCATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TaskLocation.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TaskItem.TABLE_NAME);
        onCreate(db);
    }

    public long addTask(Task task)
    {
      /*  SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TaskItem.COLUMN_NAME_DESCRIPTION, task.getDescription());
       // values.put(TaskItem.COLUMN_NAME_PRIORITY, task.getPriority().toString());
        values.put(TaskItem.COLUMN_NAME_PRIORITY, 3);
        values.put(TaskItem.COLUMN_NAME_DATE_ENABLED, 0);
        values.put(TaskItem.COLUMN_NAME_DUE_DATE, "");
        values.put(TaskItem.COLUMN_NAME_COMPLETED, 0);
        values.put(TaskItem.COLUMN_NAME_LOCATION_ENABLED, 0);

        // Inserting Row
        //long res = db.insert(TaskItem.TABLE_NAME, null, values);
    //    db.insert(TaskItem.TABLE_NAME, null, values);
     //   db.close(); // Closing database connection */
        long res = 100;
        return res;
    }
}
