package com.jugaru.pathshala.classInterface;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jugaru.pathshala.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileHolder> {
    List<UserProfile> userList;
    private Context context ;
    public ProfileAdapter(Context context, ArrayList<UserProfile> userList) {

        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_holder, parent, false);
        return new ProfileHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ProfileHolder holder, final int position) {

        holder.userFullName.setText(userList.get(position).getFirstName() + " " + userList.get(position).getLastName());
        holder.username.setText(userList.get(position).getUsername());
        if(!(userList.get(position).getProfile_Url().isEmpty() || userList.get(position).getProfile_Url() == null)){

            Glide.with(context)
                    .load(userList.get(position).getProfile_Url())
                    .into(holder.dp);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context , MessageActivity.class);
                i.putExtra("username" , userList.get(position).getUsername());
                i.putExtra("dp" , userList.get(position).getProfile_Url());
                i.putExtra("userId" , userList.get(position).getUserId());
                context.startActivity(i);
            }
        });
    }
    @Override
    public int getItemCount() {
        return userList.size();
    }
}





