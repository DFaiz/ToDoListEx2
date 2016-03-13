package il.ac.shenkar.david.todolistex2;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NumberPicker.OnValueChangeListener {

    ListView list;
    private List<Task> itemList;
    Context context = MainActivity.this;
    TaskItemAdapter adapter;
    private DBManager dbM;

    private TextView emptylist_txt;
    private TextView minutes_text;
    private TextView total_tasks_text;
    private Task tmp_task;
    private Timer refresh_timer;
    private TimerTask timer_task;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    public final int REQUEST_CODE_NEW_TASK = 1;
    public final int REQUEST_CODE_UPDATE_TASK = 2;
    public final int REQUEST_CODE_REMOVE_TASK = 3;
    public final int REQUEST_CODE_INVITE_MEMBER = 4;
    public final int REQUEST_CODE_EMP_VIEW_TASK = 5;

    private static Dialog minute_diag;

    private List<ParseObject> tsks = null;
    private Spinner sort_selector = null;
    private ArrayAdapter<String> sortSpinnerAdapter;
    private String[] sorts;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbM = DBManager.getInstance(context);
        total_tasks_text = (TextView) findViewById(R.id.totalTask);

        locationpermission();

        if(Globals.diffusr)
        {
            dbM.clearDB();
        }

        //check if any tasks exist in Parse DB
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Task");
        query.whereEqualTo("TeamName", Globals.team_name);

        if (Globals.IsManager == false) {
            SharedPreferences sharedpreferences = getSharedPreferences("il.ac.shenkar.david.todolistex2", Context.MODE_PRIVATE);
            query.whereEqualTo("Employee", sharedpreferences.getString("LoginUsr", null));

            //if not manager disable action button
            FloatingActionButton fbtn = (FloatingActionButton) findViewById(R.id.fab);
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fbtn.getLayoutParams();
            p.setBehavior(null); //should disable default animations
            p.setAnchorId(View.NO_ID); //should let you set visibility
            fbtn.setLayoutParams(p);
            fbtn.setVisibility(View.GONE);
        }
        itemList = new ArrayList<Task>();
        list = (ListView) findViewById(R.id.listView);

        try {
            tsks = query.find();
            for (ParseObject tmp : tsks) {
                tmp_task = new Task();
                tmp_task.setDescription(tmp.getString("Description"));

                int position = tmp.getInt("Category");
                switch (position) {
                    case 0:
                        tmp_task.setTask_catg(Category.GENERAL);
                        break;
                    case 1:
                        tmp_task.setTask_catg(Category.CLEANING);
                        break;
                    case 2:
                        tmp_task.setTask_catg(Category.ELECTRICITY);
                        break;
                    case 3:
                        tmp_task.setTask_catg(Category.COMPUTERS);
                        break;
                    case 4:
                        tmp_task.setTask_catg(Category.OTHER);
                        break;
                }

                position = tmp.getInt("Priority");
                switch (position) {
                    case 0:
                        tmp_task.setPriority(Priority.LOW);
                        break;
                    case 1:
                        tmp_task.setPriority(Priority.NORMAL);
                        break;
                    case 2:
                        tmp_task.setPriority(Priority.URGENT);
                        break;
                    default:
                        tmp_task.setPriority(Priority.NORMAL);
                        break;
                }

                position = tmp.getInt("Status");
                switch (position) {
                    case 0:
                        tmp_task.setTask_sts(Task_Status.WAITING);
                        break;
                    case 1:
                        tmp_task.setTask_sts(Task_Status.INPROGESS);
                        break;
                    case 2:
                        tmp_task.setTask_sts(Task_Status.DONE);
                        break;
                    default:
                        tmp_task.setTask_sts(Task_Status.WAITING);
                        break;
                }

                position = tmp.getInt("Location");
                switch (position) {
                    case 0:
                        tmp_task.setTsk_location(position);
                        break;
                    case 1:
                        tmp_task.setTsk_location(position);
                        break;
                    case 2:
                        tmp_task.setTsk_location(position);
                        break;
                    case 3:
                        tmp_task.setTsk_location(position);
                        break;
                    case 4:
                        tmp_task.setTsk_location(position);
                        break;
                    default:
                        tmp_task.setTsk_location(position);
                        break;
                }

                tmp_task.setDueDate(tmp.getDate("DueDate"));
                tmp_task.setParse_task_id(tmp.getObjectId());
                tmp_task.setEmp_name(tmp.getString("Employee"));
                syncTaskList(tmp_task);
            }
        } catch (ParseException e) {
        }

        itemList = dbM.getAllTasks();
        list.setAdapter(new TaskItemAdapter(context, itemList));

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long arg3) {

                //get item instance from list
                Task tt = (Task) ((TaskItemAdapter) parent.getAdapter()).getItem(position);

                if (Globals.IsManager == true) {
                    Globals.temp=tt.getTsk_location();
                    Log.w("loc from global",""+Globals.temp);
                    //start the create activity again, now for editing
                    Intent i = new Intent(getApplicationContext(), EditTaskActivity.class);
                    i.putExtra("task", tt);
                    startActivityForResult(i, REQUEST_CODE_UPDATE_TASK);
                }
                if (Globals.IsManager == false) {
                    //start the create activity again, now for editing
                    Intent i = new Intent(getApplicationContext(), ReportTaskStatus.class);
                    i.putExtra("task", tt);
                    startActivityForResult(i, REQUEST_CODE_EMP_VIEW_TASK);
                }
                return false;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(context, "Long press to edit task", Toast.LENGTH_SHORT).show();
            }
        });

        emptylist_txt = (TextView) findViewById(R.id.emptylist);

        if (itemList.size() == 0) {
            emptylist_txt.setVisibility(View.VISIBLE);
            total_tasks_text.setVisibility(View.GONE);
        } else {
            emptylist_txt.setVisibility(View.GONE);
            total_tasks_text.setVisibility(View.VISIBLE);
            total_tasks_text.setText("");
            total_tasks_text.setText("Total " + itemList.size());
        }
        sorts = getResources().getStringArray(R.array.sort_array);
        sort_selector = (Spinner) findViewById(R.id.sortSpinner);
        sortSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sorts);
        sortSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort_selector.setAdapter(sortSpinnerAdapter);

        sort_selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(
                    AdapterView<?> parent, View view, int position, long id) {
                Globals.last_sort=position;
                SortTaskList(position);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(context, "Spinner1:no selection", Toast.LENGTH_SHORT).show();
            }
        });

       /* refresh_timer = new Timer ();
        TimerTask timerTask = new TimerTask () {
            @Override
            public void run () {
                checkForUpdate();
                Log.w("checkForUpdate","checkForUpdate");
            }
        };
        refresh_timer.schedule (timerTask, 0l,Globals.refresh_minutes*60000);   // 1000*10*60 every 10 minut*/
        startingUp();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        //Get a Tracker (should auto-report)
        ((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);
    }

    private void startingUp() {
        Thread timer = new Thread() { //new thread
            public void run() {
                Boolean b = true;
                try {
                    do {
                        sleep(Globals.refresh_minutes*60000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                Log.w("checkForUpdate","checkForUpdate");
                            }
                        });


                    }
                    while (b == true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                }
            };
        };
        timer.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (Globals.IsManager == false) {
            menu.getItem(0).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            onAction_Settings();
        }

        if (id == R.id.action_manageteam) {
            Intent returnIntent = new Intent(this, InviteMember.class);
            returnIntent.putExtra("from", "from_main_activity");
            startActivityForResult(returnIntent, REQUEST_CODE_INVITE_MEMBER);
        }

        if (id == R.id.action_refresh) {
            checkForUpdate();
        }

        if (id == R.id.action_Logout) {
            Globals.isActivityRestarting = true;
            Intent returnIntent = new Intent(this, Login_activity.class);
            setResult(RESULT_OK, returnIntent);
            startActivity(returnIntent);
        }

        if (id == R.id.action_About) {
            int versionCode = BuildConfig.VERSION_CODE;
            String versionName = BuildConfig.VERSION_NAME;

            new AlertDialog.Builder(this)
                    .setTitle("About")
                    .setMessage("Application version " + versionCode + "\nVersion Name " + versionName + "\n\n" + "Created by David Faizulaev")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void newTaskButtonClick(View view) {
        Intent intent = new Intent(this, ListNodeActivity.class);
        startActivityForResult(intent, REQUEST_CODE_NEW_TASK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Task returned_task;

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_NEW_TASK: {
                    Toast.makeText(context, "New Task Added", Toast.LENGTH_SHORT).show();
                    returned_task = (Task) data.getSerializableExtra("task");
                    itemList.add(returned_task);
                    emptylist_txt = (TextView) findViewById(R.id.emptylist);
                    emptylist_txt.setVisibility(View.GONE);
                    total_tasks_text.setVisibility(View.VISIBLE);
                    total_tasks_text.setText("");
                    total_tasks_text.setText("Total " + itemList.size());
                    adapter = new TaskItemAdapter(context, itemList);
                    list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                }

                case REQUEST_CODE_UPDATE_TASK: {
                    returned_task = (Task) data.getSerializableExtra("task");
                    if (returned_task.getToDelete()) {
                        for (int i = 0; i < itemList.size(); i++) {
                            if (itemList.get(i).getTaskId() == returned_task.getTaskId()) {
                                dbM.deleteTask(returned_task);
                                itemList.remove(i);
                                emptylist_txt = (TextView) findViewById(R.id.emptylist);

                                if (itemList.size() == 0) {
                                    emptylist_txt.setVisibility(View.VISIBLE);
                                    total_tasks_text.setVisibility(View.GONE);
                                }

                                adapter = new TaskItemAdapter(context, itemList);
                                list.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        for (int i = 0; i < itemList.size(); i++) {
                            if (itemList.get(i).getTaskId() == returned_task.getTaskId()) {
                                    itemList.set(i, returned_task);
                                    adapter = new TaskItemAdapter(context, itemList);
                                    list.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                            }
                        }
                    }
                    break;
                }

                case REQUEST_CODE_INVITE_MEMBER: {
                    adapter = new TaskItemAdapter(context, itemList);
                    list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                }

                case REQUEST_CODE_EMP_VIEW_TASK: {
                    returned_task = (Task) data.getSerializableExtra("task");
                    for (int i = 0; i < itemList.size(); i++) {
                        if (itemList.get(i).getTaskId() == returned_task.getTaskId())
                        {
                            itemList.set(i, returned_task);
                            adapter = new TaskItemAdapter(context, itemList);
                            list.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    break;
                }

                default: {
                    adapter = new TaskItemAdapter(context, itemList);
                    list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        ;
    }

    public void onAction_Settings() {
        final Dialog d = new Dialog(MainActivity.this);
        d.setTitle("Select Number of Minutes for New Tasks Refresh");
        d.setContentView(R.layout.selectminutes);
        Button b1 = (Button) d.findViewById(R.id.setnumberofminbtn);
        Button b2 = (Button) d.findViewById(R.id.cancelminbtn);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
        np.setMaxValue(10); // max value 10
        np.setMinValue(1);   // min value 0
        np.setValue(Globals.refresh_minutes);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.refresh_minutes = np.getValue();
                SharedPreferences sharedpreferences = getSharedPreferences("il.ac.shenkar.david.todolistex2", Context.MODE_PRIVATE);
                sharedpreferences.edit().putInt("RefreshInterval", Globals.refresh_minutes).apply();
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        //connect to SQLite
        dbM = DBManager.getInstance(context);
        if(Globals.isActivityRestarting){
            dbM.clearDB();
            checkForUpdate();
        }
        else {
            SortTaskList(Globals.last_sort);
        }
    }

    private void syncTaskList(Task tmp_task) {
        boolean result = false;
        dbM = DBManager.getInstance(context);
        result = dbM.ifTaskExists(tmp_task.getParse_task_id());

        if (result == true) {
            dbM.updateTask(tmp_task);
        } else {
            long seq_tsk_id = dbM.addTask(tmp_task);
            tmp_task.setTaskId(seq_tsk_id);
            dbM.updateParseID(tmp_task);
            final Task tsktmp = tmp_task;

            if (Globals.IsManager == false) {
                new AlertDialog.Builder(this)
                        .setTitle("New Task")
                        .setMessage("You've Received a New Task")
                        .setPositiveButton("View Task", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //start the create activity again, now for editing
                                Intent i = new Intent(getApplicationContext(), ReportTaskStatus.class);
                                i.putExtra("task", tsktmp);
                                startActivityForResult(i, REQUEST_CODE_EMP_VIEW_TASK);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }

    private void checkForUpdate() {
        //check if any tasks exist in Parse DB
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Task");
        query.whereEqualTo("TeamName", Globals.team_name);
        if (Globals.IsManager == false) {
            SharedPreferences sharedpreferences = getSharedPreferences("il.ac.shenkar.david.todolistex2", Context.MODE_PRIVATE);
            query.whereEqualTo("Employee", sharedpreferences.getString("LoginUsr", null));
        }

        itemList = new ArrayList<Task>();
        list = (ListView) findViewById(R.id.listView);

        try {
            tsks = query.find();
            for (ParseObject tmp : tsks) {
                tmp_task = new Task();
                tmp_task.setDescription(tmp.getString("Description"));

                int position = tmp.getInt("Category");
                switch (position) {
                    case 0:
                        tmp_task.setTask_catg(Category.GENERAL);
                        break;
                    case 1:
                        tmp_task.setTask_catg(Category.CLEANING);
                        break;
                    case 2:
                        tmp_task.setTask_catg(Category.ELECTRICITY);
                        break;
                    case 3:
                        tmp_task.setTask_catg(Category.COMPUTERS);
                        break;
                    case 4:
                        tmp_task.setTask_catg(Category.OTHER);
                        break;
                }

                position = tmp.getInt("Priority");
                switch (position) {
                    case 0:
                        tmp_task.setPriority(Priority.LOW);
                        break;
                    case 1:
                        tmp_task.setPriority(Priority.NORMAL);
                        break;
                    case 2:
                        tmp_task.setPriority(Priority.URGENT);
                        break;
                    default:
                        tmp_task.setPriority(Priority.NORMAL);
                        break;
                }

                position = tmp.getInt("Status");
                switch (position) {
                    case 0:
                        tmp_task.setTask_sts(Task_Status.WAITING);
                        break;
                    case 1:
                        tmp_task.setTask_sts(Task_Status.INPROGESS);
                        break;
                    case 2:
                        tmp_task.setTask_sts(Task_Status.DONE);
                        break;
                    default:
                        tmp_task.setTask_sts(Task_Status.WAITING);
                        break;
                }

                position = tmp.getInt("Location");
                switch (position) {
                    case 0:
                        tmp_task.setTsk_location(position);
                        break;
                    case 1:
                        tmp_task.setTsk_location(position);
                        break;
                    case 2:
                        tmp_task.setTsk_location(position);
                        break;
                    case 3:
                        tmp_task.setTsk_location(position);
                        break;
                    case 4:
                        tmp_task.setTsk_location(position);
                        break;
                    default:
                        tmp_task.setTsk_location(position);
                        break;
                }

                tmp_task.setDueDate(tmp.getDate("DueDate"));
                tmp_task.setParse_task_id(tmp.getObjectId());
                tmp_task.setEmp_name(tmp.getString("Employee"));
                syncTaskList(tmp_task);
            }
        } catch (ParseException e) {
        }

        itemList = dbM.getAllTasks();
        adapter = new TaskItemAdapter(context, itemList);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (itemList.size() == 0) {
            emptylist_txt.setVisibility(View.VISIBLE);
            total_tasks_text.setVisibility(View.GONE);
        } else {
            emptylist_txt.setVisibility(View.GONE);
            total_tasks_text.setVisibility(View.VISIBLE);
            total_tasks_text.setText("");
            total_tasks_text.setText("Total " + itemList.size());
        }
    }

    private void SortTaskList(final int sorter_position) {

        itemList = new ArrayList<Task>();
        list = (ListView) findViewById(R.id.listView);

        if(sorter_position==Sorting.TIME.ordinal())
        {
            itemList = dbM.getAllTasks();
        }
        else
        {
            itemList = dbM.getSortedTasks(Sorting.fromInteger(sorter_position));
        }

        adapter = new TaskItemAdapter(context, itemList);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        total_tasks_text = (TextView) findViewById(R.id.totalTask);
        if (itemList.size() == 0) {
            emptylist_txt.setVisibility(View.VISIBLE);
            total_tasks_text.setVisibility(View.GONE);
        } else {
            emptylist_txt.setVisibility(View.GONE);
            total_tasks_text.setVisibility(View.VISIBLE);
            total_tasks_text.setText("Total " + itemList.size());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://il.ac.shenkar.david.todolistex2/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://il.ac.shenkar.david.todolistex2/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private void locationpermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults)     {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}