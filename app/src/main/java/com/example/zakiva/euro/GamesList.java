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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list);

        Firebase.setAndroidContext(this);
        Firebase firebase = new Firebase("https://eurofirebase.firebaseio.com/");

        firebase.child("games").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("###############################");
                System.out.println(snapshot.getValue());
                System.out.println(snapshot.getValue().getClass());
                System.out.println(snapshot.getValue());


                draw_games( (Map) snapshot.getValue());

            }

            @Override public void onCancelled(FirebaseError error) { }

        });





      //  String[] first_teams = {"England 1", "Germeny 3", "Russia 2", "Italy 2", "Italy 1", "Russia 2", "Turkey 4"};
       // String[] second_teams = {"Germany 2", "Israel 3", "Spain 7", "Israel 1", "Germeny 2", "Holland 5", "France 2"};
      //  String[] ties = {"3", "6", "9", "6", "9", "2", "7"};



    }

    public void draw_games (Map mp) {

        Firebase.setAndroidContext(this);
        Firebase firebase = new Firebase("https://eurofirebase.firebaseio.com/");

        String userName = "zahi";

        ArrayList<String[]> items = new ArrayList<String[]>();
        String[] item;


        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            System.out.println(">>" + pair.getValue().getClass() + "<<");
            System.out.println(">>" + ((Map) pair.getValue()).get("team1") + "<<");


            item = new String[5];
            item[0] = " " + ((Map) pair.getValue()).get("team1") + " " + Long.toString((long)((Map) pair.getValue()).get("score1"));
            item[1] = " " + ((Map) pair.getValue()).get("team2") + " " + Long.toString((long)((Map) pair.getValue()).get("score2"));
            item[2] = " Tie " + ((Map) pair.getValue()).get("scoreX");
            item[3] = (String) ((Map) pair.getValue()).get("time");

            Map <String, Long> bets = (Map <String, Long>) ((Map) pair.getValue()).get("bets");
            long betOfThisUser;

            if (bets.get(userName) == null){
                bets.put(userName, (long) -1);
                firebase.child("games").child((String) pair.getKey()).child("bets").setValue(bets);
                betOfThisUser = -1;

            }
            else{
                betOfThisUser = bets.get(userName);

            }
            item[4] = Long.toString(betOfThisUser);
            items.add(item);

            it.remove();
        }



        ListAdapter listAdapter = new GamesListAdapter(getBaseContext(), items);
        ListView listView = (ListView) findViewById(R.id.gamesList);
        listView.setAdapter(listAdapter);


    }
}
