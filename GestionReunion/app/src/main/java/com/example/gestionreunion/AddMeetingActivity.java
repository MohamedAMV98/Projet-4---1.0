package com.example.gestionreunion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.gestionreunion.API.MeetingListManagement;
import com.example.gestionreunion.DI.Injector;
import com.example.gestionreunion.Fragment.DatePickerFragment;
import com.example.gestionreunion.Fragment.TimePickerFragment;
import com.example.gestionreunion.Model.Meeting;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static android.graphics.BlendMode.COLOR;
import static android.graphics.Color.parseColor;

public class AddMeetingActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, PopupMenu.OnMenuItemClickListener {

    private MeetingListManagement mApiService;
    List<String> Participants = new ArrayList<>();
    EditText participantsText;
    EditText subjectText;
    Button mTimeButton;
    Button mDateButton;
    Button mRoomButton;
    Button mTime2Button;
    TextView participantsBox;
    ImageButton mAddEmailButton;
    @BindView(R.id.add_button)
    Button addButton;
    String currentDate;
    private boolean isFromClick;
    TextWatcher mTextWatcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);
        ButterKnife.bind(this);
        addMeetingToTheList();
        getSupportActionBar().setHomeButtonEnabled(true);
        mApiService = Injector.getApiService();
        participantsText = findViewById(R.id.mail_entered);
        subjectText = findViewById(R.id.subject_text2);
        addButton = findViewById(R.id.add_button);
        mTimeButton = findViewById(R.id.time_button);
        mDateButton = findViewById(R.id.date_button);
        mRoomButton = findViewById(R.id.assembly_room);
        mTime2Button = findViewById(R.id.time_button2);
        mAddEmailButton = findViewById(R.id.add_mail_button);
        participantsBox = findViewById(R.id.email_box);
        putParticipants();
        init();

        // CHOOSE TIME

        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "timePicker");
                isFromClick = true;
            }
        });

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "datePicker");
            }
        });

        mTime2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker2 = new TimePickerFragment();
                timePicker2.show(getSupportFragmentManager(), "timePicker2");
                isFromClick = false;
            }
        });
    }

    // SHOW POPUP MENU FOR CHOOSING ASSEMBLY ROOM

    public void showPopUpMenu(View v){
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.room_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.salleA :
                mRoomButton.setText(item.getTitle());
            case R.id.salleB :
                mRoomButton.setText(item.getTitle());
            case R.id.salleC :
                mRoomButton.setText(item.getTitle());
            case R.id.salleD :
                mRoomButton.setText(item.getTitle());
            case R.id.salleE :
                mRoomButton.setText(item.getTitle());
            case R.id.salleF :
                mRoomButton.setText(item.getTitle());
            case R.id.salleG :
                mRoomButton.setText(item.getTitle());
            case R.id.salleH :
                mRoomButton.setText(item.getTitle());
            case R.id.salleI :
                mRoomButton.setText(item.getTitle());
            case R.id.salleJ :
                mRoomButton.setText(item.getTitle());
            default: return false;
        }
    }

    public void init(){
        mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String participants = participantsText.getText().toString().trim();
                if(participants.isEmpty());
                    mAddEmailButton.setEnabled(false);
                    mAddEmailButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorGrey));
            }

            @Override
            public void afterTextChanged(Editable s) {
                if((participantsText.getText().length() == 0) || (subjectText.getText().length()
                    == 0) || (mTimeButton.getText().toString().contains("FROM")) || (mTime2Button.getText().toString().contains("TO"))
                || (mDateButton.getText().toString().contains("MEETING DAY")) || (mRoomButton.getText().toString().contains("ASSEMBLY ROOM"))) {
                    addButton.setEnabled(false);
                } else {
                    mAddEmailButton.setEnabled(false);
                    addButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorOrange));
                    //addButton.setEnabled(true);
                }
                if(participantsText.getText().toString().contains("@") && participantsText.getText().toString()
                        .contains(".")) {
                    mAddEmailButton.setEnabled(true);
                    mAddEmailButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorOrange));
                } else
                    mAddEmailButton.setEnabled(false);
            }
        };
        mTimeButton.addTextChangedListener(mTextWatcher);
        mTime2Button.addTextChangedListener(mTextWatcher);
        mRoomButton.addTextChangedListener(mTextWatcher);
        mDateButton.addTextChangedListener(mTextWatcher);
        participantsText.addTextChangedListener(mTextWatcher);
        subjectText.addTextChangedListener(mTextWatcher);
    }



    public void addMeetingToTheList() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Meeting newMeeting = new Meeting(mRoomButton.getText().toString(),
                        mTimeButton.getText().toString(),
                        subjectText.getText().toString(),
                        participantsText.getText().toString(), currentDate
                        );
                mApiService.addMeeting(newMeeting);
                finish();
            }
        });
    }

    // SET TIME
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(isFromClick)
            mTimeButton.setText(hourOfDay + "h" + minute);
        else
            mTime2Button.setText(hourOfDay + "h" + minute);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        mDateButton.setText(currentDate);
    }

    public void putParticipants(){
        mAddEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButton.setEnabled(true);
                String emailToDisplay = participantsText.getText().toString();
                String emailInBox = participantsBox.getText().toString();
                participantsBox.setText(emailToDisplay + ", " + emailInBox);
                participantsText.getText().clear();
            }
        });
    }
}
