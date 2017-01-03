package com.example.pr_idi.mydatabaseexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class llista_categoria extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llista_categoria);
        final ArrayList<String> categories = new ArrayList<>();
        categories.add("Acció");
        categories.add("Aventura");
        categories.add("Autoajuda");
        categories.add("Ciència ficció");
        categories.add("Clàssic");
        categories.add("Fantasia");
        categories.add("Humor");
        categories.add("Misteri");
        categories.add("Novel·la biogràfica");
        categories.add("Novel·la gràfica");
        categories.add("Novel·la històrica");
        categories.add("Novel·la negra");
        categories.add("Novel·la policíaca i terror");
        categories.add("Poesia");
        categories.add("Paranormal");
        categories.add("Romanç");
        categories.add("Young adult");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //botó undo

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, categories);

        final ListView milista = (ListView) findViewById(R.id.milista);
        milista.setAdapter(arrayAdapter);
        milista.setChoiceMode(milista.CHOICE_MODE_SINGLE);
        milista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position); //categoria sel·leccionada
                //cal afegir un boto guardar que al clicar tornem a la activitat "NewActivity"
            }
        });
        arrayAdapter.notifyDataSetChanged();

    }
}
