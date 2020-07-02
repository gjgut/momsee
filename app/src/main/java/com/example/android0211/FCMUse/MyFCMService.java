package com.example.android0211.FCMUse;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android0211.Child_mission;
import com.example.android0211.R;
import com.example.android0211.activity_child_info;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.w3c.dom.Text;

public class MyFCMService extends FirebaseMessagingService {
    private static final String TAG = MyFCMService.class.getSimpleName();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            Log.d(TAG, "MM");
            Log.d(TAG, "From: " + remoteMessage.getFrom());
            Log.i("Message","Heyyyyyyyyyyyyyyy");
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
                if (true) {
                } else {
                    handleNow();
                }
            }
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.activity_child_mission, null);
            //TextView textView = (TextView)layout.findViewById(R.id.textView2);
            if (remoteMessage.getNotification() != null) {
                sendNotification(remoteMessage.getNotification().getBody());
                Intent intent = new Intent(getApplicationContext(), OverlayService.class);
                if (remoteMessage.getNotification().getTitle().equals("LOCK"))
                    startService(intent);
                else if (remoteMessage.getNotification().getTitle().equals("mission added")) {
                    Intent broadintent = new Intent("SEND_BROAD_CAST");
                    broadintent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
                    sendBroadcast(broadintent);
                } else
                    stopService(intent);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("MyToken", s);
    }

    private void handleNow() {

        Log.d(TAG, "Short lived task is done.");

    }


    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, activity_child_info.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);


        String channelId = getString(R.string.app_name);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("FCM Message")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = getString(R.string.app_name);
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);

        }

        notificationManager.notify(0, notificationBuilder.build());

    }


}
