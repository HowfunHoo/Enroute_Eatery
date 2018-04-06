package com.enroute.enroute;

/**
 * This activity provide a button to apply for a resraurant for business user
 * @author:YouranZhang
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
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class RestActivity extends AppCompatActivity {
    //set navigation bar
    private static final String TAG = "RestActivity";
    private Context mcontext=RestActivity.this;
    private static final int ACTIVITY_NUM=3;

    //firebase
    private FirebaseAuth firebaseAuth;
    //ui
    private TextView username;
    private Button btn_busi_add_rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);

        //firebase
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user= firebaseAuth.getCurrentUser();

        //set welcome +user name
        username=(TextView)findViewById(R.id.profile_bar_name);
        username.setText("Welcome "+ user.getEmail());

        //set ui
        btn_busi_add_rest=(Button)findViewById(R.id.btn_busi_add_rest) ;

        setupBottomNavigationView();
        setupToolBar();

        //if not login,jup to login activity
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, RestLoginActivity.class));
        }
        btn_busi_add_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CreateRestaurantActivity.class));
            }
        });

    }

    /**
     * A method to set the tool bar
     * There is only one option for business-signout
     *
     */
    private void setupToolBar(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.profile_Toolbar);
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Log.d(TAG, "onMenuItemClick: clicked menu item"+item);

                switch (item.getItemId()){
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

    /**
     * A method to set up navigation view bar for each activity
     */
    private void setupBottomNavigationView(){

        Log.d(TAG, "BottomNavigationView: setup BottomNavigationView");

        BottomNavigationViewEx bottomNavigationViewEx=(BottomNavigationViewEx) findViewById(R.id.buttomNavViewbar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mcontext, bottomNavigationViewEx);

        Menu menu=bottomNavigationViewEx.getMenu();
        MenuItem menuItem=menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }

    /**
     * A method to set up menu for the tool bar
     * @param menu
     * @return the tool bar menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.busi_menu,menu);
        return true;
    }
}
