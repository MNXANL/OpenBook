package com.example.pr_idi.mydatabaseexample;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.support.v4.view.ViewCompat.animate;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private BookData bookData;
    private RecyclerView mrecView;
    private ItemAdapter adapter;
    private FloatingActionButton floatingActionButton;
    private List<Book> values;

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
        toolbar.setTitle("OpenBook");
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


        //definim si l'scroll va cap amunt o cap avall
        Action scrollAction = new Action() {
            private boolean hidden = true;
            @Override
            public void up() {
                if (hidden) {
                    hidden = false;
                    animate(floatingActionButton)
                            .translationY(floatingActionButton.getHeight() + getResources().getDimension(R.dimen.fab_margin))
                            .setInterpolator(new LinearInterpolator()).setDuration(DURATION);
                }
            }
            @Override
            public void down() {
                if (!hidden) {
                    hidden = true;
                    animate(floatingActionButton)
                            .translationY(0).setInterpolator(new LinearInterpolator()).setDuration(DURATION);
                }
            }

        };

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

        //si moc l'scroll defineixo un conjunt d'operacions
        mrecView.addItemDecoration(new DividerItemDecoration(this));

        registerForContextMenu(mrecView);
        adapter.notifyDataSetChanged();
        if(values.size() > 0) nobook.setVisibility(View.GONE);
        else displayText();
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
                    String t1 = String.valueOf(b1.getYear());
                    String t2 = String.valueOf(b2.getYear());
                    return t1.compareTo(t2);
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
        if (item.getTitle().equals("Editar")){
            Intent i = new Intent(MainActivity.this, NewActivity.class);
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
            Toast.makeText(getBaseContext(), "S'ha eliminat "+ t, Toast.LENGTH_SHORT).show();

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
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras(); //exception if empty
            Book nou = new Book();

            nou.setTitle(extras.getString("titol"));
            nou.setAuthor(extras.getString("autor"));
            nou.setYear(Integer.parseInt(extras.getString("any")));
            nou.setPublisher(extras.getString("editorial"));
            nou.setCategory(extras.getString("categoria"));
            nou.setPersonal_evaluation(extras.getString("valoracio"));

            bookData.open();

            if (requestCode == 1) {
                if (!bookData.ExistsBook(nou)) {
                    bookData.createBook(nou.getTitle(), nou.getAuthor(), String.valueOf(nou.getYear()),
                            nou.getPublisher(), nou.getCategory(), nou.getPersonal_evaluation());
                    values.add(nou);
                    sorting();
                    Toast.makeText(getBaseContext(), "S'ha afegit el llibre " + nou.getTitle(), Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getBaseContext(), "El llibre " + nou.getTitle() +" ja existeix", Toast.LENGTH_LONG).show();
                }
            }
            else if (requestCode == 2) {

                String titolantic = extras.getString("titolantic");
                String autorantic = extras.getString("autorantic");

                if (!bookData.ExistsBook(nou)) {
                    int k = 0;
                    boolean found = false;
                    while(k < values.size() && !found){
                        Book i = values.get(k);
                        if (i.getTitle().equals(titolantic) && i.getAuthor().equals(autorantic)){
                            values.remove(i);
                            values.add(nou);
                            found = true;
                        }
                        ++k;
                    }

                    bookData.UpdateBook(nou.getId(), nou.getTitle(), nou.getAuthor(), String.valueOf(nou.getYear()),
                            nou.getPublisher(), nou.getCategory(), nou.getPersonal_evaluation(), titolantic, autorantic);
                    sorting();
                    nou.setTitle(extras.getString("titol"));
                    Toast.makeText(getBaseContext(), "S'ha modificat " + nou.getTitle(), Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getBaseContext(), "No es pot renombrar a un llibre amb el mateix titol i autor!", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(getBaseContext(), "ERROR al afegir el llibre", Toast.LENGTH_LONG).show();
            }
            bookData.close();

        }
        if(values.size() > 0) nobook.setVisibility(View.GONE);
        else displayText();
    }


    public void inicialitza(){
        String[] newBook = new String[] {
                "Miguel Strogoff", "Jules Verne",     "1876", "Pierre-Jules Hetzel", "Aventura", "molt bo",
                "Ulysses", "James Joyce",             "1922", "Sylvia Beach", "Clàssic", "bo",
                "Don Quijote", "Miguel de Cervantes", "1605", "Francisco de Robles", "Clàssic", "molt bo",
                "Metamorphosis", "Kafka",             "1915", "Kurt Wolff", "Humor", "bo"
        };
        //Afegim els 4 llibres
        int i = 0;
        if (!bookData.ExistsTitle(newBook[i])) {
            bookData.createBook(newBook[i], newBook[i+1], newBook[i+2], newBook[i+3], newBook[i+4], newBook[i+5]);
        }
        i += 6;
        if (!bookData.ExistsTitle(newBook[i])) {
            bookData.createBook(newBook[i], newBook[i+1], newBook[i+2], newBook[i+3], newBook[i+4], newBook[i+5]);
        }
        i += 6;
        if (!bookData.ExistsTitle(newBook[i])) {
            bookData.createBook(newBook[i], newBook[i+1], newBook[i+2], newBook[i+3], newBook[i+4], newBook[i+5]);
        }
        i += 6;
        if (!bookData.ExistsTitle(newBook[i])) {
            bookData.createBook(newBook[i], newBook[i+1], newBook[i+2], newBook[i+3], newBook[i+4], newBook[i+5]);
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
            if(CERCATITOL == 0) {
                String author = b.getAuthor().toLowerCase();
                if (author.contains(query)) {
                    newList.add(b);
                }
            }
            else{
                String title = b.getTitle().toLowerCase();
                if (title.contains(query)) {
                    newList.add(b);
                }
            }
        }
        adapter.setFilter(newList);
        if(newList.isEmpty()){

        }
        //mirar si la llista es buida ficar imatge de fons
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        SearchManager sMan = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setOnQueryTextListener(this);
        Toast.makeText(getApplicationContext(),"SORT BY " + ORDRE + " AND FINDS BY " + CERCATITOL, Toast.LENGTH_SHORT).show();
        setOldies(menu);
        return true;
    }

    private void setOldies(Menu menu) {
        /*switch(CERCATITOL){
            case 0:
                oldFind = menu.findItem(R.id.cercaAutor);
            case 1:
                oldFind = menu.findItem(R.id.cercaTitol);
        }*/
        switch(ORDRE){
            case "titol":
                oldSort = menu.findItem(R.id.otitol);
            case "autor":
                oldSort = menu.findItem(R.id.oautor);
            case "any":
                oldSort = menu.findItem(R.id.oyear);
            case "categoria":
                oldSort = menu.findItem(R.id.ocat);
            case "valoracio":
                oldSort = menu.findItem(R.id.oval);
        }
    }

    //clic sobre elements del context menu
    public boolean onOptionsItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        //if (item.isChecked())   item.setChecked(false);else   item.setChecked(true);

        int i = item.getItemId();
        if (oldSort != null  && i != R.id.home && i != R.id.cercaAutor && i != R.id.cercaTitol) {
            item.setCheckable(true).setChecked(true);
            oldSort.setChecked(false);
            oldSort = item;
        }
        else {
            oldSort = item;
        }
        /*if (oldFind != null && i != R.id.home && i != R.id.otitol && i != R.id.oautor && i != R.id.oyear && i != R.id.ocat && i != R.id.oval) {
            item.setCheckable(true).setChecked(true);
            oldFind.setChecked(false);
            oldFind = item;
        }
        else {
            oldFind = item;
        }*/

        switch (i) {
            case R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.otitol:
                Toast t2 = Toast.makeText(getApplicationContext(),"ordenar per titol",Toast.LENGTH_SHORT);
                t2.show();
                ORDRE = "titol";
                sorting(); saveSettings();
                adapter.notifyDataSetChanged();
                return true;
            case R.id.oautor:
                Toast t3 = Toast.makeText(getApplicationContext(),"ordenar per autor",Toast.LENGTH_SHORT);
                t3.show();
                ORDRE = "autor";
                sorting(); saveSettings();
                adapter.notifyDataSetChanged();
                return true;
            case R.id.oyear:
                Toast t4 = Toast.makeText(getApplicationContext(),"ordenar per any",Toast.LENGTH_SHORT);
                t4.show();
                //item.setChecked(true);
                ORDRE = "any";
                sorting(); saveSettings();
                adapter.notifyDataSetChanged();
                return true;
            case R.id.ocat:
                Toast t5 = Toast.makeText(getApplicationContext(),"ordenar per categoria",Toast.LENGTH_SHORT);
                t5.show();
                ORDRE = "categoria";
                sorting(); saveSettings();
                adapter.notifyDataSetChanged();
                return true;
            case R.id.oval:
                Toast t6 = Toast.makeText(getApplicationContext(),"ordenar per valoracio",Toast.LENGTH_SHORT);
                t6.show();
                ORDRE = "valoracio";
                sorting(); saveSettings();
                adapter.notifyDataSetChanged();
                return true;
            case R.id.cercaTitol:
                CERCATITOL = 1; saveSettings();
                Toast.makeText(getApplicationContext(),"cercar per titol",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.cercaAutor:
                CERCATITOL = 0; saveSettings();
                Toast.makeText(getApplicationContext(),"cercar per autor",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.delete_mode:

                Toast.makeText(getApplicationContext(),"mode delete",Toast.LENGTH_SHORT).show();
                 toolbar.setTitle("OpenBook");

                if (selection_list != null) {
                    adapter.updateAdapter(selection_list);
                    clearDeleteMode("NO");
                }
                else {
                    clearDeleteMode("YES");
                }
                toolbar.setTitle("OpenBook");

                return true;
            default:
                return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
        }
       // switch_order(ORDRE, item);
       // switch_find(CERCATITOL, item);
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
        // com.example.pr_idi.mydatabaseexample.Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch(menuItem.getItemId()) {
            case R.id.nav_create:
                Intent i1 = new Intent(MainActivity.this, NewActivity.class);
                startActivityForResult(i1, 1);
                break;
            case R.id.nav_edit:
                adapter.setEdit(true);
                adapter.notifyDataSetChanged();
                //Intent i2 = new Intent(MainActivity.this, MainActivity.class);
                //startActivity(i2);
                break;
            case R.id.nav_delete:
                toolbar.getMenu().clear();
                toolbar.setTitle("Delete");
                toolbar.inflateMenu(R.menu.menu_delete);
                count_text.setVisibility(View.VISIBLE);

                selection_list = new ArrayList<>();
                counter = 0;

                adapter.setDelete(true);
                adapter.notifyDataSetChanged();
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                //Intent i3 = new Intent(MainActivity.this, MainActivity.class);
                //startActivity(i3);
                break;
            case R.id.nav_settings:
                Intent i3 = new Intent(MainActivity.this, Settings.class); startActivity(i3);
            case R.id.about:
                //fragmentManager.beginTransaction().replace(R.id.main_content, new AboutActivity()).commit();
                Intent i4 = new Intent(MainActivity.this, AboutActivity.class); startActivity(i4);
                break;
            case R.id.help:
                Intent i5 = new Intent(MainActivity.this, HelpActivity.class); startActivity(i5);
                break;
        }
        // Insert the fragment by replacing any existing fragment
        // Highlight the selected item has been done by NavigationView
        // Set action bar title
        // Close the navigation drawer
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

    // Life cycle methods. Check whether it is necessary to reimplement them

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        // let the system handle all other key events
        return super.onKeyDown(keyCode, event);
    }

    public void saveSettings () {
        SharedPreferences sp = MainActivity.this.getSharedPreferences(getString(R.string.PREFFILE), MODE_PRIVATE);
        SharedPreferences.Editor editPref = sp.edit();
        editPref.putInt(getString(R.string.settingSearch), CERCATITOL);
        editPref.putString(getString(R.string.settingOrder), ORDRE);
        editPref.putBoolean(getString(R.string.DefBooks), defBooks);
        editPref.apply();
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
        if(counter == 0) count_text.setText("| No items selected");
        else if (counter == 1) count_text.setText("| 1 item selected");
        else count_text.setText("| " + counter + " items selected");
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
    public void clearDeleteMode(String list_null){
        adapter.setDelete(false);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_search);
        count_text.setVisibility(View.GONE);
        count_text.setText("| No items selected");
        counter = 0;
        //eliminar de la db
        if (list_null.equals("NO")) {
            for (Book i : selection_list) {
                bookData.deleteBook(i);
                values.remove(i);
            }
            selection_list.clear();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            if (selection_list != null) clearDeleteMode("NO");
            else clearDeleteMode("YES");
            adapter.notifyDataSetChanged();
        }

        else {
            super.onBackPressed();
        }
    }

}