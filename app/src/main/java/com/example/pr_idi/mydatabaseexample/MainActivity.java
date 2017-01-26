package com.example.pr_idi.mydatabaseexample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private BookData bookData;
    private RecyclerView mrecView;
    private ItemAdapter adapter;
    private FloatingActionButton floatingActionButton;
    private List<Book> values;
    private Menu menu;

    private NavigationView nvDrawer;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;

    private String ORDRE = "titol";
    private int CERCATITOL = 1;
    private int DURATION = 150;
    private boolean defBooks = true;

    private MenuItem oldSort;
    private TextView nobook;
    private TextView count_text;
    private ArrayList<Book> selection_list;
    int counter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        //recycle view
        mrecView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mrecView.setLayoutManager(new LinearLayoutManager(this));
        floatingActionButton = (FloatingActionButton) findViewById(R.id.plus);

        SharedPreferences sp = MainActivity.this.getSharedPreferences(getString(R.string.PREFFILE), MODE_PRIVATE);
        CERCATITOL = sp.getInt(getString(R.string.settingSearch), 1);
        ORDRE = sp.getString(getString(R.string.settingOrder), "titol");
        defBooks = sp.getBoolean(getString(R.string.DefBooks), true);

        //Setting toolbar and drawer
        toolbar = (Toolbar) findViewById(R.id.tbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle(R.string.app_name);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);
        count_text = (TextView) findViewById(R.id.counter_text);
        count_text.setVisibility(View.GONE);
        nobook = (TextView) findViewById(R.id.nobook);

        count_text.setVisibility(View.GONE);

        //clic al botó
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        //creo la base de dades i omplo l'array amb tots els llibres
        bookData = new BookData(this);
        bookData.open();

        //agafo llibres ordenats per títol

        if (defBooks) inicialitza();
        values = bookData.getAllBooks();
        sorting();
        //creo l'adapter
        adapter = new ItemAdapter(values, this);
        mrecView.setAdapter(adapter);
        mrecView.setItemAnimator(new DefaultItemAnimator());
        mrecView.addItemDecoration(new DividerItemDecoration(this));

        registerForContextMenu(mrecView);
        adapter.notifyDataSetChanged();
        if(values.size() > 0) nobook.setVisibility(View.GONE);
        else displayText();
        saveSettings();
    }

    private int EvalToNum(String val) {
        switch (val){
            case "molt bo":
                return 5;
            case "bo":
                return 4;
            case "regular":
                return 3;
            case "dolent":
                return 2;
            case "molt dolent":
                return 1;
            default:
                return 0;
        }
    }


    private void sorting () {
        if(ORDRE.equals("titol")){
            Collections.sort(values, new Comparator<Book>(){
                public int compare(Book b1, Book b2){
                    String t1 = b1.getTitle();
                    String t2 = b2.getTitle();
                    return t1.compareTo(t2);
                }
            });
        }
        else if(ORDRE.equals("autor")){
            Collections.sort(values,  new Comparator<Book>() {
                public int compare(Book b1, Book b2){
                    String t1 = b1.getAuthor();
                    String t2 = b2.getAuthor();
                    return t1.compareTo(t2);
                }
            });
        }
        else if(ORDRE.equals("any")){
            Collections.sort(values, new Comparator<Book>() {
                public int compare(Book b1, Book b2) {
                    int t1 = b1.getYear();
                    int t2 = b2.getYear();
                    return (t1 - t2);
                }
            });
        }
        else if(ORDRE.equals("valoracio")){
            Collections.sort(values, new Comparator<Book>() {
                public int compare(Book b1, Book b2) {
                    String t1 = String.valueOf(EvalToNum(b1.getPersonal_evaluation()));
                    String t2 = String.valueOf(EvalToNum(b2.getPersonal_evaluation()));
                    return t2.compareTo(t1);
                }
            });
        }
        else{
            Collections.sort(values,  new Comparator<Book>() {
                public int compare(Book b1, Book b2){
                    String t1 = b1.getCategory();
                    String t2 = b2.getCategory();
                    return t1.compareTo(t2);
                }
            });
        }
        adapter = new ItemAdapter(values, this);
        mrecView.setAdapter(adapter);
    }


    public boolean onContextItemSelected(MenuItem item){
        Book b = adapter.getItemSelected(item); //llibre sel·leccionat per editar o eliminar
        if (item.getTitle().equals("Edit")){
            Intent i = new Intent(MainActivity.this, NewActivity.class);

            i.putExtra("mid", b.getId());
            i.putExtra("mtitol", b.getTitle());
            i.putExtra("mautor", b.getAuthor());
            i.putExtra("myear", b.getYear());
            i.putExtra("mpublisher", b.getPublisher());
            i.putExtra("mcategory", b.getCategory());
            i.putExtra("mval", b.getPersonal_evaluation());

            startActivityForResult(i, 2);
        }
        else {
            String t = b.getTitle();
            bookData.deleteBook(b);
            values.remove(b);
            Toast.makeText(getBaseContext(), t + "  " + getString(R.string.missatge1), Toast.LENGTH_SHORT).show();

        }
        sorting();
        if(values.size() > 0) nobook.setVisibility(View.GONE);
        else displayText();
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            CERCATITOL = Integer.parseInt( extras.getString("setTitle") );
            ORDRE = extras.getString("setOrder");
            defBooks = Boolean.parseBoolean( extras.getString("setBook") );
            saveSettings();
            sorting();

            setChecks(menu);
        }
        else if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras(); //exception if empty
            Book nou = new Book();

            nou.setId(extras.getLong("id"));
            nou.setTitle(extras.getString("titol"));
            nou.setAuthor(extras.getString("autor"));
            nou.setYear(extras.getInt("any"));
            nou.setPublisher(extras.getString("editorial"));
            nou.setCategory(extras.getString("categoria"));
            nou.setPersonal_evaluation(extras.getString("valoracio"));

            bookData.open();

            if (requestCode == 1) {
                if (!bookData.ExistsBook(nou)) {
                    nou = bookData.createBook(nou.getTitle(), nou.getAuthor(), String.valueOf(nou.getYear()),
                            nou.getPublisher(), nou.getCategory(), nou.getPersonal_evaluation());
                    values.add(nou);
                    sorting();
                    Toast.makeText(getBaseContext(), nou.getTitle() + "  " + getString(R.string.missatge2), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getBaseContext(), nou.getTitle() + "  " +  getString(R.string.missatge3), Toast.LENGTH_SHORT).show();
                }
            }
            else if (requestCode == 2) {
                String titolantic = extras.getString("titolantic");
                String autorantic = extras.getString("autorantic");
                int k = 0;
                boolean found = false;
                boolean abort = false;
                while (k < values.size() && !found){
                    Book i = values.get(k);
                    if (i.getTitle().equals(nou.getTitle()) && i.getAuthor().equals(nou.getAuthor()) ) {
                        found = true;
						abort = true;
                    }
					else if (i.getTitle().equals(titolantic) && i.getAuthor().equals(autorantic)) {
						values.remove(i);
                        values.add(nou);
						found = true;
					}
                    ++k;
                }
                if (!abort) {
                    bookData.UpdateBook(nou.getId(), nou.getTitle(), nou.getAuthor(), String.valueOf(nou.getYear()),
                            nou.getPublisher(), nou.getCategory(), nou.getPersonal_evaluation(), titolantic, autorantic);
                    sorting();
                    nou.setTitle(extras.getString("titol"));
                    Toast.makeText(getBaseContext(), nou.getTitle() + "  " + getString(R.string.missatge4), Toast.LENGTH_SHORT).show();
                }else {
	                Toast.makeText(getBaseContext(), getString(R.string.missatge5), Toast.LENGTH_SHORT).show();
				}
            }
            else {
                Toast.makeText(getBaseContext(), getString(R.string.missatge6), Toast.LENGTH_SHORT).show();
            }
            bookData.close();
        }
        if(values.size() > 0) nobook.setVisibility(View.GONE);
        else displayText();
    }


    public void inicialitza(){
        String[] newBook = new String[] {
                "Miguel Strogoff", "Jules Verne",     "1876", "Pierre-Jules Hetzel", "Adventure", "molt bo",
                "Ulysses", "James Joyce",             "1922", "Sylvia Beach", "Classic", "bo",
                "Don Quijote", "Miguel de Cervantes", "1605", "Francisco de Robles", "Classic", "molt bo",
                "Metamorphosis", "Kafka",             "1915", "Kurt Wolff", "Humour", "bo"
        };
        //Afegim els 4 llibres
        for (int i = 0; i < 24; i += 6) {
            if (!bookData.ExistsTitle(newBook[i])) {
                bookData.createBook(newBook[i], newBook[i+1], newBook[i+2], newBook[i+3], newBook[i+4], newBook[i+5]);
            }
        }
        defBooks = false;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        query = query.toLowerCase();
        ArrayList<Book> newList = new ArrayList<>();
        for (Book b: values){
            if (CERCATITOL == 0) {
                String author = b.getAuthor().toLowerCase();
                int pos = author.indexOf(query);
                if (author.contains(query) && pos == 0) {
                    newList.add(b);
                }
            }
            else {
                String title = b.getTitle().toLowerCase();
                int pos = title.indexOf(query);
                if (title.contains(query) && pos == 0) {
                    newList.add(b);
                }
            }
        }
        adapter.setFilter(newList);
        if(newList.isEmpty()) {
            nobook.setVisibility(View.VISIBLE);
            nobook.setText(getString(R.string.no_results));
            nobook.setClickable(false);
        }
        else {
            nobook.setVisibility(View.GONE);
            nobook.setText(getString(R.string.no_books));
            nobook.setClickable(true);
        }
        //mirar si la llista es buida ficar imatge de fons
        return true;
    }


    public boolean onCreateOptionsMenu(Menu menu){
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        getSystemService(Context.SEARCH_SERVICE);
        searchView.setOnQueryTextListener(this);
        setChecks(menu);
        return true;
    }

    private void setChecks(Menu menu) { //Formerly known as setOldies()
        if (oldSort != null)
            oldSort.setChecked(false); //Si no, excepcion al canto
        else
            emptyChecks(menu);
        switch(ORDRE){
            case "titol":
                oldSort = menu.findItem(R.id.otitol);
                menu.findItem(R.id.otitol).setChecked(true);
                break;
            case "autor":
                oldSort = menu.findItem(R.id.oautor);
                menu.findItem(R.id.oautor).setChecked(true);
                break;
            case "any":
                oldSort = menu.findItem(R.id.oyear);
                menu.findItem(R.id.oyear).setChecked(true);
                break;
            case "categoria":
                oldSort = menu.findItem(R.id.ocat);
                menu.findItem(R.id.ocat).setChecked(true);
                break;
            case "valoracio":
                oldSort = menu.findItem(R.id.oval);
                menu.findItem(R.id.oval).setChecked(true);
                break;
        }
    }

    private void emptyChecks(Menu menu) {
        menu.findItem(R.id.otitol).setChecked(false);
        menu.findItem(R.id.oautor).setChecked(false);
        menu.findItem(R.id.oyear).setChecked(false);
        menu.findItem(R.id.ocat).setChecked(false);
        menu.findItem(R.id.oval).setChecked(false);
    }

    //clic sobre elements del context menu
    public boolean onOptionsItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int i = item.getItemId();
        if (oldSort != null  && (i == R.id.otitol || i == R.id.oautor
                || i == R.id.oyear || i == R.id.ocat || i == R.id.oval)) {
            item.setCheckable(true).setChecked(true);
            oldSort.setChecked(false);
            oldSort = item;
        }
        else if (i == R.id.otitol || i == R.id.oautor|| i == R.id.oyear || i == R.id.ocat || i == R.id.oval) {
            oldSort = item;
        }
        switch (i) {
            case R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.otitol:
                //Toast t2 = Toast.makeText(getApplicationContext(),"ordenar per titol",Toast.LENGTH_SHORT);
                //t2.show();
                ORDRE = "titol";
                sorting(); saveSettings();
                adapter.notifyDataSetChanged();
                return true;
            case R.id.oautor:
                //Toast t3 = Toast.makeText(getApplicationContext(),"ordenar per autor",Toast.LENGTH_SHORT);
                //t3.show();
                ORDRE = "autor";
                sorting(); saveSettings();
                adapter.notifyDataSetChanged();
                return true;
            case R.id.oyear:
                //Toast t4 = Toast.makeText(getApplicationContext(),"ordenar per any",Toast.LENGTH_SHORT);
                //t4.show();
                ORDRE = "any";
                sorting(); saveSettings();
                adapter.notifyDataSetChanged();
                return true;
            case R.id.ocat:
                //Toast t5 = Toast.makeText(getApplicationContext(),"ordenar per categoria",Toast.LENGTH_SHORT);
                //t5.show();
                ORDRE = "categoria";
                sorting(); saveSettings();
                adapter.notifyDataSetChanged();
                return true;
            case R.id.oval:
                //Toast t6 = Toast.makeText(getApplicationContext(),"ordenar per valoracio",Toast.LENGTH_SHORT);
                //t6.show();
                ORDRE = "valoracio";
                sorting(); saveSettings();
                adapter.notifyDataSetChanged();
                return true;
            case R.id.cercaTitol:
                CERCATITOL = 1; saveSettings();
                //Toast.makeText(getApplicationContext(),"cercar per titol",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.cercaAutor:
                CERCATITOL = 0; saveSettings();
                //Toast.makeText(getApplicationContext(),"cercar per autor",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.delete_mode:
                //Toast.makeText(getApplicationContext(),"mode delete",Toast.LENGTH_SHORT).show();
                if (selection_list != null) {
                    adapter.updateAdapter(selection_list);
                    clearDeleteMode("NO", 0);
                }
                else {
                    clearDeleteMode("YES", 0);
                }
                setChecks(menu);
                toolbar.setTitle(R.string.app_name);
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
        switch(menuItem.getItemId()) {
            case R.id.nav_create:
                Intent i1 = new Intent(MainActivity.this, NewActivity.class);
                startActivityForResult(i1, 1);
                break;
            case R.id.nav_edit:
                adapter.setEdit(true);
                adapter.setDelete(false);
                Log.v("ADAPTER", String.valueOf(adapter.getItemCount()) );
                count_text.setVisibility(View.GONE);
                toolbar.getMenu().clear();
                toolbar.setTitle(R.string.selectedit);
                toolbar.inflateMenu(R.menu.menu_search);
                MenuItem mItem = menu.findItem(R.id.action_search);
                SearchView searchView = (SearchView) MenuItemCompat.getActionView(mItem);
                getSystemService(Context.SEARCH_SERVICE);
                searchView.setOnQueryTextListener(this);

                setChecks(menu);
                adapter.notifyDataSetChanged();
                break;
            case R.id.nav_delete:
                toolbar.getMenu().clear();
                adapter.setEdit(false);
                toolbar.setTitle(R.string.delete);
                toolbar.inflateMenu(R.menu.menu_delete);
                count_text.setVisibility(View.VISIBLE);
                selection_list = new ArrayList<>();
                counter = 0;
                adapter.setDelete(true);
                adapter.notifyDataSetChanged();
                break;
            case R.id.nav_settings:
                Intent i3 = new Intent(MainActivity.this, SettingsActivity.class);
                i3.putExtra("CERCATITOL", CERCATITOL);
                i3.putExtra("ORDRE", ORDRE);
                startActivityForResult(i3, 3);
                break;
            case R.id.about:
                Intent i4 = new Intent(MainActivity.this, AboutActivity.class); startActivity(i4);
                break;
            case R.id.help:
                Intent i5 = new Intent(MainActivity.this, HelpActivity.class); startActivity(i5);
                break;
        }
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
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


    public void saveSettings () {
        SharedPreferences sp = MainActivity.this.getSharedPreferences(getString(R.string.PREFFILE), MODE_PRIVATE);
        SharedPreferences.Editor editPref = sp.edit();
        editPref.putInt(getString(R.string.settingSearch), CERCATITOL);
        editPref.putString(getString(R.string.settingOrder), ORDRE);
        editPref.putBoolean(getString(R.string.DefBooks), defBooks);
        editPref.commit();
    }

    public void prepare_Selection(View vista, int position){
        if ( ((CheckBox) vista).isChecked() ) {
            selection_list.add(values.get(position));
            ++counter;
            updateCounter(counter);
        }
        else{
            selection_list.remove(values.get(position));
            --counter;
            updateCounter(counter);
        }
    }
    public void updateCounter(int counter){
        if(counter == 0) count_text.setText(R.string.noitem);
        else if (counter == 1) count_text.setText(R.string.oneitem);
        else count_text.setText("| " + counter +" "+ getString(R.string.moreitem));
    }

    public void displayText(){
        nobook.setVisibility(View.VISIBLE);
        nobook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }
    public void clearDeleteMode(String list_null, int back){
        adapter.setDelete(false);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_search);

        MenuItem mItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(mItem);
        getSystemService(Context.SEARCH_SERVICE);
        searchView.setOnQueryTextListener(this);

        count_text.setVisibility(View.GONE);
        count_text.setText(R.string.noitem);
        counter = 0;
        //eliminar de la db
        if (list_null.equals("NO") && back == 0) {
            for (Book i : selection_list) {
                Toast.makeText(getBaseContext(), i.getTitle() + "  " + getString(R.string.missatge1), Toast.LENGTH_SHORT).show();

                bookData.deleteBook(i);
                values.remove(i);
            }
            selection_list.clear();
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (values.size() > 0) nobook.setVisibility(View.GONE);
        else displayText();
    }

    @Override
    protected void onResume() {
        bookData.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        bookData.close();
        super.onPause();
    }
    @Override
    public void onBackPressed(){
        if (adapter.getEdit()) {
            adapter.setEdit(false);
            adapter.notifyDataSetChanged();
        }

        else if (adapter.getDelete()){
            adapter.setDelete(false);
            if (selection_list != null) clearDeleteMode("NO", 1);
            else clearDeleteMode("YES", 1);
            adapter.notifyDataSetChanged();
        }
        else {
            super.onBackPressed();
        }

        nobook.setText(getString(R.string.no_books));
        nobook.setClickable(true);

        toolbar.setTitle(R.string.app_name);
        sorting();
        setChecks(menu);
    }

    @Override
    public void finish() {
        if (adapter.getDelete() || adapter.getEdit()){
            adapter.setEdit(false);
            adapter.setDelete(false);
            setChecks(menu);
            toolbar.setTitle(R.string.app_name);
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_search);

            MenuItem mItem = menu.findItem(R.id.action_search);
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(mItem);
            getSystemService(Context.SEARCH_SERVICE);
            searchView.setOnQueryTextListener(this);

            count_text.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
        else {
            super.finish(); //This will finish the activity and take you back
        }
    }

}
