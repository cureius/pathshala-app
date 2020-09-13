package com.jugaru.pathshala;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jugaru.pathshala.classInterface.AboutAppFragment;
import com.jugaru.pathshala.homeFragments.AddClassFragment;
import com.jugaru.pathshala.homeFragments.CreateClassFragment;
import com.jugaru.pathshala.homeFragments.NotificationFragment;
import com.jugaru.pathshala.homeFragments.ProfileFragment;
import com.jugaru.pathshala.homeFragments.SearchFragment;
import com.jugaru.pathshala.homeFragments.StudentViewFragment;
import com.jugaru.pathshala.registration.ForgotPasswordFragment;
import com.jugaru.pathshala.registration.OTPFragment;
import com.jugaru.pathshala.registration.RegisterActivity;
import com.jugaru.pathshala.registration.UserNameActivity;

import java.util.ArrayList;
import java.util.List;

import static com.jugaru.pathshala.R.menu.app_menu;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private FrameLayout frameLayout;
    private TabLayout tabLayout;
    private List<Fragment> fragmentList;
    private AdView mAdView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        MobileAds.initialize(this, "ca-app-pub-7613539201061048~6033111687");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        init();

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        checkUsername();

        fragmentList = new ArrayList<>();
        fragmentList.add(new StudentViewFragment());
        fragmentList.add(new SearchFragment());
        fragmentList.add(new CreateClassFragment());
        fragmentList.add(new NotificationFragment());
        fragmentList.add(new ProfileFragment());

        setFragment(0);
        tabLayout.getTabAt(0).getIcon().setTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                setFragment(tab.getPosition());
            }
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(app_menu , menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId() == R.id.share_app){
//
//        }else {
//            return super.onOptionsItemSelected(item);
//        }
//        return true;
//    }

    public void setFragment(int position){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        fragmentTransaction.replace(frameLayout.getId(),fragmentList.get(position));
        fragmentTransaction.commit();
    }

    public void setFragmentStudentAbout(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        if(fragment instanceof AboutAppFragment){
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void init(){
        frameLayout = findViewById(R.id.homeFrameLayout);
        tabLayout = findViewById(R.id.tablayout);
    }

    private  void checkUsername(){

        firestore.collection("user").document(firebaseAuth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().exists()){
                                if(!task.getResult().contains("username")){
                                    Intent usernameIntent = new Intent(MainActivity.this , UserNameActivity.class);
                                    startActivity(usernameIntent);
                                    finish();
                                }
                            }else {
                                Intent registerIntent = new Intent(MainActivity.this , RegisterActivity.class);
                                startActivity(registerIntent);
                                finish();                            }
                        }else {
                            String error = task.getException().getMessage();
                            Toast.makeText(MainActivity.this , error , Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }
}