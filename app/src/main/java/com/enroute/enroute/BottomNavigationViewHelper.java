package com.enroute.enroute;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;


/**
 * A method to shift between four bottom icon
 * used an external package BottomNavigationViewEx to canceled the shift animation
 * @author:YouranZhang
 * Reference:
 * 1.https://github.com/ittianyu/BottomNavigationViewEx.git
 */

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHel";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);

    }


    public static void enableNavigation(final Context context,BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.ic_home:
                        Intent intent2=new Intent(context, MapsActivity.class);
                        context.startActivity(intent2);
                        break;
                    case R.id.ic_suggestion:
                        Intent intent4=new Intent(context,WaitForCuisinesActivity.class);
                        context.startActivity(intent4);
                        break;
                    case R.id.ic_discount:
                        Intent intent3=new Intent(context, SpecialOffersActivity.class);
                        context.startActivity(intent3);
                        break;
                    case R.id.ic_user:
                        Intent intent1=new Intent(context, UserActivity.class);
                        context.startActivity(intent1);
                        break;

                }
                return false;
            }
        });
    }
}
