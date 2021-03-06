package com.example.pr_idi.mydatabaseexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class HelpActivity extends AppCompatActivity{
    private Toolbar toolbar;
    private ListView listHelp;
    private ArrayAdapter<String> adapter;
    private String[] content ={"Create new books", "Edit books", "Delete books", "Search for books", "Sort the list"};
    private Integer[] imgid={ R.drawable.ic_create, R.drawable.ic_edit_help,
            R.drawable.ic_delete_help,R.drawable.ic_search_help,R.drawable.ic_sort_help};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        listHelp = (ListView) findViewById(R.id.listHelp);
        CustomAdapter adapter = new CustomAdapter(this, content, imgid);
        listHelp.setAdapter(adapter);

        toolbar = (Toolbar) findViewById(R.id.tbar);
        toolbar.setTitle(R.string.help2);
        TextView count_text = (TextView) findViewById(R.id.counter_text);
        count_text.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listHelp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String itemSelected = (String) adapterView.getItemAtPosition(i);
                switch (itemSelected){
                    case "Create new books":
                        Intent i1 = new Intent(HelpActivity.this, Create.class);
                        startActivity(i1);
                        break;
                    case "Edit books":
                        Intent i2 = new Intent(HelpActivity.this, Edit.class);
                        startActivity(i2);
                        break;
                    case "Delete books":
                        Intent i3 = new Intent(HelpActivity.this, Delete.class);
                        startActivity(i3);
                        break;
                    case "Search books":
                        Intent i4 = new Intent(HelpActivity.this, Search.class);
                        startActivity(i4);
                        break;
                    case "Sort the list":
                        Intent i5 = new Intent(HelpActivity.this, Sort.class);
                        startActivity(i5);
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}