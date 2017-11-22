package com.example.amk.cybrchat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
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

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
    String key = "e8ffc7e56311679f12b6fc91aa77a5eb"; //key length need to be 32 long
    byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
    byte[] keyBytes = key.getBytes(Charset.forName("UTF-8"));
    byte[] cipherData = "CannotEncryptMsg".getBytes(Charset.forName("UTF-8"));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //Setting room ID and User ID
        user = getIntent().getExtras().get("user_name").toString();
        room = getIntent().getExtras().get("room_name").toString();
        setTitle(room);
        //Buttons
        send = findViewById(R.id.send);
        msg = findViewById(R.id.msg);
        convo = findViewById(R.id.convo);

        //Looking inside the first level of chat rooms which are messages
        root = FirebaseDatabase.getInstance().getReference().child(room);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Map<String, Object> map = new HashMap<String, Object>();
                temp = root.push().getKey();
                root.updateChildren(map);

                //Encrypt message with AES 256
                String plainText = msg.getText().toString();
                Log.d("message", plainText);
                String base64Text;
                try {
                    cipherData = AES256Cipher.encrypt(ivBytes, keyBytes, plainText.getBytes(Charset.forName("UTF-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                }
                base64Text = Base64.encodeToString(cipherData, Base64.DEFAULT);
                Log.d("encrypt", base64Text);

                DatabaseReference root_msg = root.child(temp);
                Map<String,Object> namemsgmap = new HashMap<String, Object>();
                namemsgmap.put("na,e",user);
                namemsgmap.put("msg",base64Text);
                //namemsgmap.put("msg",msg.getText().toString());
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

            /*Decrypt message with AES 256*/
            String encrypted = chat_msg.toString();
            String plainText;
            try {
                cipherData = AES256Cipher.decrypt(ivBytes, keyBytes, Base64.decode(encrypted.getBytes(Charset.forName("UTF-8")), Base64.DEFAULT));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
            plainText = new String(cipherData, Charset.forName("UTF-8"));
            Log.d("dcrypt", plainText);

            convo.append(chat_user +" : " + plainText + " \n");

        }
    }
}
