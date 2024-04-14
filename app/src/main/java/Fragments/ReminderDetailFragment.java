package Fragments;

import android.Manifest;
import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hey.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Adapters.PhotoAdapter;
import Database.DbContext;
import Interfaces.IClickPhotoAdd;
import Models.Reminder;

public class ReminderDetailFragment extends Fragment  {
    private static final String title = "Chi tiết";
    private TextView cancelBtn, addBtn, titleTv;
    private ReminderCreateFragment siblingFragment;
    private CalendarView calendar;
    private TextView dateTv,timeTv;
    private SwitchCompat dateSwitch,timeSwitch,flagSwitch;
    private TimePicker timePicker;
    private CardView dateCard;
    private TextView photoAddTv;
    private RecyclerView photoRv;
    private Uri photoURI;
    private PhotoAdapter adapter;
    private LocalDate date;
    private LocalTime time;

    private Dialog d;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                    getContext().getContentResolver().takePersistableUriPermission(uri, flag);
                    adapter.getPhotos().add(uri);
                    adapter.notifyDataSetChanged();
                }
            });


    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted)
                {
                    dispatchTakePictureIntent();
                }
            });

    private ActivityResultLauncher<Intent> intentActivityPhotoResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o.getResultCode() == -1)
            {
                adapter.getPhotos().add(photoURI);
                adapter.notifyDataSetChanged();
            }
        }
    });

    public ReminderDetailFragment(ReminderCreateFragment siblingFragment,Dialog d) {
        this.siblingFragment = siblingFragment;
        this.d = d;
    }

    public ReminderDetailFragment()
    {

    }

    public static ReminderDetailFragment newInstance() {
        Bundle args = new Bundle();
        ReminderDetailFragment fragment = new ReminderDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ReminderDetailFragment newInstance(ReminderCreateFragment siblingFragment,Dialog d) {

        Bundle args = new Bundle();
        ReminderDetailFragment fragment = new ReminderDetailFragment(siblingFragment, d);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.reminder_detail,null);
        initComponent(v);
        setTitle();
        initEvent();
        initRecycleView ();

        BottomSheetFragment parent = (BottomSheetFragment) getParentFragment();
        if (parent.getReminderInstance().getDate() != null)
        {
            dateSwitch.setChecked(true);
        }
        if (parent.getReminderInstance().getTime() != null)
        {
            timeSwitch.setChecked(true);
        }

        flagSwitch.setChecked(parent.getReminderInstance().getFlag());
        return v;
    }

    private void initRecycleView()
    {
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        BottomSheetFragment parent = (BottomSheetFragment) getParentFragment();
        List<Uri> photoList = parent.getReminderInstance().getImage();
        adapter = new PhotoAdapter(photoList, new IClickPhotoAdd() {
            @Override
            public void removePhoto(int position) {
                adapter.getPhotos().remove(position);
                adapter.notifyDataSetChanged();
                parent.getReminderInstance().getImage().remove(position);
            }
        });
        photoRv.setAdapter(adapter);
        photoRv.setLayoutManager(layout);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void renderDateAndSetLocalDateVariable (int year, int month, int dayOfMonth)
    {
            String dateString = checkDaysBetweenDates (LocalDate.of(year,month+1,dayOfMonth).atStartOfDay(),LocalDate.now().atStartOfDay());
            if (dateString.length() == 0)
            {
                dateString = String.format("ngày %d tháng %d, %d", dayOfMonth, month + 1, year);
                dateString = getDayOfWeek(LocalDate.of(year, month + 1, dayOfMonth).getDayOfWeek().toString()) + ", " + dateString;
            }
            dateTv.setText(dateString);
            date = LocalDate.of(year,month + 1,dayOfMonth);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void renderTimeAndSetLocalTimeVariable (int hourOfDay, int minute)
    {
        String additionalText = "";
        if (!DateFormat.is24HourFormat(getContext()))
        {
            if (hourOfDay > 12)
            {
                hourOfDay = hourOfDay - 12;
                additionalText += "PM";
            }
            else additionalText += "AM";
        }
        String text = "";
        if (hourOfDay < 10) text += "0";
        text += hourOfDay + ":";
        if (minute < 10) text += "0";
        text += minute + "";
        timeTv.setText(text + " " + additionalText);
        time = LocalTime.of(hourOfDay,minute);

    }

    private void initEvent()
    {
        BottomSheetFragment parent = (BottomSheetFragment) getParentFragment();

        addBtn.setOnClickListener(v -> {
            parent.getReminderInstance().setDate(date);
            parent.getReminderInstance().setTime(time);
            Reminder r = parent.getReminderInstance();
            DbContext.getInstance(getContext()).add(r);

        });

        cancelBtn.setOnClickListener(v -> {
            if (siblingFragment != null)
            {
                getParentFragmentManager().popBackStack();
                getParentFragmentManager().beginTransaction().remove(this).commit();
                String s = "";
                if (timeTv.getVisibility() == View.VISIBLE) s+= timeTv.getText() + ", ";
                if (dateTv.getVisibility() == View.VISIBLE) s+= dateTv.getText();
                siblingFragment.setTextDateTime(s);

                parent.getReminderInstance().setDate(date);
                parent.getReminderInstance().setTime(time);
            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    renderDateAndSetLocalDateVariable(year, month, dayOfMonth);
                }
            }
        });

        dateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (dateSwitch.isChecked())
                {
                    if (date == null && parent.getReminderInstance().getDate() == null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            LocalDate today = LocalDate.now();
                            renderDateAndSetLocalDateVariable(today.getYear(),today.getMonth().getValue() - 1,today.getDayOfMonth());
                        }
                    }
                    else if (date == null && parent.getReminderInstance().getDate() != null)
                    {
                        LocalDate date = parent.getReminderInstance().getDate();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            renderDateAndSetLocalDateVariable(date.getYear(),date.getMonthValue() - 1, date.getDayOfMonth());
                        }
                    }
                    dateTv.setVisibility(View.VISIBLE);
                    calendar.setVisibility(View.VISIBLE);
                    calendar.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_down));
                }
                else
                {
                    dateTv.setVisibility(View.GONE);
                    calendar.animate()
                            .translationY(-calendar.getHeight())
                            .alpha(1)
                            .setDuration(300)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(@NonNull Animator animation)
                                {
                                }

                                @Override
                                public void onAnimationEnd(@NonNull Animator animation)
                                {
                                    calendar.setVisibility(View.GONE);
                                    calendar.setTranslationY(0);
                                }

                                @Override
                                public void onAnimationCancel(@NonNull Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(@NonNull Animator animation) {

                                }
                            });
                }
            }

        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    renderTimeAndSetLocalTimeVariable(hourOfDay,minute);
                }
            }
        });

        timeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (timeSwitch.isChecked())
                {
                    dateSwitch.setChecked(true);
                    if (time == null && parent.getReminderInstance().getTime() == null)
                    {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            LocalTime now = LocalTime.now();
                            renderTimeAndSetLocalTimeVariable(now.getHour(),now.getMinute());
                        }
                    }
                    else if (time == null && parent.getReminderInstance().getTime() != null)
                    {
                        LocalTime date = parent.getReminderInstance().getTime();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            renderTimeAndSetLocalTimeVariable(date.getHour(), date.getMinute());
                        }
                    }
                    timeTv.setVisibility(View.VISIBLE);
                    timePicker.setVisibility(View.VISIBLE);
                    timePicker.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_down));
                }
                else
                {
                    timeTv.setVisibility(View.GONE);
                    timePicker.animate()
                            .translationY(-timePicker.getHeight())
                            .alpha(1)
                            .setDuration(300)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(@NonNull Animator animation)
                                {
                                }

                                @Override
                                public void onAnimationEnd(@NonNull Animator animation)
                                {
                                    timePicker.setVisibility(View.GONE);
                                    timePicker.setTranslationY(0);
                                }

                                @Override
                                public void onAnimationCancel(@NonNull Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(@NonNull Animator animation) {

                                }
                            });
                }
            }
        });

        photoAddTv.setOnClickListener(v -> {
            photoAddTv.showContextMenu(photoAddTv.getX() ,photoAddTv.getY());
        });

        flagSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (flagSwitch.isChecked())
                {
                    parent.getReminderInstance().setFlag(true);
                }
                else
                {
                    parent.getReminderInstance().setFlag(false);
                }
            }
        });
    }

    private String checkDaysBetweenDates(LocalDateTime now, LocalDateTime date)
    {
        long dayBetween = 0;
        String res = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dayBetween = Duration.between(date,now).toDays();
            if (dayBetween == -2) return "Hôm kia";
            else if (dayBetween == -1) return "Hôm qua";
            else if (dayBetween == 0) return "Hôm nay";
            else if (dayBetween == 1) return "Ngày mai";
            else if (dayBetween == 2) return "Ngày kia";
        }
        return res;
    }

    private void setTitle() {
        titleTv.setText(title);
        addBtn.setText("Thêm");
        cancelBtn.setText("Quay lại");
        cancelBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.back_icon,0,0,0);

    }

    private void initComponent (View view)
    {
        cancelBtn = view.findViewById(R.id.cancel_bottom_sheet);
        addBtn = view.findViewById(R.id.add_bottom_sheet);
        BottomSheetFragment parent = (BottomSheetFragment) getParentFragment();
        if (parent.getReminderInstance().getReminderName() == null || parent.getReminderInstance().getReminderName().length() == 0)
        {
            addBtn.setEnabled(false);
        }

        else if (parent.getReminderInstance().getReminderName().length() != 0)
        {
            addBtn.setEnabled(true);
        }

        titleTv = view.findViewById(R.id.header_bottom_sheet);
        calendar = view.findViewById(R.id.calendar_choose);
        calendar.setVisibility(View.GONE);
        dateTv = view.findViewById(R.id.Date);
        timeTv = view.findViewById(R.id.Time);
        dateSwitch = view.findViewById(R.id.dateSwitch);
        timePicker = view.findViewById(R.id.timePicker);
        if (android.text.format.DateFormat.is24HourFormat(getContext()))
        {
            timePicker.setIs24HourView(true);
        }

        dateCard = view.findViewById(R.id.calendar_container);
        timeSwitch = view.findViewById(R.id.timeSwitch);
        photoRv = view.findViewById(R.id.photoImages);
        photoAddTv = view.findViewById(R.id.addPhoto);
        registerForContextMenu(photoAddTv);
        photoAddTv.setOnCreateContextMenuListener(this);
        flagSwitch = view.findViewById(R.id.flagSwitch);
    }

    private String getDayOfWeek (String day)
    {
        switch (day)
        {
            case "MONDAY":
                return "Thứ Hai";
            case "TUESDAY":
                return "Thứ Ba";
            case "WEDNESDAY":
                return "Thứ Tư";
            case "THURSDAY":
                return "Thứ Năm";
            case "FRIDAY":
                return "Thứ Sáu";
            case "SATURDAY":
                return "Thứ Bảy";
            case "SUNDAY":
                return "Chủ Nhật";
        }
        return "";
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        new MenuInflater(getContext()).inflate(R.menu.context_menu_photo,menu);

        MenuItem.OnMenuItemClickListener click = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                return onContextItemSelected(item);
            }
        };

        for (int i=0; i<menu.size();i++)
        {
            menu.getItem(i).setOnMenuItemClickListener(click);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.takePhoto)
        {
            if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
            else
            {
                dispatchTakePictureIntent();
            }
        }
        else if (item.getItemId() == R.id.getImage)
        {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        }
        return super.onContextItemSelected(item);
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return image;
    }

    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        photoFile = createImageFile();
        if (photoFile != null)
        {
            photoURI = FileProvider.getUriForFile(getContext(),
                    "com.example.android.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            intentActivityPhotoResult.launch(takePictureIntent);
        }
    }

}

