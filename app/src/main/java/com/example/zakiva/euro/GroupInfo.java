package com.example.zakiva.euro;

import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import android.widget.ListView;

public class GroupInfo extends AppCompatActivity {

    private Firebase firebase;
    String groupName;
    String password;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        //Firebase.setAndroidContext(this);
        firebase = new Firebase("https://eurofirebase.firebaseio.com/");

        final Bundle extras = getIntent().getExtras();
        groupName = extras.getString("groupName");
        ((TextView) findViewById(R.id.textView)).setText(groupName);

        setListeners();
    }

    void setListeners() {
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        //final ListView listView = getListView();
        final ListView listView = (ListView) findViewById(R.id.memberList);
        // Tell our list adapter that we only want 50 messages at a time
        final GroupMemberListAdapter mGroupMemberListAdapter = new GroupMemberListAdapter(firebase.child("BetGroups").child("GroupsToUsernameObjects").child(groupName), this, R.layout.member_element);
        listView.setAdapter(mGroupMemberListAdapter);
        mGroupMemberListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mGroupMemberListAdapter.getCount() - 1);
            }
        });
    }

    public void buttonInviteClicked (View view) {
        number = ((EditText) findViewById(R.id.editText)).getText().toString();
        firebase.child("BetGroups").child("GroupsNames").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                password = (String) snapshot.child(groupName).getValue();
                String content = "You were invited to join group " + groupName + ". The password is " + password;
                //Log.d("sms:", content);
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number, null, content, null, null);
                    Toast.makeText(getApplicationContext(),"Invitation sent",Toast.LENGTH_SHORT).show();
                } catch (Exception e1) {
                    Toast.makeText(getApplicationContext(),"Problem sending sms",Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }
}
