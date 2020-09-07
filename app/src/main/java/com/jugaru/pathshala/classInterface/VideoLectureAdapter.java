package com.jugaru.pathshala.classInterface;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.jugaru.pathshala.homeFragments.ClassAdapter;
import com.jugaru.pathshala.homeFragments.Classes;
import com.khizar1556.mkvideoplayer.MKPlayerActivity;

public class VideoLectureAdapter extends FirestoreRecyclerAdapter<UploadVideoLecture, VideoLectureAdapter.VideoViewHolder> {

    boolean teacher;
    Context context;
    String classUid;
    Activity activity;
    private VideoLectureAdapter.OnItemClickListener listener;
    public VideoLectureAdapter(@NonNull FirestoreRecyclerOptions<UploadVideoLecture> options, boolean teacher, Context context, String classUid , Activity activity) {
        super(options);
        this.teacher = teacher;
        this.context = context;
        this.classUid = classUid;
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull VideoLectureAdapter.VideoViewHolder holder, int position, @NonNull final UploadVideoLecture model) {

        holder.videoTopic.setText(model.getFileName());
        holder.videoUnit.setText(model.getFileTopic());
        holder.videoPart.setText(model.getFileUnit());
        holder.deleteVideo.setVisibility(View.INVISIBLE);
        if(teacher){
            holder.deleteVideo.setVisibility(View.VISIBLE);
        }
        holder.deleteVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.MyDialogTheme) ;
                dialog.setTitle("Delete Lecture ?");
                dialog.setIcon(R.drawable.ic_round_delete_forever_24);
                dialog.setMessage("Do you really want to delete your lecture forever ");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteVideoFile(model.getFileUrl());
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
        holder.touchOnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MKPlayerActivity.configPlayer(activity).play(model.fileUrl);
            }
        });

    }

    private void deleteVideoFile(String fileUrl) {
        StorageReference file = FirebaseStorage.getInstance().getReferenceFromUrl(fileUrl);
        file.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Video Deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Unable to Delete Video", Toast.LENGTH_SHORT).show();
            }
        });
        FirebaseFirestore.getInstance()
                .collection("/classes/" + classUid + "/videosLecture/")
                .whereEqualTo("fileUrl" , fileUrl)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String id = queryDocumentSnapshots.getDocuments().get(0).getId();
                        FirebaseFirestore.getInstance()
                                .collection("/classes/" + classUid + "/videosLecture/")
                                .document(id)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "Video Ref Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }

    @NonNull
    @Override
    public VideoLectureAdapter.VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_video_lecture, parent, false);

        return new VideoLectureAdapter.VideoViewHolder(view);
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView videoTopic , videoUnit , videoPart ;
        CardView touchOnVideo;
        ImageButton deleteVideo;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoTopic = itemView.findViewById(R.id.video_topic);
            videoUnit = itemView.findViewById(R.id.unit);
            videoPart = itemView.findViewById(R.id.part);
            touchOnVideo = itemView.findViewById(R.id.touch_pad_video);
            deleteVideo = itemView.findViewById(R.id.delete_video_btn);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });

        }
    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot , int position);
    }
    public void setOnItemClickListener(VideoLectureAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
}
