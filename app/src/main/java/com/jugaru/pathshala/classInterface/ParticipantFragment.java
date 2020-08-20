package com.jugaru.pathshala.classInterface;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jugaru.pathshala.R;
import com.jugaru.pathshala.homeFragments.Classes;

public class ParticipantFragment extends Fragment {

    public ParticipantFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_participant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView teacherHeading = view.findViewById(R.id.teacher_heading);
        TextView studentHeading = view.findViewById(R.id.students_heading);
        Classes classes = (Classes)getActivity().getIntent().getParcelableExtra("SingleClass");
        teacherHeading.setTextColor(classes.getClassThemeColor());
        studentHeading.setTextColor(classes.getClassThemeColor());
    }
}