package com.example.pr_idi.mydatabaseexample;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by marta on 6/01/17.
 */

public class CustomAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;
    public CustomAdapter(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.help_item_list, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.help_item_list, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.helpText);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.helpicon);

        txtTitle.setText(itemname[position]);
        imageView.setImageResource(imgid[position]);
        return rowView;
    };
}
