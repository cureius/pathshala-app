package com.jugaru.pathshala.homeFragments;

public class Classes {
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
