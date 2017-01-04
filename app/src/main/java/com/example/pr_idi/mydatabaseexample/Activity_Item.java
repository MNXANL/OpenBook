package com.example.pr_idi.mydatabaseexample;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RatingBar;
import android.widget.TextView;

/*Mostra un elem del recycler view resultat d'haver fet clic (sense permisos de modificació)*/
public class Activity_Item extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView t, autor, publisher, year, category;
        RatingBar val;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_item);
        t = (TextView) findViewById(R.id.titol);
        autor = (TextView) findViewById(R.id.autor);
        publisher = (TextView) findViewById(R.id.pub);
        year = (TextView) findViewById(R.id.year);
        category = (TextView) findViewById(R.id.cat);
        val = (RatingBar) findViewById(R.id.ratingBar);
        //val.setEnabled(false); //not editable

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            t.setText(extras.getString("mtitol"));
            autor.setText(extras.getString("mautor"));
            publisher.setText(extras.getString("mpublisher"));
            year.setText(extras.getString("myear"));
            category.setText(extras.getString("mcategory"));
            val.setRating(extras.getFloat("mval"));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //botó undo
    }
}
