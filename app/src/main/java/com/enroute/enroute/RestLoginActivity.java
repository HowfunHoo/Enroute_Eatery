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

public class RestLoginActivity extends AppCompatActivity {

    //set navigation parameter
    private static final String TAG = "RestLoginActivity";
    private Context mcontext=RestLoginActivity.this;
    private static final int ACTIVITY_NUM=3;

    //ui component
    private EditText et_RLogin_Email;
    private EditText et_RLogin_Password;
    private Button btn_RLogin;
    private Button btn_RSignup;

    //check input state
    private boolean empty;

    //firebase
    private FirebaseAuth mAuth;

    //variable used for check input
    private String email;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_login);

        Log.d(TAG, "onCreate: started");
        setupBottomNavigationView();


        //find ui component
        btn_RLogin=(Button)findViewById(R.id.btn_RLogin);
        btn_RSignup=(Button)findViewById(R.id.btn_Rsignup_link);

        et_RLogin_Email=(EditText)findViewById(R.id.RLoginEmail);
        et_RLogin_Password=(EditText)findViewById(R.id.RLoginPassword);

        //firebase authentication
        mAuth = FirebaseAuth.getInstance();

        btn_RLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkinputempty()){
                    Toast.makeText(RestLoginActivity.this,
                            "please vertify your imput",
                            Toast.LENGTH_SHORT).show();
                }
                else{

                    (mAuth.signInWithEmailAndPassword(et_RLogin_Email.getText().toString(),
                            et_RLogin_Password.getText().toString()))
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RestLoginActivity.this,
                                                "login successful",
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),RestActivity.class));
                                    }
                                    else{Toast.makeText(
                                            RestLoginActivity.this,
                                            "login failed",
                                            Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });

        btn_RSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent restsignupintent= new Intent(getApplicationContext(),RestSignUpActivity.class);
                startActivity(restsignupintent);
            }
        });

    }


    private boolean checkinputempty(){

        email=et_RLogin_Email.getText().toString();
        password=et_RLogin_Password.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
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
