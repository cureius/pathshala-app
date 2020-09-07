package com.jugaru.pathshala.classInterface;

public class UploadVideoLecture {
    String fileName ;
    String fileTopic ;
    String fileUnit ;
    String fileUrl ;

    public UploadVideoLecture() {
    }


    public UploadVideoLecture(String fileName, String fileTopic, String fileUnit, String fileUrl) {
        this.fileName = fileName;
        this.fileTopic = fileTopic;
        this.fileUnit = fileUnit;
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileTopic() {
        return fileTopic;
    }

    public void setFileTopic(String fileTopic) {
        this.fileTopic = fileTopic;
    }

    public String getFileUnit() {
        return fileUnit;
    }

    public void setFileUnit(String fileUnit) {
        this.fileUnit = fileUnit;
    }

    @Override
    public String toString() {
        return "VideoLecture{" +
                "fileName='" + fileName + '\'' +
                ", fileTopic='" + fileTopic + '\'' +
                ", fileUnit='" + fileUnit + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                '}';
    }
}
