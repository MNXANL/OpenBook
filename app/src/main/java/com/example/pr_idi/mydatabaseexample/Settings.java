package com.example.pr_idi.mydatabaseexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class Settings extends AppCompatActivity {
    int cercaTitol = 1;
    String ordre = "titol";
    boolean defBooks ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
        }
    }

    public void onBackPressed(){
        Intent i = getIntent();
        i.putExtra("setTitle", cercaTitol);
        i.putExtra("setOrder", ordre);
        i.putExtra("setBook", defBooks);
        setResult(RESULT_OK, i);
        finish();
    }

}