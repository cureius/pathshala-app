package com.jugaru.pathshala.classInterface;

public class UploadClassNotes {
    String fileName ;
    String fileTopic ;
    String fileUrl ;

    public UploadClassNotes() {
    }

    public UploadClassNotes(String fileName, String fileTopic, String fileUrl) {
        this.fileName = fileName;
        this.fileTopic = fileTopic;
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

    @Override
    public String toString() {
        return "ClassNotes{" +
                "fileName='" + fileName + '\'' +
                ", fileTopic='" + fileTopic + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                '}';
    }
}
