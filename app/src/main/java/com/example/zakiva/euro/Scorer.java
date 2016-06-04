package com.example.zakiva.euro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Scorer extends AppCompatActivity {

    public String userName;
    TextView chosenPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorer);
        Firebase.setAndroidContext(this);
        userName = ((Euro) this.getApplication()).getGlobalUsername();
        chosenPlayer = (TextView) findViewById(R.id.chosenPlayer);

        Firebase firebase = new Firebase(getString(R.string.firebase));

        firebase.child("players").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("###############################");
                System.out.println(snapshot.getValue());
                System.out.println(snapshot.getValue().getClass());

                draw_players((Map) snapshot.getValue());

            }

            @Override public void onCancelled(FirebaseError error) { }

        });

        firebase.child("Users").child(userName).child("scorer").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                System.out.println("on data changed >>scorer<<");

                Map<String, String> scorer = (Map<String, String>) snapshot.getValue();

                if (scorer != null) {

                    System.out.println(scorer.toString());

                    chosenPlayer.setText(scorer.get("name"));
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }

        });

    }

    public void draw_players (Map mp) {
        ArrayList<String> teams = new ArrayList<String>();
        ArrayList<String> scores = new ArrayList<String>();

        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());

            teams.add((String) pair.getKey());
            scores.add(Long.toString( (long) pair.getValue()));

            it.remove();
        }

        final ArrayList<String[]> items = new ArrayList<String[]>();
        String[] item;

        for (int i = 0; i < teams.size(); i++) {
            item = new String[2];
            item[0] = teams.get(i);
            item[1] =  scores.get(i);
            items.add(item);
        }

        Firebase firebase = new Firebase(getString(R.string.firebase));
        firebase.child("start").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                setAdapter((long) snapshot.getValue(), items);
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });
    }

    public void setAdapter (long start,  ArrayList<String[]> items) {
        ListAdapter listAdapter = new ScorerAdapter(getBaseContext(), items, this, start);
        ListView listView = (ListView) findViewById(R.id.listScorers);
        listView.setAdapter(listAdapter);
    }
}
