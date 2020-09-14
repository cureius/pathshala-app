package com.jugaru.pathshala.homeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jugaru.pathshala.R;
import com.jugaru.pathshala.classInterface.ClassChat;
import com.jugaru.pathshala.classInterface.ClassMessageAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class NotificationFragment extends Fragment {
    RecyclerView recyclerView;
    List<NotificationModel> notice;
    NotificationAdapter notificationAdapter;
    TextView wait , noNotice;
    public NotificationFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.notification_recyclerview);
        wait = view.findViewById(R.id.wait);
        noNotice = view.findViewById(R.id.noNotice);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        final Classes classes = getActivity().getIntent().getParcelableExtra("SingleClass");
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        assert classes != null;

        FirebaseFirestore.getInstance()
                .collection("user")
                .document(fuser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d(TAG , "onSuccess: " + documentSnapshot.getId());
                        Log.d(TAG , "onSuccess: " + documentSnapshot.getData());
                        Log.d(TAG , "onSuccess: " + documentSnapshot.getString("FirstName"));

                        List<String> classList = (List<String>) documentSnapshot.get("ClassList");
                        readNotification(classList);
                        }
                });
    }

    private void  readNotification( final List<String> classList){
        notice = new ArrayList<>();
        wait.setVisibility(View.VISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notice.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    NotificationModel notification = snapshot.getValue(NotificationModel.class);
                    assert notification != null;
                    if(classList.contains(notification.getClassName())){
                        notice.add(notification);
                    }
                    notificationAdapter = new NotificationAdapter(getContext() , notice );
                    recyclerView.setAdapter(notificationAdapter);
                }
                wait.setVisibility(View.INVISIBLE);
                if(notice.isEmpty()){
//                    wait.setVisibility(View.INVISIBLE);
                    noNotice.setVisibility(View.VISIBLE);
                }else {
//                    wait.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}