package com.example.gestionreunion.API;

import com.example.gestionreunion.Model.Meeting;

import java.util.List;

public interface MeetingListManagement {

    List<Meeting> getMeetingList();
    void addMeeting(Meeting meeting);
    void deleteMeeting(Meeting meeting);
    List<Meeting> filterByRoom(String room);
    List<Meeting> filterByDate(String date);
}
