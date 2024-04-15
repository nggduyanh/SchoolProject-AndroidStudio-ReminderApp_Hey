package Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import Interfaces.IDatabaseTable;
import Models.ListReminder;

public class ListReminderTable implements IDatabaseTable<ListReminder> {

    public static final String tableName = "DanhSach";

    public static final String icon = "BieuTuong";

    public static final String color = "Mau";

    public static final String id = "IdDanhSach";

    public static final String name = "TenDanhSach";


    public ListReminderTable ()
    {

    }

    @Override
    public void createTable(SQLiteDatabase db) {
        String sql = String.format("create table if not exists %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s TEXT)",tableName,id,icon,color,name);
        db.execSQL(sql);
    }

    @Override
    public void dropTable(SQLiteDatabase db) {
        String sql = String.format("drop table if exists %s",tableName);
        db.execSQL(sql);
    }

    @Override
    public void add(SQLiteDatabase db, ListReminder obj) {
        ContentValues values = new ContentValues();
        values.put(color,obj.getColor());
        values.put(name,obj.getListName());
        values.put(icon,obj.getIcon());
        db.insert(tableName,null,values);

    }

    @Override
    public void delete(SQLiteDatabase db, ListReminder obj) {
        String sql = "Delete from " + tableName + " Where " + id + " = " + obj.getId();
        db.execSQL(sql);

    }

    @Override
    public void update(SQLiteDatabase db, ListReminder obj) {
        ContentValues values = new ContentValues();
        values.put(id,obj.getId());
        values.put(color,obj.getColor());
        values.put(name,obj.getListName());
        values.put(icon,obj.getIcon());
        db.update(tableName,values,id + "=?", new String[] {String.valueOf(obj.getId())});

    }

    @Override
    public List<ListReminder> read(SQLiteDatabase db) {
        List<ListReminder> list = new ArrayList<>();
        String sql = "select * from " + tableName;
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                ListReminder listReminder = new ListReminder(
                        cursor.getInt(0),
                        cursor.getString(3),
                        0,
                        cursor.getInt(1),
                        cursor.getInt(2));

                ReminderTable reminderTable = new ReminderTable();
                int reminderNumber = reminderTable.readByListReminderID(db,cursor.getInt(0)).size();
                listReminder.setNumberReminder(reminderNumber);
                list.add(listReminder);
            }
        }
        return list;
    }


}
