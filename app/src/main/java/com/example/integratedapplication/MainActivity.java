package com.example.integratedapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    TextView barCodeTextView,coordinatesTextView;
    Button barCodeButton,coordinatesButton,detailsButton;
    private IntentIntegrator qrScan;
    String body;
    float degree;

    private float[] gravity = new float[3];
    // magnetic data
    private float[] geomagnetic = new float[3];
    // Rotation data
    private float[] rotation = new float[9];
    // orientation (azimuth, pitch, roll)
    private float[] orientation = new float[3];
    // smoothed values
    private float[] smoothed = new float[3];

    String dirTxt;

    private double bearing = 0;
    private GeomagneticField geomagneticField;
    private float currentDegree = 0f;

    // device sensor manager
    private SensorManager mSensorManager;

    TextView tvHeading,compassDirectionTextView;


    Parcelable detailObject;
//    DetailListParcable detailListParcable;

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    GoogleApiClient googleApiClient;
    SharedPreferences sharedPreferences;
    Context context = this;
    private FusedLocationProviderClient mFusedLocationClient;
    ArrayList<ListModel> details = new ArrayList<ListModel>();

    String barcode,coordinates,barCodeParce,coordinatesParce;
    /** Declaring an ArrayAdapter to set items to ListView */
//    ArrayAdapter<DetailListParcable> adapter;










    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barCodeTextView=findViewById(R.id.barCodeTextView2);
        coordinatesTextView=findViewById(R.id.coordinatesTextView4);
        barCodeButton=findViewById(R.id.buttonScan);
        coordinatesButton=findViewById(R.id.coordinatesButton);
        detailsButton=findViewById(R.id.detailsButton);
        compassDirectionTextView=findViewById(R.id.compassTextView);

        sharedPreferences=getSharedPreferences("user_name",MODE_PRIVATE);

//        barcode=barCodeTextView.getText().toString();
//        coordinates=coordinatesTextView.getText().toString();

        coordinatesButton=findViewById(R.id.coordinatesButton);
        coordinatesTextView=findViewById(R.id.coordinatesTextView4);

        qrScan = new IntentIntegrator(this);
        barCodeButton.setOnClickListener(this);
//        barCodeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tvHeading.setText("Compass Value  : "+degree);
//            }
//        });

        tvHeading = (TextView) findViewById(R.id.compassTextView);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);



//
//        FirebaseMessaging.getInstance().
//                subscribeToTopic(sharedPreferences.getString("username",null)).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
//            }
//        });

//        adapter = new ArrayAdapter<Constants>(this, android.R.layout.activity_list_item, details);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        coordinatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                displayLocationSettingsRequest(context);
//                {
                    fetchLocation();
//                }
            }
        });
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(barCodeTextView.getText().toString().length()>0 && coordinatesTextView.getText().toString().length()>0) {

                    barCodeTextView=findViewById(R.id.barCodeTextView2);
                    coordinatesTextView=findViewById(R.id.coordinatesTextView4);
                    SaveMappingDetails(barCodeTextView.getText().toString(),coordinatesTextView.getText().toString());

                    barCodeTextView.setText("");
                    coordinatesTextView.setText("");
                    compassDirectionTextView.setText("");
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please Enter both Barcode and Location Coordinates",Toast.LENGTH_LONG).show();
                }
//                adapter.notifyDataSetChanged();

//                Intent i=new Intent(MainActivity.this,ListActivity.class);
//                Bundle bundle=new Bundle();
//                bundle.putParcelable("detail", (Parcelable) details);
//                i.putExtras(bundle);
//                startActivity(i);

//                adapter.notifyDataSetChanged();

               }
        });

    }

    private void SaveMappingDetails (final String barCode, final String gpsLocation){


        String tag_string_req = "Save_Detils";
//        String url="http://lntifr.azurewebsites.net/api/LANDTApi/LoginAuth?userName="+userNameEditText.getText().toString()+"&passWord="+passWordEditText.getText().toString();
        String SaveURL="http://lntifr.azurewebsites.net/api/LANDTApi/SaveBarcodeDetails?barCode="+barCodeTextView.getText().toString()+"&gpsLocation="+coordinatesTextView.getText().toString()+"&RFIDName=1245432sdsdsad&BarcodeCompass="+compassDirectionTextView.getText().toString()+"&RFIDCompass=120E";
//        System.out.println(SaveURL);
        StringRequest strReq = new StringRequest(Request.Method.GET, SaveURL, new Response.Listener<String>() {
            //            APIConfig.URL_LOGIN + userNameEditText.getText().toString()+ passWordEditText.getText().toString()
            @Override
            public void onResponse(String response) {

                ProgressBar.dismissProgress();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String status = jObj.getString("status");

                    // Check for error node in json
                    if (status.equals("0")) {

                        Toast.makeText(getApplicationContext(),"Stored Successfully",Toast.LENGTH_LONG).show();

//                        Intent intent=new Intent(MainActivity.this,ListActivity.class);
//                        startActivity(intent);
                    } else {
                        // Error in login. Get the error message
                        Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

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
                params.put("barCode", barCode);
                params.put("gpsLocation", gpsLocation);

                return params;
            }

        };

        ApiController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
//                    JSONObject obj = new JSONObject(result.getContents());
                    String barcodeValue=result.getContents();
                    //setting values to textviews
                    barCodeTextView.setText(barcodeValue);
                    tvHeading.setText(degree+"" + ((int) bearing) + ((char) 176) + " "
                            + dirTxt);

                } catch (Exception e) {
                    e.printStackTrace();
                }
//                catch (JSONException e) {
//                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
//                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
//                    barCodeTextView.setText(result.getContents());

//                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }}

    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        qrScan.initiateScan();
//        onSensorChanged(event);

    }

    private void fetchLocation() {


        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


//            displayLocationSettingsRequest(context);

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(this)
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give this permission to acess this feature")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                            }
                        })

                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
//             Permission has already been granted
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                Criteria criteria=new Criteria();
                                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                                criteria.setPowerRequirement(Criteria.POWER_HIGH);
                                criteria.setCostAllowed(true);

                                Double latittude = location.getLatitude();
                                Double longitude = location.getLongitude();

                                coordinatesTextView.setText(latittude + "," + longitude);

//                                coordinatesTextView.setText((int) (latittude+longitude));
                            }
                        }
                    });

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //abc
            }else{



            }
        }

    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(1000);
//        locationRequest.setFastestInterval(1000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

//        fetchLocation();

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
//                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
//                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

       degree = Math.round(event.values[0]);

//        boolean accelOrMagnetic = false;
//
//
//        // get rotation matrix to get gravity and magnetic data
//        SensorManager.getRotationMatrix(rotation, null, gravity, geomagnetic);
//        // get bearing to target
//        SensorManager.getOrientation(rotation, orientation);
//        // east degrees of true North
//        bearing = orientation[0];
//        // convert from radians to degrees
//        bearing = Math.toDegrees(bearing);
//
//        // fix difference between true North and magnetical North
//        if (geomagneticField != null) {
//            bearing += geomagneticField.getDeclination();
//        }
//
//        // bearing must be in 0-360
//        if (bearing < 0) {
//            bearing += 360;
//        }
//
//        // update compass view
////        compassView.setBearing((float) bearing);
//
////        if (accelOrMagnetic) {
////            compassView.postInvalidate();
////        }

        updateTextDirection(bearing); // display text direction on screen
        currentDegree = -degree;
    }

    private void updateTextDirection(double bearing) {
        int range = (int) (degree / (360f / 16f));
        dirTxt = "";

        if (range == 15 || range == 0)
            dirTxt = "N";
        if (range == 1 || range == 2)
            dirTxt = "NE";
        if (range == 3 || range == 4)
            dirTxt = "E";
        if (range == 5 || range == 6)
            dirTxt = "SE";
        if (range == 7 || range == 8)
            dirTxt = "S";
        if (range == 9 || range == 10)
            dirTxt = "SW";
        if (range == 11 || range == 12)
            dirTxt = "W";
        if (range == 13 || range == 14)
            dirTxt = "NW";

//        textDirection.setText(); // char 176 ) = degrees ...

        // create a rotation animation (reverse turn degree degrees)
//        RotateAnimation ra = new RotateAnimation(
//                currentDegree,
//                -degree,
//                Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF,
//                0.5f);
//
//        // how long the animation will take place
//        ra.setDuration(210);}
//
//        // set the animation after the end of the reservation status
//        ra.setFillAfter(true);

        // Start the animation
//        image.startAnimation(ra);


//        compassDirectionTextView.setText("" + ((int) bearing) + ((char) 176) + " "
//                + dirTxt);

    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD
                && accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }
}
