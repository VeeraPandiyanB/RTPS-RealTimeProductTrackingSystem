package com.example.integratedapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PickListAdapter extends BaseAdapter {

    ArrayList<PickListModel> pickListModel;
    String pickListId, pickListName, pickListDate;
    Activity activity;
    LayoutInflater layoutInflater;
//    PickListModel pickListModels;


    public PickListAdapter(Activity activity, ArrayList<PickListModel> pickListModel) {
        this.activity = activity;
        this.pickListModel = pickListModel;
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return pickListModel.size();
    }

    @Override
    public Object getItem(int i) {
        return pickListModel.get(i);
    }

    @Override
    public long getItemId(int i) {
        return (i);
    }

    private class ViewHolder {
        TextView id, name, date;
        Button viewButton;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {


        final ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (view == null) {
            view = inflater.inflate(R.layout.pick_list, null);
            holder = new ViewHolder();
//            holder.id = view.findViewById(R.id.pickListIDTextView);
            holder.name = view.findViewById(R.id.pickListNameTextView);
            holder.date = view.findViewById(R.id.pickListDateTextView);
            holder.viewButton = view.findViewById(R.id.pickListViewButton);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final PickListModel pickListModels = (PickListModel) getItem(i);
//        holder.id.setText(pickListModels.getPickListID());
        System.out.println(pickListModels.getPickListID());
        holder.name.setText(pickListModels.getPickListName());
        holder.date.setText(pickListModels.getPickListDate());


        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getItemId(i);
//                System.out.println(getItem(i));
                getItem(i);
//                System.out.println(getItem(i));

                Intent intent = new Intent(activity, LineItemsActivity.class);
                intent.putExtra("Item_ID", pickListModels.getPickListID());
                activity.startActivity(intent);


            }
        });


        return view;
    }
}