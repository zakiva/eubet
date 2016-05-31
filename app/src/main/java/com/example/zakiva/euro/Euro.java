package com.example.zakiva.euro;

import android.app.Application;

/**
 * Created by zakiva on 6/1/16.
 */
public class Euro extends Application {

    private String globalUsername;

    public String getGlobalUsername() {
        return globalUsername;
    }

    public void setGlobalUsername(String str) {
        globalUsername = str;
    }
}
