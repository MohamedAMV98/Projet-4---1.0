package com.example.gestionreunion.Model;

import android.widget.TextView;


public class Meeting {
    String mPlace;
    String mHour;
    String mEndHour;
    String mSubject;
    String mParticipants;
    String mDate;

    public Meeting(String place, String hour, String subject, String participants, String date, String endHour) {
        mPlace = place;
        mHour = hour;
        mSubject = subject;
        mParticipants = participants;
        mDate = date;
        mEndHour = endHour;
    }

    public String getPlace() {
        return mPlace;
    }

    public void setPlace(String location) {
        mPlace = location;
    }

    public String getHour() {
        return mHour;
    }

    public void setHour(String hour) {
        mHour = hour;
    }

    public String getSubject() {
        return mSubject;
    }

    public void setSubject(String subject) {
        mSubject = subject;
    }

    public String getParticipants() {
        return mParticipants;
    }

    public void setParticipants(String participants) { mParticipants = participants; }

    public String getDate() {
        return mDate;
    }

    public String getEndHour() {
        return mEndHour;
    }

    public void setDate(String date) {
        mDate = date;
    }
}


