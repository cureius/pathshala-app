package com.jugaru.pathshala.classInterface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.TextView;

import com.jugaru.pathshala.R;

public class ClassActivity extends AppCompatActivity {

    TextView batch , classDescription , classFee , className , classSubject , classUid , instituteName , teacherUsername ;
    CardView classCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        init();




    }

    private  void  init(){
        batch = findViewById(R.id.batch_model_activity);
        classDescription = findViewById(R.id.class_description_model_activity);
        classFee = findViewById(R.id.class_fees_model_activity);
        className = findViewById(R.id.class_name_model_activity);
        classSubject = findViewById(R.id.class_subject_model_activity);
        classUid = findViewById(R.id.classUid_model_activity);
        instituteName = findViewById(R.id.institute_name_model_activity);
        teacherUsername = findViewById(R.id.teacher_username_model_activity);
        classCard = findViewById(R.id.class_card_view_activity);
    }

}