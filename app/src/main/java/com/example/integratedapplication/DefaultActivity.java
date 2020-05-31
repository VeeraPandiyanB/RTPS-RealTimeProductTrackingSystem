package com.example.integratedapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;

public class DefaultActivity extends AppCompatActivity {

    Button mainActivity,listActivity,pickList,logout;
    SharedPreferences sharedPreferences,logoutSharedPref;
    Context context;
    FirebaseMessagingService firebaseMessagingService;
    RemoteMessage remoteMessage;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        mainActivity=findViewById(R.id.mainActivityButton);
        listActivity=findViewById(R.id.listActivityButton);
        pickList=findViewById(R.id.pickListButton);
        logout=findViewById(R.id.logOutButton);



        mainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DefaultActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        listActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DefaultActivity.this,ListActivity.class);
                startActivity(intent);
            }
        });

//        pickList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(DefaultActivity.this,PickListActivity.class);
//                startActivity(intent);
//            }
//        });

        pickList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DefaultActivity.this,PickListActivity.class);
                startActivity(intent);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DefaultActivity.this,Login_Activity.class);
                startActivity(intent);

               sharedPreferences=getSharedPreferences("user_name",MODE_PRIVATE);
               logoutSharedPref=getSharedPreferences("isLoggedIn",MODE_PRIVATE);
               String user=sharedPreferences.getString("username",null);
               sharedPreferences.edit().clear().commit();
                FirebaseMessaging.getInstance().
                        unsubscribeFromTopic(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                firebaseMessagingService.onMessageReceived(remoteMessage);

                        Toast.makeText(getApplicationContext(),"UnSubscribed", Toast.LENGTH_LONG).show();
                    }
                });
//                try {
//                    FirebaseInstanceId.getInstance().deleteToken(getString(R.string.SenderId),"FCM");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                FirebaseMessaging.getInstance().
//                        unsubscribeFromTopic("user_name").addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(getApplicationContext(),"Unsubscribed", Toast.LENGTH_LONG).show();
//                    }
//                });
                finish();


            }
        });


    }
}
