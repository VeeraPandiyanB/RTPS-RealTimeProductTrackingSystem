package com.example.integratedapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;



public class ListViewAdapter extends BaseAdapter implements Filterable {
    public ArrayList<ListModel> list;
    Activity activity;
    Constants constants;
    ListModel listModel;
    Address address;
    ListView listView;
    DetailListParcable detailListParcable;
    TextView coor;
    ArrayList<ListModel> searchList;
    ValueFilter valueFilter;
    String barcode, gps, rfid,compass;
    LayoutInflater layoutInflater;


    public ListViewAdapter(Activity activity, ArrayList<ListModel> list) {
        super();
        this.activity = activity;
        this.list = list;

        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        searchList = list;
        getFilter();

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);

    }

    @Override
    public long getItemId(int i) {
        return (i);

    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {

            valueFilter = new ValueFilter();
        }

        return valueFilter;
    }

    private class ValueFilter extends Filter {

        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<ListModel> list = new ArrayList<>();
                for (int i = 0; i < searchList.size(); i++) {
                    if ((searchList.get(i).getBarcode())
                            .contains(constraint)) {
                        ListModel listModel = new ListModel(barcode, gps, rfid,compass);
                        listModel.setBarcode(searchList.get(i).getBarcode());
                        listModel.setGpsLocation(searchList.get(i).getGpsLocation());
                        listModel.setRfid(searchList.get(i).getRfid());
                        list.add(listModel);
                    }
                }
                results.count = list.size();
                results.values = list;
            } else {
                results.count = searchList.size();
                results.values = searchList;
            }
            return results;
        }


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            list = (ArrayList<ListModel>) results.values;
            notifyDataSetChanged();
        }
    }


    private class ViewHolder {
        TextView barCodeText, compassValueTextView,locateTextView;
//        ImageView coordinatesImageView;


    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (view == null) {
            view = inflater.inflate(R.layout.list, null);
            holder = new ViewHolder();
            holder.barCodeText = view.findViewById(R.id.barCodeListTextView);
            holder.compassValueTextView = view.findViewById(R.id.comapssValueTextView);
//            holder.rfidText = view.findViewById(R.id.rfIdListTextView);
            holder.locateTextView = view.findViewById(R.id.locationCoordinatesTextView);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
//        Constants constants= (Constants) getItem(i);
//        holder.barCodeText.setText(constants.getBarCode());
//        holder.coordinatesText.setText(constants.getCoordinates());

//        DetailListParcable detailListParcable= (DetailListParcable) getItem(i);
        final ListModel listModel = (ListModel) getItem(i);
        holder.barCodeText.setText(listModel.getBarcode());
        holder.compassValueTextView.setText(listModel.getCompassValue());
//        holder.rfidText.setText(listModel.getRfid());
//        holder.coordinatesImageView.setText(listModel.getGpsLocation());

        holder.locateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getItemId(i);
//                System.out.println(getItemId(i));
                getItem(i);
//                System.out.println(getItem(i));
                {
//                    String map = "http://maps.google.co.in/maps/places?q="+listModel.getGpsLocation();

                    String map = "http://maps.google.co.in/maps/place?q=" + listModel.getGpsLocation() + "(" + listModel.getBarcode() + ")";

                    System.out.println(map);
                    System.out.println(map);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                    mapIntent.putExtra("Barcode", listModel.getBarcode());
                    activity.startActivity(mapIntent);
                }
            }
        });

        return view;
    }



}