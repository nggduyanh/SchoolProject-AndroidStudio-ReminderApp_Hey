package Models;

import android.net.Uri;

import java.util.ArrayList;

public class Reminder {
    private int id;
    private String reminderName;
    private ArrayList<String> note;
    private Uri image;
    private Boolean status;

    public Reminder(int id, String reminderName){
        this.id =id;
        this.reminderName=reminderName;
        this.status=false;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReminderName() {
        return reminderName;
    }

    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }

    public ArrayList<String> getNote() {
        return note;
    }

    public void setNote(ArrayList<String> note) {
        this.note = note;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}
