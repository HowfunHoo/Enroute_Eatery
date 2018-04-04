package com.enroute.enroute;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.enroute.enroute.DBHelper.FirebaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
/*
 * @author Haofan Hou
 */
public class CreateOffersActivity extends AppCompatActivity {

    TextView tv_rname, tv_ifoffer, tv_tip;
    EditText et_roffer;
    CheckBox cb_ifoffer;
    Button btn_submit;

    DatabaseReference db;
    private FirebaseAuth firebaseAuth;

    FirebaseHelper firebasehelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_offers);

        tv_rname = (TextView)findViewById(R.id.tv_rname);
        tv_ifoffer = (TextView)findViewById(R.id.tv_ifoffer);
        tv_tip = (TextView)findViewById(R.id.tv_tip);
        et_roffer = (EditText) findViewById(R.id.et_roffer);
        cb_ifoffer = (CheckBox) findViewById(R.id.cb_ifoffer);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        db= FirebaseDatabase.getInstance().getReference();


    }
}
