package com.enroute.enroute;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enroute.enroute.DBHelper.FirebaseHelper;
import com.enroute.enroute.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class UserSubmitInfoActivity extends AppCompatActivity {

    //navigation helper
    private static final String TAG = "UserSubmitInfoActivity";
    private Context mcontext=UserSubmitInfoActivity.this;
    private static final int ACTIVITY_NUM=3;

    //firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    FirebaseHelper firebasehelper;

    //ui
    private EditText et_info_name;
    private EditText et_info_phone;
    private Button btn_submit;
    private TextView username;
    private LinearLayout submitform;
    private TextView succ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_submit_info);
        setupBottomNavigationView();

        //firebase
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser userInstance= firebaseAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebasehelper = new FirebaseHelper(databaseReference);

        //if not login
        //jump to login
        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this,UserLoginActivity.class));
        }

        //set welcome message
        username=(TextView)findViewById(R.id.profile_bar_name);
        username.setText("Welcome "+ userInstance.getEmail());

        //find ui
        et_info_name=(EditText)findViewById(R.id.infoSubmit_Name);
        et_info_phone=(EditText)findViewById(R.id.infoSubmit_Phone);
        btn_submit=(Button)findViewById(R.id.btn_submit);
        submitform=(LinearLayout)findViewById(R.id.infoSubmitForm);
        succ=(TextView)findViewById(R.id.succ);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser userInstance= firebaseAuth.getCurrentUser();
//                saveinfo();
                String name=et_info_name.getText().toString().trim();
                String phone=et_info_phone.getText().toString().trim();

                User user=new User(name,phone);

//                if(firebasehelper.saveUser(user)){
//                    submitform.setVisibility(View.GONE);
//                    succ.setVisibility(View.VISIBLE);
//                }

                    databaseReference.child("User").child(userInstance.getUid()).setValue(user);

                Toast.makeText(UserSubmitInfoActivity.this,"Infomation saved",Toast.LENGTH_SHORT).show();
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
