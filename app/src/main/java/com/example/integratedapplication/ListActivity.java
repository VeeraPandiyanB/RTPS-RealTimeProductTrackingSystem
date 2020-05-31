package com.example.integratedapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ListActivity extends AppCompatActivity {

    ListView listViewDetails;
    TextView barCodeText,rfidText;
    ImageView coordinatesImageView;
    private ArrayList<ListModel> list;
    ListViewAdapter listViewAdapter;
    EditText search;
    String body;
    ProgressBar progressBar;
    String coordinates;
    Parcelable detailObject;
    DetailListParcable detailListParcable;
    ListModel listModel;
    TextView locate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        search=findViewById(R.id.searchEditText);
//        Bundle bundle=getIntent().getExtras();
//        if (bundle != null) {
//            detailObject=bundle.getParcelable("detail");
//        }

//        detailListParcable=getIntent().getParcelableExtra("detail");

        listViewDetails=findViewById(R.id.detailsListView);

//        rfidText=findViewById(R.id.rfIdListTextView);
        barCodeText=findViewById(R.id.barCodeListTextView);
        locate=findViewById(R.id.locationCoordinatesTextView);
//        coordinatesImageView=findViewById(R.id.locationCoordinatesImageView);
        System.out.println(coordinatesImageView);
        mappingDetails();
        list= new ArrayList<ListModel>();

        listViewAdapter = new ListViewAdapter(ListActivity.this, list);

//                ListViewAdapter adapter = new ListViewAdapter(this, list);
        listViewDetails.setAdapter(listViewAdapter);
//        coordinates=coordinatesText.getText().toString();
//        listViewDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            listViewDetails.getItemAtPosition(i);
//                {
//
//                    String coo= String.valueOf(listViewAdapter.getItem(Integer.parseInt((listModel.getGpsLocation()))));
//                    String map = "http://maps.google.co.in/maps?q=" + coo;
//                    System.out.println(map);
//                    System.out.println(map);
//                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
//                    startActivity(mapIntent);
//                }
//            }
//        });

      search.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
              listViewAdapter.getFilter().filter(charSequence);
          }

          @Override
          public void afterTextChanged(Editable editable) {

          }
      });

    }

    private void mappingDetails() {

        String tag_string_req = "GetMappingDetails";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                APIConfig.MAPPING_DETAILS, new Response.Listener<String>() {

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
                            String compassValueList=jObject.optString("BarcodeCompass");


                            ListModel details = new ListModel(barcodeId,GPSLocation,rfId,compassValueList);
                            list.add(details);
                            listViewAdapter.notifyDataSetChanged();

//                            Toast.makeText(getApplicationContext(),"working",Toast.LENGTH_LONG).show();
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
//                params.put("start", start);
//                params.put("end",end);
//                params.put("date",date);
                return params;
            }

        };

        ApiController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }



}
