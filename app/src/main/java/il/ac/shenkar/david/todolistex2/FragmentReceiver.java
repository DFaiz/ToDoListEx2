package il.ac.shenkar.david.todolistex2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by David on 14-Mar-16.
 */
public class FragmentReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action != null && action.equals("AllTasksFragmentUpdate"))
        {
           /* AllTasksTabFragment fragment = (AllTasksTabFragment)getSupportFragmentManager().findFragmentByTag(<fragment_tag_name>);
            if (fragment != null && fragment.isAdded()) {
                fragment.method(); //Call any public method
            }*/
        }
    }
}
