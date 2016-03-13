package il.ac.shenkar.david.todolistex2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.List;

public class ReportTaskStatus extends AppCompatActivity
{

    private Task tastToEdit;
    private RadioButton acceptrb;
    private RadioButton statusrb;
    private TextView label;
    private List<ParseObject> tsks=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_task_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();

        tastToEdit = (Task)i.getSerializableExtra("task");
        label = (TextView)findViewById(R.id.categorylabel);
        label.append(" "+tastToEdit.getTask_catg().toString());

        label = (TextView)findViewById(R.id.prioritylabel);
        label.append(" "+tastToEdit.getPriority().toString());

        label = (TextView)findViewById(R.id.locationlabel);
        label.append(" " + Location.fromInteger(tastToEdit.getTsk_location()).toString());
        //label.append(" " + "Somewhere");

        label = (TextView)findViewById(R.id.duetimelabel);

        if(tastToEdit.getDueDate()!=null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdft = new SimpleDateFormat("HH:mm");
            label.append(" " + (sdf.format(tastToEdit.getDueDate())));
            label.append(" " + (sdft.format(tastToEdit.getDueDate())));
        }

        Task_Status tsk_stts = tastToEdit.getTask_sts();

        if(tsk_stts==Task_Status.WAITING)
        {
            statusrb = (RadioButton) findViewById(R.id.waitingstatusRBtn);
            statusrb.setChecked(true);
        }
        if(tsk_stts==Task_Status.INPROGESS)
        {
            statusrb = (RadioButton) findViewById(R.id.inprogstatusRBtn);
            statusrb.setChecked(true);
        }
        if(tsk_stts==Task_Status.DONE)
        {
            statusrb = (RadioButton) findViewById(R.id.donestatusRBtn);
            statusrb.setChecked(true);
           /* statusrb.setEnabled(false);
            statusrb = (RadioButton) findViewById(R.id.inprogstatusRBtn);
            statusrb.setEnabled(false);
            statusrb = (RadioButton) findViewById(R.id.waitingstatusRBtn);
            statusrb.setEnabled(false);*/
        }
        //Get a Tracker (should auto-report)
        ((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);
    }

    public void saveChangesTaskBtn(View view)
    {
        RadioButton rb;

        rb = (RadioButton) findViewById(R.id.waitingstatusRBtn);
        if(rb.isChecked())
            tastToEdit.setTask_sts(Task_Status.WAITING);
        else {
            rb = (RadioButton) findViewById(R.id.inprogstatusRBtn);
            if(rb.isChecked())
                tastToEdit.setTask_sts(Task_Status.INPROGESS);

            else
            {
                tastToEdit.setTask_sts(Task_Status.DONE);
            }
        }

        Intent returnIntent = new Intent();

        DBManager dbm = DBManager.getInstance(this);
        dbm.updateTask(tastToEdit);
        Toast.makeText(this, "Changes Were Saved", Toast.LENGTH_SHORT).show();

        //update the task in Parse
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Task");
        query.whereEqualTo("objectId", tastToEdit.getParse_task_id());

        try {
            tsks = query.find();
            for (ParseObject tmp : tsks)
            {
                tmp.put("Status",tastToEdit.getTask_sts().ordinal());
                tmp.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // if null, it means the save has succeeded

                        } else {
                            // the save call was not successful.
                        }
                    }
                });
                tmp.saveInBackground();
            }
        } catch (ParseException e) {}

        returnIntent.putExtra("task",tastToEdit);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void discardstatuschangesBtnClick(View view)
    {
        finish();
    }

    public void onRadioButtonClicked(View view)
    {
    }

    @Override
    public void onStart(){
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);

    }
}
