package com.example.zakiva.euro;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zakiva on 5/29/16.
 */
public class Team {

    public int id;
    public String name;
    public int score;


    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    Team() {
    }


    public Team(int id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;

    }
}
