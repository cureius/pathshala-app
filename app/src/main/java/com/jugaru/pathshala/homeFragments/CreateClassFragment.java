package com.jugaru.pathshala.homeFragments;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jugaru.pathshala.R;
import com.jugaru.pathshala.registration.UserNameActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateClassFragment extends Fragment {

    public CreateClassFragment() {
        // Required empty public constructor
    }

    private String classUid ;
    private EditText className , instituteName , batch ,  description , fees , teacherUid  ;
    private Button createClassBtn ;
    private StorageReference storage;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_class, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance().getReference();

        createClassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                username.setError(null);
                if (teacherUid.getText().toString().isEmpty()) {
                    teacherUid.setError("Teacher Uid required");
                    return;
                }
                if (className.getText().toString().isEmpty()) {
                    className.setError("Class Name required");
                    return;
                }

                if (instituteName.getText().toString().isEmpty()) {
                    instituteName.setError("First name is required");
                    return;
                }
                if (batch.getText().toString().isEmpty()) {
                    batch.setError("Last name is required");
                    return;
                }
                if (description.getText().toString().isEmpty()) {
                    description.setError("Give a city");
                    return;
                }
                if (fees.getText().toString().isEmpty()) {
                    fees.setError("Enter date of birth");
                    return;
                }
                classUid = className.getText().toString()+"."+teacherUid.getText().toString();
                uploadClassDetails();
//                uploadClassName();
//                uploadInstituteName();
//                uploadClassBatch();
//                uploadClassDescription();
//                uploadClassFee();


                String massage = "Your new class has been created";
                Toast.makeText(getContext(), massage, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.VISIBLE);

            }
        });



    }

    private void init(View view){
        className = view.findViewById(R.id.class_name);
        instituteName = view.findViewById(R.id.institute_name);
        batch = view.findViewById(R.id.batch_year);
        description = view.findViewById(R.id.description);
        fees = view.findViewById(R.id.fees);
        createClassBtn = view.findViewById(R.id.create_class_btn);
        teacherUid = view.findViewById(R.id.teacher_username);

    }

    private void uploadClassName() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put("ClassName", className.getText().toString());
        firestore.collection("classes").document(classUid).set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!(task.isSuccessful())) {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
    private void uploadClassBatch() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put("Batch", batch.getText().toString());
        firestore.collection("classes").document(classUid).set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!(task.isSuccessful())) {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
    private void uploadClassFee() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put("ClassFee", fees.getText().toString());
        firestore.collection("classes").document(classUid).set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!(task.isSuccessful())) {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
    private void uploadClassDescription() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put("ClassDescription", description.getText().toString());
        firestore.collection("classes").document(classUid).set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!(task.isSuccessful())) {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
    private void uploadInstituteName() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put("InstituteName", instituteName.getText().toString());
        firestore.collection("classes").document(classUid).set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!(task.isSuccessful())) {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
    private void uploadClassDetails() {


        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put("Batch", batch.getText().toString());
        map.put("ClassName", className.getText().toString());
        map.put("InstituteName", instituteName.getText().toString());
        map.put("ClassDescription", description.getText().toString());
        map.put("ClassFee", fees.getText().toString());
        map.put("ClassUid", classUid);
        firestore.collection("classes").document(classUid).set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!(task.isSuccessful())) {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

}