package com.jugaru.pathshala;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import com.google.firebase.auth.FirebaseAuth;
import com.jugaru.pathshala.registration.RegisterActivity;
import com.jugaru.pathshala.registration.UserNameActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SystemClock.sleep(1000);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent myIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(myIntent);
            finish();
            return;
        }
        Intent myIntent = new Intent(SplashActivity.this, RegisterActivity.class);
        startActivity(myIntent);
        finish();
    }
}