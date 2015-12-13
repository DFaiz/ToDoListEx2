package il.ac.shenkar.david.todolistex2;

import java.util.List;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by David on 05-Dec-15.
 */
public class TaskItemAdapter extends BaseAdapter
{
    List<Task> itemList;
    private Context context;
    private LayoutInflater inflater;
    private View.OnClickListener myClickListener;
    private DBManager dbM;

    public TaskItemAdapter(Context context, List<Task> list) {
        this.itemList = list;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        if (this.itemList != null && itemList.size() > position)
            return this.itemList.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final ViewHolder viewHold;
        Task item = (Task)getItem(position);

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.content_list_item, null);
            viewHold = new ViewHolder();
            convertView.setTag(viewHold);

            viewHold.tv = (TextView) convertView.findViewById(R.id.taskDesc);
            viewHold.cb = (CheckBox) convertView.findViewById(R.id.doneCheckBox);

            viewHold.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                {
                   @Override
                   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                       Task item = (Task) viewHold.cb.getTag();
                       item.setCompleted(viewHold.cb.isChecked());
                   }
                }
            );
        }

    else {
            viewHold = (ViewHolder) convertView.getTag();}

        viewHold.tv = (TextView)convertView.findViewById(R.id.taskDesc);
        viewHold.tv.setText(itemList.get(position).getDescription());
        viewHold.cb = (CheckBox)convertView.findViewById(R.id.doneCheckBox);
        viewHold.cb.setChecked(itemList.get(position).getCompleted());
        viewHold.cb.setTag(item);

        return convertView;
    }


    public static class ViewHolder
    {
        TextView tv;
        CheckBox cb;
    }
}
