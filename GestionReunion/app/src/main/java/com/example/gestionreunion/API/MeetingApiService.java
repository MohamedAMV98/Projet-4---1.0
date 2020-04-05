package com.example.gestionreunion.API;

import com.example.gestionreunion.Model.Meeting;

import java.util.ArrayList;
import java.util.List;

public class MeetingApiService implements MeetingListManagement {

    private List<Meeting> theList = MeetingListGenerator.generateMeetingList();

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

    @Override
    public List<Meeting> filterByRoom(String room) {
        List<Meeting> newList = new ArrayList<>();
        for(Meeting meeting : theList){
            if (meeting.getPlace().toLowerCase().contains(room.toLowerCase())){
                newList.add(meeting);
            }
        }
        return newList;
    }

    @Override
    public List<Meeting> filterByDate(String date) {
        List<Meeting> newList = new ArrayList<>();
        for (Meeting meeting : theList) {
            if (meeting.getDate().contains(date))
                newList.add(meeting);
        }
        return newList;
    }
}
