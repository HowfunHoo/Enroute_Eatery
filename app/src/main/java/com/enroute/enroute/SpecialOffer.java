package com.enroute.enroute;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class SpecialOffer extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialoffer);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        if (bottomNavigationView != null) {
            // Set action to perform when any menu-item is selected.
            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            // Write code to perform some actions.
                            selectFragment(item);
                            return false;
                        }
                    });
        }
    }



    /**
     * Perform action when any item is selected.
     *
     * @param item Item that is selected.
     */
    protected void selectFragment(MenuItem item) {

        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.home:
                // Action to perform when Home Menu item is selected.
                startActivity(new Intent(this, MapsActivity.class));
                break;
            case R.id.userprofile:
                // Action to perform when Bag Menu item is selected.
                startActivity(new Intent(this, log_in.class));
                break;
            case R.id.specialoffers:
                // Action to perform when Account Menu item is selected.
                startActivity(new Intent(this, SpecialOffer.class));
                break;
        }
    }
}
