package com.example.pr_idi.mydatabaseexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class NewActivity extends AppCompatActivity {
    private EditText titol = null;
    private EditText autor = null;
    private EditText publisher = null;
    private EditText any = null;
    private EditText categoria = null;
    private RatingBar star = null;
    private Toolbar toolbar;

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


        toolbar = (Toolbar) findViewById(R.id.tbar);
        setTitle("MyBookDB | New Book"); //Esto por alguna razón no pasa
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //botó undo


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
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ncat: //si cliquem a categoria ens canviem d'activitat
                Intent categories = new Intent(NewActivity.this, llista_categoria.class);
                startActivity(categories);
                break;
            /*case R.id.NewBookButton: //Pulsar botón para añadir el libro a la BD
                addBooks();
                break;*/
        }
    }

    private void addBooks() {

        Toast.makeText(getApplicationContext(), "The book <" + titol + "> has been added." ,Toast.LENGTH_SHORT);
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
