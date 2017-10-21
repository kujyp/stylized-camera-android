package com.asuscomm.yangyinetwork.stylizedcamera;

import com.google.firebase.database.ServerValue;

/**
 * Created by jaeyoung on 14/10/2017.
 */

public class Task {
    private String downloadUrl;
    private String path;
    private String styleName;
    private Object createdAt;

    public Task() {
        this(null, null);
    }

    public Task(String downloadUrl, String path) {
        this(downloadUrl, path, null);
    }

    public Task(String downloadUrl, String path, String styleName) {
        this(downloadUrl, path, styleName, ServerValue.TIMESTAMP);
    }

    public Task(String downloadUrl, String path, String styleName, Object createdAt) {
        this.downloadUrl = downloadUrl;
        this.path = path;
        this.styleName = styleName;
        this.createdAt = createdAt;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }
}
