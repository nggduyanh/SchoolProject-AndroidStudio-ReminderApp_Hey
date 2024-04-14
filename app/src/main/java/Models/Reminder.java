package Models;

import android.net.Uri;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class Reminder {
    private String note;
    private ListReminder listReminder;
    private int id;
    private String reminderName;

    private List<Uri> image;
    private boolean status,flag;

    private LocalDate date;
    private LocalTime time;
    public Reminder() {
        image = new ArrayList<>();
    }

    public Reminder(int id, String reminderName, boolean flag, LocalDate date, LocalTime time) {
        this.id = id;
        this.reminderName = reminderName;
        this.flag = flag;
        this.date = date;
        this.time = time;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
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


    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public List<Uri> getImage() {
        return image;
    }

    public void setImage(List<Uri> image) {
        this.image = image;
    }

    public void addImage (Uri uri)
    {
        image.add(uri);
    }

    public ListReminder getListReminder() {
        return listReminder;
    }

    public void setListReminder(ListReminder listReminder) {
        this.listReminder = listReminder;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

