package com.example.zakiva.euro;

import java.util.List;

/**
 * Created by Ariel on 6/3/2016.
 */
public class GroupName {
    String name;

    // Required default constructor for Firebase object mapping
    GroupName(){
    }

    GroupName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
