package com.example.amk.cybrchat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ChatRoomActivity extends AppCompatActivity {

    private Button addRoom;
    private EditText rName;
    private String name;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private ListView listView;
    private ArrayAdapter<String> arrAdapter;
    private ArrayList<String> num_rooms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rName = (EditText) findViewById(R.id.name_edit);
        addRoom = (Button) findViewById(R.id.addRoom);
        listView = (ListView) findViewById(R.id.listView);

        //LIST OF CHATROOMS
        arrAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, num_rooms);

        listView.setAdapter(arrAdapter);

        //REGISTER USER IF NEEDED
        req_user();

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator x = dataSnapshot.getChildren().iterator();
                Set<String> set = new HashSet<String>();

                while (x.hasNext()) {
                    set.add(((DataSnapshot) x.next()).getKey());
                }
                num_rooms.clear();
                num_rooms.addAll(set);
                arrAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), Chat.class);
                intent.putExtra("room_name", ((TextView) view).getText().toString());
                intent.putExtra("user_name", name);
                startActivity(intent);
            }
        });

        addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(rName.getText().toString(), "");
                root.updateChildren(map);
            }
        });

    }

    private void req_user(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter name: ");

        final EditText input_textfield = new EditText(this);

        builder.setView(input_textfield);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                name = input_textfield.getText().toString();
            }
        });
        //USER MUST REGISTER MAKES USER GO BACK TO REGISTER SCREEN
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                req_user();
            }
        });

        builder.show();
    }
}

