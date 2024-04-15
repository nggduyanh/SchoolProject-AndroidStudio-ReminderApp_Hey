package Models;

import android.net.Uri;

public class Group {

    private int id;

    private Uri icon;

    private String name;

    private int numberReminder;

    public Group(int id, Uri icon, String name, int numberReminder) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.numberReminder = numberReminder;
    }

    public Group(int id, String name, int numberReminder) {
        this.id = id;
        this.name = name;
        this.numberReminder = numberReminder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Uri getIcon() {
        return icon;
    }

    public void setIcon(Uri icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberReminder() {
        return numberReminder;
    }

    public void setNumberReminder(int numberReminder) {
        this.numberReminder = numberReminder;
    }
}
