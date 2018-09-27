package zhaw.ch.laundryschedule.timer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import zhaw.ch.laundryschedule.LSMainActivity;
import zhaw.ch.laundryschedule.R;

public class TimerExpiredReceiver extends BroadcastReceiver {
    private static final String TAG = "TimerExpiredReceiver";
    private static final String CHANNEL_ID_DEFAULT = "default";


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, LSMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, i, 0);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID_DEFAULT,
                "Default",
                NotificationManager.IMPORTANCE_HIGH);

        NotificationCompat.Builder b = new NotificationCompat.Builder(context, channel.getId());
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        b.setSound(notification)
                .setContentTitle(context.getString(R.string.timer_finished))
                .setAutoCancel(true)
                .setContentText(context.getString(R.string.timer_finished_text))
                .setSmallIcon(android.R.drawable.ic_notification_clear_all)
                .setContentIntent(pIntent);

        Notification n = b.build();
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager != null) {
            mNotificationManager.notify(0, n);
        }
    }
}
