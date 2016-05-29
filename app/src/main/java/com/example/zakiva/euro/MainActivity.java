package com.example.zakiva.euro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.firebase.client.Firebase;

public class MainActivity extends AppCompatActivity {

    public String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = "zahi";


    }

    public void buttonStartBetClicked(View view) {
        Intent bets = new Intent(MainActivity.this, Bets.class);
        startActivity(bets);
    }

    public void buttonCreateGamesClicked (View view) {

        Firebase.setAndroidContext(this);
        Firebase firebase = new Firebase("https://eurofirebase.firebaseio.com/");

        Game game1 = new Game(1, "Macabi", 2, "Hapoal", 5, 8, "May 30, 21:00");
        Game game2 = new Game(2, "Ramama", 1, "TLV", 3, 3, "May 20, 13:00");
        Game game3 = new Game(3, "Eilat", 7, "NY", 1, 4, "May 10, 11:00");
        Game game4 = new Game(4, "Israel", 5, "USA", 5, 9, "May 23, 21:00");

        game1.bets.put("demoUserName", -1);
        game2.bets.put("demoUserName", -1);
        game3.bets.put("demoUserName", -1);
        game4.bets.put("demoUserName", -1);


        firebase.child("games").push().setValue(game1);
        firebase.child("games").push().setValue(game2);
        firebase.child("games").push().setValue(game3);
        firebase.child("games").push().setValue(game4);
    }

}
