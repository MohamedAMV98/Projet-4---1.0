package com.example.gestionreunion.API;

import com.example.gestionreunion.Model.Meeting;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class MeetingListGenerator {
    public static List<Meeting> DEFAULT_MEETING_LIST = Arrays.asList(
            new Meeting("Room A", "10h58", "Gestion", "mohamedsheneet@gmail.com", "14/05/2019", "12h58"),
            new Meeting("Room B", "12h00", "Direction", "worthyamv@gmail.com", "14/05/2019", "14h00"),
            new Meeting("Room C", "14h00", "Ressources","jeremydu89@gmail.com", "14/05/2019","16h00"),
            new Meeting("Room D", "15h00", "Effectif", "geraldinedu01@gmail.com", "14/05/2019", "17h00")
            );

    static List<Meeting> generateMeetingList(){ return new ArrayList<>(DEFAULT_MEETING_LIST);}
}
