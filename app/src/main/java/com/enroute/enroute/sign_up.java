package com.enroute.enroute;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class sign_up extends AppCompatActivity {

    // Creating EditText .
    EditText email, password;

    // Creating button.
    Button SignUp ;

    // Creating string to hold email and password .
    String EmailHolder, PasswordHolder ;

    // Creating Progress dialog.
    ProgressDialog progressDialog;

    // Creating FirebaseAuth object.
    FirebaseAuth firebaseAuth ;

    // Creating Boolean variable that holds EditText is empty or not status.
    Boolean EditTextStatus ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // Assigning layout email ID and Password ID.
        email = findViewById(R.id.signUpEmail);
        password = findViewById(R.id.signUpPassword);

        // Assign button layout ID.
        SignUp = findViewById(R.id.signUpBtn);

        // Creating object instance.
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(sign_up.this);

        // Adding click listener to Sign Up Button.
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling method to check EditText is empty or no status.
                CheckEditTextIsEmptyOrNot();

                // If EditText is true then this block with execute.
                if(EditTextStatus){

                    if(password.getText().toString().length() >= 6) {
                        // If EditText is not empty than UserRegistrationFunction method will call.
                        UserRegistrationFunction();
                    }
                    else {
                        Toast.makeText(sign_up.this,
                                "Password should have atleast 6 characters", Toast.LENGTH_LONG).show();
                    }


                }
                // If EditText is false then this block will execute.
                else {
                    Toast.makeText(sign_up.this,
                            "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * user registration function, will take user's input
     * and register them on firebase
     */

    public void UserRegistrationFunction(){

        // Showing progress dialog at user registration time.
        progressDialog.setMessage("Please Wait, We are Registering Your Data on Server");
        progressDialog.show();

        // Creating createUserWithEmailAndPassword method and pass email and password inside it.
        firebaseAuth.createUserWithEmailAndPassword(EmailHolder, PasswordHolder).
                addOnCompleteListener(sign_up.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                // Checking if user is registered successfully.
                                if(task.isSuccessful()){

                                    // If user registered successfully then show this toast message.
                                    Toast.makeText(sign_up.this,
                                            "User Registration Successful",Toast.LENGTH_LONG).show();
                                    // And bring user to next activity screen
                                    startActivity(new Intent(sign_up.this,
                                            MapsActivity.class));

                                }else{

                                    // If something goes wrong.
                                    Toast.makeText(sign_up.this,
                                            "Something Went Wrong.",Toast.LENGTH_LONG).show();
                                }

                                // Hiding the progress dialog after all task complete.
                                progressDialog.dismiss();

                            }
                        });
    }


    /**
     * check user input whether they are empty or not
     * if empty it wont save the information
     */

    public void CheckEditTextIsEmptyOrNot(){

        // Getting name and email from EditText and save into string variables.
        EmailHolder = email.getText().toString().trim();
        PasswordHolder = password.getText().toString().trim();

        if(TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder))
        {

            EditTextStatus = false;
        }
        else {

            EditTextStatus = true ;
        }

    }
}
