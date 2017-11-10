package com.example.amk.cybrchat;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by amk on 11/9/2017.
 */

public class Chat extends AppCompatActivity {
    private Button send;
    private EditText msg;
    private TextView convo;
    private String user;
    private String room;
    private String temp;
    private String chat_msg;
    private String chat_user;
    private DatabaseReference root;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //Setting room ID and User ID
        user = getIntent().getExtras().get("user_name").toString();
        room = getIntent().getExtras().get("room_name").toString();
        setTitle(room);
        //Buttons
        send = (Button) findViewById(R.id.send);
        msg = (EditText) findViewById(R.id.msg);
        convo = (TextView) findViewById(R.id.convo);

        //Looking inside the first level of chat rooms which are messages
        root = FirebaseDatabase.getInstance().getReference().child(room);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Map<String, Object> map = new HashMap<String, Object>();
                temp = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference root_msg = root.child(temp);
                Map<String,Object> namemsgmap = new HashMap<String, Object>();
                namemsgmap.put("na,e",user);
                namemsgmap.put("msg",msg.getText().toString());

                root_msg.updateChildren(namemsgmap);

            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat_convo(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat_convo(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void append_chat_convo(DataSnapshot dataSnapshot){
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()){
            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_user = (String) ((DataSnapshot)i.next()).getValue();
            convo.append(chat_user +" : " + chat_msg + " \n");

        }
    }
}
