package com.jugaru.pathshala.homeFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jugaru.pathshala.MainActivity;
import com.jugaru.pathshala.R;
import com.jugaru.pathshala.classInterface.ClassActivity;
import com.jugaru.pathshala.classInterface.ParticipantFragment;

import java.io.Serializable;
import java.util.List;

import static android.content.ContentValues.TAG;


public class StudentViewFragment extends Fragment{

    public StudentViewFragment() {
        // Required empty public constructor
    }

    private ImageView viewChangerDots;
    private FirebaseFirestore firestore;
    private TextView studentDashboardTv;

    private RecyclerView studentDashboardRecyclerView ;
    private ClassAdapter studentAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        viewChangerDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setFragmentTeacherStudent(new TeacherViewFragment());
            }
        });
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("classes")
                .whereArrayContains("StudentList" , firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> document = task.getResult().getDocuments();
                            if (!(document.isEmpty())) {
                                Log.d(TAG, "onComplete: document list found ");
                            } else {
                                studentDashboardTv.setVisibility(View.VISIBLE);
                                return;
                            }
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

        studentDashboardRecyclerView = view.findViewById(R.id.student_dashbosrd_recyclerview);
        studentDashboardRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirestoreRecyclerOptions.Builder<Classes> classesBuilder = new FirestoreRecyclerOptions.Builder<Classes>();
        classesBuilder.setQuery(FirebaseFirestore.getInstance()
                .collection("classes")
                .whereArrayContains("StudentList" , FirebaseAuth.getInstance().getCurrentUser().getUid()), Classes.class);
        FirestoreRecyclerOptions<Classes> options =
                classesBuilder
                        .build();
        studentAdapter = new ClassAdapter(options);
        studentDashboardRecyclerView.setAdapter(studentAdapter);

        studentAdapter.setOnItemClickListener(new ClassAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Classes classes = documentSnapshot.toObject(Classes.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                Toast.makeText(getContext(), "Position:" + position + "ID:" + id + "Path" + path , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext() , ClassActivity.class);
                intent.putExtra("SingleClass" , (Parcelable) classes);
                intent.putExtra("ClassID" , id );
                intent.putExtra("ClassDocumentPath" , path );
                Intent participant_intent = new Intent(getContext() , ParticipantFragment.class);
                intent.putExtra("SingleClass" , (Parcelable) classes);
                intent.putExtra("ClassID" , id );
                intent.putExtra("ClassDocumentPath" , path );
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        studentAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        studentAdapter.stopListening();
    }


    private void init(View view){
        viewChangerDots =view.findViewById(R.id.homeMenuBtnOfStudent);
        studentDashboardTv =view.findViewById(R.id.student_dashboard_textView);
    }
}