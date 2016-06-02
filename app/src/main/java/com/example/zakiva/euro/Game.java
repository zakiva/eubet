package com.example.zakiva.euro;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zakiva on 5/29/16.
 */
public class Game {

    public int id;
    public String team1;
    public int score1;
    public String team2;
    public int score2;
    public int scoreX;
    public String description;
    public int result;
    public String time;
    public Map<String, Integer> bets;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    Game() {
    }


    public Game(int id, String team1, int score1, String team2, int score2, int scoreX, String time, String description) {
        this.id = id;
        this.team1 = team1;
        this.score1 = score1;
        this.team2 = team2;
        this.score2 = score2;
        this.scoreX = scoreX;
        this.time = time;
        this.description = description;
        this.result = -1;
        bets = new HashMap <String, Integer>();
    }
}
