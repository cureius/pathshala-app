package com.jugaru.pathshala.registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.jugaru.pathshala.R;

public class RegisterActivity extends AppCompatActivity {

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        frameLayout = findViewById(R.id.frameLayout);

        setFragment(new loginFragment());

    }

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        if(fragment instanceof ForgotPasswordFragment || fragment instanceof  OTPFragment){
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();

    }
}