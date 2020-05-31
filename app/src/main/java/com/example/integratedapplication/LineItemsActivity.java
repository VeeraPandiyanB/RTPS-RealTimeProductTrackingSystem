package com.example.integratedapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LineItemsActivity extends AppCompatActivity {

    ListView linelistDetails;
    TextView barCodeTextPick,rfidTextPick;
    ImageView coordinatesImageViewPick;
    ToggleButton pickButton;
    private ArrayList<LineItemsModel> lineItemListModel;
    LineItemsAdapter lineItemsAdapter;
    String body;
    ToggleButton pButton;
    LineItemsModel lineItemsModel;
    PickListModel pickListModel;
    String itemId,lineItemId;
    ConstraintLayout constraintLayout;
    private static final String ITEM_IS_Checked = "ItemIsChecked";
    SharedPreferences sharedPreferences;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_items);

        linelistDetails = findViewById(R.id.lineItemsListView);
        barCodeTextPick = findViewById(R.id.barCodePickListTextView);
        rfidTextPick = findViewById(R.id.rfIdPickListTextView);
        coordinatesImageViewPick = findViewById(R.id.locationCoordinatesImageViewPickList);
        pickButton = findViewById(R.id.pickButton);
        constraintLayout = findViewById(R.id.pickListConstraintLayout);
        sharedPreferences=getApplicationContext().getSharedPreferences("ItemIsChecked", Context.MODE_PRIVATE);


        itemId = getIntent().getExtras().getString("Item_ID");

//        System.out.println(itemId);


        pickListDetails();


        lineItemListModel = new ArrayList<LineItemsModel>();

        lineItemsAdapter = new LineItemsAdapter(LineItemsActivity.this, lineItemListModel);

//                ListViewAdapter adapter = new ListViewAdapter(this, list);
        linelistDetails.setAdapter(lineItemsAdapter);

    }

    private void pickListDetails() {



            String tag_string_req = "GetPickListDetails";
            final String pickListUrl="http://lntifr.azurewebsites.net/api/LANDTApi/GetPickBarcodeDetails?PickListId="+itemId;
            System.out.println(pickListUrl);

//            System.out.println(pickListUrl);
            StringRequest strReq = new StringRequest(Request.Method.GET,pickListUrl
                    , new Response.Listener<String>() {




                @Override
                public void onResponse(String response) {

                    ProgressBar.dismissProgress();
//                JSONArray jsonArray = new JSONArray(stringIn);
//
//                JSONObject obj = jsonArray.getJSONObject(0);
                    try {
//                    JSONObject jObj = new JSONObject(response);
//                    JSONArray busDetails = jObj.getJSONArray("busdetails");

                        JSONArray jsonArray=new JSONArray(response);

                        // Check for error node in json
                        if (!jsonArray.isNull(0)) {
//                        if (jobj.length()>0){
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jObject = jsonArray.getJSONObject(i);
                                String barcodeId = jObject.optString("BarcodeId");
                                String GPSLocation = jObject.optString("GPSLocation");
                                String rfId=jObject.optString("RFIDEpc");
                                Boolean isPicked=jObject.optBoolean("IsPicked");

                                LineItemsModel details = new LineItemsModel(barcodeId,GPSLocation,rfId,isPicked);
                                lineItemListModel.add(details);
                                lineItemsAdapter.notifyDataSetChanged();
                                System.out.println(lineItemsAdapter.getItemId(i));
//                                final ConstraintLayout cLayout=(ConstraintLayout)findViewById(R.id.pickListConstraintLayout);
//                                ToggleButton pButton=(ToggleButton)findViewById(R.id.pickButton);

//
                                if (isPicked.equals(true)){
                                    lineItemsAdapter.getItemId(i);
                                    System.out.println(lineItemsAdapter.getItemId(i));

                                    lineItemId = getIntent().getExtras().getString("LineItem_ID");
//                                    pButton.setChecked(lineItemListModel.get(i).isPicked);
////                                    pButton=(ToggleButton)findViewById(R.id.pickButton);
//////                                    cLayout.setBackgroundResource(R.color.babyblue);
////
////                                    (pButton).setTextOff(getText(R.string.unp));
////                                    pButton.requestLayout();
                                }
//                                Toast.makeText(getApplicationContext(),"working",Toast.LENGTH_LONG).show();
                            }

                        } else {

                            Toast.makeText(getApplicationContext(),"Something Went Wrong", Toast.LENGTH_LONG).show();

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
//                params.put("PickListId", pickListId);
//                params.put("end",end);
//                params.put("date",date);
                    return params;
                }

            };

            ApiController.getInstance().addToRequestQueue(strReq, tag_string_req);

        }


}
