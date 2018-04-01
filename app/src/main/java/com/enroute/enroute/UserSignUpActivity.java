package com.enroute.enroute;

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
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class UserSignUpActivity extends AppCompatActivity {

    //navigation helper
    private static final String TAG = "UserLoginActivity";
    private Context mcontext=UserSignUpActivity.this;
    private static final int ACTIVITY_NUM=3;


    //declare ui component
    private Button btn_sign_up;
    private Button btn_login_link;
    private EditText et_SignUp_Email;
    private EditText et_SignUp_Password;


    //firebase
    FirebaseAuth firebaseAuth ;

    //check input state
    private boolean empty;


    //variable used for check input
    private String email;
    private String password;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        //ui component
        btn_sign_up=(Button)findViewById(R.id.btn_sign_up);
        btn_login_link=(Button)findViewById(R.id.btn_signup_link);
        et_SignUp_Email=(EditText)findViewById(R.id.signUpEmail) ;
        et_SignUp_Password=(EditText)findViewById(R.id.signUpPassword) ;

        Log.d(TAG, "onCreate: started");
        setupBottomNavigationView();
        firebaseAuth = FirebaseAuth.getInstance();

//        btn_sign_up.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent loginintent= new Intent(getApplicationContext(),UserActivity.class);
//                startActivity(loginintent);
//            }
//        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkinputempty()){
                    Toast.makeText(UserSignUpActivity.this,
                            "please vertify your input"
                            ,Toast.LENGTH_SHORT).show();
                }
                else{
                    firebaseAuth.createUserWithEmailAndPassword(et_SignUp_Email.getText().toString()
                            ,et_SignUp_Password.getText().toString())
                            .addOnCompleteListener(UserSignUpActivity.this
                                    , new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(UserSignUpActivity.this,
                                                        "Sign Up successful",
                                                        Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(),UserActivity.class));
                                            }
                                            else{
                                                Toast.makeText(UserSignUpActivity.this,
                                                        "sign up failed",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                    Intent loginintent= new Intent(getApplicationContext(),UserActivity.class);
                    startActivity(loginintent);
                }

            }
        });


//        btn_login_link.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view2) {
//                Intent tologinintent= new Intent(getApplicationContext(),UserActivity.class);
//                startActivity(tologinintent);
//            }
//        });

    }

    private boolean checkinputempty(){

        email=et_SignUp_Email.getText().toString();
        password=et_SignUp_Password.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            empty=true;
        }

        else empty=false;

        return empty;
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

