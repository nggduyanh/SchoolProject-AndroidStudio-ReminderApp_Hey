package Models;

import android.net.Uri;

public class Photo {

    private int id;
    private Uri photoUri;

    private Reminder reminder;

    public Photo(int id,Uri photoUri, Reminder reminder) {
        this.photoUri = photoUri;
        this.reminder = reminder;
        this.id = id;
    }

    public Photo(Uri photoUri, Reminder reminder) {
        this.photoUri = photoUri;
        this.reminder = reminder;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

    public Reminder getReminder() {
        return reminder;
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
