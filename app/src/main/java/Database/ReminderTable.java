package Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import Interfaces.IDatabaseTable;
import Models.ListReminder;
import Models.Reminder;

public class ReminderTable implements IDatabaseTable<Reminder> {

    public static final String tableName = "LoiNhac";
    public static final String name = "TenLoiNhac";
    public static final String note = "GhiChu";
    public static final String date = "Ngay";
    public static final String hour = "Gio";
    public static final String flag = "GanCo";
    public static final String ID = "IdLoiNhac";
    public static final String ListReminderFK = "IdDanhSach";

    @Override
    public void createTable(SQLiteDatabase db) {
        String sql = String.format("create table if not exists %s (%s Integer PRIMARY KEY AUTOINCREMENT, %s Text,%s Text, %s Text, %s Text, %s Integer, %s Integer, FOREIGN KEY (%s) REFERENCES DanhSach (IdDanhSach)) ",tableName,ID,name,note,date,hour,flag,ListReminderFK,ListReminderFK);
        db.execSQL(sql);
    }

    @Override
    public void dropTable(SQLiteDatabase db) {
        String sql = String.format("drop table if exists %s",tableName);
        db.execSQL(sql);
    }

    @Override
    public void add(SQLiteDatabase db, Reminder obj) {
        ContentValues values = new ContentValues();
        values.put(ID,obj.getId());
        values.put(name,obj.getReminderName());
        values.put(date,obj.getDate() == null ? null : obj.getDate().toString());
        values.put(hour,obj.getTime() == null ? null : obj.getTime().toString());
        values.put(flag,obj.getFlag() ? 1 : 0);
//        values.put(note,obj.getNote());
        values.put(ListReminderFK,obj.getListReminder().getId());
        db.insert(tableName,null,values);
        db.close();
    }

    @Override
    public void delete(SQLiteDatabase db, Reminder obj) {
        if (obj.getImage().size() == 0)
        {
            String sql = "Delete from " + tableName + " Where " + ID + " = " + obj.getId();
            db.execSQL(sql);
            db.close();
        }
    }

    @Override
    public void update(SQLiteDatabase db, Reminder obj) {
        ContentValues values = new ContentValues();
        values.put(ID,obj.getId());
        values.put(name,obj.getReminderName());
        values.put(date,obj.getDate() == null ? null : obj.getDate().toString());
        values.put(hour,obj.getTime() == null ? null : obj.getTime().toString());
        values.put(flag,obj.getFlag() ? 1 : 0);
        values.put(note,obj.getNote());
        values.put(ListReminderFK,obj.getListReminder().getId());
        db.update(tableName,values,ID + "=?", new String[] {String.valueOf(obj.getId())});
        db.close();
    }

    @Override
    public List<Reminder> read(SQLiteDatabase db)
    {
        List<Reminder> list = new ArrayList<>();
        String sql = "select * from " + tableName;
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                Reminder reminder = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    reminder = new Reminder(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getInt(4) == 1,
                            LocalDate.parse(cursor.getString(2)),
                            LocalTime.parse(cursor.getString(3)));
                }
                list.add(reminder);
            }
        }
        return list;
    }

    public List<Reminder> readByListReminderID (SQLiteDatabase db, int listReminderID)
    {
        List<Reminder> list = new ArrayList<>();
        String sql = "select * from " + tableName + " where " + ListReminderFK + " = ?";
        Cursor cursor = db.rawQuery(sql,new String[] {String.valueOf(listReminderID)});
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                Reminder reminder = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    reminder = new Reminder(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getInt(4) == 1,
                            cursor.getString(2) == null ? null : LocalDate.parse(cursor.getString(2)),
                            cursor.getString(3) == null ? null: LocalTime.parse(cursor.getString(3)));
                }
                list.add(reminder);
            }
        }
        return list;
    }
}
