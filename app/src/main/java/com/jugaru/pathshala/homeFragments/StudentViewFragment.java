package com.jugaru.pathshala.homeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.jugaru.pathshala.MainActivity;
import com.jugaru.pathshala.R;
import com.jugaru.pathshala.registration.CreateAccountFragment;
import com.jugaru.pathshala.registration.RegisterActivity;


public class StudentViewFragment extends Fragment {

    public StudentViewFragment() {
        // Required empty public constructor
    }

    private ImageView viewChangerDots;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        viewChangerDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setFragmentTeacherStudent(new TeacherViewFragment());
            }
        });
    }

    private void init(View view){
        viewChangerDots =view.findViewById(R.id.homeMenuBtnOfStudent);
    }
}