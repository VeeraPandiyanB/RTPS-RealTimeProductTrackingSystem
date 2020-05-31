package com.example.integratedapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.protocol.RequestUserAgent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PickListActivity extends AppCompatActivity {

    ListView pickListViewDetails;
    TextView pickListName,pickListDate;
    Button viewButton;
    private ArrayList<PickListModel> pickListModels;
    PickListAdapter pickListAdapter;
    SharedPreferences sharedPreferences;
    String body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_list);

        pickListViewDetails=findViewById(R.id.pickListView);
//        pickListId=findViewById(R.id.pickListIDTextView);
        pickListName=findViewById(R.id.pickListNameTextView);
        pickListDate=findViewById(R.id.pickListDateTextView);
        viewButton=findViewById(R.id.pickListViewButton);

        sharedPreferences=getSharedPreferences("user_name",MODE_PRIVATE);

        pickListUserNameDetails();

        pickListModels= new ArrayList<PickListModel>();

        pickListAdapter = new PickListAdapter(PickListActivity.this,pickListModels);

//                ListViewAdapter adapter = new ListViewAdapter(this, list);
        pickListViewDetails.setAdapter(pickListAdapter);
//        pickListViewDetails.notifyAll();
        pickListViewDetails.invalidateViews();
//        pickListAdapter.notifyDataSetInvalidated();

//        viewButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                pickListUserNameDetails();
//            }
//        });


    }

    private void pickListUserNameDetails() {

        String tag_string_req = "GetPickListDetails";

//        StringRequest strReq = new StringRequest(Request.Method.GET,
//                APIConfig.PICK_LIST_DETAILS, new Response.Listener<String>() {
        System.out.println("RequestUserAgent **********************************************= " + sharedPreferences.getString("username",null));
        StringRequest strReq = new StringRequest(Request.Method.GET,
                " http://lntifr.azurewebsites.net/api/LANDTApi/GetPickListDetails?userName="+sharedPreferences.getString("username",null), new Response.Listener<String>() {
        @Override
            public void onResponse(String response) {
            System.out.println("response *****************************= " + response);
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
                            String pickListId = jObject.optString("PickListId");
                            String pickListName = jObject.optString("PickListName");
                            String assignOn=jObject.optString("AssignOn");

                            PickListModel details = new PickListModel(pickListId,pickListName,assignOn);
                            pickListModels.add(details);
                            pickListAdapter.notifyDataSetChanged();
//                            pickListViewDetails.notifyAll();
                            pickListViewDetails.invalidateViews();
//                            pickListAdapter.notifyDataSetInvalidated();



//                            Toast.makeText(getApplicationContext(),"working",Toast.LENGTH_LONG).show();
                        }
                        System.out.println("pickListModels ************************************= " + pickListModels);

                    } else {

                        Toast.makeText(getApplicationContext(),"Something Went Wrong", Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    // JSON error
                    e.printStackTrace();
                    System.out.println(e);
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
//                params.put("userName",sharedPreferences.getString("username",null));

//                System.out.println(preferenceManager.getUsername());
//                params.put("end",end);
//                params.put("date",date);
                return params;
            }

        };

        ApiController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
