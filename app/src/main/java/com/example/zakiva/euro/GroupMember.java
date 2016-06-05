package com.example.zakiva.euro;

import java.util.List;

/**
 * Created by Ariel on 6/2/2016.
 */

public class GroupMember {
    String username;
    int score;
    int minusScore;

    // Required default constructor for Firebase object mapping
    GroupMember(){
    }

    GroupMember(String username, int score, int minusScore) {
        this.username = username;
        this.score = score;
        this.minusScore = minusScore;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public int getMinusScore() {
        return minusScore;
    }
}
