package il.ac.shenkar.david.todolistex2;

import java.util.Calendar;
import java.util.Date;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.app.DialogFragment;

import java.text.SimpleDateFormat;

public class ListNodeActivity extends AppCompatActivity {

    Task t = new Task();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_node);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText date = (EditText)findViewById(R.id.taskDateEdit);
        date.setInputType(InputType.TYPE_NULL);

        EditText time = (EditText)findViewById(R.id.taskTimeEdit);
        time.setInputType(InputType.TYPE_NULL);

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
                if(hasFocus)showTimePickerDialog(v);
            }
        });
    }

    public void addTaskBtn (View view)
    {
        EditText desc = (EditText)findViewById(R.id.newTaskDesc);
        EditText nts = (EditText)findViewById(R.id.newTaskNotes);
        EditText date = (EditText)findViewById(R.id.taskDateEdit);
        EditText time = (EditText)findViewById(R.id.taskTimeEdit);

        Date myDate = null;
        String time_Date_str = null;

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
        }

        else
        {
            t = new Task(desc.getText().toString(),nts.getText().toString());

            RadioButton rb = (RadioButton) findViewById(R.id.lowRBtn);
            if(rb.isChecked())
                t.setPriority(Priority.LOW);
            else {
                    rb = (RadioButton) findViewById(R.id.medRBtn);
                    if(rb.isChecked())
                        t.setPriority(Priority.MEDIUM);

                    else
                    {
                        rb = (RadioButton) findViewById(R.id.highRBtn);
                        if(rb.isChecked())
                            t.setPriority(Priority.HIGH);
                        else
                            t.setPriority(Priority.MEDIUM);
                    }
                }


            time_Date_str = date.getText()+" "+time.getText();
            try
            {
                myDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(time_Date_str);
                t.setDueDate(myDate);
                t.setHasDate(true);
            }catch(Exception e){myDate=null;};

            Intent returnIntent = new Intent();

            DBManager.getInstance(this).addTask(t);

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
        Intent returnIntent = new Intent(this,MainActivity.class);
        setResult(RESULT_OK, returnIntent);
        startActivity(returnIntent);
    }

    public void onRadioButtonClicked(View view)
    {
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
