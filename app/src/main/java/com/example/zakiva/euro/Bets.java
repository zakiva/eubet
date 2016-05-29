package com.example.zakiva.euro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Bets extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bets);
    }

    public void buttonWinningTeamClicked(View view) {
        Intent win = new Intent(Bets.this, WinningTeam.class);
        startActivity(win);
    }

    public void buttonGamesClicked(View view) {
        Intent games = new Intent(Bets.this, GamesList.class);
        startActivity(games);
    }
}
