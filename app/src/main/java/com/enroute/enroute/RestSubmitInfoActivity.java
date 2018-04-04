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
    private static final String TAG = "RestSubmitInfoActivity";
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
        FirebaseUser user= firebaseAuth.getCurrentUser();
        Log.d("CURUSR", String.valueOf(user));
        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebasehelper = new FirebaseHelper(databaseReference);

//        final String Uid = user.getUid();
        final String Bemail=user.getEmail();


        databaseReference= FirebaseDatabase.getInstance().getReference();

        //if not login
        //jump to login
        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this,RestLoginActivity.class));
        }
        //set welcome message
        username=(TextView)findViewById(R.id.profile_bar_name);
        username.setText("Welcome "+ user.getEmail());


        Intent intent = new Intent(RestSubmitInfoActivity.this, PreferenceTabsActivity.class);
        intent.putExtra("Bname", et_Rinfo_name.getText().toString());
        intent.putExtra("Bphone", et_Rinfo_phone.getText().toString());

        Bname=et_Rinfo_name.getText().toString();
        Bphone=et_Rinfo_phone.getText().toString();

//        Intent intent = this.getIntent();
        final String Bname = intent.getStringExtra("Bname");
        final String Bphone = intent.getStringExtra("Bphone");
        btn_Rsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_Rinfo_name.getText().length()>0 && et_Rinfo_phone.getText().length()>0){

                    Business business=new Business(Bemail,Bname,Bphone);
                    firebasehelper.saveBusiness(business);
//                    databaseReference.child("Business").push().setValue(business);
                    Toast.makeText(RestSubmitInfoActivity.this,"Infomation saved",Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(new Intent(getApplicationContext(),RestActivity.class)));

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
