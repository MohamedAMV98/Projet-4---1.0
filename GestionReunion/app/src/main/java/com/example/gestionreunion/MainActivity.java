package com.example.gestionreunion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.BindView;

import com.example.gestionreunion.API.MeetingListManagement;
import com.example.gestionreunion.DI.Injector;
import com.example.gestionreunion.Fragment.DatePickerFragment;
import com.example.gestionreunion.Model.Meeting;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.fab)
    FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    MeetingListManagement mApiService;
    MeetingAdapter mMeetingAdapter;
    private List<Meeting> mList;
    String searchDate;

    List<Meeting> filteredDateList = new ArrayList<>();
    List<Meeting> filteredRoomList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // INIT LIST
        mApiService = Injector.getApiService();
        mList = mApiService.getMeetingList();
        mRecyclerView = findViewById(R.id.recycler_view);
        mMeetingAdapter = new MeetingAdapter(mList, this);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mMeetingAdapter);
        // GO TO ADD MEETING MENU
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenAddMeetingActivity();
            }
        });
        initList(mList);
    }

    public void initList(List<Meeting> aList) {
        mRecyclerView.setAdapter(new MeetingAdapter(aList, this));
    }

    public void OpenAddMeetingActivity() {
        Intent goTo = new Intent(this, AddMeetingActivity.class);
        startActivity(goTo);
    }

    @Override
    protected void onResume() {
        initList(mList);
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(!(item.getTitle().toString().contains("Assembly Room") || item.getTitle().toString().contains("Assembly Date")))
            initList(mApiService.filterByRoom(item.getTitle().toString().toLowerCase()));
        switch (item.getItemId()) {
            case R.id.item_date:
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "datepicker");
                return true;
            case R.id.item_one:
                filteredDateList.clear();
                filteredRoomList.clear();
                initList(mList);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.exemple_menu, menu);
        return true;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);
        searchDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        initList(mApiService.filterByDate(searchDate));
    }
}
