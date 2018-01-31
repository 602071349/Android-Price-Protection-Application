package com.example.bikenmaharjan.priceapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by alan on 11/29/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final int BROADCAST_NOTIFICATION_ID = 1;


    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String notificationBody = "";
        String notificationTitle = "";
        String notificationData = "";
        String notificationurl ="";

        String notification_name = "";
        String notification_original = "";
        String notification_Best = "";
        String notification_url = "";
        String notification_barcode = "";
        String notification_date = "";
        String id= "";
        String notification_image ="";
        try{


            notificationData = remoteMessage.getData().toString();
            notificationTitle = remoteMessage.getNotification().getTitle();
            notificationBody = remoteMessage.getNotification().getBody();

            notificationurl = remoteMessage.getData().get("score");
            notification_name = remoteMessage.getData().get("name");
            notification_date = remoteMessage.getData().get("date");
            notification_original = remoteMessage.getData().get("original");
            notification_Best = remoteMessage.getData().get("BestPrice");
            notification_url = remoteMessage.getData().get("url");
            notification_barcode = remoteMessage.getData().get("barcode");
            notification_image = remoteMessage.getData().get("image");




            id = remoteMessage.getMessageId();


            String title = notificationTitle;
            String message = notificationBody;
            String messageId = id;


            /* ADD variable here */
            sendMessageNotification(title, message, messageId,notificationurl
                    ,notification_name,notification_original,notification_Best,notification_url,notification_barcode,notification_date,notification_image);

            Log.d("message1111",title+message+messageId);



        }catch (NullPointerException e){
            Log.e(TAG, "onMessageReceived: NullPointerException: " + e.getMessage() );
        }
        Log.d(TAG, "onMessageReceived: data: " + notificationData);
        Log.d(TAG, "onMessageReceived: notification body: " + notificationBody);
        Log.d(TAG, "onMessageReceived: notification title: " + notificationTitle);


        //String dataType = remoteMessage.getData().get(getString("data_ty"));





//        if(dataType.equals(getString(R.string.direct_message))){

//            String title = remoteMessage.getData().get("message_title");
//            String message = remoteMessage.getData().get(getString(R.string.data_message));
//            String messageId = remoteMessage.getData().get(getString(R.string.data_message_id));
//            sendMessageNotification(title, message, messageId);
//        }
    }

    /**
     * Build a push notification for a chat message
     * @param title
     * @param message
     */
    // inside app works
    private void sendMessageNotification(String title, String message, String messageId, String url,
                                         String notification_name,String notification_original,String notification_Best,
                                         String notification_url,String notification_barcode, String notification_date,String notification_image){
        Log.d(TAG, "sendChatmessageNotification: building a chatmessage notification");

        //get the notification id
        int notificationId = buildNotificationId(messageId);

        // Instantiate a Builder object.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"fcm_default_channel");



        // Creates an Intent for the Activity
        Intent pendingIntent = new Intent(getApplicationContext(), DetailActivity.class);
        pendingIntent.putExtra("score",url);
        pendingIntent.putExtra("name",notification_name);
        pendingIntent.putExtra("date",notification_date);
        pendingIntent.putExtra("original_price",notification_original);
        pendingIntent.putExtra("best_price",notification_Best);
        pendingIntent.putExtra("url", notification_url);
        pendingIntent.putExtra("barcode",notification_barcode);
        pendingIntent.putExtra("image",notification_image);



        // Sets the Activity to start in a new, empty task
        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Creates the PendingIntent
        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        pendingIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        //add properties to the builder
        builder.setSmallIcon(R.drawable.icon)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.drawable.icon))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title)
                .setAutoCancel(true)
                //.setSubText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setOnlyAlertOnce(true);

        builder.setContentIntent(notifyPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(notificationId, builder.build());

        Log.i("sendMessageNotification","good");
    }


    private int buildNotificationId(String id){

        Log.d(TAG, "buildNotificationId: building a notification id.");

        int notificationId = 0;

        for(int i = 0; i < 9; i++){
            notificationId = notificationId + id.charAt(0);
        }

        Log.d(TAG, "buildNotificationId: id: " + id);
        Log.d(TAG, "buildNotificationId: notification id:" + notificationId);
        Log.i("buildNotificationId","good");
        return notificationId;



    }

}