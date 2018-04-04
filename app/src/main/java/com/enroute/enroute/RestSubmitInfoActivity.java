package com.enroute.enroute;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.enroute.enroute.DBHelper.FirebaseHelper;
import com.enroute.enroute.model.Business;
import com.enroute.enroute.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class RestSubmitInfoActivity extends AppCompatActivity {

    //navigation helper
    private static final String TAG = "UserSubmitInfoActivity";
    private Context mcontext=RestSubmitInfoActivity.this;
    private static final int ACTIVITY_NUM=3;

    //firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    FirebaseHelper firebasehelper;

    //ui
    private EditText et_Rinfo_name;
    private EditText et_Rinfo_phone;
    private Button btn_Rsubmit;
    private TextView username;

    private String Bname;
    private String Bphone;
    private String Bemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_submit_info);
        setupBottomNavigationView();

        //find ui
        et_Rinfo_name=(EditText)findViewById(R.id.RinfoSubmit_Name);
        et_Rinfo_phone=(EditText)findViewById(R.id.RinfoSubmit_Phone);
        btn_Rsubmit=(Button)findViewById(R.id.btn_Rsubmit);

        //firebase
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser userInstance= firebaseAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebasehelper = new FirebaseHelper(databaseReference);
        final String Bemail=userInstance.getEmail();
        final String Uid = userInstance.getUid();

        Bname=et_Rinfo_name.getText().toString();
        Bphone=et_Rinfo_phone.getText().toString();
        //if not login
        //jump to login
        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this,RestLoginActivity.class));
        }

        //set welcome message
        username=(TextView)findViewById(R.id.profile_bar_name);
        username.setText("Welcome "+ userInstance.getEmail());

        btn_Rsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_Rinfo_name.getText().length()>0 && et_Rinfo_phone.getText().length()>0){
//
                    Business business=new Business(Bemail,Bname,Bphone);
                    if(firebasehelper.saveBusiness(business));
//                    databaseReference.child("Business").push().setValue(business);
//                    databaseReference.child("Business").child(Uid).setValue(business);
                    { finish();
                    startActivity(new Intent(new Intent(getApplicationContext(),RestActivity.class)));}
                }
                else {
                    Toast.makeText(RestSubmitInfoActivity.this, "All blanks should be filled!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //setup button navigation
    private void setupBottomNavigationView(){

        Log.d(TAG, "BottomNavigationView: setup BottomNavigationView");

        BottomNavigationViewEx bottomNavigationViewEx=(BottomNavigationViewEx) findViewById(R.id.buttomNavViewbar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mcontext, bottomNavigationViewEx);

        Menu menu=bottomNavigationViewEx.getMenu();
        MenuItem menuItem=menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }
}
