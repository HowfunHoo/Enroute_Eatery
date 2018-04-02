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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class RestActivity extends AppCompatActivity {

    private static final String TAG = "RestActivity";
    private Context mcontext=RestActivity.this;
    private static final int ACTIVITY_NUM=3;

    private FirebaseAuth firebaseAuth;
    private TextView username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        //set ui
        setupBottomNavigationView();
        setupToolBar();

        username=(TextView)findViewById(R.id.profile_bar_name);

        //todo:set welcome +user name
//        username.setText("Welcome"+ user.getname());
        firebaseAuth=FirebaseAuth.getInstance();

        //if not login,jup to login activity
        if(firebaseAuth.getCurrentUser() == null){
            Toast.makeText(RestActivity.this,"Please sign in",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(this, RestLoginActivity.class));
        }

        Log.d(TAG, "onCreate: started");
        FirebaseUser user= firebaseAuth.getCurrentUser();



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
                        Intent editintent=new Intent(getApplicationContext(),RestEditActivity.class);
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
