package com.jugaru.pathshala.classInterface;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jugaru.pathshala.MainActivity;
import com.jugaru.pathshala.R;
import com.jugaru.pathshala.homeFragments.Classes;
import com.jugaru.pathshala.registration.RegisterActivity;
import com.jugaru.pathshala.registration.UserNameActivity;

import java.util.HashMap;
import java.util.Map;

public class TeacherFeesFragment extends Fragment {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private LinearLayout UPIsetup , mainPage;
    private EditText UPIid;
    private Button submit;
    public TeacherFeesFragment() {
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
        return inflater.inflate(R.layout.fragment_teacher_fees, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UPIsetup = view.findViewById(R.id.UPI_setup);
        mainPage = view.findViewById(R.id.main_page);
        UPIid = view.findViewById(R.id.upi_id_input);
        submit = view.findViewById(R.id.upi_submit);
        UPIsetup.setVisibility(View.INVISIBLE);
        mainPage.setVisibility(View.INVISIBLE);
        checkUPI();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadUPIid();
            }
        });
    }
    private void uploadUPIid() {
        if(!UPIid.getText().toString().isEmpty()){
            Classes classes = getActivity().getIntent().getParcelableExtra("SingleClass");
            Map<String, Object> map = new HashMap<>();
            map.put("PaymentUPI", UPIid.getText().toString());
            firestore.collection("classes").document(classes.getClassUid()).update(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!(task.isSuccessful())) {
                                String error = task.getException().getMessage();
                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            Toast.makeText(getContext(), "UPI ID has been set", Toast.LENGTH_SHORT).show();
        }else {
            UPIid.setError("Enter Upi ID");
        }
    }
    private  void checkUPI(){
        Classes classes = getActivity().getIntent().getParcelableExtra("SingleClass");
        firestore.collection("classes").document(classes.getClassUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                                if(!task.getResult().contains("PaymentUPI")){
                                    UPIsetup.setVisibility(View.VISIBLE);
                                }else {
                                    mainPage.setVisibility(View.VISIBLE);
                                }
                            return;

                        }else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}