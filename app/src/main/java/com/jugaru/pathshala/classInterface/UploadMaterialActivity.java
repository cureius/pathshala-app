package com.jugaru.pathshala.classInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jugaru.pathshala.R;
import com.jugaru.pathshala.registration.UserNameActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UploadMaterialActivity extends AppCompatActivity {

    private CardView selectFileBtn, uploadFileBtn;
    private EditText fileName;
    private TextView uploadFileHeading;
    private ImageView icon;
    StorageReference storageReference;
    FirebaseFirestore firebaseFirestore;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_metarial);

        selectFileBtn = findViewById(R.id.select_file_button);
        uploadFileBtn = findViewById(R.id.upload_file_button);
        fileName = findViewById(R.id.file_name_edit_text);
        icon = findViewById(R.id.upload_file_icon);
        uploadFileHeading = findViewById(R.id.textView_upload_material_heading);

        int themeColor = getIntent().getIntExtra("themeColor", 0);
        final String heading = getIntent().getStringExtra("HeadingName");
        final String classUid = getIntent().getStringExtra("classUid");
        uploadFileHeading.setText(heading);
        icon.setImageResource(getIntent().getIntExtra("Icon", R.drawable.addicon));
        selectFileBtn.setCardBackgroundColor(themeColor);
        uploadFileBtn.setCardBackgroundColor(themeColor);
        icon.setColorFilter(themeColor);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        uploadFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert heading != null;
                if (heading.equals("Upload Notes")) {
                    selectFile();
                } else {
                    selectVideoLecture();
                }
            }
        });
    }
    private void selectFile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF file"), 1);
    }
    private void selectVideoLecture() {
        Intent intent = new Intent();
        intent.setType("video/*");
//        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video file"), 2);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uploadFile(data.getData());
        }
        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uploadVideoFile(data.getData());
        }
    }
    private void uploadVideoFile(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading.....");
        progressDialog.show();
        final String classUid = getIntent().getStringExtra("classUid");
        StorageReference reference = storageReference.child("classes/videoLecture/" + System.currentTimeMillis() + ".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete()) ;
                        Uri url = uri.getResult();
                        UploadClassNotes uploadClassNotes = new UploadClassNotes(fileName.getText().toString(), url.toString());
                        Map<String, Object> map = new HashMap<>();
                        map.put("Material", uploadClassNotes);
                        firebaseFirestore.collection("/classes/" + classUid + "/videos/").document(String.valueOf(System.currentTimeMillis())).set(map)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!(task.isSuccessful())) {
                                            String error = Objects.requireNonNull(task.getException()).getMessage();
                                            Toast.makeText(UploadMaterialActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        Toast.makeText(UploadMaterialActivity.this, "Video Uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded:" + (int) progress + "%");
                        progressDialog.show();
                    }
                });
    }

    private void uploadFile(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading.....");
        progressDialog.show();
        final String classUid = getIntent().getStringExtra("classUid");
        StorageReference reference = storageReference.child("classes/classNote/" + System.currentTimeMillis() + ".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete()) ;
                        Uri url = uri.getResult();
//                        UploadClassNotes uploadClassNotes = new UploadClassNotes(fileName.getText().toString(), url.toString());
                        Map<String, Object> map = new HashMap<>();
                        map.put("fileName", fileName.getText().toString());
                        map.put("fileUri", url.toString());
                        firebaseFirestore.collection("/classes/" + classUid + "/classNotes/").document(String.valueOf(System.currentTimeMillis())).set(map)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!(task.isSuccessful())) {
                                            String error = Objects.requireNonNull(task.getException()).getMessage();
                                            Toast.makeText(UploadMaterialActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        Toast.makeText(UploadMaterialActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded:" + (int) progress + "%");
                        progressDialog.show();
                    }
                });
    }

}