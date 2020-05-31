package com.example.integratedapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;


import java.util.Objects;

public class FirebaseMessagingService extends  com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG="FirebaseMessagingServic";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    SharedPreferences sharedPreferences;
//String username=sharedPreferences.getString("username",null);


    public FirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        final String strTitle = remoteMessage.getNotification().getTitle();
        final String message = remoteMessage.getNotification().getBody();
        Log.d(TAG, "onMessageReceived: Message Received: \n" +
                "Title: " + strTitle + "\n" +
                "Message: " + message);
        sharedPreferences=getApplicationContext().getSharedPreferences("isLoggedIn", Context.MODE_PRIVATE);
//        sharedPreferences=getSharedPreferences("user_name",MODE_PRIVATE);
//        if (sharedPreferences.equals(sharedPreferences.getString("username",null))) {
//        FirebaseMessaging.getInstance().
//                subscribeToTopic("Admin").addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
////                firebaseMessagingService.onMessageReceived(remoteMessage);
      if ( sharedPreferences.getBoolean(KEY_IS_LOGGEDIN,false)){

                sendNotification(strTitle,message);}
      else {
      }
//                Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
//            }
//        });

//        }
    }





    @Override
    public void onDeletedMessages() {

    }

    private  void sendNotification(String title,String messageBody) {
        Intent[] intents= new Intent[1];
        Intent intent= new      Intent(this,PickListActivity.class);
//
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("PickList-ID", title);
//        intent.putExtra("ShortDesc", messageBody);
        intents[0]=intent;
        PendingIntent pendingIntent=PendingIntent.getActivities(this,0,
                intents, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri
                (RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationbuilder=
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_arrow_back_black_24dp)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setLargeIcon(BitmapFactory.decodeResource
                                (getResources(), R.mipmap.ic_launcher));;

        NotificationManager notificationManager=(NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationbuilder.build());
    }

}

