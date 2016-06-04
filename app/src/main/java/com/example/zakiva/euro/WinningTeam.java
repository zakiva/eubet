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

public class WinningTeam extends AppCompatActivity {

    public String userName;
    TextView chosenTeam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning_team);
        Firebase.setAndroidContext(this);
        userName = ((Euro) this.getApplication()).getGlobalUsername();
        chosenTeam = (TextView) findViewById(R.id.chosenTeam);

        Firebase firebase = new Firebase(getString(R.string.firebase));

        firebase.child("teams").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("###############################");
                System.out.println(snapshot.getValue());
                System.out.println(snapshot.getValue().getClass());

                draw_teams( (Map) snapshot.getValue());

            }

            @Override public void onCancelled(FirebaseError error) { }

        });


     //   String[] teams = {"England", "Germeny", "Spain", "Israel", "Italy", "Russia", "France", "England", "Germeny", "Spain", "Israel", "Italy", "Russia", "France",};
     //   String[] scores = {"3", "6", "9", "6", "9", "2", "7", "3", "6", "9", "6", "9", "2", "7"};

        firebase.child("Users").child(userName).child("winningTeam").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                System.out.println("on data changed >>winning<<");

                Map<String, String> winningTeam = (Map<String, String>) snapshot.getValue();

                if (winningTeam != null) {
                    System.out.println(winningTeam.toString());

                    chosenTeam.setText(winningTeam.get("name"));

                }
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }

        });

    }

    public void draw_teams (Map mp) {
        ArrayList<String> teams = new ArrayList<String>();
        ArrayList<String> scores = new ArrayList<String>();

        final Iterator it = mp.entrySet().iterator();
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
        ListAdapter listAdapter = new WinningTeamAdapter(getBaseContext(), items, this, start);
        ListView listView = (ListView) findViewById(R.id.listWinningTeams);
        listView.setAdapter(listAdapter);
    }
}
