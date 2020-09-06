package com.jugaru.pathshala.classInterface;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.jugaru.pathshala.homeFragments.Classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassChatFragment extends Fragment {
    RecyclerView recyclerView;
    EditText msg_edittext;
    ImageButton sendBtn;
    String senderUsername , senderDp ;
    FirebaseUser fuser;
    FirebaseFirestore firestore;
    DatabaseReference reference;
    Intent intent;

    ClassMessageAdapter classMessageAdapter;
    List<ClassChat> mchat ;
    public ClassChatFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_chat, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sendBtn = view.findViewById(R.id.btn_send_class);
        msg_edittext = view.findViewById(R.id.text_send_class);
        recyclerView = view.findViewById(R.id.class_chat_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        final Classes classes = getActivity().getIntent().getParcelableExtra("SingleClass");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        assert classes != null;
        readMessages(classes.getClassUid());

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String msg = msg_edittext.getText().toString();
                if(!msg.equals("")){
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    firestore.collection("user").document(fuser.getUid()).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            senderUsername = (String) documentSnapshot.get("username");
                            senderDp = (String) documentSnapshot.get("profile_Url");
                            sendMessage(fuser.getUid() , classes.getClassUid()  , msg , senderUsername , senderDp );
                        }
                    });
                }else {
                    Toast.makeText(getContext(), "Please Enter a Message", Toast.LENGTH_SHORT).show();
                }
                msg_edittext.setText("");
            }
        });
    }
    private void sendMessage(String sender, String classUid, String message, String senderUsername, String senderDp) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("sender" , sender);
        hashMap.put("classUid" , classUid);
        hashMap.put("message" , message);
        hashMap.put("username" , senderUsername);
        hashMap.put("photoUrl" , senderDp);

        reference.child("ClassChats").push().setValue(hashMap);
    }
    private void  readMessages( final String classUid){
        mchat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("ClassChats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ClassChat chat = snapshot.getValue(ClassChat.class);
                    assert chat != null;
                    if(chat.getClassUid().equals(classUid)){
                        mchat.add(chat);
                    }
                    classMessageAdapter = new ClassMessageAdapter(getContext() , mchat );
                    recyclerView.setAdapter(classMessageAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}