package com.jugaru.pathshala.homeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jugaru.pathshala.MainActivity;
import com.jugaru.pathshala.R;

public class TeacherViewFragment extends Fragment {

    public TeacherViewFragment() {
        // Required empty public constructor
    }

    private ImageView viewChangerDots;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teacher_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        viewChangerDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setFragmentTeacherStudent(new StudentViewFragment());
            }
        });

    }

    private void init(View view){
        viewChangerDots =view.findViewById(R.id.homeMenuBtnOfTeacher);
    }
}