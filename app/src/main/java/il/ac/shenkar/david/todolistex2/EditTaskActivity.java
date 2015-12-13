package il.ac.shenkar.david.todolistex2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.app.DialogFragment;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by David on 13-Dec-15.
 */
public class EditTaskActivity extends AppCompatActivity
{
    Task edited_t;
    Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText date = (EditText)findViewById(R.id.taskDateEdit);
        date.setInputType(InputType.TYPE_NULL);

        EditText time = (EditText)findViewById(R.id.taskTimeEdit);
        time.setInputType(InputType.TYPE_NULL);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(R.string.title_activity_list_edit);
/*
        EditText desc = (EditText)findViewById(R.id.newTaskDesc);
        EditText nts = (EditText)findViewById(R.id.newTaskNotes);

        Date myDate = null;
        String time_Date_str = null;

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

        spin = (Spinner) findViewById(R.id.categorySpinner);

        //get the intent (will determine if we in update mode)
        Intent i = getIntent();
        //if we have extra in our intent,its mean we on update mode
        if(i.hasExtra("task"))
        {
            edited_t = (Task)i.getSerializableExtra("item");

            if(edited_t.getHasDate())
            {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat sdft = new SimpleDateFormat("HH:mm");
                date.setText(sdf.format(edited_t.getDueDate()));
                time.setText(sdft.format(edited_t.getDueDate()));
            }

            //set priority
            switch (edited_t.getPriority())
            {
                case URGENT:
                    RadioButton hrb = (RadioButton)findViewById(R.id.highRBtn);
                    hrb.setChecked(true);
                    break;
                case NORMAL:
                    RadioButton mrb = (RadioButton)findViewById(R.id.medRBtn);
                    mrb.setChecked(true);
                    break;
                case LOW:
                    RadioButton nrb = (RadioButton)findViewById(R.id.lowRBtn);
                    nrb.setChecked(true);
                    break;
            }

            //set the task description
            desc.setText(edited_t.getDescription());
            if((edited_t.getTask_notes()!="")&&(edited_t.getTask_notes()!=null))
            {
                //set the task notes
                desc.setText(edited_t.getTask_notes());
            }

            Category tmp_ctg = edited_t.getTask_catg();

            switch(tmp_ctg)
            {
                case GENERAL:
                    spin.setSelection(0);
                    break;
                case CLEANING:
                    spin.setSelection(1);
                    break;
                case ELECTRICITY:
                    spin.setSelection(2);
                    break;
                case COMPUTERS:
                    spin.setSelection(3);
                    break;
                case OTHER:
                    spin.setSelection(4);
                    break;
            }

        }*/

    }

    public void editTaskbtn (View view)
    {
        boolean state=true;

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
            state = false;
        }

        if(desc.getText().toString().length()==100)
        {
            state = false;

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

        if (state) {
            edited_t = new Task(desc.getText().toString(), nts.getText().toString());

            RadioButton rb = (RadioButton) findViewById(R.id.lowRBtn);
            if (rb.isChecked())
                edited_t.setPriority(Priority.LOW);
            else {
                rb = (RadioButton) findViewById(R.id.medRBtn);
                if (rb.isChecked())
                    edited_t.setPriority(Priority.NORMAL);

                else {
                    rb = (RadioButton) findViewById(R.id.highRBtn);
                    if (rb.isChecked())
                        edited_t.setPriority(Priority.URGENT);
                    else
                        edited_t.setPriority(Priority.NORMAL);
                }
            }

            time_Date_str = date.getText() + " " + time.getText();
            try {
                myDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(time_Date_str);
                edited_t.setDueDate(myDate);
                edited_t.setHasDate(true);
            }catch(Exception e){myDate=null;}

            edited_t.setTask_sts(Task_Status.WAITING);

            int position = spin.getSelectedItemPosition();
            switch (position) {
                case 0:
                    edited_t.setTask_catg(Category.GENERAL);
                    break;
                case 1:
                    edited_t.setTask_catg(Category.CLEANING);
                    break;
                case 2:
                    edited_t.setTask_catg(Category.ELECTRICITY);
                    break;
                case 3:
                    edited_t.setTask_catg(Category.COMPUTERS);
                    break;
                case 4:
                    edited_t.setTask_catg(Category.OTHER);
                    break;
            }
            Intent returnIntent = new Intent();
            returnIntent.putExtra("task", edited_t);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }

    public void cancelTaskEdit(View view)
    {
        Toast.makeText(this, "Task edit discarded", Toast.LENGTH_LONG).show();
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
