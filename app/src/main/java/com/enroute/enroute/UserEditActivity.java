package com.enroute.enroute;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.enroute.enroute.DBHelper.FirebaseHelper;
import com.enroute.enroute.interfaces.UserCallbacks;
import com.enroute.enroute.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.enroute.enroute.model.User;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UserEditActivity extends AppCompatActivity {
    private EditText edit_Text1;
    private EditText edit_Text2;
    private Button   bt_edit;
    private Button   bt_edit2;
    DatabaseReference db;
    private FirebaseAuth firebaseAuth;
    private String Uemail;
    FirebaseHelper firebasehelper;
    private String a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        edit_Text1=(EditText)findViewById(R.id.edit_Text1);
        edit_Text2=(EditText)findViewById(R.id.edit_Text2);
        bt_edit=(Button)findViewById(R.id.bt_edit);
        bt_edit2=(Button)findViewById(R.id.bt_edit2);


        db= FirebaseDatabase.getInstance().getReference();
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser currentUser= firebaseAuth.getCurrentUser();
        Uemail = currentUser.getEmail();
        a = currentUser.getUid();
        firebasehelper=new FirebaseHelper(db);

        firebasehelper.retrieveUser(Uemail, new UserCallbacks() {
            @Override
            public void onUserCallback(User user){
                edit_Text1.setText(user.getUname());
                edit_Text2.setText(user.getUphone());
            }
        });


        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editintent = new Intent(getApplicationContext(),UserActivity.class);
                startActivity(editintent);
            }
        });

        bt_edit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    db.child("User").child(a).child("uname").setValue(edit_Text1.getText().toString());
                    db.child("User").child(a).child("uphone").setValue(edit_Text2.getText().toString());
                    Intent editintent = new Intent(getApplicationContext(),UserActivity.class);
                    startActivity(editintent);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

}

