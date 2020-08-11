package com.jugaru.pathshala.homeFragments;

import android.os.Parcel;
import android.os.Parcelable;

public class Classes implements Parcelable {
    String Batch ;
    String ClassDescription ;
    String ClassFee ;
    String ClassName ;
    String ClassSubject ;
    String ClassUid ;
    String InstituteName ;
    String TeacherUid ;
    String TeacherUsername ;
    int ClassThemeColor ;

    protected Classes(Parcel in) {
        Batch = in.readString();
        ClassDescription = in.readString();
        ClassFee = in.readString();
        ClassName = in.readString();
        ClassSubject = in.readString();
        ClassUid = in.readString();
        InstituteName = in.readString();
        TeacherUid = in.readString();
        TeacherUsername = in.readString();
        ClassThemeColor = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Batch);
        dest.writeString(ClassDescription);
        dest.writeString(ClassFee);
        dest.writeString(ClassName);
        dest.writeString(ClassSubject);
        dest.writeString(ClassUid);
        dest.writeString(InstituteName);
        dest.writeString(TeacherUid);
        dest.writeString(TeacherUsername);
        dest.writeInt(ClassThemeColor);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Classes> CREATOR = new Creator<Classes>() {
        @Override
        public Classes createFromParcel(Parcel in) {
            return new Classes(in);
        }

        @Override
        public Classes[] newArray(int size) {
            return new Classes[size];
        }
    };

    public String getBatch() {
        return Batch;
    }

    public void setBatch(String batch) {
        Batch = batch;
    }

    public String getClassDescription() {
        return ClassDescription;
    }

    public void setClassDescription(String classDescription) {
        ClassDescription = classDescription;
    }

    public String getClassFee() {
        return ClassFee;
    }

    public void setClassFee(String classFee) {
        ClassFee = classFee;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getClassSubject() {
        return ClassSubject;
    }

    public void setClassSubject(String classSubject) {
        ClassSubject = classSubject;
    }

    public String getClassUid() {
        return ClassUid;
    }

    public void setClassUid(String classUid) {
        ClassUid = classUid;
    }

    public String getInstituteName() {
        return InstituteName;
    }

    public void setInstituteName(String instituteName) {
        InstituteName = instituteName;
    }

    public String getTeacherUid() {
        return TeacherUid;
    }

    public void setTeacherUid(String teacherUid) {
        TeacherUid = teacherUid;
    }

    public String getTeacherUsername() {
        return TeacherUsername;
    }

    public void setTeacherUsername(String teacherUsername) {
        TeacherUsername = teacherUsername;
    }

    public int getClassThemeColor() {
        return ClassThemeColor;
    }

    public void setClassThemeColor(int classThemeColor) {
        ClassThemeColor = classThemeColor;
    }

    public Classes() {
    }

    @Override
    public String toString() {
        return "Classes{" +
                "Batch='" + Batch + '\'' +
                ", ClassDescription='" + ClassDescription + '\'' +
                ", ClassFee='" + ClassFee + '\'' +
                ", ClassName='" + ClassName + '\'' +
                ", ClassSubject='" + ClassSubject + '\'' +
                ", ClassUid='" + ClassUid + '\'' +
                ", InstituteName='" + InstituteName + '\'' +
                ", TeacherUid='" + TeacherUid + '\'' +
                ", TeacherUsername='" + TeacherUsername + '\'' +
                '}';
    }

}
