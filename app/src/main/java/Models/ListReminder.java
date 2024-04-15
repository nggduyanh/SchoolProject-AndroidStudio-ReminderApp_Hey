package Models;

import android.net.Uri;

public class ListReminder {

    private int id;
    private String listName;
    private int numberReminder;
    private int icon;
    private int color;

    public ListReminder() {
    }

    public ListReminder(int id) {
        this.id = id;
    }

    public ListReminder(int id, String listName, int numberReminder) {
        this.id = id;
        this.listName = listName;
        this.numberReminder = numberReminder;
    }

    public ListReminder(String listName, int icon, int color) {
        this.listName = listName;
        this.icon = icon;
        this.color = color;
    }

    public ListReminder(int id, String listName, int numberReminder, int icon, int color) {
        this.id = id;
        this.listName = listName;
        this.numberReminder = numberReminder;
        this.icon = icon;
        this.color = color;
    }

    public ListReminder(String listName, int numberReminder, int icon, int color) {
        this.listName = listName;
        this.numberReminder = numberReminder;
        this.icon = icon;
        this.color = color;
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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
