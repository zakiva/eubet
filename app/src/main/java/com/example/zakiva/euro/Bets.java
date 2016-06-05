package com.example.zakiva.euro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Bets extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bets);
        ((ImageView) findViewById(R.id.game_icon)).bringToFront();

    }

    public void buttonWinningTeamClicked(View view) {
        Intent win = new Intent(Bets.this, WinningTeam.class);
        startActivity(win);
    }

    public void buttonGamesClicked(View view) {
        Intent games = new Intent(Bets.this, GamesList.class);
        startActivity(games);
    }

    public void buttonScorersClicked(View view) {
        Intent scorer = new Intent(Bets.this, Scorer.class);
        startActivity(scorer);
    }
}
