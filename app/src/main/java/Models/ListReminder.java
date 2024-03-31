package Models;

import android.net.Uri;

public class ListReminder {

    private int id;

    private String listName;

    private int numberReminder;

    private Uri icon;

    public ListReminder(int id, String listName, int numberReminder) {
        this.id = id;
        this.listName = listName;
        this.numberReminder = numberReminder;
    }

    public ListReminder(int id, String listName, int numberReminder, Uri icon) {
        this.id = id;
        this.listName = listName;
        this.numberReminder = numberReminder;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public int getNumberReminder() {
        return numberReminder;
    }

    public void setNumberReminder(int numberReminder) {
        this.numberReminder = numberReminder;
    }

    public Uri getIcon() {
        return icon;
    }

    public void setIcon(Uri icon) {
        this.icon = icon;
    }
}
