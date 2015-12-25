package il.ac.shenkar.david.todolistex2;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditTaskActivity extends AppCompatActivity
{
    private Task tastToEdit;
    private Spinner spin;
    private Spinner loc_spin;
    private RadioButton rb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //get the intent (will determine if we in update mode)
        Intent i = getIntent();

        tastToEdit = (Task)i.getSerializableExtra("task");

        EditText desc = (EditText)findViewById(R.id.editTaskDesc);
        desc.setText(tastToEdit.getDescription());

        Priority prty = tastToEdit.getPriority();

        if(prty==Priority.LOW)
        {
            rb = (RadioButton) findViewById(R.id.editlowRBtn);
            rb.setChecked(true);
        }
        if(prty==Priority.NORMAL)
        {
            rb = (RadioButton) findViewById(R.id.editmedRBtn);
            rb.setChecked(true);
        }
        if(prty==Priority.URGENT)
        {
            rb = (RadioButton) findViewById(R.id.edithighRBtn);
            rb.setChecked(true);
        }

        spin = (Spinner) findViewById(R.id.categorySpinner);
        Category tsk_ctgry = tastToEdit.getTask_catg();

        if(tsk_ctgry==Category.GENERAL)
        {
            spin.setSelection(0);
        }
        else
        {
            if(tsk_ctgry==Category.CLEANING)
            {
                spin.setSelection(1);
            }
            else
            {
                if(tsk_ctgry==Category.ELECTRICITY)
                {
                    spin.setSelection(2);
                }
                else
                {
                    if(tsk_ctgry==Category.COMPUTERS)
                    {
                        spin.setSelection(3);
                    }
                    else
                    {
                        if(tsk_ctgry==Category.OTHER)
                        {
                            spin.setSelection(4);
                        }
                    }
                }
            }
        }

        loc_spin = (Spinner) findViewById(R.id.locationSpinner);
        Locations selected_loc = tastToEdit.getTsk_location();

        if(selected_loc==Locations.Meeting_Room)
        {
            spin.setSelection(0);
        }
        else
        {
            if(selected_loc==Locations.Office_245)
            {
                spin.setSelection(1);
            }
            else
            {
                if(selected_loc==Locations.Lobby)
                {
                    spin.setSelection(2);
                }
                else
                {
                    if(selected_loc==Locations.NOC)
                    {
                        spin.setSelection(3);
                    }
                    else
                    {
                        if(selected_loc==Locations.VPsoffice)
                        {
                            spin.setSelection(4);
                        }
                    }
                }
            }
        }

        EditText date = (EditText)findViewById(R.id.editTaskDate);
        date.setInputType(InputType.TYPE_NULL);
        date = (EditText)findViewById(R.id.editTaskDate);

        EditText time = (EditText)findViewById(R.id.editTaskTime);
        time.setInputType(InputType.TYPE_NULL);
        time = (EditText)findViewById(R.id.editTaskTime);
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

        if(tastToEdit.getHasDate())
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdft = new SimpleDateFormat("HH:mm");
            date.setText(sdf.format(tastToEdit.getDueDate()));
            time.setText(sdft.format(tastToEdit.getDueDate()));
        }
    }

    public void saveChangesTaskBtn(View view)
    {
        EditText desc = (EditText)findViewById(R.id.editTaskDesc);
        EditText date = (EditText)findViewById(R.id.editTaskDate);
        EditText time = (EditText)findViewById(R.id.editTaskTime);
        TextView desc_title = (TextView) findViewById(R.id.tskdescedit);

        Date myDate = null;
        boolean state=true;
        String time_Date_str = null;
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
            desc_title.setTextColor(Color.RED);
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
            desc_title.setTextColor(Color.RED);
            state = false;
        }

        if (state)
        {
            tastToEdit.setDescription(desc.getText().toString());

            rb = (RadioButton) findViewById(R.id.editlowRBtn);
            if(rb.isChecked())
                tastToEdit.setPriority(Priority.LOW);
            else {
                rb = (RadioButton) findViewById(R.id.editmedRBtn);
                if(rb.isChecked())
                    tastToEdit.setPriority(Priority.NORMAL);

                else
                {
                    rb = (RadioButton) findViewById(R.id.edithighRBtn);
                    if(rb.isChecked())
                        tastToEdit.setPriority(Priority.URGENT);
                    else
                        tastToEdit.setPriority(Priority.NORMAL);
                }
            }

            time_Date_str = date.getText()+" "+time.getText();
            try
            {
                myDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(time_Date_str);
                tastToEdit.setDueDate(myDate);
                tastToEdit.setHasDate(true);
            }catch(Exception e){myDate=null;}

            int position = spin.getSelectedItemPosition();
            switch(position)
            {
                case 0:
                    tastToEdit.setTask_catg(Category.GENERAL);
                    break;
                case 1:
                    tastToEdit.setTask_catg(Category.CLEANING);
                    break;
                case 2:
                    tastToEdit.setTask_catg(Category.ELECTRICITY);
                    break;
                case 3:
                    tastToEdit.setTask_catg(Category.COMPUTERS);
                    break;
                case 4:
                    tastToEdit.setTask_catg(Category.OTHER);
                    break;
            }

            Intent returnIntent = new Intent();

            // DBManager.getInstance(this).addTask(t);
            //  DBManager dbm = new DBManager(this);

            returnIntent.putExtra("task",tastToEdit);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }

    public void discardchangesBtnClick(View view)
    {
        finish();
    }

    public void deleteTaskBtn(View view)
    {
        Intent returnIntent = new Intent(this,MainActivity.class);
        tastToEdit.setToDelete(true);
        returnIntent.putExtra("task",tastToEdit);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void onRadioButtonClicked(View view)
    {
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new EditDatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new EditTimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

}