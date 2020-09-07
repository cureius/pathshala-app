package com.jugaru.pathshala.classInterface;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jugaru.pathshala.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MyNoteAdapter extends FirestoreRecyclerAdapter<UploadClassNotes, MyNoteAdapter.NotesViewHolder> {

    boolean teacher;
    Context context;
    String classUid;
    public MyNoteAdapter(@NonNull FirestoreRecyclerOptions<UploadClassNotes> options, boolean teacher, Context context, String classUid) {
        super(options);
        this.teacher = teacher;
        this.context = context;
        this.classUid = classUid;
    }

    @Override
    protected void onBindViewHolder(@NonNull final NotesViewHolder holder, final int position, @NonNull final UploadClassNotes model) {

        String identify = model.getFileUrl();
        holder.materialName.setText(model.getFileName());
        holder.materialTopic.setText(model.getFileTopic());
        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile(holder.materialName.getContext() , model.getFileName() ,
                ".pdf" , DIRECTORY_DOWNLOADS , model.getFileUrl());
            }
        });
        holder.deleteBtn.setVisibility(View.INVISIBLE);
        if(teacher){
            holder.deleteBtn.setVisibility(View.VISIBLE);
        }
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.MyDialogTheme) ;
                dialog.setTitle("Delete File ?");
                dialog.setIcon(R.drawable.ic_round_delete_forever_24);
                dialog.setMessage("Do you really want to delete your file for ever ");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFile(model.getFileUrl());
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
        holder.touchOnNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.materialName.getContext() , NotesViewerActivity.class);
                intent.putExtra("url" , model.getFileUrl());
                holder.materialName.getContext().startActivity(intent);
                Toast.makeText(holder.materialName.getContext(), "welcome to " + model.getFileUrl(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_note_material, parent, false);
        return new NotesViewHolder(view);
    }
    public static class NotesViewHolder extends RecyclerView.ViewHolder {
        TextView materialName  , materialTopic ;
        ImageButton downloadBtn , deleteBtn;
        LinearLayout touchOnNotes;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            materialName = itemView.findViewById(R.id.notes_name_model);
            materialTopic = itemView.findViewById(R.id.notes_topic_model);
            downloadBtn = itemView.findViewById(R.id.download_notes_btn);
            deleteBtn = itemView.findViewById(R.id.delete_notes_btn);
            touchOnNotes = itemView.findViewById(R.id.note_model_touchePad);
        }
    }
    private void downloadFile(Context context ,String fileName , String fileExtension , String destinationDirectory , String url ) {
        DownloadManager downloadManager = (DownloadManager) context.
                getSystemService(context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context , destinationDirectory , fileName + fileExtension);
        downloadManager.enqueue(request);

    }

    private void deleteFile(String fileUrl) {
        StorageReference file = FirebaseStorage.getInstance().getReferenceFromUrl(fileUrl);
        file.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "File Deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Unable to Delete File", Toast.LENGTH_SHORT).show();
            }
        });
        FirebaseFirestore.getInstance()
                .collection("/classes/" + classUid + "/classNotes/")
                .whereEqualTo("fileUrl" , fileUrl)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String id = queryDocumentSnapshots.getDocuments().get(0).getId();
                        FirebaseFirestore.getInstance()
                                .collection("/classes/" + classUid + "/classNotes/")
                                .document(id)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "File Ref Deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }
}
