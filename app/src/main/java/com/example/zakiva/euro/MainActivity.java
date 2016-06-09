package com.example.zakiva.euro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Firebase firebase;
    int isUserExist;
    String curUsername = "NULL";
    Button b;

    public boolean isAlphaNumeric(String s){
        String pattern= "^[a-zA-Z0-9 ]*$";
        if(s.matches(pattern)){
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Locale locale = new Locale("en_US");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        b = (Button) findViewById(R.id.buttonStartBet);
        Firebase.setAndroidContext(this);
        firebase = new Firebase(getString(R.string.firebase));
        isUserExist = 0;
        curUsername = getFromLocalDatabase("username");
        if (!curUsername.equals("NULL")){
            ((TextView) findViewById(R.id.textView)).setText("Hello " + curUsername);
            ((EditText) findViewById(R.id.editText)).setVisibility(View.INVISIBLE);
           // b.setText("Start Bet!");
            isUserExist = 1;
            ((Euro) this.getApplication()).setGlobalUsername(curUsername);
            Intent bets = new Intent(MainActivity.this, Bets.class);
            startActivity(bets);
        }

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
            Intent bets = new Intent(MainActivity.this, Bets.class);
            startActivity(bets);
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
            Intent bets = new Intent(MainActivity.this, Bets.class);
            startActivity(bets);
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
        b.setEnabled(false);

        ((Euro) this.getApplication()).setGlobalUsername(curUsername);
        final String userName = ((EditText) findViewById(R.id.editText)).getText().toString();
        if (userName.equals("")){
            Toast.makeText(getApplicationContext(),"Please enter your user name",Toast.LENGTH_SHORT).show();
            b.setEnabled(true);
        }
        else if (!isAlphaNumeric(userName)){
            Toast.makeText(getApplicationContext(),"Please use only alphanumeric characters",Toast.LENGTH_SHORT).show();
            b.setEnabled(true);
        }
        else {
            firebase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.child(userName).exists()){
                        Toast.makeText(getApplicationContext(),"User name already taken. Please choose another",Toast.LENGTH_SHORT).show();
                        b.setEnabled(true);
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




}
