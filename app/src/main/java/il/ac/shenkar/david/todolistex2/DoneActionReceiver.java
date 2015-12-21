package il.ac.shenkar.david.todolistex2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
*/
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by David on 21-Dec-15.
 */
public class DoneActionReceiver extends BroadcastReceiver
{

    //GoogleApiClient mApiClient=null;

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(0);
        Task myTask =  (Task)intent.getSerializableExtra("task");

        DBManager db = new DBManager(context);
        myTask.setCompleted(true);
       // db.updateTask(myTask);
    }
}
