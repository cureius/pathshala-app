package com.jugaru.pathshala.classInterface;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jugaru.pathshala.R;

public class ProfileHolder extends RecyclerView.ViewHolder {
    TextView username , userFullName  ;
    ImageView dp;
    public ProfileHolder(@NonNull View itemView) {
        super(itemView);
        username = itemView.findViewById(R.id.profile_username_model);
        userFullName = itemView.findViewById(R.id.username_model);
        dp = itemView.findViewById(R.id.profile_pic_model);
    }
}
