package com.jugaru.pathshala.classInterface;

import android.app.DownloadManager;
import android.content.Context;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.jugaru.pathshala.R;

import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MyNoteAdapter extends FirestoreRecyclerAdapter<UploadClassNotes, MyNoteAdapter.NotesViewHolder> {

    public MyNoteAdapter(@NonNull FirestoreRecyclerOptions<UploadClassNotes> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final NotesViewHolder holder, final int position, @NonNull final UploadClassNotes model) {

        holder.materialName.setText(model.getFileName());
        holder.materialTopic.setText(model.getFileName());
        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile(holder.materialName.getContext() , model.getFileName() ,
                ".pdf" , DIRECTORY_DOWNLOADS , model.getFileUrl());
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
        return new MyNoteAdapter.NotesViewHolder(view);
    }
    public class NotesViewHolder extends RecyclerView.ViewHolder {
        TextView materialName  , materialTopic ;
        ImageButton downloadBtn;
        LinearLayout touchOnNotes;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            materialName = itemView.findViewById(R.id.notes_name_model);
            materialTopic = itemView.findViewById(R.id.notes_topic_model);
            downloadBtn = itemView.findViewById(R.id.download_notes_btn);
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
}
