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

public class WinningTeam extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning_team);
        Firebase.setAndroidContext(this);

        Firebase firebase = new Firebase("https://eurofirebase.firebaseio.com/");

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


    }

    public void draw_teams (Map mp) {
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

        ArrayList<String[]> items = new ArrayList<String[]>();
        String[] item;

        for (int i = 0; i < teams.size(); i++) {
            item = new String[1];
            item[0] = " " + teams.get(i) +" " + scores.get(i);
            items.add(item);
        }


        ListAdapter listAdapter = new WinningTeamListAdapter(getBaseContext(), items);
        ListView listView = (ListView) findViewById(R.id.listWinningTeams);
        listView.setAdapter(listAdapter);
    }
}
