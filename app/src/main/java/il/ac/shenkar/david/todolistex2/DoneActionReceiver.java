package il.ac.shenkar.david.todolistex2;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by David on 21-Dec-15.
 */
public class DoneActionReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(0);
        Task myTask =  (Task)intent.getSerializableExtra("task");

       // DBManager db = new DBManager(context);
     //   myTask.setCompleted(true);
       // db.updateTask(myTask);
    }
}
