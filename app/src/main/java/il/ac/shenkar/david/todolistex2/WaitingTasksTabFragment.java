package il.ac.shenkar.david.todolistex2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 02-Mar-16.
 */
public class WaitingTasksTabFragment extends Fragment
{
    View v;
    ListView all_list;
    private List<Task> itemListWaitTasks;
    private TextView emptylist_txt;
    private TextView total_tasks_text;
    private Spinner sort_selector = null;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    public final int REQUEST_CODE_NEW_TASK = 1;
    public final int REQUEST_CODE_UPDATE_TASK = 2;
    public final int REQUEST_CODE_REMOVE_TASK = 3;
    public final int REQUEST_CODE_INVITE_MEMBER = 4;
    public final int REQUEST_CODE_EMP_VIEW_TASK = 5;

    private DBManager dbM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.all_tasks, container, false);
        Bundle bundle = getArguments();
        int pager_position = bundle.getInt("position");

        itemListWaitTasks = new ArrayList<Task>();
        Log.w("WaitingTasksTabFragment",""+pager_position+1);
        itemListWaitTasks = TaskHashList.getTaskList(pager_position+1);

        all_list = (ListView) v.findViewById(R.id.waitingtasks_listView);
        all_list.setAdapter(new TaskItemAdapter(getActivity(), itemListWaitTasks));
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

        emptylist_txt = (TextView) v.findViewById(R.id.wait_tabemptylist);

        total_tasks_text = (TextView) v.findViewById(R.id.waitt_tab_totalTask);

        if (itemListWaitTasks.size() == 0) {
            emptylist_txt.setVisibility(View.VISIBLE);
            total_tasks_text.setVisibility(View.GONE);
        } else {
            emptylist_txt.setVisibility(View.GONE);
            total_tasks_text.setVisibility(View.VISIBLE);
            total_tasks_text.setText("");
            total_tasks_text.setText("Total " + itemListWaitTasks.size());
        }
        sort_selector = (Spinner) v.findViewById(R.id.all_tasks_sortSpinner);
        sort_selector.setVisibility(View.GONE);
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
                    itemListWaitTasks.add(returned_task);
                    TaskHashList.resetTaskList(1,itemListWaitTasks);
                    all_list.setAdapter(new TaskItemAdapter(getActivity(), itemListWaitTasks));
                    all_list.deferNotifyDataSetChanged();
                    break;
                }

                case REQUEST_CODE_UPDATE_TASK: {
                    returned_task = (Task) data.getSerializableExtra("task");
                    if (returned_task.getToDelete()) {
                        for (int i = 0; i < itemListWaitTasks.size(); i++) {
                            if (itemListWaitTasks.get(i).getTaskId() == returned_task.getTaskId()) {
                                dbM.deleteTask(returned_task);
                                itemListWaitTasks.remove(i);
                                all_list.setAdapter(new TaskItemAdapter(getActivity(), itemListWaitTasks));
                                all_list.deferNotifyDataSetChanged();
                            }
                        }
                    } else {
                        for (int i = 0; i < itemListWaitTasks.size(); i++) {
                            if (itemListWaitTasks.get(i).getTaskId() == returned_task.getTaskId()) {
                                itemListWaitTasks.set(i, returned_task);
                                all_list.setAdapter(new TaskItemAdapter(getActivity(), itemListWaitTasks));
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
                    for (int i = 0; i < itemListWaitTasks.size(); i++) {
                        if (itemListWaitTasks.get(i).getTaskId() == returned_task.getTaskId())
                        {
                            itemListWaitTasks.set(i, returned_task);
                            all_list.setAdapter(new TaskItemAdapter(getActivity(), itemListWaitTasks));
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
}
