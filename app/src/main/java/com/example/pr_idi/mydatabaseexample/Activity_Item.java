package com.example.pr_idi.mydatabaseexample;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

/*Mostra un elem del recycler view resultat d'haver fet clic (sense permisos de modificació)*/
public class Activity_Item extends AppCompatActivity {
    private TextView t, autor, publisher, year, category;
    private RatingBar stars;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_item);
        t = (TextView) findViewById(R.id.titol);
        autor = (TextView) findViewById(R.id.autor);
        publisher = (TextView) findViewById(R.id.pub);
        year = (TextView) findViewById(R.id.year);
        category = (TextView) findViewById(R.id.cat);
        stars = (RatingBar) findViewById(R.id.ratingBar);
        //stars.setEnabled(false); //not editable

        toolbar = (Toolbar) findViewById(R.id.tbar);
        toolbar.setTitle(R.string.view);
        TextView count_text = (TextView) findViewById(R.id.counter_text);
        count_text.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            t.setText(extras.getString("mtitol"));
            autor.setText(extras.getString("mautor"));
            publisher.setText(extras.getString("mpublisher"));

            int ye = extras.getInt("myear");
            year.setText(String.valueOf(ye));

            category.setText(extras.getString("mcategory"));
            String val = extras.getString("mval");

            switch (val){
                case "molt bo":
                    stars.setRating(5.0f);
                    break;
                case "bo":
                    stars.setRating(4.0f);
                    break;
                case "regular":
                    stars.setRating(3.0f);
                    break;
                case "dolent":
                    stars.setRating(2.0f);
                    break;
                case "molt dolent":
                    stars.setRating(1.0f);
                    break;
                default:
                    stars.setRating(0.0f);
                    break;
            }
        }
    }
}
