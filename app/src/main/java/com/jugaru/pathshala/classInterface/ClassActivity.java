package com.jugaru.pathshala.classInterface;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.jugaru.pathshala.MainActivity;
import com.jugaru.pathshala.R;
import com.jugaru.pathshala.homeFragments.Classes;
import com.jugaru.pathshala.homeFragments.CreateClassFragment;
import com.jugaru.pathshala.homeFragments.NotificationFragment;
import com.jugaru.pathshala.homeFragments.ProfileFragment;
import com.jugaru.pathshala.homeFragments.SearchFragment;
import com.jugaru.pathshala.homeFragments.StudentViewFragment;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.http2.Header;

public class ClassActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView batch , classDescription , classFee , className , classSubject , classUid , instituteName , teacherUsername , hClassName ,
                        hClassBatch , hClassUid , hClassSubject ;
    private CardView classCard;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FrameLayout frameLayout;
    private List<Fragment> fragmentList;
    private  View header ;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        setTheme(R.style.NavigationTheme_);
        init();
        setSupportActionBar(toolbar);

        Classes classes =(Classes)getIntent().getParcelableExtra("SingleClass");
        String ID = getIntent().getStringExtra("ClassID");
        String path = getIntent().getStringExtra("ClassDocumentPath");
        assert classes != null;
        toolbar.setBackgroundColor(classes.getClassThemeColor());
//        toolbar.getTitle()
        toolbar.setTitle(classes.getClassName());

        Menu menu = navigationView.getMenu();
//        menu.findItem(R.id.nav_rate_us).;

//        View header = navigationView.getHeaderView();
        header = navigationView.getHeaderView(0);
        header.findViewById(R.id.header_relative_layout).setBackgroundColor(classes.getClassThemeColor());
        hClassName = (TextView) header.findViewById(R.id.class_name_nav);
        hClassBatch = (TextView) header.findViewById(R.id.batch_nav);
        hClassUid = (TextView) header.findViewById(R.id.classUid_nav);
        hClassSubject = (TextView) header.findViewById(R.id.class_subject_nav);

        hClassName.setText(classes.getClassName());
        hClassSubject.setText(classes.getClassSubject());
        hClassUid.setText(classes.getClassUid());
        hClassBatch.setText("(" + classes.getBatch() + ")");

//        navigationView.getHeaderView(0).findViewById(R.id.class_name_nav).setTooltipText(classes.getClassName());
//        navigationView.getHeaderView(0).findViewById(R.id.batch_nav).setTooltipText(classes.getBatch());
//        navigationView.getHeaderView(0).findViewById(R.id.class_subject_nav).setTooltipText(classes.getClassSubject());
//        navigationView.getHeaderView(0).findViewById(R.id.classUid_nav).setTooltipText(classes.getClassUid());


        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this , drawerLayout , toolbar , R.string.navigation_drawer_open , R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_class_chat);

        fragmentList = new ArrayList<>();
        fragmentList.add(new ClassChatFragment());
        fragmentList.add(new VideoLectureFragment());
        fragmentList.add(new ClassNotesFragment());
        fragmentList.add(new StudentFeesFragment());
        fragmentList.add(new ParticipantFragment());

        setFragment(0);

//        batch.setText(classes.getBatch());
//        classDescription.setText(classes.getClassDescription());
//        classFee.setText(classes.getClassFee());
//        className.setText(classes.getClassName());
//        classSubject.setText(classes.getClassSubject());
//        classUid.setText(classes.getClassUid());
//        instituteName.setText(classes.getInstituteName());
//        teacherUsername.setText(classes.getTeacherUsername());
//        classCard.setCardBackgroundColor(classes.getClassThemeColor());
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    private  void  init(){
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_class_view);
        toolbar = findViewById(R.id.class_toolbar);
        frameLayout = findViewById(R.id.inner_class_frame_layout);
//        batch = findViewById(R.id.batch_model_activity);
//        classDescription = findViewById(R.id.class_description_model_activity);
//        classFee = findViewById(R.id.class_fees_model_activity);
//        className = findViewById(R.id.class_name_model_activity);
//        classSubject = findViewById(R.id.class_subject_model_activity);
//        classUid = findViewById(R.id.classUid_model_activity);
//        instituteName = findViewById(R.id.institute_name_model_activity);
//        teacherUsername = findViewById(R.id.teacher_username_model_activity);
//        classCard = findViewById(R.id.class_card_view_activity);
    }

    public void setFragment(int position){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        fragmentTransaction.replace(frameLayout.getId(),fragmentList.get(position));
        fragmentTransaction.commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case  R.id.nav_class_chat:
                setFragment(0);
                break;
            case  R.id.nav_class_notes:
                setFragment(1);
                break;
            case  R.id.nav_video_lecture:
                setFragment(2);
                break;
            case  R.id.nav_class_fees:
                setFragment(3);
                break;
            case  R.id.nav_participant:
                setFragment(4);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}