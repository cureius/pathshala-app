package com.jugaru.pathshala.registration;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jugaru.pathshala.MainActivity;
import com.jugaru.pathshala.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.widget.Toast.LENGTH_SHORT;

public class UserNameActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private Button createAccountBtn;
    private EditText username,firstName,lastName,DOB, city,school,college,occupation,about;
    private ProgressBar progressBar;
    private Uri photoUri;
    public final static String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";
    private StorageReference storage;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);

        init();

        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance().getReference();

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore = FirebaseFirestore.getInstance();
                checkPermission();

            }
        });
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setError(null);
                if (username.getText().toString().isEmpty() || username.getText().toString().length() < 3) {
                    username.setError("At least 3 characters required  ");
                    return;
                }
                if (!username.getText().toString().matches(USERNAME_PATTERN)) {
                    username.setError("Give username using only  \"a to z , _ ,  and 0 to 9\" ");
                    return;
                }
                if (firstName.getText().toString().isEmpty()) {
                    firstName.setError("First name is required");
                    return;
                }
                if (lastName.getText().toString().isEmpty()) {
                    lastName.setError("Last name is required");
                    return;
                }
                if (city.getText().toString().isEmpty()) {
                    city.setError("Give a city");
                    return;
                }
                if (DOB.getText().toString().isEmpty()) {
                    DOB.setError("Enter date of birth");
                    return;
                }
                if (school.getText().toString().isEmpty()) {
                    school.setError("Give a school name");
                    return;
                }
                if (college.getText().toString().isEmpty()) {
                    college.setText("NA");
                    return;
                }if (occupation.getText().toString().isEmpty()) {
                    occupation.setText("NA");
                    return;
                }if (about.getText().toString().isEmpty()) {
                    about.setText("");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                firestore = FirebaseFirestore.getInstance();
                firestore.collection("user").whereEqualTo("username", username.getText().toString())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> document = task.getResult().getDocuments();
                            if (document.isEmpty()) {
                                uploadData();
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                username.setError("username already exists");
                                return;
                            }
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(UserNameActivity.this, error, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
    }

    private void init() {
        profileImageView = findViewById(R.id.profile_image);
        createAccountBtn = findViewById(R.id.create_account_btn);
//        removeBtn = findViewById(R.id.resend_btn);
        progressBar = findViewById(R.id.progressBar);
        username = findViewById(R.id.username);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        DOB = findViewById(R.id.DOB);
        city = findViewById(R.id.place);
        school = findViewById(R.id.school_name);
        college = findViewById(R.id.college_name);
        occupation = findViewById(R.id.occupation);
        about = findViewById(R.id.about);
    }

    private void selectImage() {
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityMenuIconColor(getResources().getColor(R.color.colorAccent))
//                .setBackgroundColor(getResources().getColor(android.R.color.white))
//                .setActivityTitle("")
                .setFixAspectRatio(true)
                .setAspectRatio(1, 1)
                .start(this);
    }

    private void uploadData() {
        if (photoUri != null) {////upload profile with photo
            final StorageReference ref = storage.child("profile/" + firebaseAuth.getCurrentUser().getUid());
            UploadTask uploadTask = ref.putFile(photoUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        String error = task.getException().getMessage();
                        Toast.makeText(UserNameActivity.this, error, Toast.LENGTH_SHORT).show();

                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            url = uri.toString();
                        }
                    });
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        uploadUsername();
                        uploadFirstName();
                        uploadLastName();
                        uploadCity();
                        uploadDOB();
                        uploadSchoolName();
                        uploadCollegeName();
                        uploadAbout();
                        uploadOccupation();
                    } else {
                        // Handle failures
                        // ...
                        progressBar.setVisibility(View.INVISIBLE);
                        String error = task.getException().getMessage();
                        Toast.makeText(UserNameActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            uploadUsername();
            uploadFirstName();
            uploadLastName();
            uploadCity();
            uploadDOB();
            uploadSchoolName();
            uploadCollegeName();
            uploadAbout();
            uploadOccupation();
        }
    }

    private void uploadUsername() {

        Map<String, Object> map = new HashMap<>();
        map.put("username", username.getText().toString());
        map.put("profile_Url", url);

        firestore.collection("user").document(firebaseAuth.getCurrentUser().getUid()).update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent myIntent = new Intent(UserNameActivity.this, MainActivity.class);
                            startActivity(myIntent);
                            finish();
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            String error = task.getException().getMessage();
                            Toast.makeText(UserNameActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void uploadFirstName() {

        Map<String, Object> map = new HashMap<>();
        map.put("FirstName", firstName.getText().toString());
        firestore.collection("user").document(firebaseAuth.getCurrentUser().getUid()).update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!(task.isSuccessful())) {
                            progressBar.setVisibility(View.INVISIBLE);
                            String error = task.getException().getMessage();
                            Toast.makeText(UserNameActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void uploadLastName() {

        Map<String, Object> map = new HashMap<>();
        map.put("LastName", lastName.getText().toString());
        firestore.collection("user").document(firebaseAuth.getCurrentUser().getUid()).update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!(task.isSuccessful())) {
                            progressBar.setVisibility(View.INVISIBLE);
                            String error = task.getException().getMessage();
                            Toast.makeText(UserNameActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void uploadDOB() {

        Map<String, Object> map = new HashMap<>();
        map.put("DateOfBirth", DOB.getText().toString());
        firestore.collection("user").document(firebaseAuth.getCurrentUser().getUid()).update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!(task.isSuccessful())) {
                            progressBar.setVisibility(View.INVISIBLE);
                            String error = task.getException().getMessage();
                            Toast.makeText(UserNameActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void uploadCity() {

        Map<String, Object> map = new HashMap<>();
        map.put("city", city.getText().toString());
        firestore.collection("user").document(firebaseAuth.getCurrentUser().getUid()).update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!(task.isSuccessful())) {
                            progressBar.setVisibility(View.INVISIBLE);
                            String error = task.getException().getMessage();
                            Toast.makeText(UserNameActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void uploadSchoolName() {

        Map<String, Object> map = new HashMap<>();
        map.put("school", school.getText().toString());
        firestore.collection("user").document(firebaseAuth.getCurrentUser().getUid()).update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!(task.isSuccessful())) {
                            progressBar.setVisibility(View.INVISIBLE);
                            String error = task.getException().getMessage();
                            Toast.makeText(UserNameActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void uploadCollegeName() {

        Map<String, Object> map = new HashMap<>();
        map.put("college", college.getText().toString());
        firestore.collection("user").document(firebaseAuth.getCurrentUser().getUid()).update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!(task.isSuccessful())) {
                            progressBar.setVisibility(View.INVISIBLE);
                            String error = task.getException().getMessage();
                            Toast.makeText(UserNameActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void uploadOccupation() {

        Map<String, Object> map = new HashMap<>();
        map.put("occupation", occupation.getText().toString());
        firestore.collection("user").document(firebaseAuth.getCurrentUser().getUid()).update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!(task.isSuccessful())) {
                            progressBar.setVisibility(View.INVISIBLE);
                            String error = task.getException().getMessage();
                            Toast.makeText(UserNameActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void uploadAbout() {

        Map<String, Object> map = new HashMap<>();
        map.put("about", about.getText().toString());
        firestore.collection("user").document(firebaseAuth.getCurrentUser().getUid()).update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!(task.isSuccessful())) {
                            progressBar.setVisibility(View.INVISIBLE);
                            String error = task.getException().getMessage();
                            Toast.makeText(UserNameActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                photoUri = result.getUri();

                Glide
                        .with(this)
                        .load(photoUri)
                        .centerCrop()
                        .placeholder(R.drawable.profileplaceholder)
                        .into(profileImageView);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(), LENGTH_SHORT).show();
            }
        }
    }

    public void checkPermission() {

        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    selectImage();
                } else {
                    Toast.makeText(UserNameActivity.this, "Please give the permission", LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
    }
}