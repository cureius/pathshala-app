package com.jugaru.pathshala.registration;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jugaru.pathshala.MainActivity;
import com.jugaru.pathshala.R;

import org.w3c.dom.Text;

import java.util.List;

import static com.jugaru.pathshala.registration.CreateAccountFragment.VALID_EMAIL_ADDRESS_REGEX;

/**
 * A simple {@link Fragment} subclass.
 */
public class loginFragment extends Fragment {

    public loginFragment() {
        // Required empty public constructor
    }

    private EditText emailOrPhone , password;
    private Button loginBtn;
    private ProgressBar progressBar;
    private TextView createAccountTv, forgotPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailOrPhone.setError(null);
                password.setError(null);

                if(emailOrPhone.getText().toString().isEmpty()){
                    emailOrPhone.setError("Required");
                    return;
                }
                if(password.getText().toString().isEmpty()){
                    password.setError("Required");
                    return;
                }
                if(emailOrPhone.getText().toString().matches("\\\\d{10}")) {
                    progressBar.setVisibility(View.VISIBLE);
                    FirebaseFirestore.getInstance().collection("user").whereEqualTo("phone", emailOrPhone.getText().toString())
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<DocumentSnapshot> document = task.getResult().getDocuments();
                                if (document.isEmpty()) {
                                    emailOrPhone.setError("Phone not found");
                                    progressBar.setVisibility(View.INVISIBLE);
                                    return;
                                } else {
                                    String email = document.get(0).get("email").toString();
                                    login(email);
                                }
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
                if(VALID_EMAIL_ADDRESS_REGEX.matcher(emailOrPhone.getText().toString()).find()) {
                    progressBar.setVisibility(View.VISIBLE);
                    login(emailOrPhone.getText().toString());
                }else {
                    emailOrPhone.setError("Please enter a valid Email");
                }

            }
        });

        createAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterActivity)getActivity()).setFragment(new CreateAccountFragment());
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterActivity)getActivity()).setFragment(new ForgotPasswordFragment());
            }
        });

    }
    private void init(View view) {

        emailOrPhone = view.findViewById(R.id.email_or_phone);
        password = view.findViewById(R.id.loginPassword);
        progressBar = view.findViewById(R.id.progressBar);
        loginBtn = view.findViewById(R.id.loginBtn);
        createAccountTv = view.findViewById(R.id.createAccountTv);
        forgotPassword = view.findViewById(R.id.forgotPasswordTv);
    }

    private void  login(String email){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email , password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent mainIntent = new Intent(getContext(), MainActivity.class);
                    startActivity(mainIntent);
                    getActivity().finish();
                }else{
                    String error = task.getException().getMessage();
                    Toast.makeText(getContext(), error , Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

}