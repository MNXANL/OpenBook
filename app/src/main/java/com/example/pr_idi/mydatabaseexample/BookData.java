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
    private String[] allColumns = {
            MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_TITLE,
            MySQLiteHelper.COLUMN_AUTHOR,
            MySQLiteHelper.COLUMN_YEAR,
            MySQLiteHelper.COLUMN_PUBLISHER,
            MySQLiteHelper.COLUMN_CATEGORY,
            MySQLiteHelper.COLUMN_PERSONAL_EVALUATION
    };


    public BookData(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    public Book createBook(String title, String author, String year, String publisher,
                           String category, String val) {
        ContentValues values = new ContentValues();
        Log.d("Creating", "Creating " + title + " " + author + " " + publisher + " " + year + " "
                + category + " " + val);

        // Add data: Note that this method only provides title and author
        // Must modify the method to add the full data
        values.put(MySQLiteHelper.COLUMN_TITLE, title);
        values.put(MySQLiteHelper.COLUMN_AUTHOR, author);
        values.put(MySQLiteHelper.COLUMN_PUBLISHER, publisher);
        values.put(MySQLiteHelper.COLUMN_YEAR, year);
        values.put(MySQLiteHelper.COLUMN_CATEGORY, category);
        values.put(MySQLiteHelper.COLUMN_PERSONAL_EVALUATION, val);

        // Actual insertion of the data using the values variable
        long insertId = database.insert(MySQLiteHelper.TABLE_BOOKS, null, values);

        // Main activity calls this procedure to create a new book
        // and uses the result to update the listview.
        // Therefore, we need to get the data from the database
        // (you can use this as a query example) to feed the view.

        Cursor cursor = database.query(
                MySQLiteHelper.TABLE_BOOKS, allColumns,
                MySQLiteHelper.COLUMN_ID + " = " + insertId,
                null,
                null,
                null,
                null
        );

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
        database.delete(MySQLiteHelper.TABLE_BOOKS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();

        Cursor cursor = database.query(
                MySQLiteHelper.TABLE_BOOKS,
                allColumns,
                null,
                null,
                null,
                null,
                null
        );

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



    public void UpdateBook (long id, String titol, String autor, String year, String pub, String cat,
                            String val, String titold, String autold){
        ContentValues cv = new ContentValues();
        cv.put(MySQLiteHelper.COLUMN_ID, id);
        cv.put(MySQLiteHelper.COLUMN_TITLE, titol);
        cv.put(MySQLiteHelper.COLUMN_AUTHOR, autor);
        cv.put(MySQLiteHelper.COLUMN_PUBLISHER, pub);
        cv.put(MySQLiteHelper.COLUMN_YEAR, year);
        cv.put(MySQLiteHelper.COLUMN_CATEGORY, cat);
        cv.put(MySQLiteHelper.COLUMN_PERSONAL_EVALUATION, val);
        String[] args = new String[] {titold, autold};

        database.update(MySQLiteHelper.TABLE_BOOKS, cv, MySQLiteHelper.COLUMN_TITLE + " = ? and " +
                MySQLiteHelper.COLUMN_AUTHOR + " = ? ", args);

    }

     private Book cursorToBook(Cursor cursor) {
        Book book = new Book();
        book.setId(cursor.getLong(0));
        book.setTitle(cursor.getString(1));
        book.setAuthor(cursor.getString(2));
        book.setYear( cursor.getInt(3) );
        book.setPublisher(cursor.getString(4));
        book.setCategory(cursor.getString(5));
        book.setPersonal_evaluation(cursor.getString(6));
        return book;
    }

    public boolean ExistsBook(Book b) {
        String titol = b.getTitle();
        String autor = b.getAuthor();
        int any = b.getYear();
        String editorial = b.getPublisher();
        String categoria = b.getCategory();
        String Query = "SELECT * FROM " + MySQLiteHelper.TABLE_BOOKS +
                " WHERE title = '" + titol +
                "' AND author = '" + autor +  "' ;";
                /*"' AND " + MySQLiteHelper.COLUMN_YEAR + " = '" + any +
                "' AND " + MySQLiteHelper.COLUMN_PUBLISHER + " = '" + editorial +
                "' AND " + MySQLiteHelper.COLUMN_CATEGORY + " = '" + categoria + "' ;";*/

        Cursor cursor = database.rawQuery(Query, null);
        if (cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        else {
            cursor.close();
            return true;
        }
    }

    public boolean ExistsTitle(String title) {
        String[] args = new String[] {title};
        Cursor cursor = database.rawQuery(" SELECT * FROM " + MySQLiteHelper.TABLE_BOOKS + " WHERE " + MySQLiteHelper.COLUMN_TITLE + " = ? ", args);
        if (cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        else {
            cursor.close();
            return true;
        }
    }
}