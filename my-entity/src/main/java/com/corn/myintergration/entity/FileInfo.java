package com.corn.myintergration.entity;

/**
 * @Auther: suyiming
 * @Date: 18-11-17 10:35
 * @Description:
 */

public class FileInfo {
    private String path;
    private String  fileSize;
    private String fileName;
    private String fileType;
    private String info;
    private String isConform;
    public FileInfo() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getIsConform() {
        return isConform;
    }

    public void setIsConform(String isConform) {
        this.isConform = isConform;
    }
}
