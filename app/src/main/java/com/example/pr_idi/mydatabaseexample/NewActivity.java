package com.example.pr_idi.mydatabaseexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

public class NewActivity extends AppCompatActivity {
    private EditText titol = null;
    private EditText autor = null;
    private EditText publisher = null;
    private EditText any = null;
    private Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    private RatingBar star = null;
    private Toolbar toolbar;
    private String categoria;

    private String titolantic;
    private String autorantic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_item);
        titol = (EditText) findViewById(R.id.ntitol);
        autor = (EditText) findViewById(R.id.nautor);
        publisher = (EditText) findViewById(R.id.npub);
        any = (EditText) findViewById(R.id.nyear);
        star = (RatingBar) findViewById(R.id.ratingBar);
        toolbar = (Toolbar) findViewById(R.id.tbar);
        setSupportActionBar(toolbar);
        setTitle("New Activity");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //botó undo
        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            titolantic = extras.getString("mtitol");
            autorantic = extras.getString("mautor");
            titol.setText(extras.getString("mtitol"));
            autor.setText(extras.getString("mautor"));
            titol.setText(extras.getString("mtitol"));
            autor.setText(extras.getString("mautor"));
            publisher.setText(extras.getString("mpublisher"));

            int ye = extras.getInt("myear");
            any.setText(String.valueOf(ye));

            spinner.setSelection(adapter.getPosition(extras.getString("mcategory")));
            String val = extras.getString("mval");

            switch (val){
                case "molt bo":
                    star.setRating(5.0f);                    break;
                case "bo":
                    star.setRating(4.0f);                    break;
                case "regular":
                    star.setRating(3.0f);                    break;
                case "dolent":
                    star.setRating(2.0f);                    break;
                case "molt dolent":
                    star.setRating(1.0f);                    break;
                default:
                    star.setRating(0.0f);                    break;
            }
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Toast.makeText(getBaseContext(),parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                categoria = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ConfirmNewButton: //si cliquem a categoria ens canviem d'activitat
                Book b = new Book();
                b.setTitle(titol.getText().toString());
                b.setAuthor(autor.getText().toString());

                b.setYear(Integer.parseInt(any.getText().toString()));

                b.setPublisher(publisher.getText().toString());
                b.setCategory(categoria);
                float num = star.getRating();
                int enter = Math.round(num); //ho passem al enter mes proper
                switch (enter){
                    case 5:
                        b.setPersonal_evaluation("molt bo");
                        break;
                    case 4:
                        b.setPersonal_evaluation("bo");
                        break;
                    case 3:
                        b.setPersonal_evaluation("regular");
                        break;
                    case 2:
                        b.setPersonal_evaluation("dolent");
                        break;
                    case 1:
                        b.setPersonal_evaluation("molt dolent");
                        break;
                }

                //By this point everything should have been created... or not.
                if(titol.getText() == null || autor.getText() == null || publisher.getText() == null || any.getText() == null || star.getRating() == 0.0f){
                    Toast.makeText(getBaseContext(),"No hi poden haver camps buits" , Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent i = getIntent();
                    i.putExtra("titolantic", titolantic);
                    i.putExtra("autorantic",autorantic);
                    i.putExtra("titol", b.getTitle());
                    i.putExtra("autor", b.getAuthor());

                    int any = b.getYear();
                    String any2 = String.valueOf(any);
                    i.putExtra("any", any2);

                    //i.putExtra("any", b.getYear());
                    i.putExtra("categoria", b.getCategory());
                    i.putExtra("editorial", b.getPublisher());
                    i.putExtra("valoracio", b.getPersonal_evaluation());
                    setResult(RESULT_OK,i);
                    finish();

                    Toast.makeText(getApplicationContext(), "Libro in", Toast.LENGTH_SHORT).show();
                    //startActivity(i);
                }
        }
    }
}