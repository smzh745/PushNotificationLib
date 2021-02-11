package push.notifiction.mylibrary;/*
package com.docs.reader.pdf.viewer.app.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import com.docs.reader.pdf.viewer.app.R;
import com.docs.reader.pdf.viewer.app.activity.Starter_Activity;

public class FirebaseBackgroundService  extends WakefulBroadcastReceiver {

    private static final String TAG = "FirebaseService";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "I'm in!!!");

        String title = null;
        String message = null;
        String packageName = null;
        String button = null;
        try {
            if (intent.getExtras() != null) {
                for (String key : intent.getExtras().keySet()) {
                    Object value = intent.getExtras().get(key);
                    Log.e("FirebaseDataReceiver", "Key: " + key + " Value: " + value);
                    if(key.equalsIgnoreCase("title") && value != null) {
                        title = value.toString();
                    }else if(key.equalsIgnoreCase("message") && value != null) {
                        message = value.toString();
                    }else if(key.equalsIgnoreCase("package") && value != null) {
                        packageName = value.toString();
                    }else if(key.equalsIgnoreCase("button") && value != null) {
                        button = value.toString();
                    }
                }

                if (message != null) {
                    try {
                        GetAllNotificationValues.writeTitle(context, title);
                        GetAllNotificationValues.writeMessage(context, message);
                        GetAllNotificationValues.writePackageName(context, packageName);
                        GetAllNotificationValues.writeButtonName(context, button);
                    } catch (Exception e) {
                        title = "";
                        e.printStackTrace();
                    }

                    if(title.equalsIgnoreCase(ExtraUtils.Update)){
//                        String noti_msg = "There are some changes in the App and need to update/install forcefully.\n\nPlease Update now?";
                        try {
                            if (!ExtraUtils.isPackageExisted(packageName, context)) {
                                sendNotificationMsg(message, context);
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if(title.equalsIgnoreCase(ExtraUtils.NEW_App)){
                        String noti_msg = "If you like to use our more apps, Click here";
                        sendNotificationMsg(noti_msg, context);

                    }else {
                        sendNotificationMsg(message, context);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void sendNotificationMsg(String body, Context context) {
        final CharSequence name = "My Channel";// The user-visible name of the channel.

        try {
            Intent intent = new Intent(context, Starter_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            int notifyID = 1;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;// The id of the channel.
                String CHANNEL_ID = "my_channel_01";
                NotificationChannel mChannel = new NotificationChannel(
                        CHANNEL_ID, name, importance);
                mChannel.setSound(null, null);
                assert notificationManager != null;
                notificationManager.createNotificationChannel(mChannel);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(body)
                        */
/* .setDefaults(Notification.DEFAULT_SOUND)*//*

                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

                notificationManager.notify(notifyID, mBuilder.build());

            } else {
//Get an instance of NotificationManager//

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context.getApplicationContext())
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(body)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        */
/*  .setDefaults(Notification.DEFAULT_SOUND)*//*

                        .setAutoCancel(true).setContentIntent(pendingIntent);


                NotificationManager mNotificationManager =

                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                assert mNotificationManager != null;
                mNotificationManager.notify(notifyID, mBuilder.build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}*/
