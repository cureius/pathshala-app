package com.jugaru.pathshala.classInterface;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jugaru.pathshala.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class UserProfileActivity extends AppCompatActivity {

    private String firstName , lastName , dateOfBirth , address , school , college , email , phone ,
            username , fullName , aboutProfile , occupation , photoUrl ;
    private TextView userProfileName , userProfileMobileNumber , userProfileEmail , userProfileAddress ,
            userProfileDOB , userProfileSchool , userProfileCollege , userProfileOccupation ,
            userProfileUsername , userProfileAbout , chatBack;
    private CircleImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        init();
        String userId = getIntent().getStringExtra("userId");
        showProfileDetails(userId);
        chatBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }
    private void init(){
        profilePicture = findViewById(R.id.profile_picture);
        userProfileAddress =findViewById(R.id.addressProfileTextView);
        userProfileCollege = findViewById(R.id.collegeProfileTextView);
        userProfileDOB = findViewById(R.id.DOBProfileTextView);
        userProfileEmail = findViewById(R.id.emailProfileTextView);
        userProfileMobileNumber =findViewById(R.id.numberProfileTextView);
        userProfileOccupation = findViewById(R.id.workProfileTextView);
        userProfileSchool = findViewById(R.id.schoolProfileTextView);
        userProfileName = findViewById(R.id.nameProfileTextView);
        userProfileUsername = findViewById(R.id.user_profile_username);
        userProfileAbout = findViewById(R.id.user_profile_about);
        chatBack = findViewById(R.id.edit_profile);

    }
    private void showProfileDetails(String userId) {
        FirebaseFirestore.getInstance()
                .collection("user")
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d(TAG , "onSuccess: " + documentSnapshot.getId());
                        Log.d(TAG , "onSuccess: " + documentSnapshot.getData());
                        Log.d(TAG , "onSuccess: " + documentSnapshot.getString("FirstName"));

                        photoUrl = documentSnapshot.getString("profile_Url");
                        firstName = documentSnapshot.getString("FirstName");
                        lastName = documentSnapshot.getString("LastName");
                        fullName = firstName+" "+lastName;
                        aboutProfile = documentSnapshot.getString("about");
                        occupation = documentSnapshot.getString("occupation");
                        address = documentSnapshot.getString("city");
                        college = documentSnapshot.getString("college");
                        email = documentSnapshot.getString("email");
                        phone = documentSnapshot.getString("phone");
                        dateOfBirth = documentSnapshot.getString("DateOfBirth");
                        school = documentSnapshot.getString("school");
                        username = documentSnapshot.getString("username");

                        userProfileName.setText(fullName);
                        userProfileAddress.setText(address);
                        userProfileCollege.setText(college);
                        userProfileEmail.setText(email);
                        userProfileMobileNumber.setText(phone);
                        userProfileDOB.setText(dateOfBirth);
                        userProfileSchool.setText(school);
                        userProfileUsername.setText(username);
                        userProfileAbout.setText(aboutProfile);
                        userProfileOccupation.setText(occupation);
                        if (!(photoUrl.isEmpty())) {
                            Glide
                                    .with(UserProfileActivity.this)
                                    .load(photoUrl)
                                    .centerCrop()
                                    .placeholder(R.drawable.profileplaceholder)
                                    .into(profilePicture);
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