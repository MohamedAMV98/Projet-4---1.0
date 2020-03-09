package com.example.gestionreunion.API;

import com.example.gestionreunion.Model.Meeting;

import java.util.List;

public class MeetingApiService implements MeetingListManagement {

    List<Meeting> theList = MeetingListGenerator.generateMeetingList();

    @Override
    public List<Meeting> getMeetingList() {
        return theList;
    }

    @Override
    public void addMeeting(Meeting meeting) {
        theList.add(meeting);
    }

    @Override
    public void deleteMeeting(Meeting meeting) {
        theList.remove(meeting);
    }
}
