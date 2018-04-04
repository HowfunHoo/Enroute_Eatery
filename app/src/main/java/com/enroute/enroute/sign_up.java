package com.enroute.enroute;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

/**
 * Programmer: Nirav Jadeja
 *
 * this activity is for regisration of the user in the firebase
 * it takes their name, email, password and restaurant type
 *
 * References:
 * 1. https://androidjson.com/add-firebase-google-login-integration/
 * 2. https://firebase.google.com/docs/android/setup
 * 3. https://firebase.google.com/docs/auth/android/custom-auth
 */

public class sign_up extends AppCompatActivity {

    EditText email, password, userName; //editext for username, email and password

    Button SignUp ; // sign up button

    FirebaseDatabase database;
    DatabaseReference emailRef;

    String EmailHolder, PasswordHolder ; // for email and password

    ProgressDialog progressDialog; // process dialog box

    FirebaseAuth firebaseAuth ; // firebase object

    // Creating Boolean variable that holds EditText is empty or not status.
    Boolean EditTextStatus ;

    private Spinner dropdown;
    Contact receivedPersonInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // Assigning layout email ID and Password ID.
        email = findViewById(R.id.signUpEmail);
        password = findViewById(R.id.signUpPassword);
        userName = findViewById(R.id.signUpName);


        database = FirebaseDatabase.getInstance();
        emailRef = database.getReference("Email");

        // Assign button layout ID.
        SignUp = findViewById(R.id.signUpBtn);

        dropdown = (Spinner) findViewById(R.id.spinner1);

        // Spinner for restaurant from the menu
        String[] items = new String[]{"Indian", "Italian", "Mexican", "Thai", "Chinese" };

        final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
                R.layout.support_simple_spinner_dropdown_item, items) {};

        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        dropdown.setSelection(0); //setting default time 0

        // Creating object instance.
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(sign_up.this);

        // Adding click listener to Sign Up Button.
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling method to check EditText is empty or no status.
                CheckEditTextIsEmptyOrNot();

                // checking condition for user registration
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
        ;
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

                                    dataStore();
                                    // And bring user to next activity screen which is main Map activity
                                    startActivity(new Intent(sign_up.this,
                                            MapsActivity.class));

                                }else{
                                    // If something goes wrong, toast error message
                                    Toast.makeText(sign_up.this,
                                            "Something Went Wrong.",Toast.LENGTH_LONG).show();
                                }
                                // closing dialog box once task completes
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

    // this function stores the user name, email id, and restaurant type to the firebase database
    public void dataStore(){
        int hash = Objects.hash(email);
        String name = userName.getText().toString();
        String emailID = email.getText().toString();
        String rType = dropdown.getSelectedItem().toString();

        Contact person = new Contact(name, emailID, rType);
        emailRef.child(Integer.toString(hash)).setValue(person); // setting the value in the firebase
    }
}
