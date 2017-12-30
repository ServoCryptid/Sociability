package sociability.com;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by Larisa on 03.12.2017.
 */

public class NotificationService extends NotificationListenerService {
    private static final String TAG = NotificationService.class.getSimpleName();

    Context context;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");

        super.onCreate();
        context = getApplicationContext();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) { //TODO: ask for security notification access ?
        Log.i(TAG,"onNotificationPosted: " + sbn.toString());

        try {
            String pack = sbn.getPackageName();
            Bundle extras = sbn.getNotification().extras;
            String title = extras.getString("android.title");
            String text = extras.getCharSequence("android.text").toString();

            Log.i("Package", pack);
            Log.i("Title", title);
            Log.i("Text", text);

            Intent msgrcv = new Intent("Msg");
            msgrcv.putExtra("package", pack);
            msgrcv.putExtra("title", title);
            msgrcv.putExtra("text", text);

            LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);

        } catch (Exception e) {
            Log.e(TAG, "onNotificationPosted: ", e);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG,"Notification Removed");
    }
}
