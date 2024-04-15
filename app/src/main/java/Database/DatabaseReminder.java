package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;

import Models.ListReminder;
import Models.Photo;
import Models.Reminder;

public class DatabaseReminder extends SQLiteOpenHelper {

    private ListReminderTable listReminder;
    private PhotoTable photo;
    private ReminderTable reminder;

    public DatabaseReminder(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        listReminder = new ListReminderTable();
        photo = new PhotoTable();
        reminder = new ReminderTable();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        listReminder.createTable(db);
        reminder.createTable(db);
        photo.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        listReminder.dropTable(db);
        reminder.dropTable(db);
        photo.dropTable(db);
        onCreate(db);
    }

    public List<Reminder> getReminderByID(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        return reminder.readByListReminderID(db,id);
    }


    public List<ListReminder> getListReminder ()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        List<ListReminder> l =  listReminder.read(db);
        db.close();
        return l;
    }

    public List<Reminder> getReminder ()
    {
        SQLiteDatabase db = getReadableDatabase();
        List<Reminder> l = reminder.read(db);
        db.close();
        return l ;
    }

    public void add (ListReminder lr)
    {
        SQLiteDatabase db = getWritableDatabase();
        listReminder.add(db,lr);
        db.close();

    }

    public void add (Reminder r)
    {
        SQLiteDatabase db = getWritableDatabase();
        reminder.add(db,r);
        Reminder tbl = reminder.readLastRow(getReadableDatabase());
        if (r.getImage().size() > 0)
        {
            PhotoTable photoTable = new PhotoTable();
            for (Uri uri : r.getImage())
            {
                Photo p = new Photo(uri,tbl);
                photoTable.add(db,p);
            }
        }
        db.close();
    }



    public Reminder getLastRowReminder ()
    {
        return reminder.readLastRow(getReadableDatabase());
    }

    public void deleteListReminder (ListReminder lr)
    {

    }


    public void update (Reminder r)
    {
        SQLiteDatabase db = getWritableDatabase();
        reminder.update(db,r);
        db.close();

    }

    public void update (ListReminder lr)
    {
        SQLiteDatabase db = getWritableDatabase();
        listReminder.update(db,lr);
        db.close();

    }

    public void update (Photo p)
    {
        SQLiteDatabase db = getWritableDatabase();
        photo.update(db,p);
        db.close();

    }

    public void delete (Reminder r)
    {
        SQLiteDatabase db = getWritableDatabase();
        reminder.delete(db,r);
        db.close();
    }

    public void delete (ListReminder lr)
    {
        SQLiteDatabase db = getWritableDatabase();
        listReminder.delete(db,lr);
        db.close();

    }

    public void delete (Photo p )
    {
        SQLiteDatabase db = getWritableDatabase();
        photo.delete(db,p);
        db.close();
    }


}
