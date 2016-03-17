package il.ac.shenkar.david.todolistex2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by David on 17-Mar-16.
 */
public class EmployeeItemAdapter extends BaseAdapter
{
    List<String> empNameList;
    private Context context;
    private LayoutInflater inflater;

    public EmployeeItemAdapter(Context context, List<String> emp_name_list) {
        this.empNameList = emp_name_list;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return empNameList.size();
    }

    @Override
    public Object getItem(int position) {
        if (this.empNameList != null && empNameList.size() > position)
            return this.empNameList.get(position);
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

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.content_employee_name, null);
            viewHold = new ViewHolder();
            convertView.setTag(viewHold);

            viewHold.emp_name = (TextView) convertView.findViewById(R.id.empname_label);
        }

        else {
            viewHold = (ViewHolder) convertView.getTag();}

        viewHold.emp_name = (TextView) convertView.findViewById(R.id.empname_label);
        viewHold.emp_name.setText(empNameList.get(position).toString());

        return convertView;
    }

    public static class ViewHolder
    {
        TextView emp_name;
    }
}
