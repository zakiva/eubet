package com.example.zakiva.euro;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.firebase.client.Firebase;

public class MyGroups extends AppCompatActivity {

    private Firebase firebase;
    String userName;
    Button b1;
    Button b2;

    @Override
    public void onResume() {
        super.onResume();
        b1 = ((Button) findViewById(R.id.buttonCreateGroup));
        b2 = ((Button) findViewById(R.id.buttonJoinGroup));
        b1.setEnabled(true);
        b2.setEnabled(true);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        b1 = ((Button) findViewById(R.id.buttonCreateGroup));
        b2 = ((Button) findViewById(R.id.buttonJoinGroup));
        b1.setEnabled(true);
        b2.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
        firebase = new Firebase(getString(R.string.firebase));
        userName = ((Euro) this.getApplication()).getGlobalUsername();
        b1 = (Button) findViewById(R.id.buttonCreateGroup);
        b2 = (Button) findViewById(R.id.buttonJoinGroup);
        setListeners();
    }

    void setListeners() {

        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = (ListView) findViewById(R.id.myGroupList);
        // Tell our list adapter that we only want 50 messages at a time
        final GroupListAdapter mGroupListAdapter = new GroupListAdapter(firebase.child("BetGroups").child("UsernameToGroupsObjects").child(userName), this, R.layout.group_element);
        listView.setAdapter(mGroupListAdapter);
        mGroupListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(0);
            }
        });
    }

    public void buttonJoinGroupClicked (View view){
        b1.setEnabled(false);
        b2.setEnabled(false);
        Intent ug = new Intent(MyGroups.this, UsersGroups.class);
        ug.putExtra("createOrJoinFlag", "join");
        startActivity(ug);
    }

    public void buttonCreateGroupClicked (View view){
        b1.setEnabled(false);
        b2.setEnabled(false);
        Intent ug = new Intent(MyGroups.this, UsersGroups.class);
        ug.putExtra("createOrJoinFlag", "create");
        startActivity(ug);
    }
}
