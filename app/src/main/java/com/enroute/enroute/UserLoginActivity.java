package com.enroute.enroute;

/**
 * Created by youranzhang on 2018-03-30.
 */


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class UserLoginActivity extends AppCompatActivity {


    //set navigation parameter
    private static final String TAG = "UserLoginActivity";
    private Context mcontext=UserLoginActivity.this;
    private static final int ACTIVITY_NUM=3;

    //declare ui component
    private Button btn_Login;
    private Button btn_rest_link;
    private Button btn_signup_link;
    private EditText et_LoginEmail;
    private  EditText et_LoginPassword;

    private ProgressDialog progressDialog;

    //firebase
    private FirebaseAuth mAuth;

    //firebase helper
//    FirebaseHelper firebasehelper;

    DatabaseReference db;

    //check input state
    private boolean empty;

    //variable used for check input
    private String email;
    private String password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        Log.d(TAG, "onCreate: started");
        setupBottomNavigationView();

        //find ui component
        btn_Login=(Button)findViewById(R.id.btn_Login);
        btn_rest_link=(Button)findViewById(R.id.btn_rest_link);
        btn_signup_link=(Button)findViewById(R.id.btn_signup_link);

        et_LoginEmail=(EditText)findViewById(R.id.LoginEmail);
        et_LoginPassword=(EditText)findViewById(R.id.LoginPassword);

        progressDialog=new ProgressDialog(this);


        //firebase authentication
        mAuth = FirebaseAuth.getInstance();


        //if already login
        //jump to activity page
//        if(mAuth.getCurrentUser() != null){
//            finish();
//            Intent userlogin= new Intent(getApplicationContext(),UserActivity.class);
//            startActivity(userlogin);
//        }

        //login button,to user profile activity
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkpasswordempty()){
                    Toast.makeText(UserLoginActivity.this,
                            "please enter password",
                            Toast.LENGTH_SHORT).show();
                }
                if(checkemailempty()){
                    Toast.makeText(UserLoginActivity.this,
                            "please enter email",
                            Toast.LENGTH_SHORT).show();
                }

                else{
                    progressDialog.setMessage("Log...in...");
                    progressDialog.show();

                    (mAuth.signInWithEmailAndPassword(et_LoginEmail.getText().toString(),
                            et_LoginPassword.getText().toString()))
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(UserLoginActivity.this,
                                                "login successful",
                                                Toast.LENGTH_SHORT).show();
                                        finish();
                                        Intent userlogin= new Intent(getApplicationContext(),UserActivity.class);
                                        startActivity(userlogin);
                                    }
                                    else{Toast.makeText(
                                            UserLoginActivity.this,
                                            "login failed",
                                            Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }

            }
        });


        //link button for sign up activity
        btn_signup_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                Intent signupintent= new Intent(getApplicationContext(),UserSignUpActivity.class);
                startActivity(signupintent);
            }
        });


        //restaurant login button
        btn_rest_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                Intent restloginintent= new Intent(getApplicationContext(),RestLoginActivity.class);
                startActivity(restloginintent);

            }
        });

    }
    //return empty or not state
    private boolean checkemailempty(){

        email=et_LoginEmail.getText().toString();

        if(TextUtils.isEmpty(email)){
            empty=true;
        }

        else empty=false;

        return empty;
    }
    private boolean checkpasswordempty(){

        password=et_LoginPassword.getText().toString();

        if(TextUtils.isEmpty(password)){
            empty=true;
        }

        else empty=false;

        return empty;
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
}
