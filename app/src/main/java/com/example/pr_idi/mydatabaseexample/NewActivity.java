package com.example.pr_idi.mydatabaseexample;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

public class NewActivity extends AppCompatActivity {
    private EditText titol = null;
    private EditText autor = null;
    private EditText publisher = null;
    private EditText any = null;
    private EditText categoria = null;
    private RatingBar star = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        titol = (EditText) findViewById(R.id.ntitol);
        autor = (EditText) findViewById(R.id.nautor);
        publisher = (EditText) findViewById(R.id.npub);
        any = (EditText) findViewById(R.id.nyear);
        categoria = (EditText) findViewById(R.id.ncat);
        star = (RatingBar) findViewById(R.id.ratingBar);
        /*ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.activity_new_item, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);*/

        //si tornem d'una altra activitat restaurem l'estat
        if(savedInstanceState != null){
            String t = savedInstanceState.getString("titol");
            titol.setText(t);
            String a = savedInstanceState.getString("autor");
            autor.setText(a);
            String p = savedInstanceState.getString("publisher");
            publisher.setText(p);
            String an = savedInstanceState.getString("any");
            any.setText(an);
            String c = savedInstanceState.getString("categoria");
            categoria.setText(c);
            Float f = savedInstanceState.getFloat("star");
            star.setRating(f);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //botó undo

    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ncat: //si cliquem a categoria ens canviem d'activitat
                Intent categories = new Intent(NewActivity.this, llista_categoria.class);
                break;
        }
    }
    @Override   //guardem l'estat dels altres camps emplenats
    protected void onSaveInstanceState(Bundle guardarEstado) {
        super.onSaveInstanceState(guardarEstado);
        guardarEstado.putString("titol", titol.toString());
        guardarEstado.putString("autor", autor.toString());
        guardarEstado.putString("publisher", publisher.toString());
        guardarEstado.putString("any", any.toString());
        guardarEstado.putString("categoria", categoria.toString());
        guardarEstado.putFloat("star", star.getRating());
    }
    @Override  //restaurem l'estat
    protected void onRestoreInstanceState(Bundle recEstado) {
        super.onRestoreInstanceState(recEstado);
        String t = recEstado.getString("titol");
        titol.setText(t);
        String a = recEstado.getString("autor");
        autor.setText(a);
        String p = recEstado.getString("publisher");
        publisher.setText(p);
        String an = recEstado.getString("any");
        any.setText(an);
        String c = recEstado.getString("categoria");
        categoria.setText(c);
        Float f = recEstado.getFloat("star");
        star.setRating(f);
    }
}
