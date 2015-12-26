package il.ac.shenkar.david.todolistex2;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.app.DialogFragment;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ListNodeActivity extends AppCompatActivity
{
    Task t = new Task();
    Spinner spin;
    Spinner location_spinner;
    Spinner empolyeeSpinner;
    int task_id=1;
    String time_Date_str = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_node);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        location_spinner = (Spinner) findViewById(R.id.locationSpinner);

        empolyeeSpinner = (Spinner) findViewById(R.id.employeeSpinner);
        ArrayAdapter<String> empolyeeSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        empolyeeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        empolyeeSpinner.setAdapter(empolyeeSpinnerAdapter);
        //empolyeeSpinnerAdapter.add("my name is");
        //empolyeeSpinnerAdapter.notifyDataSetChanged();

        RadioButton rb = (RadioButton) findViewById(R.id.todaydatebtn);
        rb.setChecked(true);

        RadioButton rbprty = (RadioButton) findViewById(R.id.medRBtn);
        rbprty.setChecked(true);
    }

    public void addTaskBtn (View view)
    {
        boolean state=true;

        EditText desc = (EditText)findViewById(R.id.newTaskDesc);
        EditText date = (EditText)findViewById(R.id.taskDateEdit);
        EditText time = (EditText)findViewById(R.id.taskTimeEdit);

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
                    .setIcon(android.R.drawable.ic_dialog_info)
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
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
            desc.setBackgroundColor(Color.RED);
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
                    t.setHasDate(true);
                    Intent alarmNotificationIntent = new Intent(this, ReminderNotification.class);
                    alarmNotificationIntent.putExtra("task", t);

                    PendingIntent pendingIntent =
                            PendingIntent.getBroadcast(this, (int) task_id, alarmNotificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

                    Calendar calendar = Calendar.getInstance();

                    calendar.setTimeInMillis(t.getDueDate().getTime());

                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

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
                        t.setHasDate(true);

                        Intent alarmNotificationIntent = new Intent(this, ReminderNotification.class);
                        alarmNotificationIntent.putExtra("task", t);

                        PendingIntent pendingIntent =
                                PendingIntent.getBroadcast(this, (int) task_id, alarmNotificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

                        Calendar calendar = Calendar.getInstance();

                        calendar.setTimeInMillis(t.getDueDate().getTime());

                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

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
                            t.setHasDate(true);

                            Intent alarmNotificationIntent = new Intent(this, ReminderNotification.class);
                            alarmNotificationIntent.putExtra("task", t);

                            PendingIntent pendingIntent =
                                    PendingIntent.getBroadcast(this, (int) task_id, alarmNotificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                            AlarmManager alarmManager = (AlarmManager)getSystemService(this.ALARM_SERVICE);

                            Calendar calendar = Calendar.getInstance();

                            calendar.setTimeInMillis(t.getDueDate().getTime());

                            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

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

            position = location_spinner.getSelectedItemPosition();
            switch(position)
            {
                case 0:
                    t.setTsk_location(Locations.Meeting_Room);
                    break;
                case 1:
                    t.setTsk_location(Locations.Office_245);
                    break;
                case 2:
                    t.setTsk_location(Locations.Lobby);
                    break;
                case 3:
                    t.setTsk_location(Locations.NOC);
                    break;
                case 4:
                    t.setTsk_location(Locations.VPsoffice);
                    break;
            }

            t.setTaskId(task_id);
            task_id++;
            Intent returnIntent = new Intent();

           // DBManager.getInstance(this).addTask(t);

          //  DBManager dbm = new DBManager(this);
         //   long seq_tsk_id = dbm.addTask(t);
          //  t.setTaskId(seq_tsk_id);

            returnIntent.putExtra("task",t);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }

    public void discardBtnClick(View view)
    {
        Toast.makeText(this, "Task creation discarded", Toast.LENGTH_LONG).show();
        Intent returnIntent = new Intent(this,MainActivity.class);
        setResult(RESULT_OK, returnIntent);
        startActivity(returnIntent);
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
}
