package com.example.zakiva.euro;

import java.util.List;

/**
 * Created by Ariel on 6/2/2016.
 */

public class GroupMember {
    String username;
    int score;

    // Required default constructor for Firebase object mapping
    GroupMember(){
    }

    GroupMember(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }
}
