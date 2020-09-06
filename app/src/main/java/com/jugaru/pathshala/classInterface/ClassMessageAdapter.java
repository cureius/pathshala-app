package com.jugaru.pathshala.classInterface;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jugaru.pathshala.R;

import java.util.List;

public class ClassMessageAdapter extends RecyclerView.Adapter<ClassMessageAdapter.ViewHolder>  {
    List<ClassChat> mchat;
    private Context context ;
    private String imageUrl;

    FirebaseUser fuser;

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    public ClassMessageAdapter(Context context, List<ClassChat> mchat ) {

        this.context = context;
        this.mchat = mchat;
    }

    @NonNull
    @Override
    public ClassMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_right, parent, false);
            return new ClassMessageAdapter.ViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.class_chat_item_left, parent, false);
            return new ClassMessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ClassMessageAdapter.ViewHolder holder, final int position) {

        ClassChat chat = mchat.get(position);
        holder.show_message.setText(chat.getMessage());
//        chat.setUsername("test");
        holder.username.setText(chat.getUsername());
        Glide
                .with(context)
                .load(chat.getPhotoUrl())
                .into(holder.profile_image);
    }

    @Override
    public int getItemCount() {
        return mchat.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView show_message , username ;
        ImageView profile_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            username = itemView.findViewById(R.id.message_username);
            profile_image = itemView.findViewById(R.id.chat_profile_image);
        }
    }
    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(mchat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }
}
