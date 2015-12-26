package il.ac.shenkar.david.todolistex2;

import android.content.Context;
import android.content.Intent;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.graphics.BitmapFactory;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by David on 21-Dec-15.
 */

public class ReminderNotification extends BroadcastReceiver
{
    public ReminderNotification()
    {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        //User support library
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context).setSmallIcon(R.drawable.icon_star
                        )
                        .setAutoCancel(true)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.icon_star))
                        .setContentTitle("this is a tests2")
                        .setContentText("this is a test");
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        /*
        // The PendingIntent to launch our activity if the user selects this notification
        Task task = (Task)intent.getSerializableExtra("task");
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
        // Set the info for the views that show in the notification panel.

        Intent snzInt = new Intent(context, SnoozeReminderReceiver.class);
        snzInt.putExtra("task", task);
        PendingIntent snoozeIntent = PendingIntent.getBroadcast(context, 0,snzInt, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent doneInt = new Intent(context,DoneActionReceiver.class);
        doneInt.putExtra("task", task);
        PendingIntent doneIntent = PendingIntent.getBroadcast(context, 0,doneInt, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Build notification
        Notification noti = new Notification.Builder(context)
                .setContentTitle("Time Reminder").setContentText(task.getDescription())
                .setSmallIcon(R.drawable.tasker_launcher).setContentIntent(contentIntent)
                .addAction(android.R.drawable.ic_lock_idle_alarm, "Snooze 1 hour", snoozeIntent)
                .addAction(R.drawable.ic_action_done, "Mark as done", doneIntent).setAutoCancel(true)
                .build();
        noti.defaults |= Notification.DEFAULT_SOUND;
        noti.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, noti);*/

        //Toast.makeText(context, "Action: " + intent.getAction(), Toast.LENGTH_LONG).show();
  //      Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    //    vibrator.vibrate(2000);

    }
}
