package il.ac.shenkar.david.todolistex2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class ReportTaskStatus extends AppCompatActivity
{

    private Task tastToEdit;
    private RadioButton statusrb;
    private TextView label;
    private List<ParseObject> tsks=null;
    private ParseFile imgFile;

    private int CAMERA_REQUEST = 100;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_task_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        Camerapermission();

        tastToEdit = (Task)i.getSerializableExtra("task");
        label = (TextView)findViewById(R.id.categorylabel);
        label.append(" "+tastToEdit.getTask_catg().toString());

        label = (TextView)findViewById(R.id.prioritylabel);
        label.append(" "+tastToEdit.getPriority().toString());

        label = (TextView)findViewById(R.id.locationlabel);
        label.append(" " + Location.fromInteger(tastToEdit.getTsk_location()).toString());

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
        }
        //Get a Tracker (should auto-report)
        ((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.statusgroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(R.id.donestatusRBtn);
                if (rb.isChecked()) {
                    openCamera();
                }
            }
        });
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

        if(tastToEdit.getTask_sts()==Task_Status.DONE)
        {
            // Convert photo - bitmap to byte
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Compress image to lower quality scale 1 - 100
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] image = stream.toByteArray();

            imgFile = new ParseFile(tastToEdit.getParse_task_id()+".png", image);
        }

        Toast.makeText(this, "Changes Were Saved", Toast.LENGTH_SHORT).show();

        //update the task in Parse
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Task");
        query.whereEqualTo("objectId", tastToEdit.getParse_task_id());

        try {
            tsks = query.find();
            for (ParseObject tmp : tsks)
            {
                tmp.put("Status",tastToEdit.getTask_sts().ordinal());
                if(tastToEdit.getTask_sts()==Task_Status.DONE)
                {
                    tmp.put("Photo",imgFile);
                }

                tmp.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // if null, it means the save has succeeded
                        } else {
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

    private void Camerapermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(ReportTaskStatus.this
                ,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ReportTaskStatus.this,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(ReportTaskStatus.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    private void openCamera() {
        try {
            Intent cameraIntent = new Intent(
            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch ( requestCode ) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                for( int i = 0; i < permissions.length; i++ ) {
                    if( grantResults[i] == PackageManager.PERMISSION_GRANTED ) {
                        Log.d( "Permissions", "Permission Granted: " + permissions[i] );
                    } else if( grantResults[i] == PackageManager.PERMISSION_DENIED ) {
                        Log.d( "Permissions", "Permission Denied: " + permissions[i] );
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    public String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }
}
