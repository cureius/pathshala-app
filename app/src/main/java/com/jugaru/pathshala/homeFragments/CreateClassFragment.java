package com.jugaru.pathshala.homeFragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jugaru.pathshala.R;
import com.jugaru.pathshala.registration.UserNameActivity;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yuku.ambilwarna.AmbilWarnaDialog;

import static android.content.ContentValues.TAG;

public class CreateClassFragment extends Fragment {

    public CreateClassFragment() {
        // Required empty public constructor
    }

    private String classUid , teacherUsername ;
    private TextView themeBar;
    private EditText className , instituteName , batch ,  description , fees , classSubject;
    private Button createClassBtn , setThemeColourBtn;
    private StorageReference storage;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private String teacherUid;
    int themeDefaultColour = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_class, container, false);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        themeDefaultColour = 30155229;
        themeBar.setBackgroundColor(R.color.unitedNationBlue);
        Log.d(TAG, "onSuccess: " + themeDefaultColour);
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance().getReference();

        FirebaseFirestore.getInstance()
                .collection("user")
                .document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d(TAG , "onSuccess: " + documentSnapshot.getId());
                        Log.d(TAG , "onSuccess: " + documentSnapshot.getData());
                        Log.d(TAG , "onSuccess: " + documentSnapshot.getString("FirstName"));

                        teacherUsername= documentSnapshot.getString("username");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        setThemeColourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(getContext(), themeDefaultColour, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        themeDefaultColour = color;
                        themeBar.setBackgroundColor(themeDefaultColour);
                        setThemeColourBtn.setBackgroundColor(themeDefaultColour);
                    }
                });
                colorPicker.show();
            }
        });

        createClassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                username.setError(null);
                if (className.getText().toString().isEmpty()) {
                    className.setError("Class Name required");
                    return;
                }
                if (classSubject.getText().toString().isEmpty()) {
                    classSubject.setError("Class Subject required");
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

                teacherUid = firebaseAuth.getCurrentUser().getUid();
                classUid = className.getText().toString().toLowerCase().trim()+"."+teacherUsername;
                uploadClassDetails();

                String massage = "Your new class has been created";
                Toast.makeText(getContext(), massage, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.VISIBLE);
            }
        });

    }

    private void init(View view){
        className = view.findViewById(R.id.class_name);
        classSubject = view.findViewById(R.id.class_subject);
        instituteName = view.findViewById(R.id.institute_name);
        batch = view.findViewById(R.id.batch_year);
        description = view.findViewById(R.id.description);
        fees = view.findViewById(R.id.fees);
        createClassBtn = view.findViewById(R.id.create_class_btn);
        setThemeColourBtn = view.findViewById(R.id.choose_class_theme_colour_btn);
        themeBar = view.findViewById(R.id.class_registration_theme);
    }

    private void uploadClassDetails() {

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put("Batch", batch.getText().toString());
        map.put("TeacherUid", teacherUid);
        map.put("TeacherUsername", teacherUsername);
        map.put("ClassName", className.getText().toString());
        map.put("ClassSubject", classSubject.getText().toString());
        map.put("InstituteName", instituteName.getText().toString());
        map.put("ClassDescription", description.getText().toString());
        map.put("ClassFee", fees.getText().toString());
        map.put("ClassUid", classUid);
        map.put("ClassThemeColor", themeDefaultColour);
        map.put("StudentList", Arrays.asList("demoStudent0" , "demoStudent1"));

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