package com.enroute.enroute;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class log_in extends AppCompatActivity {

    //private static final String TAG = "CustomAuthActivity";
    // Declare auth
    private FirebaseAuth mAuth;

    private EditText emailLogin;
    private EditText passLogin;

    private LinearLayout profileLayout;
    private ImageView imageProfilePic;
    private TextView textName, textEmail;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;

    private SignInButton googleLoginBtn;
    public ProgressDialog mProgressDialog;
    public LinearLayout ProfileLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // EditTexts
        emailLogin = findViewById(R.id.loginEmail);
        passLogin  = findViewById(R.id.loginPass);

        // Buttons
        Button loginBtn = findViewById(R.id.loginBtn);                 //Log in button
        Button signUpScreenBtn = findViewById(R.id.signUpScreenBtn);   // SignUp screen button
        googleLoginBtn = (SignInButton)findViewById(R.id.googleLoginBtn);
        ProfileLayout = (LinearLayout) findViewById(R.id.llProfile);

        textName = (TextView) findViewById(R.id.txtName); // for displaying google name of user
        textEmail = (TextView) findViewById(R.id.txtEmail); // for displaying google id

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();


        // sign-up button click listener, fire up the registration page
        signUpScreenBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                startActivity(new Intent(log_in.this,
                        sign_up.class));
            }
        });

        // login button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (emailLogin.getText().toString().isEmpty() || passLogin.getText().toString().isEmpty()) {

                    Toast.makeText(log_in.this, "Invalid Email or Password, Please try again!", Toast.LENGTH_LONG).show();
                }
                else{

                    updateLoginScreen(emailLogin.getText().toString(), passLogin.getText().toString());
                }
            }
        });

        // google sign in button
        googleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }


    /**
     * This method will Check if logged in successfully or not.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    /**
     * this is for authentication with firebase, if the task is successful
     * then it will update the UI and signed in page will be there.
     * and if something went wrong it will generate login failed message (in log)
     * and pop up the message and wont update the UI
     * @param acct
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            startActivity(new Intent(log_in.this,
                                    MapsActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    /**
     * Check if user is signed in (non-null) and update UI accordingly.
     */
    @Override
    public void onStart() {super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    /**
     * this is for to get update UI or not
     * it will check whether user is there or not and if s/he is then it will update the UI
     * in the absence, it wont change the UI
     * @param user
     */
    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            textEmail.setText(user.getEmail());
            textName.setText(user.getUid());
            findViewById(R.id.googleLoginBtn).setVisibility(View.VISIBLE);
        } else {
            textEmail.setText("Signed out");
            textName.setText(null);

            findViewById(R.id.googleLoginBtn).setVisibility(View.VISIBLE);
        }
    }

    /**
     * dialog box for displaying the process
     */
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void updateLoginScreen(String email, String password){
        (mAuth.signInWithEmailAndPassword(email, password))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(log_in.this,
                                    "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(log_in.this,
                                    MapsActivity.class));
                        } else {
                            Toast.makeText(log_in.this,
                                    "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }

}
