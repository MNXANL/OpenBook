package com.example.pr_idi.mydatabaseexample;


import android.app.ListActivity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

public class Activity_Item extends ListActivity {
    private TextView t, autor, publisher, year, category;
    private RatingBar val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__item);
        t = (TextView) findViewById(R.id.titol);
        autor = (TextView) findViewById(R.id.autor);
        publisher = (TextView) findViewById(R.id.pub);
        year = (TextView) findViewById(R.id.year);
        category = (TextView) findViewById(R.id.cat);
        val = (RatingBar) findViewById(R.id.ratingBar);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            t.setText(extras.getString("mtitol"));
            autor.setText(extras.getString("mautor"));
            publisher.setText(extras.getString("mpublisher"));
            year.setText(extras.getString("myear"));
            category.setText(extras.getString("mcategory"));
            val.setRating(extras.getFloat("mval"));
        }
    }
}
