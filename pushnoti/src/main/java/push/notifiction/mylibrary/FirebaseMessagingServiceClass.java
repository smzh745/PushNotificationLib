package push.notifiction.mylibrary;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.N)
public class FirebaseMessagingServiceClass extends FirebaseMessagingService {
    private final CharSequence name = "My Channel";// The user-visible name of the channel.

    //{"mode":"promote app","image":"no","packagename":"COM.GOOGL3","title":"saA","message":"zxZXZX"}
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        try {
            super.onMessageReceived(remoteMessage);

            String title = null;
            String message = null;
            String action = null;
            String packageName = "";
            String mode = "none";

            Log.d("MainActivty", Objects.requireNonNull(remoteMessage.getFrom()));
            if (remoteMessage.getData().size() > 0) {
                Log.d("MainActivity", "Data: " + remoteMessage.getData());
                Map<String, String> data = remoteMessage.getData();


                for (String key : data.keySet()) {
                    String value = data.get(key);
                    Log.d("MainActivity", "onMessageReceived: " + "Key: " + key + " Value: " + value);


                    assert value != null;
                    JSONObject jsonObject1 = new JSONObject(value);
                    title = jsonObject1.getString("title");
                    message = jsonObject1.getString("message");
                    action = jsonObject1.getString("action");

                    if (jsonObject1.has("mode")) {
                        mode = jsonObject1.getString("mode");
                    }

                    if (jsonObject1.has("packagename")) {
                        packageName = jsonObject1.getString("packagename");
                    }


                    Log.d("MainActivity", "onMessageReceived: " + title + "\n" + message);
//                        sendNotificationMsg(message);

                }


                try {
                    GetAllNotificationValues.writeTitle(this, title);
                    GetAllNotificationValues.writeMessage(this, message);
                    GetAllNotificationValues.writePackageName(this, packageName);
                    GetAllNotificationValues.writeModeName(this, mode);
                } catch (Exception e) {
                    title = "";
                    e.printStackTrace();
                }

                if (mode.equalsIgnoreCase(ExtraUtils.UPDATE_APP)) {
//                        String noti_msg = "There are some changes in the App and need to update/install forcefully.\n\nPlease UPDATE_APP now?";
                    try {
                        if (!ExtraUtils.isPackageExisted(packageName, this)) {
                            sendNotificationMsg(message, action);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (mode.equalsIgnoreCase(ExtraUtils.PROMOTE_APP)) {
                    String noti_msg = "If you like to use our more apps, Click here";
                    sendNotificationMsg(noti_msg, action);

                } else {
                    sendNotificationMsg(message, action);
                }


            }
              /*  if (remoteMessage.getNotification() != null) {
                    Log.d("MainActivity","message: " + remoteMessage.getNotification().getBody());
                    //    sendNotificationMsg(remoteMessage.getNotification().getBody());
                }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNotificationMsg(String body, String action) {
        try {
            Intent intent = new Intent(action);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int notifyID = 1;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;// The id of the channel.
                String CHANNEL_ID = "my_channel_01";
                NotificationChannel mChannel = new NotificationChannel(
                        CHANNEL_ID, name, importance);
                mChannel.setSound(null, null);
                assert notificationManager != null;
                notificationManager.createNotificationChannel(mChannel);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher)
                        /*          .setContentTitle(getString(R.string.app_name))*/
                        .setContentText(body)
                        /* .setDefaults(Notification.DEFAULT_SOUND)*/
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

                notificationManager.notify(notifyID, mBuilder.build());

            } else {
//Get an instance of NotificationManager//

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_launcher)
                        /*         .setContentTitle(getString(R.string.app_name))*/
                        .setContentText(body)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        /*  .setDefaults(Notification.DEFAULT_SOUND)*/
                        .setAutoCancel(true).setContentIntent(pendingIntent);


                NotificationManager mNotificationManager =

                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                assert mNotificationManager != null;
                mNotificationManager.notify(notifyID, mBuilder.build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}