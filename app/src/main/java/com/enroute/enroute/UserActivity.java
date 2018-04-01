package com.enroute.enroute;

/**
 * Created by youranzhang on 2018-03-30.
 */


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class UserActivity extends AppCompatActivity {
    private static final String TAG = "UserActivity";
    private Context mcontext=UserActivity.this;
    private static final int ACTIVITY_NUM=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Log.d(TAG, "onCreate: started");
        setupBottomNavigationView();
        setupToolBar();


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
                        Intent signoutintent=new Intent(getApplicationContext(),UserLoginActivity.class);
                        startActivity(signoutintent);
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
