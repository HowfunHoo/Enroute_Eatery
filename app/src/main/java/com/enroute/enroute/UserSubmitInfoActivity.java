package com.enroute.enroute;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.enroute.enroute.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserSubmitInfoActivity extends AppCompatActivity {

    //firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    //ui
    private EditText et_info_name;
    private EditText et_info_phone;
    private Button btn_submit;
    private TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_submit_info);

        //firebase
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser userInstance= firebaseAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference();

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





        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveinfo();
            }
        });

    }

    private final void saveinfo() {
        FirebaseUser userInstance= firebaseAuth.getCurrentUser();

//        save user infomation
        String name=et_info_name.getText().toString().trim();
        String phone=et_info_phone.getText().toString().trim();

        User user=new User(name,phone);

        databaseReference.child(userInstance.getUid()).setValue(user);

        Toast.makeText(UserSubmitInfoActivity.this,"Infomation saved",Toast.LENGTH_SHORT).show();
    }
}
