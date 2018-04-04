package com.enroute.enroute;

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
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Activity for users to sign up
 * @author:YouranZhang
 */
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

    private ProgressDialog progressDialog;

    //firebase
    FirebaseAuth firebaseAuth ;

    //check input state
    private boolean empty;
    private Boolean legal;


    //variable used for check input
    private String email;
    private String password;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        //ui component
        btn_sign_up=(Button)findViewById(R.id.btn_sign_up);
        btn_login_link=(Button)findViewById(R.id.btn_login_link);

        et_SignUp_Email=(EditText)findViewById(R.id.signUpEmail);
        et_SignUp_Password=(EditText)findViewById(R.id.signUpPassword);


        progressDialog=new ProgressDialog(this);


        Log.d(TAG, "onCreate: started");
        setupBottomNavigationView();
        firebaseAuth = FirebaseAuth.getInstance();

//        if already login
//        jump to activity page
        if(firebaseAuth.getCurrentUser() != null){
            finish();
            Intent userlogin= new Intent(getApplicationContext(),UserActivity.class);
            startActivity(userlogin);
        }


        btn_login_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent loginintent= new Intent(getApplicationContext(),UserLoginActivity.class);
                startActivity(loginintent);
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkemailempty()){
                    Toast.makeText(UserSignUpActivity.this,
                            "please enter email",
                            Toast.LENGTH_SHORT).show();
                }
                else if (checkemaillegal()){
                    Toast.makeText(UserSignUpActivity.this,
                            "Your email is illegal",
                            Toast.LENGTH_SHORT).show();
                }
                else if(et_SignUp_Password.length()<6){
                    Toast.makeText(UserSignUpActivity.this,
                            "Your password will need at least 6 number",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.setMessage("sign...up...");
                    progressDialog.show();

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
                                                      Intent loginintent= new Intent(getApplicationContext(),UserSubmitInfoActivity.class);
                                                      finish();
                                                      startActivity(loginintent);
                                                                                           }
                                            else{
                                                Toast.makeText(UserSignUpActivity.this,
                                                        "sign up failed",
                                                        Toast.LENGTH_SHORT).show();
                                                progressDialog.cancel();
                                            }
                                        }
                                    });

                }

            }
        });


    }

    /**
     * A method to check if the input is empty
     * @return boolean value
     */
    private boolean checkemailempty(){

        email=et_SignUp_Email.getText().toString();

        if(TextUtils.isEmpty(email)){
            empty=true;
        }

        else empty=false;

        return empty;
    }
    /**
     * A method to check if the email is legal
     * @return: boolean value
     */
    private boolean checkemaillegal(){
        email=et_SignUp_Email.getText().toString();
        if(email.contains("@")){
            legal=false;
        }
        else legal=true;
        return legal;
    }


    /**
     * A method to display the bottom navigation bar
     */
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

