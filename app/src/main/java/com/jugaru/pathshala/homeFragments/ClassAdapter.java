package com.jugaru.pathshala.homeFragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.jugaru.pathshala.R;

public class ClassAdapter extends FirestoreRecyclerAdapter<Classes , ClassAdapter.ClassViewHolder> {

    public ClassAdapter(@NonNull FirestoreRecyclerOptions<Classes> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ClassViewHolder holder, int position, @NonNull Classes classes) {

        holder.batch.setText("(" + classes.getBatch() + ")");
        holder.classDescription.setText(classes.getClassDescription());
        holder.classFee.setText(classes.getClassFee());
        holder.className.setText(classes.getClassName());
        holder.classSubject.setText(classes.getClassSubject());
        holder.classUid.setText(classes.getClassUid());
        holder.instituteName.setText(classes.getInstituteName());
        holder.teacherUsername.setText(classes.getTeacherUsername());
        holder.classCard.setCardBackgroundColor(classes.getClassThemeColor());
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_design, parent, false);

        return new ClassViewHolder(view);
    }

    class  ClassViewHolder extends RecyclerView.ViewHolder{

        TextView batch , classDescription , classFee , className , classSubject , classUid , instituteName , teacherUsername ;
        CardView classCard;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);

            batch = itemView.findViewById(R.id.batch_model);
            classDescription = itemView.findViewById(R.id.class_description_model);
            classFee = itemView.findViewById(R.id.class_fees_model);
            className = itemView.findViewById(R.id.class_name_model);
            classSubject = itemView.findViewById(R.id.class_subject_model);
            classUid = itemView.findViewById(R.id.classUid_model);
            instituteName = itemView.findViewById(R.id.institute_name_model);
            teacherUsername = itemView.findViewById(R.id.teacher_username_model);
            classCard = itemView.findViewById(R.id.class_card_view);

        }
    }
}
