package il.ac.shenkar.david.todolistex2;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.app.DialogFragment;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.parse.ParseException;
import com.parse.FindCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class ListNodeActivity extends AppCompatActivity
{
    private  Task t = new Task();
    private Spinner spin;
    private Spinner empolyeeSpinner;
    private int task_id=1;
    private String time_Date_str = null;
    private Locations returned_selc_loc;
    private EditText loc;

    private ParseObject parse_task=null;
    private List<String> team_memebers=null;
    private DBManager dbm;

    private static final int ACTIVITY_SELECT_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_node);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loc = (EditText)findViewById(R.id.taskLocation);
        loc.setClickable(false);

        EditText date = (EditText)findViewById(R.id.taskDateEdit);
        date.setInputType(InputType.TYPE_NULL);
        date = (EditText)findViewById(R.id.taskDateEdit);
        date.setText("");
        date.setVisibility(View.GONE);

        EditText time = (EditText)findViewById(R.id.taskTimeEdit);
        time.setInputType(InputType.TYPE_NULL);
        time = (EditText)findViewById(R.id.taskTimeEdit);
        time.setText("");
        time.setVisibility(View.GONE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //add listener to pop up date picker
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) showDatePickerDialog(v);
            }
        });

        //add listener to pop up time picker
        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) showTimePickerDialog(v);
            }
        });

        spin = (Spinner) findViewById(R.id.categorySpinner);

        empolyeeSpinner = (Spinner) findViewById(R.id.employeeSpinner);
        ArrayAdapter<String> empolyeeSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        empolyeeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        empolyeeSpinner.setAdapter(empolyeeSpinnerAdapter);

        //check is username & password exist
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("OTSUser");
        query.whereContains("TeamName", Globals.team_name);
        query.whereEqualTo("IsManager",0);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> usrs, ParseException e) {
                if (e == null) {

                    team_memebers = new ArrayList<String>(usrs.size());
                    for (ParseObject prso:usrs) {
                        team_memebers.add(new String(prso.getString("Username")));
                    }
                } else {//handle the error
                }
            }
        });

        for (String str:team_memebers)
        {
            empolyeeSpinnerAdapter.add(str);
        }
        empolyeeSpinnerAdapter.notifyDataSetChanged();

        RadioButton rb = (RadioButton) findViewById(R.id.todaydatebtn);
        rb.setChecked(true);

        RadioButton rbprty = (RadioButton) findViewById(R.id.medRBtn);
        rbprty.setChecked(true);

        returned_selc_loc = null;
    }

    public void addTaskBtn (View view)
    {
        boolean state=true;

        EditText desc = (EditText)findViewById(R.id.newTaskDesc);
        EditText date = (EditText)findViewById(R.id.taskDateEdit);
        EditText time = (EditText)findViewById(R.id.taskTimeEdit);
        loc = (EditText)findViewById(R.id.taskLocation);

        Date myDate = null;
        RadioButton rb;

       if(desc.getText().toString().matches(""))
       {
            new AlertDialog.Builder(this)
                    .setTitle("Fill Description")
                    .setMessage("Task description is empty.\nPlease fill task description")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            return;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
           desc.setBackgroundColor(Color.RED);
           state = false;
       }

        if(desc.getText().toString().length()==100)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Fill Description")
                    .setMessage("Task description length exceeded.\nDescription can contain 100 characters max.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            desc.setBackgroundColor(Color.RED);
            desc.requestFocus();
            state = false;
        }

        if ((returned_selc_loc==null)&&(state))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Select Location")
                    .setMessage("Task location must be selected in order to proceed.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            desc.setBackgroundColor(Color.RED);
            loc.requestFocus();
            loc.setClickable(false);
            state = false;
        }

        if (state)
        {
            t = new Task(desc.getText().toString());

            rb = (RadioButton) findViewById(R.id.lowRBtn);
            if(rb.isChecked())
                t.setPriority(Priority.LOW);
            else {
                    rb = (RadioButton) findViewById(R.id.medRBtn);
                    if(rb.isChecked())
                        t.setPriority(Priority.NORMAL);

                    else
                    {
                        rb = (RadioButton) findViewById(R.id.highRBtn);
                        if(rb.isChecked())
                            t.setPriority(Priority.URGENT);
                        else
                            t.setPriority(Priority.NORMAL);
                    }
                }

            rb = (RadioButton) findViewById(R.id.todaydatebtn);
            if(rb.isChecked())
            {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                time_Date_str = sdf.format(cal.getTime());
                try
                {
                    myDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(time_Date_str);
                    t.setDueDate(myDate);
                }catch(Exception e){myDate=null;}
            }


            else
            {
                rb = (RadioButton) findViewById(R.id.tomorrowdatebtn);
                if(rb.isChecked())
                {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, 1);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    time_Date_str = sdf.format(cal.getTime());
                    try
                    {
                        myDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(time_Date_str);

                        t.setDueDate(myDate);

                    }catch(Exception e){myDate=null;}
                }
                else
                {
                    rb = (RadioButton) findViewById(R.id.customdatebtn);
                    if(rb.isChecked())
                    {
                        time_Date_str = date.getText()+" "+time.getText();
                        try
                        {
                            myDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(time_Date_str);
                            t.setDueDate(myDate);

                            Intent alarmNotificationIntent = new Intent(this, ReminderNotification.class);
                            alarmNotificationIntent.putExtra("task", t);

                            PendingIntent pendingIntent =
                                    PendingIntent.getBroadcast(this, (int) task_id, alarmNotificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                            AlarmManager alarmManager = (AlarmManager)getSystemService(this.ALARM_SERVICE);

                            Calendar calendar = Calendar.getInstance();

                            calendar.setTimeInMillis(t.getDueDate().getTime());

                            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            Toast.makeText(this, "Alarm Set", Toast.LENGTH_LONG).show();
                        }catch(Exception e){myDate=null;}

                        t.setTask_sts(Task_Status.WAITING);
                    }
                }
            }

            int position = spin.getSelectedItemPosition();
            switch(position)
            {
                case 0:
                    t.setTask_catg(Category.GENERAL);
                    break;
                case 1:
                    t.setTask_catg(Category.CLEANING);
                    break;
                case 2:
                    t.setTask_catg(Category.ELECTRICITY);
                    break;
                case 3:
                    t.setTask_catg(Category.COMPUTERS);
                    break;
                case 4:
                    t.setTask_catg(Category.OTHER);
                    break;
            }

            t.setTsk_location(returned_selc_loc);
            t.setEmp_name("");
            t.setTaskId(task_id);
            task_id++;
            Intent returnIntent = new Intent();
            dbm = DBManager.getInstance(this);
            long seq_tsk_id = dbm.addTask(t);
            t.setTaskId(seq_tsk_id);

            parse_task = new ParseObject("Task");
            parse_task.put("Description",t.getDescription());
            parse_task.put("DueDate", t.getDueDate());
            parse_task.put("Priority",t.getPriority().ordinal());
            position = (t.getCompleted()) ? 1 : 0;
            parse_task.put("IsCompleted",position);
            parse_task.put("Location",t.getTsk_location().ordinal());
            parse_task.put("Category", t.getTask_catg().ordinal());
            parse_task.put("Status", t.getTask_sts().ordinal());
            parse_task.put("TeamName",Globals.team_name);
            parse_task.put("Employee",t.getEmp_name());
            parse_task.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // if null, it means the save has succeeded
                        String id = parse_task.getObjectId(); // Here you go
                        t.setParse_task_id(id);
                        dbm.updateParseID(t);
                    } else {
                        // the save call was not successful.
                    }
                }
            });

            returnIntent.putExtra("task", t);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }

    public void discardBtnClick(View view)
    {
        Toast.makeText(this, "Task Creation Discarded", Toast.LENGTH_LONG).show();
        finish();
    }

    public void onRadioButtonClicked(View view)
    {
    }

    public void onRadioTimeDateButtonClicked(View view)
    {
        RadioButton rb = (RadioButton) findViewById(R.id.todaydatebtn);
        if(rb.isChecked())
        {
            EditText date = (EditText)findViewById(R.id.taskDateEdit);
            date.setText("");
            date.setVisibility(View.GONE);
            EditText time = (EditText)findViewById(R.id.taskTimeEdit);
            time.setText("");
            time.setVisibility(View.GONE);
        }

        else
        {
           rb = (RadioButton) findViewById(R.id.tomorrowdatebtn);
            if(rb.isChecked())
            {
                EditText date = (EditText)findViewById(R.id.taskDateEdit);
                date.setText("");
                date.setVisibility(View.GONE);
                EditText time = (EditText)findViewById(R.id.taskTimeEdit);
                time.setText("");
                time.setVisibility(View.GONE);
            }

            else
            {
                rb = (RadioButton) findViewById(R.id.customdatebtn);
                if(rb.isChecked())
                {
                    EditText date = (EditText)findViewById(R.id.taskDateEdit);
                    date.setVisibility(View.VISIBLE);
                    EditText time = (EditText)findViewById(R.id.taskTimeEdit);
                    time.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public  void gotoselectloc (View v)
    {
        Intent intent = new Intent(this,select_location.class);
        startActivityForResult(intent, ACTIVITY_SELECT_LOCATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACTIVITY_SELECT_LOCATION: {
                        returned_selc_loc = (Locations) data.getSerializableExtra("location");
                        loc.setText(returned_selc_loc.toString());
                        loc.setClickable(false);
                        break;
                }
                default:
                    break;
            }
        }
    }
}
