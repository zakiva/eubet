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

                        if (Integer.parseInt(sa[11]) >= 0) {
                            return 1;
                        }
                        if (Integer.parseInt(sb[11]) >= 0) {
                            return -1;
                        }


                        int v = sa[10].compareTo(sb[10]);

                        return v;

                        // it can also return 0, and 1
                    }
                }
        );

        ListAdapter listAdapter = new GamesListAdapter(getBaseContext(), items);
        ListView listView = (ListView) findViewById(R.id.gamesList);
        listView.setAdapter(listAdapter);
    }
}
