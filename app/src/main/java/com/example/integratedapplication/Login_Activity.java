package com.example.integratedapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.security.AccessController.getContext;

public class Login_Activity extends AppCompatActivity {

    AppCompatEditText userNameEditText,passWordEditText;
    Button logIn;
    SharedPreferences sharedPreferences,logginSharedPref;
    String body;
    String userName;
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    Boolean isLogeedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        userNameEditText=findViewById(R.id.userNameEditText);
        passWordEditText=findViewById(R.id.passWordEditText);
        logIn=findViewById(R.id.loginButton);





        //sharedPreferences Code
        sharedPreferences=getSharedPreferences("user_name",MODE_PRIVATE);
        logginSharedPref=getSharedPreferences("isLoggedIn",MODE_PRIVATE);
        logginSharedPref=getApplicationContext().getSharedPreferences("isLoggedIn", Context.MODE_PRIVATE);

//        prefManager = new PrefManager(getApplicationContext());


        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((userNameEditText.getText().length()) > 0 && (passWordEditText.getText().length()>0)) {
//                    System.out.println(preferenceManager.getUsername());
                    ProgressBar.setProgressBar(Login_Activity.this, "Please Wait");
                    login(userNameEditText.getText().toString(),passWordEditText.getText().toString());
                   userName=userNameEditText.getText().toString();


//                    SharedPreferences.Editor editor=sharedPreferences.edit();
//                    editor.putBoolean(KEY_IS_LOGGEDIN,true );
//                    editor.commit();
//                    isLoggedIn();
//                    FirebaseMessaging.getInstance().
//                            subscribeToTopic(userName).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
////                firebaseMessagingService.onMessageReceived(remoteMessage);
//
//                            Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
//                        }
//                    });

//                    String userName=userNameEditText.getText().toString();

//                    prefManager.setLogin(userNameEditText.getText().toString(),passWordEditText.getText().toString());

                }else
                {
                    Toast.makeText(getApplicationContext(),"Enter Correct Details",Toast.LENGTH_LONG).show();
                }

            }
        });

//
//        if (isLoggedIn()){
//
//    if(true){
//
//    FirebaseMessaging.getInstance().
//            subscribeToTopic(userName).addOnSuccessListener(new OnSuccessListener<Void>() {
//        @Override
//        public void onSuccess(Void aVoid) {
//            Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
//        }
//    });
//    }
}
    public void setLogin(boolean isLoggedIn) {
        //SharedPreference Code
        ;

    }

    public boolean isLoggedIn(){
        return logginSharedPref.getBoolean(KEY_IS_LOGGEDIN, false);
    }


    private void login (final String username, final String password){

        String tag_string_req = "login";
        String url="http://lntifr.azurewebsites.net/api/LANDTApi/LoginAuth?userName="+userNameEditText.getText().toString()+"&passWord="+passWordEditText.getText().toString();

        //SharedPreference Code
        sharedPreferences=getSharedPreferences("user_name",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("username",userNameEditText.getText().toString());
        editor.commit();


//        System.out.println(preferenceManager.setUsername(userNameEditText.getText().toString()));
        StringRequest strReq = new StringRequest(Request.Method.GET,
               url, new Response.Listener<String>() {
//            APIConfig.URL_LOGIN + userNameEditText.getText().toString()+ passWordEditText.getText().toString()
            @Override
            public void onResponse(String response) {

                ProgressBar.dismissProgress();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String status = jObj.getString("status");

                    // Check for error node in json
                    if (status.equals("0")) {
                            if(sharedPreferences.contains("username")) {
                                Intent intent = new Intent(Login_Activity.this, DefaultActivity.class);
                                startActivity(intent);
                                finish();

                                Toast.makeText(getApplicationContext(), "Login Successfull", Toast.LENGTH_LONG).show();

                                SharedPreferences.Editor editor=logginSharedPref.edit();
                                editor.putBoolean(KEY_IS_LOGGEDIN,true );
                                editor.commit();
                                isLoggedIn();
                                      FirebaseMessaging.getInstance().
                                        subscribeToTopic(userName).addOnSuccessListener(new OnSuccessListener<Void>() {
                                             @Override
                                               public void onSuccess(Void aVoid) {
//                firebaseMessagingService.onMessageReceived(remoteMessage);

                            Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
                        }
                    });
                            }
                    } else {
                        // Error in login. Get the error message
                        Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    // JSON error
                    e.printStackTrace();

                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//                ProgressBar.dismissProgress();
//            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    return;
                }

                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // exception
                }
            }

        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userName", username);
//                preferenceManager.setUsername(username);
                params.put("passWord", password);

                return params;
            }

        };

        ApiController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}
