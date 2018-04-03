package com.enroute.enroute;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserEditActivity extends AppCompatActivity {
    private EditText edit_Text1;
    private EditText edit_Text2;
    private Button   bt_edit;
    private Button   bt_edit2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        edit_Text1=(EditText)findViewById(R.id.edit_Text1);
        edit_Text2=(EditText)findViewById(R.id.edit_Text2);
        bt_edit=(Button)findViewById(R.id.bt_edit);
        bt_edit2=(Button)findViewById(R.id.bt_edit2);

        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editintent = new Intent(getApplicationContext(),UserActivity.class);
                startActivity(editintent);
            }
        });

        bt_edit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

    }
}
