package com.asuscomm.yangyinetwork.stylizedcamera;

/**
 * Created by jaeyoung on 14/10/2017.
 */

public class Task {
    private String uri;

    public Task() {
    }

    public Task(String path) {
        uri = path;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
