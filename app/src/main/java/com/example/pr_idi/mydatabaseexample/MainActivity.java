package com.example.pr_idi.mydatabaseexample;


import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class MainActivity extends ListActivity {
    private BookData bookData;
    private RatingBar ratingBar;
   /* private RecyclerView mrecView;
    private ItemAdapter adapter;*/

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


        /* To do:  Aqui dejo lo que has hecho con tu onCreate

         ************************

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        //recycle view
        mrecView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mrecView.setLayoutManager(new LinearLayoutManager(this));

        //creo la base de dades i omplo l'array amb tots els llibres
        bookData = new BookData(this);
        bookData.open();
        List<Book> values = bookData.getAllBooks();

        //creo l'adapter
        adapter = new ItemAdapter(values);
        mrecView.setAdapter(adapter);
        mrecView.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnItemClickListener(new com.example.pr_idi.mydatabaseexample2.OnItemClickListener() {
            @Override
            public void onItemClick(Book item) {
                Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_LONG).show();
                Book book = (Book) getListAdapter().getItem(mrecView.getChildAdapterPosition());
                Intent i = new Intent(MainActivity.this, Activity_Item.class);
                i.putExtra("mtitol", book.getTitle());
                i.putExtra("mautor", book.getAuthor());
                i.putExtra("myear", book.getYear());
                i.putExtra("mpublisher", book.getPublisher());
                i.putExtra("mcategory", book.getCategory());
                i.putExtra("mval", book.getPersonal_evaluation());
                startActivity(i);
            }
        });
        mrecView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        // use the SimpleCursorAdapter to show the
        // elements in a ListView


        ArrayAdapter<Book> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mrecView.setLayoutManager(MainActivity.this);
        mrecView.setItemAnimator(new DefaultItemAnimator());
        mrecView.setAdapter(adapter);
        */
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
                // ToDo: enlazar el help con el HelpTabsActivity
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
            case R.id.ratingBar:
                String rating = String.valueOf(ratingBar.getRating());
                Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();
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