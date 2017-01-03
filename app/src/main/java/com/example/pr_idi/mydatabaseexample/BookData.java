package com.example.pr_idi.mydatabaseexample;

/**
 * BookData
 * Created by pr_idi on 10/11/16.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BookData {

    // Database fields
    private SQLiteDatabase database;

    // Helper to manipulate table
    private MySQLiteHelper dbHelper;

    // Here we only select Title and Author, must select the appropriate columns
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_TITLE, MySQLiteHelper.COLUMN_AUTHOR, };

    public BookData(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Book createBook(String title, String author,String publisher, String year,
                           String category, String val) {
        ContentValues values = new ContentValues();
        Log.d("Creating", "Creating " + title + " " + author + " " + publisher + " " + year + " "
                + category + " " + val);

        // Add data: Note that this method only provides title and author
        // Must modify the method to add the full data
        values.put(MySQLiteHelper.COLUMN_TITLE, title);
        values.put(MySQLiteHelper.COLUMN_AUTHOR, author);
        // Invented data
        values.put(MySQLiteHelper.COLUMN_PUBLISHER, publisher);
        values.put(MySQLiteHelper.COLUMN_YEAR,year);
        values.put(MySQLiteHelper.COLUMN_CATEGORY, category);
        values.put(MySQLiteHelper.COLUMN_PERSONAL_EVALUATION, val);

        // Actual insertion of the data using the values variable
        long insertId = database.insert(MySQLiteHelper.TABLE_BOOKS, null,
                values);

        // Main activity calls this procedure to create a new book
        // and uses the result to update the listview.
        // Therefore, we need to get the data from the database
        // (you can use this as a query example)
        // to feed the view.

        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Book newBook = cursorToBook(cursor);

        // Do not forget to close the cursor
        cursor.close();

        // Return the book
        return newBook;
    }

    public void deleteBook(Book book) {
        long id = book.getId();
        System.out.println("Book deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_BOOKS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Book book = cursorToBook(cursor);
            books.add(book);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return books;
    }
    public List<Book> getAllBooksbyTitol() {
        List<Book> books = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + MySQLiteHelper.TABLE_BOOKS +
                " ORDER BY " + MySQLiteHelper.COLUMN_TITLE + " DESC", new String[]{});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Book book = cursorToBook(cursor);
            books.add(book);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return books;
    }
    public List<Book> getAllBooksbyAutor() {
        List<Book> books = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + MySQLiteHelper.TABLE_BOOKS +
                " ORDER BY " + MySQLiteHelper.COLUMN_AUTHOR + " DESC", new String[]{});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Book book = cursorToBook(cursor);
            books.add(book);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return books;
    }
    public List<Book> getAllBooksbyCategoria() {
        List<Book> books = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + MySQLiteHelper.TABLE_BOOKS +
                " ORDER BY " + MySQLiteHelper.COLUMN_CATEGORY + " DESC", new String[]{});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Book book = cursorToBook(cursor);
            books.add(book);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return books;
    }
    public List<Book> getAllBooksbyAny() {
        List<Book> books = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + MySQLiteHelper.TABLE_BOOKS +
                " ORDER BY " + MySQLiteHelper.COLUMN_YEAR + " DESC", new String[]{});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Book book = cursorToBook(cursor);
            books.add(book);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return books;
    }

    public void UpdateBook (int id, String titol, String autor, String pub, String year, String cat,
                       String val){
        ContentValues cv = new ContentValues();
        cv.put(MySQLiteHelper.COLUMN_ID, id);
        cv.put(MySQLiteHelper.COLUMN_TITLE, titol);
        cv.put(MySQLiteHelper.COLUMN_AUTHOR, autor);
        cv.put(MySQLiteHelper.COLUMN_PUBLISHER, pub);
        cv.put(MySQLiteHelper.COLUMN_YEAR, year);
        cv.put(MySQLiteHelper.COLUMN_CATEGORY, cat);
        cv.put(MySQLiteHelper.COLUMN_PERSONAL_EVALUATION, val);
        database.update(MySQLiteHelper.TABLE_BOOKS, cv, MySQLiteHelper.COLUMN_ID + "=?",
                new String[]{Integer.toString(id)});
    }

    public Book getItem(int id){
        Cursor c = database.rawQuery(" select " + MySQLiteHelper.COLUMN_ID + "," +
                MySQLiteHelper.COLUMN_TITLE + "," + MySQLiteHelper.COLUMN_AUTHOR + "," +
                MySQLiteHelper.COLUMN_PUBLISHER + "," + MySQLiteHelper.COLUMN_YEAR + "," +
                MySQLiteHelper.COLUMN_CATEGORY + "," + MySQLiteHelper.COLUMN_PERSONAL_EVALUATION
                + " from" + MySQLiteHelper.TABLE_BOOKS + " where " + MySQLiteHelper.COLUMN_ID
                + "= ?", new String[]{Integer.toString(id)});
        return cursorToBook(c);
    }
    private Book cursorToBook(Cursor cursor) {
        Book book = new Book();
        book.setId(cursor.getLong(0));
        book.setTitle(cursor.getString(1));
        book.setAuthor(cursor.getString(2));
        return book;
    }
    public boolean ExistsBook(Book b) {
        String titol = b.getTitle();
        String autor = b.getAuthor();
        int any = b.getYear();
        String editorial = b.getPublisher();
        String categoria = b.getCategory();
        String Query = "Select * from " + MySQLiteHelper.TABLE_BOOKS + " where " +
                MySQLiteHelper.COLUMN_TITLE + " = " + titol + " and " + MySQLiteHelper.COLUMN_AUTHOR
                + " = " + autor + " and " + MySQLiteHelper.COLUMN_YEAR + " = " + any + " and " +
                MySQLiteHelper.COLUMN_PUBLISHER + " = " + editorial + " and " +
                MySQLiteHelper.COLUMN_CATEGORY + " = " + categoria;
        Cursor cursor = database.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
    public boolean ExistsTitle(String title) {
        String Query = "Select * from " + MySQLiteHelper.TABLE_BOOKS + " where " +
                MySQLiteHelper.COLUMN_TITLE + " = ' " + title + " ' ; ";
        Cursor cursor = database.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}