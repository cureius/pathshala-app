package com.jugaru.pathshala.classInterface;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.jugaru.pathshala.R;
import com.jugaru.pathshala.homeFragments.Classes;

import java.util.Objects;

public class ClassNotesFragment extends Fragment {

    private FloatingActionButton floatingActionButton;
    private FirebaseAuth firebaseAuth;
    public ClassNotesFragment() {
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
        return inflater.inflate(R.layout.fragment_class_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        super.onViewCreated(view, savedInstanceState);
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab_class_notes);
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
                intent.putExtra("HeadingName" , "Upload Notes" );
                intent.putExtra("Icon" , R.drawable.ic_baseline_insert_drive_file_24);

                startActivity(intent);
            }
        });
    }
}