package com.enroute.enroute;

/**
 * Created by youranzhang on 2018-03-30.
 */


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.enroute.enroute.DBHelper.FirebaseHelper;
import com.enroute.enroute.interfaces.UserCallbacks;
import com.enroute.enroute.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    private static final String TAG = "UserActivity";
    private Context mcontext=UserActivity.this;
    private static final int ACTIVITY_NUM=3;

    private FirebaseAuth firebaseAuth;
    private TextView username;
    TextView profile_name;
    TextView profile_phone;
    DatabaseReference db;
    FirebaseHelper firebasehelper;
    private int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //Initialize Firebase Database
        db= FirebaseDatabase.getInstance().getReference();

        //firebase
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user= firebaseAuth.getCurrentUser();
        firebasehelper=new FirebaseHelper(db);

        //set welcome +user name
        username=(TextView)findViewById(R.id.profile_bar_name);
        username.setText("Welcome "+ user.getEmail());
        profile_name = (TextView)findViewById(R.id.profile_name);
        profile_phone = (TextView)findViewById(R.id.profole_phone);
        //set ui
        setupBottomNavigationView();
        setupToolBar();

        //if not login,jup to login activity
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, UserLoginActivity.class));
        }

        Log.d(TAG, "onCreate: started");

        firebasehelper.retrieveUser(new UserCallbacks() {
            @Override
            public void onUserCallback(ArrayList<User> users) {
                profile_name.setText("");
                if (count >= users.size()) {
                    profile_name.setText("nop");
                } else {
                    profile_name.setText(users.get(0).getUname());

                }
            }
        });

    }


    private void setupToolBar(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.profile_Toolbar);
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Log.d(TAG, "onMenuItemClick: clicked menu item"+item);

                switch (item.getItemId()){
                    case R.id.profile_edit:
                        Log.d(TAG, "onMenuItemClick: edit the profile");
                        Intent editintent=new Intent(getApplicationContext(),UserEditActivity.class);
                        startActivity(editintent);
                        break;
                    case R.id.profile_signout:
                        Log.d(TAG, "onMenuItemClick: signout");
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(getApplicationContext(),UserLoginActivity.class));
                        break;
                }

                return false;
            }
        });
    }

    private void setupBottomNavigationView(){

        Log.d(TAG, "BottomNavigationView: setup BottomNavigationView");

        BottomNavigationViewEx bottomNavigationViewEx=(BottomNavigationViewEx) findViewById(R.id.buttomNavViewbar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mcontext, bottomNavigationViewEx);

        Menu menu=bottomNavigationViewEx.getMenu();
        MenuItem menuItem=menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }
}
