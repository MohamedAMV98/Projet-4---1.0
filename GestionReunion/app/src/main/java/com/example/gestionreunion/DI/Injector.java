package com.example.gestionreunion.DI;

import com.example.gestionreunion.API.MeetingApiService;
import com.example.gestionreunion.API.MeetingListManagement;

public class Injector {

    public static MeetingListManagement mApiService = new MeetingApiService();

    public static MeetingListManagement getApiService() {
        return mApiService;
    }
}
