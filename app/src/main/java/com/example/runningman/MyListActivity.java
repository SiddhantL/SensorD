package com.example.runningman;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyListActivity extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] maintitle;

    public MyListActivity(Activity context, String[] maintitle) {
        super(context, R.layout.devices_list, maintitle);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.maintitle=maintitle;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.devices_list, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.text1);
        RelativeLayout lays= rowView.findViewById(R.id.layouts);
        titleText.setText(maintitle[position]);
        if((position+1)%2==0){
lays.setBackgroundColor(Color.RED);
titleText.setTextColor(Color.WHITE);
        }else{
            lays.setBackgroundColor(Color.WHITE);
            titleText.setTextColor(Color.RED);
        }
        return rowView;

    };
}
