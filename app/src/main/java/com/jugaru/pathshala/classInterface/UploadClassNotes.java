package com.jugaru.pathshala.classInterface;

public class UploadClassNotes {
    String fileName ;
    String fileUrl ;

    public UploadClassNotes() {
    }

    public UploadClassNotes(String fileName, String fileUrl) {
        this.fileName = fileName;
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
}
