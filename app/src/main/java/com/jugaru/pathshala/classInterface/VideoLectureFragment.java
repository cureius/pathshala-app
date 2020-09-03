package com.jugaru.pathshala.classInterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jugaru.pathshala.R;
import com.jugaru.pathshala.homeFragments.ClassAdapter;
import com.jugaru.pathshala.homeFragments.Classes;
import com.khizar1556.mkvideoplayer.MKPlayerActivity;

import java.util.Objects;

public class VideoLectureFragment extends Fragment {

    private FloatingActionButton floatingActionButton ;
    private FirebaseAuth firebaseAuth;
    private RecyclerView videoLectureRecyclerView ;
    private VideoLectureAdapter videoLectureAdapter;

    public VideoLectureFragment() {
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
        return inflater.inflate(R.layout.fragment_video_lecture, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab_class_video);
        Classes classes = (Classes) Objects.requireNonNull(getActivity()).getIntent().getParcelableExtra("SingleClass");
        if(firebaseAuth.getCurrentUser().getUid().equals(classes.getTeacherUid())){
            floatingActionButton.setVisibility(View.VISIBLE);
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Classes classes = (Classes)getActivity().getIntent().getParcelableExtra("SingleClass");
                Intent intent = new Intent(getContext() , UploadMaterialActivity.class);
                intent.putExtra("themeColor" , classes.getClassThemeColor());
                intent.putExtra("classUid" , classes.getClassUid());
                intent.putExtra("HeadingName" , "Upload Video Lecture" );
                intent.putExtra("Icon" , R.drawable.ic_round_ondemand_video_24 );
                startActivity(intent);
            }
        });
        videoLectureRecyclerView = view.findViewById(R.id.recyclerView_VideoLectures);
        videoLectureRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        String classUid = classes.getClassUid();
        FirestoreRecyclerOptions.Builder<UploadVideoLecture> uploadVideoLectureBuilder = new FirestoreRecyclerOptions.Builder<UploadVideoLecture>();
        uploadVideoLectureBuilder.setQuery(FirebaseFirestore.getInstance()
                .collection("/classes/" + classUid + "/videosLecture/") , UploadVideoLecture.class);
        FirestoreRecyclerOptions<UploadVideoLecture> options =
                uploadVideoLectureBuilder
                        .build();
        videoLectureAdapter = new VideoLectureAdapter(options);
        videoLectureRecyclerView.setAdapter(videoLectureAdapter);

        videoLectureAdapter.setOnItemClickListener(new VideoLectureAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                UploadVideoLecture videoLecture = documentSnapshot.toObject(UploadVideoLecture.class);
                String url = videoLecture.getFileUrl();
//                String path = documentSnapshot.getReference().getPath();
//                Toast.makeText(getContext(), "Position:" + position + "ID:" + id + "Path" + path , Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getContext() , ClassActivity.class);
//                intent.putExtra("SingleClass" , (Parcelable) classes);
//                intent.putExtra("ClassID" , id );
//                intent.putExtra("ClassDocumentPath" , path );
//                startActivity(intent);
                Toast.makeText(getContext(), "Video Clicked", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();
                MKPlayerActivity.configPlayer(getActivity()).play(url);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        videoLectureAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        videoLectureAdapter.stopListening();
    }

}