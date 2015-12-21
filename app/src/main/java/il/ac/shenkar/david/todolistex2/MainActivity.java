package il.ac.shenkar.david.todolistex2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.carrier.CarrierMessagingService;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ListView list;
    List<Task> itemList;
    Context context = MainActivity.this;
    TaskItemAdapter adapter;
    DBManager dbM;

    public final int REQUEST_CODE_NEW_TASK = 1;
    public final int REQUEST_CODE_UPDATE_TASK = 2;
    public final int REQUEST_CODE_REMOVE_TASK = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        itemList = new ArrayList<Task>();
        list  = (ListView)findViewById(R.id.listView);
        list.setAdapter(new TaskItemAdapter(context, itemList));

       /* list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long arg3) {

                Task item = (Task) ((TaskItemAdapter)parent.getAdapter()).getItem(position);
                EditTaskLongClick(view,item);
                return false;
            }
        });
*/
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long arg3) {

        //get item instance from list
        Task tt = (Task) ((TaskItemAdapter)parent.getAdapter()).getItem(position);

        //start the create activity again, now for editing
        Intent i = new Intent(getApplicationContext(),EditTaskActivity.class);
        i.putExtra("task", tt);
        startActivityForResult(i, REQUEST_CODE_UPDATE_TASK);

        return false;
    }});

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(context, "Long press to edit task", Toast.LENGTH_SHORT).show();
            }
        });

        dbM = DBManager.getInstance(context);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void newTaskButtonClick (View view)
    {
        Intent intent = new Intent(this,ListNodeActivity.class);
        startActivityForResult(intent, REQUEST_CODE_NEW_TASK);
    }

    public void EditTaskLongClick (View view,Task tt)
    {
        Intent intent = new Intent(this,EditTaskActivity.class);
        intent.putExtra("task", tt);
        //startActivityForResult(intent, REQUEST_CODE_UPDATE_TASK);
        startActivityForResult(intent, REQUEST_CODE_NEW_TASK);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case REQUEST_CODE_NEW_TASK:
                    Task t = (Task)data.getSerializableExtra("task");
                    itemList.add(t);
                    adapter =  new TaskItemAdapter(context, itemList);
                    list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                case REQUEST_CODE_UPDATE_TASK:
                    Task edited_t = (Task)data.getSerializableExtra("task");
                    itemList.add(edited_t);
                    adapter =  new TaskItemAdapter(context, itemList);
                    list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    adapter =  new TaskItemAdapter(context, itemList);
                    list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        //get the list
        // list = (ListView) findViewById(R.id.listView);
        //fill the list with tasks
        // list.setAdapter(new TaskItemAdapter(context, itemList));

      /*  list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long arg3) {

                Toast.makeText(context, "Long pressed to edit task", Toast.LENGTH_SHORT).show();

                return false;
            }
        }); */

       /* list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(context, "Long press to edit task", Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }
}