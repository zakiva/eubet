package com.example.zakiva.euro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

public class GamesList extends AppCompatActivity {

    Firebase firebase;
    public String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list);

        userName = ((Euro) this.getApplication()).getGlobalUsername();

        if (!(userName.equals("zahi"))){
            ((Button) findViewById(R.id.buttonCreateGames)).setVisibility(View.GONE);
        }

        Firebase.setAndroidContext(this);
        firebase = new Firebase(getString(R.string.firebase));

        firebase.child("games").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                draw_games((Map) snapshot.getValue());
                System.out.println("###############################");
                System.out.println(snapshot.getValue());
                System.out.println(snapshot.getValue().getClass());
                System.out.println(snapshot.getValue());
                draw_games((Map) snapshot.getValue());
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });
    }

    public void draw_games(Map<String, Object> map) {
        System.out.println(">>>>>>>>>>>>>draw_games<<<<<<<<<<<<<<<");


        final ArrayList<String[]> items = new ArrayList<String[]>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Map game = (Map) entry.getValue();
            Map<String, Long> bets = (Map<String, Long>) game.get("bets");

            final String[] item = new String[12];
            item[0] = (String) game.get("team1");
            item[1] = (String) game.get("team2");
            item[2] = " Tie " + game.get("scoreX");
            item[3] = (String) game.get("time");
            //item[4] = bets.get(((Euro) this.getApplication()).getGlobalUsername()) == null ? Integer.toString(-1) : Long.toString((long) bets.get(((Euro) this.getApplication()).getGlobalUsername()));
            item[4] = bets.get(userName) == null ? Integer.toString(-1) : Long.toString((long) bets.get(userName));
            item[5] = key;
            item[6] = (String) game.get("description");
            item[7] = Long.toString((long) game.get("result"));
            item[8] = Long.toString((long) game.get("score1"));
            item[9] = Long.toString((long) game.get("score2"));
            item[10] = Long.toString((long) game.get("id"));
            item[11] = Long.toString((long) game.get("result"));

            items.add(item);
        }

        Collections.sort(items, new Comparator() {

                    public int compare(Object o1, Object o2) {
                        String[] sa = (String[]) o1;
                        String[] sb = (String[]) o2;

                        if (Integer.parseInt(sa[11]) >= 0 && Integer.parseInt(sb[11]) < 0) {
                            return 1;
                        }
                        if (Integer.parseInt(sb[11]) >= 0 && Integer.parseInt(sa[11]) < 0) {
                            return -1;
                        }


                        int a = Integer.parseInt(sa[10]);
                        int b = Integer.parseInt(sb[10]);

                        return a - b;

                    }
                }
        );

        ListAdapter listAdapter = new GamesListAdapter(getBaseContext(), items);
        ListView listView = (ListView) findViewById(R.id.gamesList);
        listView.setAdapter(listAdapter);
    }

    public void buttonCreateGamesClicked (View view) {

        //Game game1 = new Game(1, "France", 2, "Romania", 24, 10, "10 June, 22:00", "Group A");
        //Game game2 = new Game(2, "Albania", 11, "Switzerland", 3, 7, "11 June, 16:00", "Group A");
        //Game game3 = new Game(3, "Wales", 6, "Slovakia", 6, 6, "11 June, 19:00", "Group B");
        //Game game4 = new Game(4, "England", 4, "Russia", 8, 6, "11 June, 22:00", "Group B");

        //Game game1 = new Game(5, "Turkey", 8, "Croatia", 4, 6, "12 June, 16:00", "Group D");
        //Game game2 = new Game(6, "Poland", 3, "Northen Ireland", 12, 7, "12 June, 19:00", "Group C");
        //Game game3 = new Game(7, "Germany", 2, "Ukraine", 14, 8, "12 June, 22:00", "Group C");

        //Game game1 = new Game(8, "Spain", 2, "Czech Republic", 8, 16, "13 June, 16:00", "Group D");
        //Game game2 = new Game(9, "Ireland", 7, "Sweden", 6, 5, "13 June, 19:00", "Group E");
        //Game game3 = new Game(10, "Belgium", 5, "Italy", 7, 6, "13 June, 22:00", "Group E");

      //  Game game1 = new Game(11, "Austria", 3, "Hungary", 10, 6, "14 June, 19:00", "Group F");
     //   Game game2 = new Game(12, "Portugal", 2, "Iceland", 14, 7, "14 June, 22:00", "Group F");

      //  game1.bets.put("demoUserName", -1);
       // game2.bets.put("demoUserName", -1);
       // game3.bets.put("demoUserName", -1);
       // game4.bets.put("demoUserName", -1);


      //  firebase.child("games").push().setValue(game1);
      //  firebase.child("games").push().setValue(game2);
       // firebase.child("games").push().setValue(game3);
       // firebase.child("games").push().setValue(game4);
    }
}
