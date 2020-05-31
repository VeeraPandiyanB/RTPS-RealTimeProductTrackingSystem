package com.example.integratedapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.example.integratedapplication.R.string.unpick;

public class LineItemsAdapter extends BaseAdapter {

    public ArrayList<LineItemsModel> lineItemsModels;
    Activity activity;
    LayoutInflater layoutInflater;
    //    LineItemsModel lineItemsModel;
    SharedPreferences sharedPreferences;

    String body;

    private static final String ITEM_IS_Checked = "ItemIsChecked";


    public LineItemsAdapter(Activity activity, ArrayList<LineItemsModel> lineItemsModels) {
        this.lineItemsModels = lineItemsModels;
        this.activity = activity;
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sharedPreferences= activity.getSharedPreferences("isChecked",MODE_PRIVATE);

        sharedPreferences=activity.getSharedPreferences("ItemIsChecked",MODE_PRIVATE);

//        SharedPreferences.Editor editor = sharedPreferences.edit();

    }



    @Override
    public int getCount() {
        return lineItemsModels.size();
    }

    @Override
    public Object getItem(int i) {
        return lineItemsModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return (i);
    }

    private class ViewHolder {
        TextView barCodeTextPickList, rfidTextPickList;
        ImageView coordinatesImageViewPickList;
        ToggleButton pickButtonPickList;

        SharedPreferences sharedPreferences;
        LineItemsModel lineModel;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (view == null) {
            view = inflater.inflate(R.layout.list_line_items, null);
            holder = new LineItemsAdapter.ViewHolder();
            holder.barCodeTextPickList = view.findViewById(R.id.barCodePickListTextView);
            holder.rfidTextPickList = view.findViewById(R.id.rfIdPickListTextView);
            holder.coordinatesImageViewPickList = view.findViewById(R.id.locationCoordinatesImageViewPickList);
            holder.pickButtonPickList = view.findViewById(R.id.pickButton);


            view.setTag(holder);




        }


        else {
            holder = (ViewHolder) view.getTag();
        }

        final LineItemsModel lineItemsModel = (LineItemsModel) getItem(i);
        holder.barCodeTextPickList.setText(lineItemsModel.getBarcode());
        holder.rfidTextPickList.setText(lineItemsModel.getRfid());
//        holder.coordinatesImageView.setText(listModel.getGpsLocation());



        holder.coordinatesImageViewPickList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getItemId(i);
//                System.out.println(getItemId(i));
                getItem(i);
//                System.out.println(getItem(i));
                {
//                    String map = "http://maps.google.co.in/maps/places?q="+listModel.getGpsLocation();

                    String map = "http://maps.google.co.in/maps/place?q=" + lineItemsModel.getGpsLocation() + "(" + lineItemsModel.getBarcode() + ")";

                    System.out.println(map);
                    System.out.println(map);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                    mapIntent.putExtra("Barcode", lineItemsModel.getBarcode());
                    activity.startActivity(mapIntent);
                }
            }
        });
//        constraintLayout = view.findViewById(R.id.pickListConstraintLayout);


        final ConstraintLayout constraintLayout=view.findViewById(R.id.pickListConstraintLayout);

        if (lineItemsModel.isPicked()){
//            SharedPreferences.Editor editor=sharedPreferences.edit();
//            editor.putBoolean(ITEM_IS_Checked,false );
//            editor.commit();
//            itemIsChecked();

            holder.pickButtonPickList.setChecked(lineItemsModels.get(i).isPicked());
            constraintLayout.setBackgroundColor(Color.parseColor("#45b6fe"));
//            holder.pickButtonPickList.getText().toString();
            holder.pickButtonPickList.setText("UnPick");
//            holder.pickButtonPickList.requestLayout();


        }
        else
        {
//            SharedPreferences.Editor editor=sharedPreferences.edit();
//            editor.putBoolean(ITEM_IS_Checked,true );
//            editor.commit();
//            itemIsChecked();
//            holder.pickButtonPickList.setChecked(lineItemsModels.get(i).isPicked());
            holder.pickButtonPickList.setChecked(lineItemsModels.get(i).isPicked());
            constraintLayout.setBackgroundColor(Color.parseColor("#ffffff"));
//            holder.pickButtonPickList.getText().toString();
            holder.pickButtonPickList.setTextOff("Pick");
//            holder.pickButtonPickList.requestLayout();

        }


        final String pickButtontext= holder.pickButtonPickList.getText().toString();
        holder.pickButtonPickList.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        getItem(i);
        getItemId(i);
        Boolean picked=lineItemsModel.isPicked();

        if (picked==false) {

//                        prefs.edit().putBoolean("checked", true).apply();
//                        System.out.println(prefs);

//                    editor.putBoolean("ischeck", true);
//                    editor.commit();
            String tag_string_req = "ChangePickStatus";
            String pickUrl = "http://lntifr.azurewebsites.net/api/LANDTApi/ChangePickStatus?barCode=" + lineItemsModel.getBarcode() + "&isPicked=1";
            lineItemsModel.setPicked(true);
            System.out.println(pickUrl);
//        //SharedPreference Code
//        SharedPreferences.Editor editor=sharedPreferences.edit();
//        editor.putString("username",userNameEditText.getText().toString());
//        editor.commit();


//        System.out.println(preferenceManager.setUsername(userNameEditText.getText().toString()));
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    pickUrl, new Response.Listener<String>() {
                //            APIConfig.URL_LOGIN + userNameEditText.getText().toString()+ passWordEditText.getText().toString()
                @Override
                public void onResponse(String response) {

                    ProgressBar.dismissProgress();

                    try {
//                                JSONObject jObj = new JSONObject(response);
//                                String status = jObj.getString("status");

                        // Check for error node in json
//                                if (status.equals("0")) {
//                        if(sharedPreferences.contains("username")) {
//                            Intent intent = new Intent(Login_Activity.this, DefaultActivity.class);
//                            startActivity(intent);
//                            finish();

                        Toast.makeText(activity, "Items are picked", Toast.LENGTH_LONG).show();
//                        }
//                                } else {
//                                    // Error in login. Get the error message
//                                    Toast.makeText(activity, "Not picked", Toast.LENGTH_LONG).show();
//                                }
                    } catch (Exception e) {
                        // JSON error
                        e.printStackTrace();

                        Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
//                params.put("userName", username);
////                preferenceManager.setUsername(username);
//                params.put("passWord", password);

                    return params;
                }

            };

            constraintLayout.setBackgroundColor(Color.parseColor("#45b6fe"));
            holder.pickButtonPickList.setText("UnPick");
            ApiController.getInstance().addToRequestQueue(strReq, tag_string_req);


//                    lineItemsModel.setPicked(true);


        }
        else {
//                    prefs.edit().putBoolean("UnChecked", false).commit();
//                        editor.putBoolean("notcheck",false);
//                        editor.commit();
//            getItem(i);
//            getItemId(i);

            String tag_string_req = "ChangePickStatus";
            String unPickUrl = "http://lntifr.azurewebsites.net/api/LANDTApi/ChangePickStatus?barCode=" + lineItemsModel.getBarcode() + "&isPicked=0";
            lineItemsModel.setPicked(false);
            System.out.println(unPickUrl);
//        //SharedPreference Code
//        SharedPreferences.Editor editor=sharedPreferences.edit();
//        editor.putString("username",userNameEditText.getText().toString());
//        editor.commit();


//        System.out.println(preferenceManager.setUsername(userNameEditText.getText().toString()));
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    unPickUrl, new Response.Listener<String>() {
                //            APIConfig.URL_LOGIN + userNameEditText.getText().toString()+ passWordEditText.getText().toString()
                @Override
                public void onResponse(String response) {

                    ProgressBar.dismissProgress();

                    try {
//                                JSONObject jObj = new JSONObject(response);
//                                String status = jObj.getString("status");
//
//                                // Check for error node in json
//                                if (status.equals("1")) {
//                        if(sharedPreferences.contains("username")) {
//                            Intent intent = new Intent(Login_Activity.this, DefaultActivity.class);
//                            startActivity(intent);
//                            finish();

                        Toast.makeText(activity, "Items are Unpicked", Toast.LENGTH_LONG).show();
//                        }
//                                } else {
//                                    // Error in login. Get the error message
//                                    Toast.makeText(activity, "Not picked", Toast.LENGTH_LONG).show();
//                                }
                    } catch (Exception e) {
                        // JSON error
                        e.printStackTrace();

                        Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        System.out.println(e.getMessage());
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
//                params.put("userName", username);
////                preferenceManager.setUsername(username);
//                params.put("passWord", password);

                    return params;
                }

            };
            constraintLayout.setBackgroundColor(Color.parseColor("#ffffff"));
//            holder.pickButtonPickList.setTextOff("Pick");
            holder.pickButtonPickList.setText("Pick");
            ApiController.getInstance().addToRequestQueue(strReq, tag_string_req);


//            constraintLayout.setBackgroundColor(Color.parseColor("#ffffff"));
//            holder.pickButtonPickList.setTextOff("Pick");

        }
    }
});

//
//        holder.pickButtonPickList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
//                SharedPreferences.Editor editor = sharedPreferences.edit();

//                if (isChecked) {
//                    getItem(i);
//                    getItemId(i);
//
////                        prefs.edit().putBoolean("checked", true).apply();
////                        System.out.println(prefs);
//
////                    editor.putBoolean("ischeck", true);
////                    editor.commit();
//                    String tag_string_req = "ChangePickStatus";
//                    String pickUrl = "http://lntifr.azurewebsites.net/api/LANDTApi/ChangePickStatus?barCode=" + lineItemsModel.getBarcode() + "&isPicked=1";
//                    System.out.println(pickUrl);
////        //SharedPreference Code
////        SharedPreferences.Editor editor=sharedPreferences.edit();
////        editor.putString("username",userNameEditText.getText().toString());
////        editor.commit();
//
//
////        System.out.println(preferenceManager.setUsername(userNameEditText.getText().toString()));
//                    StringRequest strReq = new StringRequest(Request.Method.GET,
//                            pickUrl, new Response.Listener<String>() {
//                        //            APIConfig.URL_LOGIN + userNameEditText.getText().toString()+ passWordEditText.getText().toString()
//                        @Override
//                        public void onResponse(String response) {
//
//                            ProgressBar.dismissProgress();
//
//                            try {
////                                JSONObject jObj = new JSONObject(response);
////                                String status = jObj.getString("status");
//
//                                // Check for error node in json
////                                if (status.equals("0")) {
////                        if(sharedPreferences.contains("username")) {
////                            Intent intent = new Intent(Login_Activity.this, DefaultActivity.class);
////                            startActivity(intent);
////                            finish();
//
//                                    Toast.makeText(activity, "Items are picked", Toast.LENGTH_LONG).show();
////                        }
////                                } else {
////                                    // Error in login. Get the error message
////                                    Toast.makeText(activity, "Not picked", Toast.LENGTH_LONG).show();
////                                }
//                            } catch (Exception e) {
//                                // JSON error
//                                e.printStackTrace();
//
//                                Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                            }
//
//                        }
//                    }, new Response.ErrorListener() {
//
////            @Override
////            public void onErrorResponse(VolleyError error) {
////
////                Toast.makeText(getApplicationContext(),
////                        error.getMessage(), Toast.LENGTH_LONG).show();
////                ProgressBar.dismissProgress();
////            }
//
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            if (error == null || error.networkResponse == null) {
//                                return;
//                            }
//
//                            //get status code here
//                            final String statusCode = String.valueOf(error.networkResponse.statusCode);
//                            //get response body and parse with appropriate encoding
//                            try {
//                                body = new String(error.networkResponse.data, "UTF-8");
//                            } catch (UnsupportedEncodingException e) {
//                                // exception
//                            }
//                        }
//
//                    }) {
//
//                        @Override
//                        protected Map<String, String> getParams() {
//                            // Posting parameters to login url
//                            Map<String, String> params = new HashMap<String, String>();
////                params.put("userName", username);
//////                preferenceManager.setUsername(username);
////                params.put("passWord", password);
//
//                            return params;
//                        }
//
//                    };
//
//                    ApiController.getInstance().addToRequestQueue(strReq, tag_string_req);
//
//                    constraintLayout.setBackgroundColor(Color.parseColor("#45b6fe"));
//                    holder.pickButtonPickList.setTextOn("UnPick");
//
////                    lineItemsModel.setPicked(true);
//
//
//                } else {
////                    prefs.edit().putBoolean("UnChecked", false).commit();
////                        editor.putBoolean("notcheck",false);
////                        editor.commit();
//                    getItem(i);
//                    getItemId(i);
//
//                    String tag_string_req = "ChangePickStatus";
//                    String unPickUrl = "http://lntifr.azurewebsites.net/api/LANDTApi/ChangePickStatus?barCode=" + lineItemsModel.getBarcode() + "&isPicked=0";
//                    System.out.println(unPickUrl);
////        //SharedPreference Code
////        SharedPreferences.Editor editor=sharedPreferences.edit();
////        editor.putString("username",userNameEditText.getText().toString());
////        editor.commit();
//
//
////        System.out.println(preferenceManager.setUsername(userNameEditText.getText().toString()));
//                    StringRequest strReq = new StringRequest(Request.Method.GET,
//                            unPickUrl, new Response.Listener<String>() {
//                        //            APIConfig.URL_LOGIN + userNameEditText.getText().toString()+ passWordEditText.getText().toString()
//                        @Override
//                        public void onResponse(String response) {
//
//                            ProgressBar.dismissProgress();
//
//                            try {
////                                JSONObject jObj = new JSONObject(response);
////                                String status = jObj.getString("status");
////
////                                // Check for error node in json
////                                if (status.equals("1")) {
////                        if(sharedPreferences.contains("username")) {
////                            Intent intent = new Intent(Login_Activity.this, DefaultActivity.class);
////                            startActivity(intent);
////                            finish();
//
//                                    Toast.makeText(activity, "Items are Unpicked", Toast.LENGTH_LONG).show();
////                        }
////                                } else {
////                                    // Error in login. Get the error message
////                                    Toast.makeText(activity, "Not picked", Toast.LENGTH_LONG).show();
////                                }
//                            } catch (Exception e) {
//                                // JSON error
//                                e.printStackTrace();
//
//                                Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                           System.out.println(e.getMessage());
//                            }
//
//                        }
//                    }, new Response.ErrorListener() {
//
////            @Override
////            public void onErrorResponse(VolleyError error) {
////
////                Toast.makeText(getApplicationContext(),
////                        error.getMessage(), Toast.LENGTH_LONG).show();
////                ProgressBar.dismissProgress();
////            }
//
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            if (error == null || error.networkResponse == null) {
//                                return;
//                            }
//
//                            //get status code here
//                            final String statusCode = String.valueOf(error.networkResponse.statusCode);
//                            //get response body and parse with appropriate encoding
//                            try {
//                                body = new String(error.networkResponse.data, "UTF-8");
//                            } catch (UnsupportedEncodingException e) {
//                                // exception
//                            }
//                        }
//
//                    }) {
//
//                        @Override
//                        protected Map<String, String> getParams() {
//                            // Posting parameters to login url
//                            Map<String, String> params = new HashMap<String, String>();
////                params.put("userName", username);
//////                preferenceManager.setUsername(username);
////                params.put("passWord", password);
//
//                            return params;
//                        }
//
//                    };
//
//                    ApiController.getInstance().addToRequestQueue(strReq, tag_string_req);
//
//
//                    constraintLayout.setBackgroundColor(Color.parseColor("#ffffff"));
//                    holder.pickButtonPickList.setTextOff("Pick");
//
//                }
//            }
//        });
//
        return view;
    }


}



