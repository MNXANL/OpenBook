package com.example.pr_idi.mydatabaseexample;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.view.animation.LinearInterpolator;

import static android.support.v4.view.ViewCompat.animate;

public class MainActivity extends AppCompatActivity {
    private BookData bookData;
    private RecyclerView mrecView;
    private ItemAdapter adapter;
    private FloatingActionButton floatingActionButton;
    private static final int DURATION = 150;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //recycle view
        mrecView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mrecView.setLayoutManager(new LinearLayoutManager(this));
        floatingActionButton = (FloatingActionButton) findViewById(R.id.plus);
        //clic al botó
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creo un nou llibre
                Intent intent = new Intent(MainActivity.this, NewActivity.class);
            }
        });
        //definim si l'scroll va cap amunt o cap avall
        Action scrollAction = new Action() {
            private boolean hidden = true;
            @Override
            public void up() {
                if (hidden) {
                    hidden = false;
                    animate(floatingActionButton)
                            .translationY(floatingActionButton.getHeight() +
                                    getResources().getDimension(R.dimen.fab_margin))
                            .setInterpolator(new LinearInterpolator())
                            .setDuration(DURATION);
                }
            }
            @Override
            public void down() {
                if (!hidden) {
                    hidden = true;
                    animate(floatingActionButton)
                            .translationY(0)
                            .setInterpolator(new LinearInterpolator())
                            .setDuration(DURATION);

                }
            }

        };
        //creo la base de dades i omplo l'array amb tots els llibres
        bookData = new BookData(this);
        bookData.open();
        //agafo llibres ordenats per títol
        inicialitza();
        List<Book> values = bookData.getAllBooksbyTitol();
        //creo l'adapter
        adapter = new ItemAdapter(values);
        mrecView.setAdapter(adapter);
        mrecView.setItemAnimator(new DefaultItemAnimator());
        //si moc l'scroll defineixo un conjunt d'operacions
        adapter.setOnScrollListener(mrecView, scrollAction);
        //si faig clic algun element de la llista
        adapter.setOnItemClickListener(new com.example.pr_idi.mydatabaseexample.OnItemClickListener() {
            @Override
            public void onItemClick(Book item) {
                Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_LONG).show();
                /*Book book = (Book) getListAdapter().getItem(mrecView.getChildAdapterPosition(v));
                Intent i = new Intent(MainActivity.this, Activity_Item.class);
                i.putExtra("mtitol", book.getTitle());
                i.putExtra("mautor", book.getAuthor());
                i.putExtra("myear", book.getYear());
                i.putExtra("mpublisher", book.getPublisher());
                i.putExtra("mcategory", book.getCategory());
                i.putExtra("mval", book.getPersonal_evaluation());
                startActivity(i);*/
            }
        });
        //li associo el context menu
       registerForContextMenu(mrecView);
       adapter.notifyDataSetChanged();
    }
    // Basic method to add pseudo-random list of books so that
    // you have an example of insertion and deletion

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    /*public void onClick(View view) {
        @SuppressWarnings("unchecked")
        //ArrayAdapter<Book> adapter = (ArrayAdapter<Book>) getListAdapter();
        Book book;
        switch (view.getId()) {
            case R.id.add:
                Intent i = new Intent(MainActivity.this, Activity_Item.class);
                startActivityForResult(i, NEW_ITEM);

                String[] newBook = new String[] { "Miguel Strogoff", "Jules Verne", "Ulysses", "James Joyce", "Don Quijote", "Miguel de Cervantes", "Metamorphosis", "Kafka" };
                int nextInt = new Random().nextInt(4);
                // save the new book to the database
                book = bookData.createBook(newBook[nextInt*2], newBook[nextInt*2 + 1]);

                // After I get the book data, I add it to the adapter
                //adapter.add(book);
                //break;
           case R.id.delete:
                if (getListAdapter().getCount() > 0) {
                    book = (Book) getListAdapter().getItem(0);
                    bookData.deleteBook(book);
                    adapter.remove(book);
                }
                break;
            case default:

                break;
        }
        adapter.notifyDataSetChanged();
    }*/

    public void inicialitza(){
        String[] newBook = new String[] { "Miguel Strogoff", "Jules Verne", "Ulysses",
                "James Joyce", "Don Quijote", "Miguel de Cervantes", "Metamorphosis", "Kafka" };
        //Afegim els 4 llibres
        int i = 0;
        if (!bookData.ExistsTitle(newBook[i])) {
            Book book = bookData.createBook(newBook[i], newBook[i + 1], "1876",
                    "Pierre-Jules Hetzel", "Aventura", "molt bo");
            adapter.add(book);
        }
        i += 2;
        if (!bookData.ExistsTitle(newBook[i])) {
            Book book = bookData.createBook(newBook[i], newBook[i + 1], "1922",
                    "Sylvia Beach", "Clàssic", "bo");
            adapter.add(book);
        }
        i += 2;
        if (!bookData.ExistsTitle(newBook[i])) {
            Book book = bookData.createBook(newBook[i], newBook[i + 1], "1605",
                    "Francisco de Robles", "Clàssic", "molt bo");
            adapter.add(book);
        }
        i += 2;
        if (!bookData.ExistsTitle(newBook[i])) {
            Book book = bookData.createBook(newBook[i], newBook[i + 1], "1915",
                    "Kurt Wolff", "Humor", "bo");
            adapter.add(book);
        }
    }
    //context menu actuarà sobre el recycle view
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        int id = v.getId();
        MenuInflater inflater = getMenuInflater();
        switch (id){
            case R.id.my_recycler_view:
                inflater.inflate(R.menu.context_menu, menu);
                break;

        }
    }
    //clic sobre elements del context menu
    public boolean onOptionsItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Book book = (Book)adapter.getOnItemClickListener();
        switch (item.getItemId()) {
            case R.id.edit:
                //generem una nova activity ja inicialitzada amb els valors del llibre sel·leccionat
                Intent intent = new Intent(MainActivity.this, Activity_Item.class);
                intent.putExtra("mtitol", book.getTitle().toString());
                intent.putExtra("mautor", book.getAuthor().toString());
                intent.putExtra("mpublisher", book.getPublisher().toString());
                String y = String.valueOf(book.getYear());
                intent.putExtra("myear", y);
                intent.putExtra("mcategory", book.getCategory().toString());
                String val = book.getPersonal_evaluation().toString();
                float p = 0;
                switch (val){ //segons la valoració hi hauran més o menys estrelles pintades
                    case "molt bo":
                        p = 5;
                        break;
                    case "bo":
                        p = 4;
                        break;
                    case "regular":
                        p = 3;
                        break;
                    case "dolent":
                        p = 2;
                        break;
                    case "molt dolent":
                        p = 1;
                        break;
                }
                intent.putExtra("mval", p);
                startActivity(intent);
                return true;
            case R.id.delete:
                //cal ficar l'operacio borrar
                bookData.deleteBook(book);
                Toast t = Toast.makeText(getApplicationContext(),"@string/missatge1",Toast.LENGTH_SHORT);
                t.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        super.onPause();
    }

}