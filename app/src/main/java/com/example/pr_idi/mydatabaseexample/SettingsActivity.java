package com.example.pr_idi.mydatabaseexample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Spinner spinner_search;
    private Spinner spinner_sort;
    private ArrayAdapter<CharSequence> adapter_search;
    private ArrayAdapter<CharSequence> adapter_sort;
    private Button save;
    int cercaTitol;
    String ordre;
    //boolean defBooks ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getPreferences();
        save = (Button) findViewById(R.id.ConfigurationButton);
        toolbar = (Toolbar) findViewById(R.id.tbar);
        toolbar.setTitle(R.string.settings);

            Bundle extras = getIntent().getExtras();
            cercaTitol = extras.getInt("CERCATITOL");
            ordre = extras.getString("ORDRE");

        TextView count_text = (TextView) findViewById(R.id.counter_text);
        count_text.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner_search = (Spinner) findViewById(R.id.spinner_search);
        spinner_sort =(Spinner) findViewById(R.id.spinner_sort);

        adapter_search = ArrayAdapter.createFromResource(this, R.array.searchOptions, android.R.layout.simple_spinner_item);
        adapter_search.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_search.setAdapter(adapter_search);

        adapter_sort = ArrayAdapter.createFromResource(this, R.array.orderOptions, android.R.layout.simple_spinner_item);
        adapter_sort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sort.setAdapter(adapter_sort);

        //Fiquem el valor actual
        if(cercaTitol == 1) spinner_search.setSelection(adapter_search.getPosition("Title"));
        else spinner_search.setSelection(adapter_search.getPosition("Author"));

        switch (ordre){
            case "titol":
                spinner_sort.setSelection(adapter_sort.getPosition("Title"));
                break;
            case "autor":
                spinner_sort.setSelection(adapter_sort.getPosition("Author"));
                break;
            case "any" :
                spinner_sort.setSelection(adapter_sort.getPosition("Publishing Year"));
                break;
            case "categoria":
                spinner_sort.setSelection(adapter_sort.getPosition("Category"));
                break;
            case "valoracio":
                spinner_sort.setSelection(adapter_sort.getPosition("Rating"));
                break;
        }


        //listener spinner search
        spinner_search.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                //Toast.makeText(getBaseContext(),parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                String c = parent.getItemAtPosition(position).toString();
                if(c == "Title") cercaTitol = 1;
                else cercaTitol = 0;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //listener spinner sort
        spinner_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                //Toast.makeText(getBaseContext(),parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                ordre = parent.getItemAtPosition(position).toString().toLowerCase();
                translateOrder();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void translateOrder() {
        switch (ordre){
            case "title": ordre="titol"; break;
            case "author": ordre="autor"; break;
            case "publishing year": ordre="any"; break;
            case "category": ordre="categoria"; break;
            case "rating": ordre="valoracio"; break;
            case "Title": ordre="titol"; break;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ConfigurationButton: //si cliquem a categoria ens canviem d'activitat

                savePreferences();
                Intent i = getIntent();
                String cercaTitle = String.valueOf(cercaTitol);
                i.putExtra("setTitle", cercaTitle);
                i.putExtra("setOrder", ordre);
                //i.putExtra("setBook", defBooks);
                setResult(RESULT_OK, i);
                finish();
                break;

        }
    }

    private void getPreferences() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        cercaTitol = SP.getInt(String.valueOf(R.string.settingSearch), cercaTitol);
        ordre = SP.getString(String.valueOf(R.string.settingOrder), ordre);
        //defBooks = SP.getBoolean(String.valueOf(R.string.DefBooks), true);
    }
    private void savePreferences() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editPref = SP.edit();
        editPref.putInt(getString(R.string.settingSearch), cercaTitol);
        editPref.putString(getString(R.string.settingOrder), ordre);
        //editPref.putBoolean(getString(R.string.DefBooks), defBooks);
        editPref.apply();
    }
}
