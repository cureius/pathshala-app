package com.jugaru.pathshala.classInterface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.jugaru.pathshala.R;
import com.jugaru.pathshala.homeFragments.ClassAdapter;
import com.jugaru.pathshala.homeFragments.Classes;

public class VideoLectureAdapter extends FirestoreRecyclerAdapter<UploadVideoLecture, VideoLectureAdapter.VideoViewHolder> {

    private VideoLectureAdapter.OnItemClickListener listener;
    public VideoLectureAdapter(@NonNull FirestoreRecyclerOptions<UploadVideoLecture> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull VideoLectureAdapter.VideoViewHolder holder, int position, @NonNull UploadVideoLecture model) {

        holder.videoTopic.setText(model.getFileName());
        holder.videoUnit.setText(model.getFileName());
        holder.videoPart.setText(model.getFileName());

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

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoTopic = itemView.findViewById(R.id.video_topic);
            videoUnit = itemView.findViewById(R.id.unit);
            videoPart = itemView.findViewById(R.id.part);
            touchOnVideo = itemView.findViewById(R.id.touch_pad_video);
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
