package com.jugaru.pathshala.homeFragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jugaru.pathshala.R;
import com.jugaru.pathshala.classInterface.ClassChat;
import com.jugaru.pathshala.classInterface.ClassMessageAdapter;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>  {
    List<NotificationModel> notificationModels;
    private Context context;

    FirebaseUser fuser;

    public static final int NOTICE_TYPE_VIDEO = 0;
    public static final int NOTICE_TYPE_NOTES = 1;

public NotificationAdapter(Context context, List<NotificationModel> notice ) {

        this.context = context;
        this.notificationModels = notice;
        }

@NonNull
@Override
public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == NOTICE_TYPE_NOTES) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.material_notice, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    } else {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_lecture_notice, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }
}

@Override
public void onBindViewHolder(@NonNull final NotificationAdapter.ViewHolder holder, final int position) {

        NotificationModel notice = notificationModels.get(position);
        holder.content.setText(notice.getContent());
        holder.className.setText(notice.getClassName());

        }

@Override
public int getItemCount() {
        return notificationModels.size();
        }

public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView content , className ;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        content = itemView.findViewById(R.id.content);
        className = itemView.findViewById(R.id.name_class);
    }
}
    @Override
    public int getItemViewType(int position) {
        if(notificationModels.get(position).getType().equals("video")){
            return NOTICE_TYPE_VIDEO;
        }
        else {
            return NOTICE_TYPE_NOTES;
        }
    }
}

