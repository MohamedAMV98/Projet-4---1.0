package com.example.gestionreunion.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.gestionreunion.API.MeetingListManagement;
import com.example.gestionreunion.DI.Injector;
import com.example.gestionreunion.Model.Meeting;

import java.util.List;

public class DeleteDialogFragment extends AppCompatDialogFragment {

    MeetingListManagement mApiService;
    public deleteMeetingInterface ourInterface;
    public Meeting ourMeeting;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Meeting")
                .setMessage("Do you really want to delete this meeting ?")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("YES, I DO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ourInterface.deleteMeeting(ourMeeting);
                    }
                });
        return builder.create();
    }

    public interface deleteMeetingInterface{
        void deleteMeeting(Meeting meeting);
    }

    public static DeleteDialogFragment newInstance() {
        return new DeleteDialogFragment();
    }

    public void setInterface(deleteMeetingInterface theInterface, Meeting theMeeting){
        ourInterface = theInterface;
        ourMeeting = theMeeting;
    }
}
