package com.enroute.enroute;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.enroute.enroute.DBHelper.FirebaseHelper;
import com.enroute.enroute.interfaces.UserCallbacks;
import com.enroute.enroute.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haoyu on 2018/4/2.
 */

public class UserProfile extends AppCompatActivity {

    private DatabaseReference db;
    private FirebaseHelper firebasehelper;
    TextView profile_name;
    TextView profile_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        profile_name = (TextView)findViewById(R.id.profile_name);
        profile_phone = (TextView)findViewById(R.id.profole_phone);

        db= FirebaseDatabase.getInstance().getReference();

        firebasehelper=new FirebaseHelper(db);

        firebasehelper.retrieveUser(new UserCallbacks() {
            @Override
            public void onUserCallback(ArrayList<User> users) {
                try {
                    //String a = users.get(0);
                   // profile_name.setText((users)list.get[0][0] );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}