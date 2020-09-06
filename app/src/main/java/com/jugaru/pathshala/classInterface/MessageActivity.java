package com.jugaru.pathshala.classInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.google.firebase.firestore.auth.User;
import com.jugaru.pathshala.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class MessageActivity extends AppCompatActivity {
    CircleImageView profilePic;
    TextView userName;

    RecyclerView recyclerView;
    EditText msg_edittext;
    ImageButton sendBtn;

    FirebaseUser fuser;
    DatabaseReference reference;
    Intent intent;

    MessageAdapter messageAdapter;
    List<Chat> mchat ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        profilePic = findViewById(R.id.profile_pic_chat);
        userName = findViewById(R.id.chat_username);
        sendBtn = findViewById(R.id.btn_send);
        msg_edittext = findViewById(R.id.text_send);
        recyclerView = findViewById(R.id.chat_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        // This portion maybe removed
        Toolbar toolbar = findViewById(R.id.toolbarChat);
        profilePic.findViewById(R.id.profile_pic_chat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // up to this

        intent = getIntent();
        final String userid = intent.getStringExtra("userId");
        final String DpUrl = intent.getStringExtra("dp");
        final String username = intent.getStringExtra("username");

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        Glide.with(MessageActivity.this)
                .load(DpUrl)
                .into(profilePic);
        userName.setText(username);
        readMessages(fuser.getUid() , userid , DpUrl);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = msg_edittext.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fuser.getUid() , userid , msg);

                }else {
                    Toast.makeText(MessageActivity.this, "Please Enter a Message", Toast.LENGTH_SHORT).show();
                }

                msg_edittext.setText("");
            }
        });
    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("sender" , sender);
        hashMap.put("receiver" , receiver);
        hashMap.put("message" , message);

        reference.child("Chats").push().setValue(hashMap);

    }

    private void  readMessages(final String myid, final String userid, final String imageurl){
        mchat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){

                        mchat.add(chat);

                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this , mchat , imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}