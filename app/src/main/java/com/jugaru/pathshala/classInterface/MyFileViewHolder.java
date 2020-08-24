package com.jugaru.pathshala.classInterface;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jugaru.pathshala.R;

public class MyFileViewHolder extends RecyclerView.ViewHolder {
    TextView noteName , noteTopic ;
    ImageButton downloadNotesBtn ;
    LinearLayout touchToOpen ;
    public MyFileViewHolder(@NonNull View itemView) {
        super(itemView);
        noteName = itemView.findViewById(R.id.notes_name_model);
        noteTopic = itemView.findViewById(R.id.notes_topic_model);
        downloadNotesBtn = itemView.findViewById(R.id.download_notes_btn);
        touchToOpen = itemView.findViewById(R.id.note_model_touchePad);


    }
}
