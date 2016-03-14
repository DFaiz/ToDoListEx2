package il.ac.shenkar.david.todolistex2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 02-Mar-16.
 */
public class AllTasksTabFragment extends Fragment
{
    View v;
    ListView all_list;
    private List<Task> itemListAllTasks;
    private TextView emptylist_txt;
    private TextView total_tasks_text;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    public final int REQUEST_CODE_NEW_TASK = 1;
    public final int REQUEST_CODE_UPDATE_TASK = 2;
    public final int REQUEST_CODE_REMOVE_TASK = 3;
    public final int REQUEST_CODE_INVITE_MEMBER = 4;
    public final int REQUEST_CODE_EMP_VIEW_TASK = 5;
    private DBManager dbM;

    private Spinner sort_selector = null;
    private ArrayAdapter<String> sortSpinnerAdapter;
    private String[] sorts;

    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActivity().registerReceiver(new FragmentReceiver(), new IntentFilter("AllTasksFragmentUpdate"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.all_tasks, container, false);
        Bundle bundle = getArguments();
        int pager_position = bundle.getInt("position");

        itemListAllTasks = new ArrayList<Task>();
        itemListAllTasks = TaskHashList.getTaskList(pager_position+1);

        all_list = (ListView) v.findViewById(R.id.alltasks_listView);
        all_list.setAdapter(new TaskItemAdapter(getActivity(), itemListAllTasks));
        all_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long arg3) {

                //get item instance from list
                Task tt = (Task) ((TaskItemAdapter) parent.getAdapter()).getItem(position);

                if (Globals.IsManager == true) {
                    //start the create activity again, now for editing
                    Intent i = new Intent(getActivity(), EditTaskActivity.class);
                    i.putExtra("task", tt);
                    startActivityForResult(i, REQUEST_CODE_UPDATE_TASK);
                }
                if (Globals.IsManager == false) {
                    //start the create activity again, now for editing
                    Intent i = new Intent(getActivity(), ReportTaskStatus.class);
                    i.putExtra("task", tt);
                    startActivityForResult(i, REQUEST_CODE_EMP_VIEW_TASK);
                }
                return false;
            }
        });

        all_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getActivity(), "Long press to edit task", Toast.LENGTH_SHORT).show();
            }
        });

        emptylist_txt = (TextView) v.findViewById(R.id.alltab_emptylist);

        total_tasks_text = (TextView) v.findViewById(R.id.allt_tab_totalTask);

        if (itemListAllTasks.size() == 0) {
            emptylist_txt.setVisibility(View.VISIBLE);
            total_tasks_text.setVisibility(View.GONE);
        } else {
            emptylist_txt.setVisibility(View.GONE);
            total_tasks_text.setVisibility(View.VISIBLE);
            total_tasks_text.setText("");
            total_tasks_text.setText("Total " + itemListAllTasks.size());
        }

        sorts = getResources().getStringArray(R.array.sort_array);
        sort_selector = (Spinner) v.findViewById(R.id.all_tasks_sortSpinner);
        sortSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sorts);
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
                Toast.makeText(getActivity(), "Error - No value selected", Toast.LENGTH_SHORT).show();
            }
        });

        dbM = DBManager.getInstance(getActivity());
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Task returned_task;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_NEW_TASK: {
                    Toast.makeText(getActivity(), "New Task Added", Toast.LENGTH_SHORT).show();
                    returned_task = (Task) data.getSerializableExtra("task");
                    itemListAllTasks.add(returned_task);
                    TaskHashList.resetTaskList(1,itemListAllTasks);
                    all_list.setAdapter(new TaskItemAdapter(getActivity(), itemListAllTasks));
                    all_list.deferNotifyDataSetChanged();
                    break;
                }

                case REQUEST_CODE_UPDATE_TASK: {
                    returned_task = (Task) data.getSerializableExtra("task");
                    if (returned_task.getToDelete()) {
                        for (int i = 0; i < itemListAllTasks.size(); i++) {
                            if (itemListAllTasks.get(i).getTaskId() == returned_task.getTaskId()) {
                                dbM.deleteTask(returned_task);
                                itemListAllTasks.remove(i);
                                all_list.setAdapter(new TaskItemAdapter(getActivity(), itemListAllTasks));
                                all_list.deferNotifyDataSetChanged();
                            }
                        }
                    } else {
                        for (int i = 0; i < itemListAllTasks.size(); i++) {
                            if (itemListAllTasks.get(i).getTaskId() == returned_task.getTaskId()) {
                                itemListAllTasks.set(i, returned_task);
                                all_list.setAdapter(new TaskItemAdapter(getActivity(), itemListAllTasks));
                                all_list.deferNotifyDataSetChanged();
                            }
                        }
                    }
                    break;
                }

                /*case REQUEST_CODE_INVITE_MEMBER: {
                   // adapter.notifyDataSetChanged();
                    break;
                }*/

                case REQUEST_CODE_EMP_VIEW_TASK:
                {
                    returned_task = (Task) data.getSerializableExtra("task");
                    for (int i = 0; i < itemListAllTasks.size(); i++) {
                        if (itemListAllTasks.get(i).getTaskId() == returned_task.getTaskId())
                        {
                            itemListAllTasks.set(i, returned_task);
                            all_list.setAdapter(new TaskItemAdapter(getActivity(), itemListAllTasks));
                            all_list.deferNotifyDataSetChanged();
                        }
                    }
                    break;
                }
                default: {
                    super.onActivityResult(requestCode,resultCode,data);
                    break;
                }
            }
        }
    }

    private void SortTaskList(final int sorter_position) {

        itemListAllTasks = new ArrayList<Task>();
        all_list = (ListView) v.findViewById(R.id.alltasks_listView);

        if(sorter_position==Sorting.TIME.ordinal())
        {
            itemListAllTasks = dbM.getAllTasks();
        }
        else
        {
            itemListAllTasks = dbM.getSortedTasks(Sorting.fromInteger(sorter_position));
        }

        all_list.setAdapter(new TaskItemAdapter(getActivity(), itemListAllTasks));
        all_list.deferNotifyDataSetChanged();
        total_tasks_text = (TextView) v.findViewById(R.id.allt_tab_totalTask);
        if (itemListAllTasks.size() == 0) {
            emptylist_txt.setVisibility(View.VISIBLE);
            total_tasks_text.setVisibility(View.GONE);
        } else {
            emptylist_txt.setVisibility(View.GONE);
            total_tasks_text.setVisibility(View.VISIBLE);
            total_tasks_text.setText("Total " + itemListAllTasks.size());
        }
    }

    public void updateList ()
    {

    }
}