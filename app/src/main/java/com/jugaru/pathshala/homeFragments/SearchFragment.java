package com.jugaru.pathshala.homeFragments;

import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.jugaru.pathshala.MainActivity;
import com.jugaru.pathshala.R;
import com.jugaru.pathshala.classInterface.ProfileAdapter;
import com.jugaru.pathshala.classInterface.UserProfile;
import com.jugaru.pathshala.registration.UserNameActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class SearchFragment extends Fragment {

    private AutoCompleteTextView searchBar;
    private ImageView searchBtn;
    private RecyclerView searchClassRecyclerview;
    private Button joinBtn;
    private ClassAdapter searchClassAdapter;


    public SearchFragment() {
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final List<String> codeList = new ArrayList<>();

        init(view);
        FirebaseFirestore.getInstance()
                .collection("classes")
                .whereLessThan("ClassThemeColor" , 0)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot: task.getResult()){
                            codeList.add((String) documentSnapshot.get("ClassUid"));
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_list_item_1 , codeList);
                        searchBar.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  searchedClassUid = searchBar.getText().toString();
                searchClassRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

                FirestoreRecyclerOptions.Builder<Classes> classesBuilder = new FirestoreRecyclerOptions.Builder<Classes>();
                classesBuilder.setQuery(FirebaseFirestore.getInstance()
                        .collection("classes")
                        .whereEqualTo("ClassUid" , searchedClassUid ), Classes.class);
                FirestoreRecyclerOptions<Classes> options =
                        classesBuilder
                                .build();

                searchClassAdapter = new ClassAdapter(options);
                searchClassRecyclerview.setAdapter(searchClassAdapter);
                searchClassAdapter.startListening();
                joinBtn.setVisibility(View.VISIBLE);

            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String  searchedClassUid = searchBar.getText().toString();
                final FirebaseAuth firebaseAuth ;
                firebaseAuth = FirebaseAuth.getInstance();
                final String applicantUid;
                applicantUid = firebaseAuth.getCurrentUser().getUid();

                FirebaseFirestore.getInstance()
                        .collection("user")
                        .document(firebaseAuth.getCurrentUser().getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                Log.d(TAG , "onSuccess: " + documentSnapshot.getId());
                                Log.d(TAG , "onSuccess: " + documentSnapshot.getData());
                                List<String> listOfClass = (List<String>) documentSnapshot.get("ClassList");
                                listOfClass.add(searchedClassUid);
                                Log.d(TAG , "onSuccess: " + listOfClass);

                                FirebaseFirestore fireStore1 = FirebaseFirestore.getInstance();
                                Map<String, Object> map = new HashMap<>();
                                map.put("ClassList", listOfClass);

                                fireStore1.collection("user").document(firebaseAuth.getCurrentUser().getUid()).update(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (!(task.isSuccessful())) {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Toast.makeText(getContext(), "Class Added", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        });

                FirebaseFirestore.getInstance()
                        .collection("classes")
                        .document(searchedClassUid)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                Log.d(TAG , "onSuccess: " + documentSnapshot.getId());
                                Log.d(TAG , "onSuccess: " + documentSnapshot.getData());
                                List<String> listOfStudents = (List<String>) documentSnapshot.get("StudentList");
                                listOfStudents.add(applicantUid);
                                Log.d(TAG , "onSuccess: " + listOfStudents);

                                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                                Map<String, Object> map = new HashMap<>();
                                map.put("StudentList", listOfStudents);

                                firestore.collection("classes").document(searchedClassUid).update(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (!(task.isSuccessful())) {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Toast.makeText(getContext(), "You have been joined to the class", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        });

            }
        });
    }

    private void init(View view){
        searchBar =view.findViewById(R.id.search_bar);
        searchBtn =view.findViewById(R.id.search_icon_btn);
        searchClassRecyclerview =view.findViewById(R.id.class_search_recyclerview);
        joinBtn =view.findViewById(R.id.join_class_btn);
    }
}