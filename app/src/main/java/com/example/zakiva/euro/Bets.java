package com.example.zakiva.euro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

public class Bets extends AppCompatActivity {

    private Firebase firebase;
    String username = "";
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bets);
        ((ImageView) findViewById(R.id.game_icon)).bringToFront();
        firebase = new Firebase(getString(R.string.firebase));
        username = getFromLocalDatabase("username");
        tv = ((TextView) findViewById(R.id.score));
        calculateScoreForUser(username, "s");

    }

    public String getFromLocalDatabase(String key){
        SharedPreferences sharedPref = getSharedPreferences("FILE1", 0);
        //int defaultValue = getResources().getInteger(R.string.saved_high_score_default);
        return sharedPref.getString(key, "NULL");
    }

    public void calculateScoreForUser (final String user, final String key) {

        firebase.child("games").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                long totalScore = 0;

                Map<String, Object> games = (Map) snapshot.getValue();

                for (Map.Entry<String, Object> entry : games.entrySet()) {
                    Map game = (Map) entry.getValue();
                    Map<String, Long> bets = (Map<String, Long>) game.get("bets");
                    long result = (long) game.get("result");
                    if (bets.get(user) == null)
                        continue;
                    long userBet = (long) bets.get(user);
                    if (result>=0){
                        if (result==userBet){
                            if (result==0){
                                totalScore += (long) game.get("score1");
                            }
                            else if (result==1){
                                totalScore += (long) game.get("score2");
                            }
                            else if (result == 2){
                                totalScore += (long) game.get("scoreX");
                            }
                        }
                    }

                }
                addGeneralResultsAndWrite(totalScore, user, key);
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });
    }



    public void addGeneralResultsAndWrite(final long gamesScore, final String userName, final String key){

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                long totalScore = gamesScore;

                Map<String, Object> data = (Map) snapshot.getValue();
                String winner = (String) data.get("winner");
                String scorer = (String) data.get("scorer");

                Map<String, Object> users = (Map) data.get("Users");
                Object o = users.get(userName);
                if (!(o.getClass().equals(Boolean.class))) {
                    Map<String, Object> user = (Map) users.get(userName);

                    if (user.get("winningTeam") != null) {
                        Map<String, Object> winningTeam = (Map) user.get("winningTeam");
                        String team = (String) winningTeam.get("name");
                        if (team.equals(winner))
                            totalScore += (long) winningTeam.get("score");
                    }

                    if (user.get("scorer") != null) {
                        Map<String, Object> scorerChosen = (Map) user.get("scorer");
                        String player = (String) scorerChosen.get("name");
                        if (player.equals(scorer))
                            totalScore += (long) scorerChosen.get("score");
                    }
                }

                writeUserScore(userName, totalScore, key);
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });

    }

    public void writeUserScore (String userName, long score, String key) {
        int s = (int) score;
        //GroupMember groupmember = new GroupMember(userName, s, s * (-1));
        //s = 5;
        //Log.d(userName, ": " + key);
        firebase.child("UsersScores").child(userName).setValue(s);
        tv.setText("Your score is " + Integer.toString(s));
        //firebase.child("BetGroups").child("GroupsToUsernameObjects").child(groupName).child(key).child("minusScore").setValue(-1*s);
        //firebase.child("Users").child(userName).child("score").child(key).child("score").setValue(score);
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

    public void buttonBetGroupsClicked (View view) {
        Intent ug = new Intent(Bets.this, MyGroups.class);
        startActivity(ug);
    }
}
