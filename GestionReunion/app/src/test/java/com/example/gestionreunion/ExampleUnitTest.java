package com.example.gestionreunion;

import android.util.Log;

import com.example.gestionreunion.API.MeetingListGenerator;
import com.example.gestionreunion.API.MeetingListManagement;
import com.example.gestionreunion.DI.Injector;
import com.example.gestionreunion.Model.Meeting;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;

import org.hamcrest.collection.IsEmptyCollection;

import static android.content.ContentValues.TAG;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import java.util.ListIterator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private MeetingListManagement mApiService;
    private String roomToFilter;
    private String dateToFilter;
    private List<Meeting> aList;

    @Before
    public void setUp() {
        mApiService = Injector.getApiService();
        aList = mApiService.getMeetingList();
    }

    @Test
    public void get_meetings_with_success() {
        List<Meeting> newList = mApiService.getMeetingList();
        List<Meeting> originalList = MeetingListGenerator.DEFAULT_MEETING_LIST;
        assertThat(originalList, IsIterableContainingInAnyOrder.containsInAnyOrder(newList.toArray()));
    }

    @Test
    public void add_meeting_to_list() {
        Meeting mMeeting = new Meeting("Room B", "10h50", "Unitaire", "worthyamv@gmail.com", "25/02/2020",
                "11h30");
        mApiService.addMeeting(mMeeting);
        assertTrue(mApiService.getMeetingList().contains(mMeeting));
        // Delete the meeting to bring back initial list
        mApiService.deleteMeeting(mMeeting, aList);
    }

    @Test
    public void delete_meeting_from_list() {
        Meeting meeting = mApiService.getMeetingList().get(0);
        mApiService.deleteMeeting(meeting, aList);
        assertFalse(mApiService.getMeetingList().contains(meeting));
    }

    @Test
    public void filter_by_room() {
        roomToFilter = "Room A";
        List<Meeting> list = mApiService.filterByRoom(roomToFilter);
        for(Meeting meeting : list) {
            assertTrue(meeting.getPlace().equals(roomToFilter));
        }
    }

    @Test
    public void filterByDate(){
        dateToFilter = "14/05/2019";
        List<Meeting> list = mApiService.filterByDate(dateToFilter);
        for(Meeting meeting : list){
            assertTrue(meeting.getDate().equals(dateToFilter));
        }
    }
}