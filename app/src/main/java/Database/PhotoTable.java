package Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Interfaces.IDatabaseTable;
import Models.Photo;
import Models.Reminder;

public class PhotoTable implements IDatabaseTable<Photo> {

    public static final String tableName = "Anh";

    public static final String UriPhoto = "Uri";

    public static final String ID = "IdAnh";

    public static final String ReminderFK = "IdLoiNhac";

    @Override
    public void createTable(SQLiteDatabase db) {

        String sql = String.format("create table if not exists %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT, %s Integer, FOREIGN KEY (%s) REFERENCES LoiNhac(IdLoiNhac) ON DELETE CASCADE)", tableName, ID, UriPhoto, ReminderFK, ReminderFK);
        db.execSQL(sql);
    }

    @Override
    public void dropTable(SQLiteDatabase db) {
        String sql = String.format("drop table if exists %s", tableName);
        db.execSQL(sql);
    }

    @Override
    public void add(SQLiteDatabase db, Photo obj) {
        ContentValues values = new ContentValues();
        values.put(UriPhoto, obj.getPhotoUri().toString());
        values.put(ReminderFK, obj.getReminder().getId());
        db.insert(tableName, null, values);
    }

    @Override
    public void delete(SQLiteDatabase db, Photo obj) {
        String sql = "Delete from " + tableName + " Where " + UriPhoto + " = " + obj.getPhotoUri() + " AND " + ReminderFK + " = " + obj.getReminder().getId();
        db.execSQL(sql);
    }

    public void deleteByReminder(SQLiteDatabase db, Reminder r)
    {
        String sql = "Delete from " + tableName + " Where " +  ReminderFK + " = " + r.getId();
        db.execSQL(sql);
    }

    @Override
    public void update(SQLiteDatabase db, Photo obj)
    {
        ContentValues values = new ContentValues();
        values.put(UriPhoto,obj.getId());
        db.update(tableName,values,ID + "=?", new String[] {String.valueOf(obj.getId())});

    }

    @Override
    public List<Photo> read(SQLiteDatabase db) {
        return null;
    }

    public List<Photo> readByReminder (SQLiteDatabase db,Reminder r)
    {
        List<Photo> list = new ArrayList<>();
        String sql = "select * from " + tableName + " where " + ReminderFK + " = ?";
        Cursor cursor = db.rawQuery(sql,new String[] {String.valueOf(r.getId())});
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                Photo photo = new Photo(
                        cursor.getInt(0),
                        Uri.parse(cursor.getString(1)),
                        r);
                list.add(photo);
            }
        }
        return list;
    }
}
