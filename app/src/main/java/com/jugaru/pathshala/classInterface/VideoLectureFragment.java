package com.jugaru.pathshala.classInterface;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.jugaru.pathshala.R;
import com.jugaru.pathshala.homeFragments.Classes;

import java.util.Objects;

public class VideoLectureFragment extends Fragment {

    private FloatingActionButton floatingActionButton ;
    private FirebaseAuth firebaseAuth;

    public VideoLectureFragment() {
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
        return inflater.inflate(R.layout.fragment_video_lecture, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab_class_video);
        Classes classes = (Classes) Objects.requireNonNull(getActivity()).getIntent().getParcelableExtra("SingleClass");
        if(firebaseAuth.getCurrentUser().getUid().equals(classes.getTeacherUid())){
            floatingActionButton.setVisibility(View.VISIBLE);
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Classes classes = (Classes)getActivity().getIntent().getParcelableExtra("SingleClass");
                Intent intent = new Intent(getContext() , UploadMaterialActivity.class);
                intent.putExtra("themeColor" , classes.getClassThemeColor());
                intent.putExtra("classUid" , classes.getClassUid());
                intent.putExtra("HeadingName" , "Upload Video Lecture" );
                intent.putExtra("Icon" , R.drawable.ic_round_ondemand_video_24 );
                startActivity(intent);
            }
        });
    }
}