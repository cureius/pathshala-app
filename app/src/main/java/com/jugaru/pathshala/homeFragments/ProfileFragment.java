package com.jugaru.pathshala.homeFragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jugaru.pathshala.R;
import com.jugaru.pathshala.registration.RegisterActivity;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class ProfileFragment extends Fragment {

    private String firstName , lastName , dateOfBirth , address , school , college , email , phone , username , fullName , aboutProfile , occupation , photoUrl ;
    private TextView userProfileName , userProfileMobileNumber , userProfileEmail , userProfileAddress , userProfileDOB , userProfileSchool , userProfileCollege ,
            userProfileOccupation , userProfileUsername , userProfileAbout , editTV;
    private RelativeLayout logOut , deleteAccount , editProfile;
    private StorageReference storage;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private CircleImageView profilePicture;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance().getReference();

        showProfileDetails();

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.MyDialogTheme) ;
                dialog.setTitle("Log Out !");
                dialog.setIcon(R.drawable.ic_baseline_person_24);
                dialog.setMessage("Do you really want to logout from your account ");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.MyDialogTheme) ;
                dialog.setTitle("Delete Account?");
                dialog.setIcon(R.drawable.ic_round_delete_forever_24);
                dialog.setMessage("Do you really want to delete your account for ever ");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMyAccount();
                    }
                });
                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileTOUsernameIntent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(profileTOUsernameIntent);
            }
        });

        editTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileTOUsernameIntent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(profileTOUsernameIntent);
            }
        });

    }
    private void init(View view){
        profilePicture = view.findViewById(R.id.profile_picture);
        userProfileAddress = view.findViewById(R.id.addressProfileTextView);
        userProfileCollege = view.findViewById(R.id.collegeProfileTextView);
        userProfileDOB = view.findViewById(R.id.DOBProfileTextView);
        userProfileEmail = view.findViewById(R.id.emailProfileTextView);
        userProfileMobileNumber = view.findViewById(R.id.numberProfileTextView);
        userProfileOccupation = view.findViewById(R.id.workProfileTextView);
        userProfileSchool = view.findViewById(R.id.schoolProfileTextView);
        userProfileName = view.findViewById(R.id.nameProfileTextView);
        userProfileUsername = view.findViewById(R.id.user_profile_username);
        userProfileAbout = view.findViewById(R.id.user_profile_about);
        logOut = view.findViewById(R.id.logoutProfileRelativeLayout);
        deleteAccount = view.findViewById(R.id.deleteProfileRelativeLayout);
        editProfile = view.findViewById(R.id.editProfileRelativeLayout);
        editTV = view.findViewById(R.id.edit_profile);

    }

    private void showProfileDetails() {
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
//                            Picasso.get().load(photoUrl).into(profilePicture);
                            Glide
                                    .with(getContext())
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

    private void deleteMyAccount() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("user").document(firebaseAuth.getCurrentUser().getUid()).delete() .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent profileTOLoginIntent = new Intent(getContext(), RegisterActivity.class);
                        startActivity(profileTOLoginIntent);
                        getActivity().finish();
                    }

                });

            }
        });
    }

    private void logout(){
        firebaseAuth.signOut();
        Intent profileTOLoginIntent = new Intent(getContext(), RegisterActivity.class);
        startActivity(profileTOLoginIntent);
        getActivity().finish();
    }
}