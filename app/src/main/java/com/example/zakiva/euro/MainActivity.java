package com.example.zakiva.euro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Euro) this.getApplication()).setGlobalUsername("global user name");
        Firebase.setAndroidContext(this);
        firebase = new Firebase(getString(R.string.firebase));

        isUserExist = 0;
        String curUsername = getFromLocalDatabase("username");
        if (!curUsername.equals("NULL")){
            ((TextView) findViewById(R.id.textView)).setText("Hello " + curUsername);
            ((EditText) findViewById(R.id.editText)).setVisibility(View.INVISIBLE);
            isUserExist = 1;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        isUserExist = 0;
        String curUsername = getFromLocalDatabase("username");
        if (!curUsername.equals("NULL")){
            ((TextView) findViewById(R.id.textView)).setText("Hello " + curUsername);
            ((EditText) findViewById(R.id.editText)).setVisibility(View.INVISIBLE);
            isUserExist = 1;
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        isUserExist = 0;
        String curUsername = getFromLocalDatabase("username");
        if (!curUsername.equals("NULL")){
            ((TextView) findViewById(R.id.textView)).setText("Hello " + curUsername);
            ((EditText) findViewById(R.id.editText)).setVisibility(View.INVISIBLE);
            isUserExist = 1;
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
        final String userName = ((EditText) findViewById(R.id.editText)).getText().toString();
        if (userName.equals("")){
            Toast.makeText(getApplicationContext(),"Pleae enter your user name",Toast.LENGTH_SHORT).show();
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
                        Intent bets = new Intent(MainActivity.this, Bets.class);
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

    public void buttonBetGroupsClicked (View view) {
        Intent ug = new Intent(MainActivity.this, UsersGroups.class);
        startActivity(ug);
    }

}
