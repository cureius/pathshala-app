package com.jugaru.pathshala.classInterface;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jugaru.pathshala.R;
import com.jugaru.pathshala.homeFragments.Classes;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class ParticipantFragment extends Fragment {
    private TextView teacherName , teacherUsername ;
    private CircleImageView teacherPic ;
    private FirebaseAuth firebaseAuth ;
    private FirebaseUser firebaseUser;
    private StorageReference storage;
    private  String photoUrl ;

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

        teacherName = view.findViewById(R.id.user);
        teacherUsername = view.findViewById(R.id.profile_username_model_teacher);
        teacherPic = view.findViewById(R.id.profile_pic_model_teacher);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance().getReference();

        FirebaseFirestore.getInstance()
                .collection("user")
                .document(classes.getTeacherUid())
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
                                    .with(getContext())
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