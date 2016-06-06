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

public class UsersGroups extends AppCompatActivity {
    //int createOrJoinFlag;
    private Firebase firebase;
    String userName = "";
    String createOrJoinFlag = "";
    Button b;

    @Override
    public void onResume() {
        super.onResume();
        b = ((Button) findViewById(R.id.buttonCreateOrJoin));
        b.setEnabled(true);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        b = ((Button) findViewById(R.id.buttonCreateOrJoin));
        b.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_groups);
        //((EditText) findViewById(R.id.editText)).setVisibility(View.INVISIBLE);
        //((EditText) findViewById(R.id.editText2)).setVisibility(View.INVISIBLE);
        //((TextView) findViewById(R.id.textView2)).setVisibility(View.INVISIBLE);
        //((TextView) findViewById(R.id.textView3)).setVisibility(View.INVISIBLE);
        b = ((Button) findViewById(R.id.buttonCreateOrJoin));
        Firebase.setAndroidContext(this);
        firebase = new Firebase(getString(R.string.firebase));
        userName = getFromLocalDatabase("username");
        final Bundle extras = getIntent().getExtras();
        createOrJoinFlag = extras.getString("createOrJoinFlag");
        if (createOrJoinFlag.equals("join")){
            ((Button) findViewById(R.id.buttonCreateOrJoin)).setText("Join Group");
            ((TextView) findViewById(R.id.textView)).setText("Join an existing Bet Group");
        } else {
            ((Button) findViewById(R.id.buttonCreateOrJoin)).setText("Create Group");
            ((TextView) findViewById(R.id.textView)).setText("Create a new Bet Group");
        }
    }

    public void buttonCreateOrJoinClicked (View view){
        b.setEnabled(false);
        if (!createOrJoinFlag.equals("join")){ // Create new group
            final String groupName = ((EditText) findViewById(R.id.editText)).getText().toString();
            final String password = ((EditText) findViewById(R.id.editText2)).getText().toString();
            if (groupName.equals("")){
                Toast.makeText(getApplicationContext(),"Pleae enter group name",Toast.LENGTH_SHORT).show();
                b.setEnabled(true);
            }
            else if (password.equals("")){
                Toast.makeText(getApplicationContext(),"Pleae enter password",Toast.LENGTH_SHORT).show();
                b.setEnabled(true);
            }
            else {
                firebase.child("BetGroups").child("GroupsNames").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.child(groupName).exists()){
                            Toast.makeText(getApplicationContext(),"Group name is already taken. Please choose another",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            firebase.child("BetGroups").child("GroupsNames").child(groupName).setValue(password);
                            firebase.child("BetGroups").child("GroupsToUsername").child(groupName).child(userName).setValue(true);
                            firebase.child("BetGroups").child("UsernameToGroups").child(userName).child(groupName).setValue(true);
                            GroupMember groupmember = new GroupMember(userName, 0, 0);
                            firebase.child("BetGroups").child("GroupsToUsernameObjects").child(groupName).push().setValue(groupmember);
                            GroupName groupname = new GroupName(groupName);
                            firebase.child("BetGroups").child("UsernameToGroupsObjects").child(userName).push().setValue(groupname);
                            Toast.makeText(getApplicationContext(),"Group created!",Toast.LENGTH_SHORT).show();

                            Intent gi = new Intent(UsersGroups.this, GroupInfo.class);
                            gi.putExtra("groupName", groupName);
                            startActivity(gi);
                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
            }
        }
        else { // Join existing group
            final String groupName = ((EditText) findViewById(R.id.editText)).getText().toString();
            final String password = ((EditText) findViewById(R.id.editText2)).getText().toString();
            if (groupName.equals("")){
                Toast.makeText(getApplicationContext(),"Pleae enter group name",Toast.LENGTH_SHORT).show();
                b.setEnabled(true);
            }
            else if (password.equals("")){
                Toast.makeText(getApplicationContext(),"Pleae enter password",Toast.LENGTH_SHORT).show();
                b.setEnabled(true);
            }
            else {
                firebase.child("BetGroups").child("GroupsNames").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.child(groupName).exists()){
                            Toast.makeText(getApplicationContext(),"Group name does not exist",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (snapshot.child(groupName).getValue().equals(password)){
                                firebase.child("BetGroups").child("GroupsToUsername").child(groupName).child(userName).setValue(true);
                                firebase.child("BetGroups").child("UsernameToGroups").child(userName).child(groupName).setValue(true);
                                GroupMember groupmember = new GroupMember(userName, 0, 0);
                                firebase.child("BetGroups").child("GroupsToUsernameObjects").child(groupName).push().setValue(groupmember);
                                GroupName groupname = new GroupName(groupName);
                                firebase.child("BetGroups").child("UsernameToGroupsObjects").child(userName).push().setValue(groupname);
                                Toast.makeText(getApplicationContext(),"You have joined the group!",Toast.LENGTH_SHORT).show();

                                Intent gi = new Intent(UsersGroups.this, GroupInfo.class);
                                gi.putExtra("groupName", groupName);
                                startActivity(gi);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Wrong password",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
            }
        }
    }

    public String getFromLocalDatabase(String key){
        SharedPreferences sharedPref = getSharedPreferences("FILE1", 0);
        //int defaultValue = getResources().getInteger(R.string.saved_high_score_default);
        return sharedPref.getString(key, "NULL");
    }
}
