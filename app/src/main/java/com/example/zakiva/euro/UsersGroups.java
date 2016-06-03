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
    int createOrJoinFlag;
    private Firebase firebase;
    String userName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_groups);
        ((EditText) findViewById(R.id.editText)).setVisibility(View.INVISIBLE);
        ((EditText) findViewById(R.id.editText2)).setVisibility(View.INVISIBLE);
        ((TextView) findViewById(R.id.textView2)).setVisibility(View.INVISIBLE);
        ((TextView) findViewById(R.id.textView3)).setVisibility(View.INVISIBLE);
        ((Button) findViewById(R.id.buttonCreateOrJoin)).setVisibility(View.INVISIBLE);
        Firebase.setAndroidContext(this);
        firebase = new Firebase(getString(R.string.firebase));
        userName = getFromLocalDatabase("username");
    }

    public void buttonCreateGroupClicked (View view){
        ((EditText) findViewById(R.id.editText)).setVisibility(View.VISIBLE);
        ((EditText) findViewById(R.id.editText)).setText("");
        ((EditText) findViewById(R.id.editText2)).setVisibility(View.VISIBLE);
        ((EditText) findViewById(R.id.editText2)).setText("");
        ((TextView) findViewById(R.id.textView2)).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.textView3)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.buttonCreateOrJoin)).setText("Create");
        ((Button) findViewById(R.id.buttonCreateOrJoin)).setVisibility(View.VISIBLE);
        createOrJoinFlag = 1;
    }

    public void buttonJoinGroupClicked (View view){
        ((EditText) findViewById(R.id.editText)).setVisibility(View.VISIBLE);
        ((EditText) findViewById(R.id.editText)).setText("");
        ((EditText) findViewById(R.id.editText2)).setVisibility(View.VISIBLE);
        ((EditText) findViewById(R.id.editText2)).setText("");
        ((TextView) findViewById(R.id.textView2)).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.textView3)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.buttonCreateOrJoin)).setText("Join");
        ((Button) findViewById(R.id.buttonCreateOrJoin)).setVisibility(View.VISIBLE);
        createOrJoinFlag = 2;
    }

    public void buttonCreateOrJoinClicked (View view){
        if (createOrJoinFlag == 1){ // Create new group
            final String groupName = ((EditText) findViewById(R.id.editText)).getText().toString();
            final String password = ((EditText) findViewById(R.id.editText2)).getText().toString();
            if (groupName.equals("")){
                Toast.makeText(getApplicationContext(),"Pleae enter group name",Toast.LENGTH_SHORT).show();
            }
            else if (password.equals("")){
                Toast.makeText(getApplicationContext(),"Pleae enter password",Toast.LENGTH_SHORT).show();
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
                            GroupMember groupmember = new GroupMember(userName, 0);
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
            }
            else if (password.equals("")){
                Toast.makeText(getApplicationContext(),"Pleae enter password",Toast.LENGTH_SHORT).show();
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
                                GroupMember groupmember = new GroupMember(userName, 0);
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
