package Interfaces;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public interface IDatabaseTable<T> {

    public void createTable (SQLiteDatabase db);

    public void dropTable (SQLiteDatabase db);

    public void add (SQLiteDatabase db, T obj);
    public void delete (SQLiteDatabase db, T obj);

    public void update (SQLiteDatabase db, T obj);
    public List<T> read (SQLiteDatabase db);

}
