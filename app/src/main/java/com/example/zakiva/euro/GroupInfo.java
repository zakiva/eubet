package com.example.zakiva.euro;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.provider.ContactsContract;
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

import java.util.Map;

public class GroupInfo extends AppCompatActivity {

    private Firebase firebase;
    String groupName;
    String password;
    String number;




    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (1) :
                String name = "";
                String name2 = "";
                String phone = "";

                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        int column = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        name = c.getString(column);
                    }
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
                    while (phones.moveToNext())
                    {
                        name2 = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (name.equals(name2)){
                            ((EditText) findViewById(R.id.editText)).setText(phone);
                        }
                    }

                    /*
                    firebase.child("Test").push().setValue(9);
                    Uri contactData = data.getData();
                    Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                    firebase.child("Test").push().setValue(10);
                    if (c.moveToFirst()) {
                        int column = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        firebase.child("Test").push().setValue(column);
                        String name = c.getString(column);
                        firebase.child("Test").push().setValue(11);
                        firebase.child("Test").push().setValue(name);

                    }
                    */
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        //Firebase.setAndroidContext(this);
        firebase = new Firebase(getString(R.string.firebase));

        final Bundle extras = getIntent().getExtras();
        groupName = extras.getString("groupName");
        ((TextView) findViewById(R.id.textView)).setText(groupName);

        firebase.child("BetGroups").child("GroupsToUsernameObjects").child(groupName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot member: snapshot.getChildren()) {
                    GroupMember groupmember = member.getValue(GroupMember.class);
                    //Log.d(groupmember.getUsername(), member.getKey());
                    //Log.e("Get Data", post.<YourMethod>());
                    calculateScoreForUser(groupmember.getUsername(), member.getKey());
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                //Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });

        setListeners();
    }

    void setListeners() {
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        //final ListView listView = getListView();
        final ListView listView = (ListView) findViewById(R.id.memberList);
        // Tell our list adapter that we only want 50 messages at a time
        final GroupMemberListAdapter mGroupMemberListAdapter = new GroupMemberListAdapter(firebase.child("BetGroups").child("GroupsToUsernameObjects").child(groupName).orderByChild("minusScore"), this, R.layout.member_element);
        listView.setAdapter(mGroupMemberListAdapter);
        mGroupMemberListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mGroupMemberListAdapter.getCount() - 1);
            }
        });
    }

    public void buttonCClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 1);
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
                    Toast.makeText(getApplicationContext(), "Invitation sent", Toast.LENGTH_SHORT).show();
                } catch (Exception e1) {
                    Toast.makeText(getApplicationContext(), "Problem sending sms", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
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
        firebase.child("BetGroups").child("GroupsToUsernameObjects").child(groupName).child(key).child("score").setValue(s);
        firebase.child("BetGroups").child("GroupsToUsernameObjects").child(groupName).child(key).child("minusScore").setValue(-1*s);
        //firebase.child("Users").child(userName).child("score").child(key).child("score").setValue(score);
    }
}
