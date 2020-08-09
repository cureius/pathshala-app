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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jugaru.pathshala.MainActivity;
import com.jugaru.pathshala.R;
import com.jugaru.pathshala.registration.UserNameActivity;

import java.util.List;

import static android.content.ContentValues.TAG;

public class TeacherViewFragment extends Fragment {

    public TeacherViewFragment() {
        // Required empty public constructor
    }

    private ImageView viewChangerDots;
    private FirebaseFirestore firestore;
    private TextView teacherDashboardTv;

    private RecyclerView teacherDashboardRecyclerView ;
    private ClassAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teacher_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        viewChangerDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setFragmentTeacherStudent(new StudentViewFragment());
            }
        });
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("classes")
                .whereEqualTo("TeacherUid" , firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> document = task.getResult().getDocuments();
                            if (!(document.isEmpty())) {
                                Log.d(TAG, "onComplete: document list found ");
                            } else {
                                teacherDashboardTv.setVisibility(View.VISIBLE);
                                return;
                            }
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

        teacherDashboardRecyclerView = view.findViewById(R.id.teacherDashboard_recyclerView);
        teacherDashboardRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirestoreRecyclerOptions.Builder<Classes> classesBuilder = new FirestoreRecyclerOptions.Builder<Classes>();
        classesBuilder.setQuery(FirebaseFirestore.getInstance()
                .collection("classes")
                .whereEqualTo("TeacherUid" , FirebaseAuth.getInstance().getCurrentUser().getUid()), Classes.class);
        FirestoreRecyclerOptions<Classes> options =
                classesBuilder
                        .build();

        adapter = new ClassAdapter(options);
        teacherDashboardRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void init(View view){
        viewChangerDots =view.findViewById(R.id.homeMenuBtnOfTeacher);
        teacherDashboardTv =view.findViewById(R.id.teacher_dashboard_textView);
    }
}