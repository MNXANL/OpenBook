package com.example.pr_idi.mydatabaseexample;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.List;

import static android.support.v4.view.ViewCompat.animate;

public class MainActivity extends AppCompatActivity {
    private BookData bookData;
    private RecyclerView mrecView;
    private ItemAdapter adapter;
    private FloatingActionButton floatingActionButton;
    private static final int DURATION = 150;

    private NavigationView nvDrawer;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        //recycle view
        mrecView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mrecView.setLayoutManager(new LinearLayoutManager(this));
        floatingActionButton = (FloatingActionButton) findViewById(R.id.plus);

        //Setting toolbar and drawer
        toolbar = (Toolbar) findViewById(R.id.tbar);
        setTitle("MyBookDB | Main Menu");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true); //Burger

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);

        //clic al FloatingActionBtton
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creo un nou llibre
                Intent intent = new Intent(MainActivity.this, NewActivity.class);
                startActivity(intent);
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
                            .translationY(floatingActionButton.getHeight() + getResources().getDimension(R.dimen.fab_margin))
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

        inicialitza(); //COMENTADO PARA QUE NO GENERE MIL LIBRITOS

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
        String[] newBook = new String[] {
                "Miguel Strogoff", "Jules Verne",
                "Ulysses", "James Joyce",
                "Don Quijote", "Miguel de Cervantes",
                "Metamorphosis", "Kafka"
        };
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
    //context menu actuarà sobre el recycler view
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
        Book book = (Book) adapter.getOnItemClickListener();
        switch (item.getItemId()) {
            case R.id.edit:
                //generem una nova activity ja inicialitzada amb els valors del llibre sel·leccionat
                Intent intent = new Intent(MainActivity.this, Activity_Item.class);
                intent.putExtra("mtitol", book.getTitle());
                intent.putExtra("mautor", book.getAuthor());
                intent.putExtra("mpublisher", book.getPublisher());
                String y = String.valueOf(book.getYear());
                intent.putExtra("myear", y);
                intent.putExtra("mcategory", book.getCategory());
                String val = book.getPersonal_evaluation();
                float p = 0;
                switch (val){ //segons la valoració hi hauran més o menys estrelles pintades
                    case "molt bo":
                        p = 5; break;
                    case "bo":
                        p = 4; break;
                    case "regular":
                        p = 3; break;
                    case "dolent":
                        p = 2; break;
                    case "molt dolent":
                        p = 1; break;
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
            case R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            default:
                return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    selectDrawerItem(menuItem);
                    return true;
                }
            });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        FragmentManager fragmentManager = getSupportFragmentManager();


        switch(menuItem.getItemId()) {
            case R.id.docs:
                fragmentManager.beginTransaction().replace(R.id.main_content, new FirstFragment()).commit();
                break;
            case R.id.recent:
                fragmentManager.beginTransaction().replace(R.id.main_content, new SecondFragment()).commit();
                break;
            case R.id.about:
                //fragmentManager.beginTransaction().replace(R.id.main_content, new AboutFragment()).commit();
                Intent ii = new Intent(MainActivity.this, Activity_Item.class);
                startActivity(ii);
                break;
            case R.id.help:
                fragmentManager.beginTransaction().replace(R.id.main_content, new FirstFragment()).commit();
                break;
        }
        /*try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        // Insert the fragment by replacing any existing fragment

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
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