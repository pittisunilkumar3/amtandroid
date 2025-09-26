package com.qdocs.ssre241123.models;

public class TeacherDocument {
    private String id;
    private String title;
    private String fileName;
    private String fileType;
    private String fileSize;
    private String createdAt;
    private String fileUrl;
    private String description;
    private String uploadedBy;
    private String category;

    public TeacherDocument() {
    }

    public TeacherDocument(String id, String title, String fileName, String fileType, 
                          String fileSize, String createdAt, String fileUrl) {
        this.id = id;
        this.title = title;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.createdAt = createdAt;
        this.fileUrl = fileUrl;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "TeacherDocument{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", description='" + description + '\'' +
                ", uploadedBy='" + uploadedBy + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
