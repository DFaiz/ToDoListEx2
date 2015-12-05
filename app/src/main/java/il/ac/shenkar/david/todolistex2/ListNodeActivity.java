package il.ac.shenkar.david.todolistex2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class ListNodeActivity extends AppCompatActivity {

    Task t = new Task();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_node);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addTaskBtn (View view)
    {
        EditText desc = (EditText)findViewById(R.id.newTaskDesc);
        EditText nts = (EditText)findViewById(R.id.newTaskNotes);

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
/*
        if(desc.getText().toString().length()==100)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Fill Description")
                    .setMessage("Task description length exceeded.\nDescription can contain 100 characters max.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            return;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }
*/
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
                    }
        }

            Intent returnIntent = new Intent();

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
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void onRadioButtonClicked(View view)
    {
    }
}
