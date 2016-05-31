package com.example.zakiva.euro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class GamesList extends AppCompatActivity {

    Firebase firebase;
    public String userName = "TTTTTTTTTTTTT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list);

        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://eurofirebase.firebaseio.com/");

        firebase.child("games").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                draw_games((Map) snapshot.getValue());
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });
    }

    public void draw_games(Map<String, Object> map) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>draw_games<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");


        final ArrayList<String[]> items = new ArrayList<String[]>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Map game = (Map) entry.getValue();
            Map<String, Long> bets = (Map<String, Long>) game.get("bets");

            final String[] item = new String[6];
            item[0] = " " + game.get("team1") + " " + Long.toString((long) game.get("score1"));
            item[1] = " " + game.get("team2") + " " + Long.toString((long) game.get("score2"));
            item[2] = " Tie " + game.get("scoreX");
            item[3] = (String) game.get("time");
            //item[4] = bets.get(((Euro) this.getApplication()).getGlobalUsername()) == null ? Integer.toString(-1) : Long.toString((long) bets.get(((Euro) this.getApplication()).getGlobalUsername()));
            item[4] = bets.get(userName) == null ? Integer.toString(-1) : Long.toString((long) bets.get(userName));
            item[5] = key;

            items.add(item);
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>end for<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");


        ListAdapter listAdapter = new GamesListAdapter(getBaseContext(), items);
        ListView listView = (ListView) findViewById(R.id.gamesList);
        listView.setAdapter(listAdapter);
    }
}
