package com.example.pr_idi.mydatabaseexample;


import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class MainActivity extends ListActivity {
    private BookData bookData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        bookData = new BookData(this);
        bookData.open();

        List<Book> values = bookData.getAllBooks();

        // use the SimpleCursorAdapter to show the elements in a ListView
        ArrayAdapter<Book> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    // Basic method to add pseudo-random list of books so that you have an example of insertion and deletion
    // Will be called via the onClick attribute of the buttons in main.xml
    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<Book> adapter = (ArrayAdapter<Book>) getListAdapter();
        Book book;
        switch (view.getId()) {
            case R.id.add:
                String[] newBook = new String[] { "Miguel Strogoff", "Jules Verne", "Ulysses", "James Joyce", "Don Quijote", "Miguel de Cervantes", "Metamorphosis", "Kafka" };
                int nextInt = new Random().nextInt(4);
                // save the new book to the database
                book = bookData.createBook(newBook[nextInt*2], newBook[nextInt*2 + 1]);

                // After I get the book data, I add it to the adapter
                adapter.add(book);
                break;
            case R.id.delete:
                if (getListAdapter().getCount() > 0) {
                    book = (Book) getListAdapter().getItem(0);
                    bookData.deleteBook(book);
                    adapter.remove(book);
                }
                break;
            case R.id.help:
                //Aqui deberiamos crear alguna Activity aparte que solamente sea un Wall of Text (WoT)
                Toast.makeText(MainActivity.this, "Halluda no se cmo fnsiona la pinxe aplicasion esta wey", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about:
                //IT JUST WORKS!
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.about_content).setTitle(R.string.about_title);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.button:
                //Deberia mover este codigo Bethesda y ponerlo en la toolbar (que ni siquiera está)
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);

                break;
        }
       adapter.notifyDataSetChanged();
    }


    // Life cycle methods. Check whether it is necessary to reimplement them
    @Override
    protected void onResume() {
        bookData.open();
        super.onResume();
    }


    // Life cycle methods. Check whether it is necessary to reimplement them
    @Override
    protected void onPause() {
        bookData.close();
        Toast.makeText(MainActivity.this, "Pausing...", Toast.LENGTH_LONG).show();
        super.onPause();
    }

}