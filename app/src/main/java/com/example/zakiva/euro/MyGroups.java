package com.example.zakiva.euro;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.firebase.client.Firebase;

public class MyGroups extends AppCompatActivity {

    private Firebase firebase;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
        firebase = new Firebase(getString(R.string.firebase));
        userName = ((Euro) this.getApplication()).getGlobalUsername();
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
                listView.setSelection(mGroupListAdapter.getCount() - 1);
            }
        });
    }

    public void buttonJoinOrCreateGroupClicked (View view){
        Intent ug = new Intent(MyGroups.this, UsersGroups.class);
        startActivity(ug);
    }
}
