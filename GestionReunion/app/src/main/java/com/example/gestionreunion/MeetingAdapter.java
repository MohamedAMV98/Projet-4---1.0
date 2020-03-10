package com.example.gestionreunion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionreunion.API.MeetingListManagement;
import com.example.gestionreunion.DI.Injector;
import com.example.gestionreunion.Fragment.DeleteDialogFragment;
import com.example.gestionreunion.Model.Meeting;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Intent.getIntent;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.reunionViewHolder> implements DeleteDialogFragment.deleteMeetingInterface{
    private List<Meeting> theList;
    public Activity mContext;
    DeleteDialogFragment mDialog;
    public int thePosition;
    public final String POSITION_KEY = "THE KEY";

    @Override
    public void deleteMeeting(Meeting meeting) {
        theList.remove(meeting);
        notifyItemRemoved(theList.indexOf(meeting));
        notifyItemRangeChanged(theList.indexOf(meeting), theList.size());
    }

    public class reunionViewHolder extends RecyclerView.ViewHolder {
        public TextView mPlace;
        public TextView mHour;
        public TextView mSubject;
        public TextView mParticipants;
        public ImageButton mDeleteButton;
        public reunionViewHolder(@NonNull View itemView) {
            super(itemView);
            mPlace = itemView.findViewById(R.id.meeting_place);
            mHour = itemView.findViewById(R.id.meeting_hour);
            mSubject = itemView.findViewById(R.id.meeting_subject);
            mParticipants = itemView.findViewById(R.id.meeting_participants);
            mDeleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    public MeetingAdapter(List<Meeting> meetingList, Activity context) {
        theList = meetingList;
        mContext = context;
    }

    @NonNull
    @Override
    public reunionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reunion_fragment, parent, false);
        reunionViewHolder rvh = new reunionViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(reunionViewHolder holder, final int position) {
        mDialog = new DeleteDialogFragment();
        thePosition = position;
        Meeting mMeeting = theList.get(position);
        holder.mPlace.setText(mMeeting.getPlace());
        holder.mParticipants.setText(mMeeting.getParticipants());
        holder.mSubject.setText(mMeeting.getSubject());
        holder.mHour.setText(mMeeting.getHour());
        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    @Override
    public int getItemCount() { return theList.size(); }

    /**
     * Configure dialog
     */

    public void openDialog(){
        DeleteDialogFragment dialog = DeleteDialogFragment.newInstance();
        dialog.show(((MainActivity)mContext).getSupportFragmentManager(), "this");
        dialog.setInterface(this, theList.get(thePosition));
    }
}


