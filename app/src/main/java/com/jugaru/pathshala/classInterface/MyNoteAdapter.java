package com.jugaru.pathshala.classInterface;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jugaru.pathshala.R;

import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MyNoteAdapter extends RecyclerView.Adapter<MyFileViewHolder> {
    ClassNotesFragment classNotesFragment ;
    ArrayList<UploadClassNotes> uploadClassNotesArrayList ;

    public MyNoteAdapter(ClassNotesFragment classNotesFragment, ArrayList<UploadClassNotes> uploadClassNotesArrayList) {

        this.classNotesFragment = classNotesFragment;
        this.uploadClassNotesArrayList = uploadClassNotesArrayList;
    }

    @NonNull
    @Override
    public MyFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(classNotesFragment.getContext()); //here could a problem occur
        View view = layoutInflater.inflate(R.layout.model_note_material , null , false);

        return new MyFileViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final MyFileViewHolder holder, final int position) {

        holder.noteName.setText((CharSequence) uploadClassNotesArrayList.get(position).getFileName());
        holder.noteTopic.setText((CharSequence) uploadClassNotesArrayList.get(position).getFileName());
        holder.downloadNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile(holder.noteName.getContext() , uploadClassNotesArrayList.get(position).getFileName() ,
                        ".pdf" , DIRECTORY_DOWNLOADS , uploadClassNotesArrayList.get(position).getFileUrl());
            }
        });
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
    @Override
    public int getItemCount() {
        return uploadClassNotesArrayList.size();
    }
}
