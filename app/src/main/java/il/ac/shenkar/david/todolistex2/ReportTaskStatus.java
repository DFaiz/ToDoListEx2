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

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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
        label.append(" " + tastToEdit.getTsk_location().toString());

        label = (TextView)findViewById(R.id.duetimelabel);
        label.append(" " + tastToEdit.getDueDate().toString());

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
        }
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

        Toast.makeText(this, "Changes Were Saved", Toast.LENGTH_SHORT).show();
        Intent returnIntent = new Intent();

        DBManager dbm = DBManager.getInstance(this);
        dbm.updateTask(tastToEdit);

        //update the task in Parse
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Task");
        query.whereContains("objectID", tastToEdit.getParse_task_id());

        try {
            tsks = query.find();
            if(tsks.size()>1)
                Log.w("tsks","multiple results returned");
            if(tsks.size()==0)
                Log.w("tsks","no results");
            if(tsks.size()==1){
                for (ParseObject tmp : tsks)
                {
                    tmp.put("Status",tastToEdit.getTask_sts().ordinal());
                    tmp.saveInBackground();
                }
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
}
