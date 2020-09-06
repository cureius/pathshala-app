package com.jugaru.pathshala.classInterface;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.LogDescriptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jugaru.pathshala.R;
import com.jugaru.pathshala.homeFragments.Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class ParticipantFragment extends Fragment {
    private TextView teacherName , teacherUsername ;
    private CircleImageView teacherPic ;
    private FirebaseAuth firebaseAuth ;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private String photoUrl ;
    private static ArrayList<String> listOfStudents = new ArrayList<String>();
    private ArrayList<UserProfile> list = new ArrayList<>();
    private RecyclerView participantRecyclerView;
    private ProfileAdapter profileAdapter;

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
        teacherName = view.findViewById(R.id.user);
        teacherUsername = view.findViewById(R.id.profile_username_model_teacher);
        teacherPic = view.findViewById(R.id.profile_pic_model_teacher);
        participantRecyclerView = view.findViewById(R.id.class_participant_recyclerview);
        Classes classes = (Classes)getActivity().getIntent().getParcelableExtra("SingleClass");
        teacherHeading.setTextColor(classes.getClassThemeColor());
        studentHeading.setTextColor(classes.getClassThemeColor());
        String classTeacher = classes.getTeacherUid();

        participantRecyclerView.setHasFixedSize(true);
        participantRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        StorageReference storage = FirebaseStorage.getInstance().getReference();

        showTeacherDetails(classTeacher);
        String classUid = classes.getClassUid();

        db = FirebaseFirestore.getInstance();
        if (list.size() > 0)
            list.clear();
        db = FirebaseFirestore.getInstance();
        db.collection("user")
                .whereArrayContains("ClassList" , classUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot: task.getResult()){
                            UserProfile userProfile = new UserProfile();
                            userProfile.setUsername(documentSnapshot.getString("username"));
                            userProfile.setFirstName(documentSnapshot.getString("FirstName"));
                            userProfile.setLastName(documentSnapshot.getString("LastName"));
                            userProfile.setProfile_Url(documentSnapshot.getString("profile_Url"));
                            userProfile.setUserId(documentSnapshot.getId());
                            list.add(userProfile);
                        }
                        profileAdapter = new ProfileAdapter(getContext() , list);
                        participantRecyclerView.setAdapter(profileAdapter);
                        Log.d(TAG , "onSuccessListMain: " +list.toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
//        Log.d(TAG , "onSuccessListMain: " +profileAdapter.getItemCount());
    }
    private void showTeacherDetails(String classTeacher){
        FirebaseFirestore.getInstance()
                .collection("user")
                .document(classTeacher)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d(TAG , "onSuccess: " + documentSnapshot.getId());
                        Log.d(TAG , "onSuccess: " + documentSnapshot.getData());
                        Log.d(TAG , "onSuccess: " + documentSnapshot.getString("FirstName"));
                        teacherName.setText(documentSnapshot.getString("FirstName") + " " + documentSnapshot.getString("LastName"));
                        photoUrl = documentSnapshot.getString("profile_Url");
                        teacherUsername.setText(documentSnapshot.getString("username"));
                        if (!(photoUrl.isEmpty())) {
//                            Picasso.get().load(photoUrl).into(profilePicture);
                            Glide
                                    .with(Objects.requireNonNull(getActivity()).getApplicationContext())
                                    .load(photoUrl)
                                    .centerCrop()
                                    .placeholder(R.drawable.profileplaceholder)
                                    .into(teacherPic);
                            return;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}