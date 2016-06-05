package com.example.zakiva.euro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Firebase firebase;
    int isUserExist;
    String curUsername = "NULL";
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b = (Button) findViewById(R.id.buttonStartBet);
        Firebase.setAndroidContext(this);
        firebase = new Firebase(getString(R.string.firebase));
        isUserExist = 0;
        curUsername = getFromLocalDatabase("username");
        if (!curUsername.equals("NULL")){
            ((TextView) findViewById(R.id.textView)).setText("Hello " + curUsername);
            ((EditText) findViewById(R.id.editText)).setVisibility(View.INVISIBLE);
            b.setText("Start bet");
            isUserExist = 1;
        } else {
            b.setText("Login");
        }
        ((Euro) this.getApplication()).setGlobalUsername(curUsername);

    }
    @Override
    public void onResume() {
        super.onResume();
        b = (Button) findViewById(R.id.buttonStartBet);
        isUserExist = 0;
        curUsername = getFromLocalDatabase("username");
        if (!curUsername.equals("NULL")){
            ((TextView) findViewById(R.id.textView)).setText("Hello " + curUsername);
            ((EditText) findViewById(R.id.editText)).setVisibility(View.INVISIBLE);
            b.setText("Start bet");
            isUserExist = 1;
        } else {
            b.setText("Login");
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        b = (Button) findViewById(R.id.buttonStartBet);
        isUserExist = 0;
        curUsername = getFromLocalDatabase("username");
        if (!curUsername.equals("NULL")){
            ((TextView) findViewById(R.id.textView)).setText("Hello " + curUsername);
            ((EditText) findViewById(R.id.editText)).setVisibility(View.INVISIBLE);
            b.setText("Start bet");
            isUserExist = 1;
        } else {
            b.setText("Login");
        }
    }

    public String getFromLocalDatabase(String key){
        SharedPreferences sharedPref = getSharedPreferences("FILE1", 0);
        //int defaultValue = getResources().getInteger(R.string.saved_high_score_default);
        return sharedPref.getString(key, "NULL");
    }

    public void storeInLocalDatabae(String key, String value ){
        SharedPreferences sharedPref = getSharedPreferences("FILE1", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void buttonStartBetClicked(View view) {

        if (isUserExist == 1){
            Intent bets = new Intent(MainActivity.this, Bets.class);
            startActivity(bets);
            return;
        }
        ((Euro) this.getApplication()).setGlobalUsername(curUsername);
        final String userName = ((EditText) findViewById(R.id.editText)).getText().toString();
        if (userName.equals("")){
            Toast.makeText(getApplicationContext(),"Please enter your user name",Toast.LENGTH_SHORT).show();
        }
        else {
            firebase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.child(userName).exists()){
                        Toast.makeText(getApplicationContext(),"User name already taken. Please choose another",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        storeInLocalDatabae("username", userName);
                        firebase.child("Users").child(userName).setValue(true);
                        Intent bets = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(bets);
                    }
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
        }
    }

    public void buttonCreateGamesClicked (View view) {

        Game game1 = new Game(1, "Macabi", 2, "Hapoal", 5, 8, "May 30, 21:00", "Group A");
        Game game2 = new Game(2, "Ramama", 1, "TLV", 3, 3, "May 20, 13:00", "Group B");
        Game game3 = new Game(3, "Eilat", 7, "NY", 1, 4, "May 10, 11:00", "Group C");
        Game game4 = new Game(4, "Israel", 5, "USA", 5, 9, "May 23, 21:00", "Group D");

        game1.bets.put("demoUserName", -1);
        game2.bets.put("demoUserName", -1);
        game3.bets.put("demoUserName", -1);
        game4.bets.put("demoUserName", -1);


        firebase.child("games").push().setValue(game1);
        firebase.child("games").push().setValue(game2);
        firebase.child("games").push().setValue(game3);
        firebase.child("games").push().setValue(game4);
    }

    public void buttonBetGroupsClicked (View view) {
        if (curUsername.equals("NULL")){
            Toast.makeText(getApplicationContext(),"Please login",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent ug = new Intent(MainActivity.this, MyGroups.class);
        startActivity(ug);
    }

}
