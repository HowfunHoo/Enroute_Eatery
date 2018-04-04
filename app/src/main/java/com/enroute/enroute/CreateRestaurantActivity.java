package com.enroute.enroute;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.enroute.enroute.DBHelper.FirebaseHelper;
import com.enroute.enroute.model.Restaurant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class CreateRestaurantActivity extends AppCompatActivity {

    DatabaseReference db;
    FirebaseHelper firebasehelper;
    EditText et_rname, et_rpicurl, et_raddress, et_rcity, et_raveragecost, et_rcuisines, et_rphone;
    Button btn_submit;
    CheckBox cb_rdalcard;
    TextView tv_tip, tv_title1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);

        //INITIALIZE FIREBASE DB
        db= FirebaseDatabase.getInstance().getReference();
        firebasehelper = new FirebaseHelper(db);

        et_rname = (EditText) findViewById(R.id.et_rname);
        et_rpicurl = (EditText) findViewById(R.id.et_rpicurl);
        et_raddress = (EditText) findViewById(R.id.et_raddress);
        et_rcity = (EditText) findViewById(R.id.et_rcity);
        et_raveragecost = (EditText) findViewById(R.id.et_raveragecost);
        et_rcuisines = (EditText) findViewById(R.id.et_rcuisines);
        et_rphone = (EditText) findViewById(R.id.et_rphone);

        cb_rdalcard = (CheckBox) findViewById(R.id.cb_rdalcard);

        btn_submit = (Button) findViewById(R.id.btn_submit);

        tv_tip = (TextView) findViewById(R.id.tv_tip);
        tv_title1 = (TextView) findViewById(R.id.tv_title1);

        //Click event: Submit the newly created restaurant
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Set ID
                String rid = Long.toString(generateID(9));

                //Get Data
                String rname=et_rname.getText().toString();
                String rpicurl=et_rpicurl.getText().toString();
                String raddress=et_raddress.getText().toString();
                String rcity=et_rcity.getText().toString();
                String raveragecost=et_raveragecost.getText().toString();
                String rcuisines=et_rcuisines.getText().toString();
                String rphone=et_rphone.getText().toString();
                boolean rdalcard;
                if (cb_rdalcard.isChecked()){
                    rdalcard = true;
                }
                else {
                    rdalcard = false;
                }


                //Validation
                if(rid != null && rname.length()>0 && raddress.length()>0 && rcity.length()>0
                        && raveragecost.length()>0 && rcuisines.length()>0 && rphone.length()>0) {

                    //Set Data
                    Restaurant restaurant=new Restaurant(rid, rname, raddress, rcity, raveragecost,
                            rdalcard, rcuisines, rphone);

                    //THEN SAVE
                    if(firebasehelper.saveRestaurant(restaurant))
                    {
                        //Clear the page
                        et_rname.setVisibility(View.GONE);
                        et_rpicurl.setVisibility(View.GONE);
                        et_raddress.setVisibility(View.GONE);
                        et_rcity.setVisibility(View.GONE);
                        et_raveragecost.setVisibility(View.GONE);
                        et_rcuisines.setVisibility(View.GONE);
                        et_rphone.setVisibility(View.GONE);
                        cb_rdalcard.setVisibility(View.GONE);
                        btn_submit.setVisibility(View.GONE);
                        tv_title1.setVisibility(View.GONE);

                        tv_tip.setVisibility(View.VISIBLE);
                        finish();
                        startActivity(new Intent(getApplicationContext(),RestActivity.class));
                    }
                }else
                {
                    Toast.makeText(CreateRestaurantActivity.this,
                            "All blanks muct be filled", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    /**
     * This method returns a random number depends on the length.
     * This method is used to generate the ID for the new restaurant.
     * @param length the length of the required random number
     * @return the ID for the new restaurant
     */
    public static long generateID(int length) {
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }
}
