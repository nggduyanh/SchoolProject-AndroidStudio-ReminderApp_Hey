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

    public List<ListReminder> getListReminder ()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return listReminder.read(db);
    }

    public List<Reminder> getReminder ()
    {
        SQLiteDatabase db = getReadableDatabase();
        return reminder.read(db);
    }

    public void add (ListReminder lr)
    {
        SQLiteDatabase db = getWritableDatabase();
        listReminder.add(db,lr);
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

    }

    public Reminder getLastRowReminder ()
    {
        return reminder.readLastRow(getReadableDatabase());
    }

    public void deleteListReminder (ListReminder lr)
    {

    }

}
