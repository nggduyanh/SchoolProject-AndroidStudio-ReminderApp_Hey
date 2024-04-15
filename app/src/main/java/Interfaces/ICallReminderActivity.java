package Interfaces;

import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public interface ICallReminderActivity {

    void intentCall();

    void getListPosition(int position);
}
