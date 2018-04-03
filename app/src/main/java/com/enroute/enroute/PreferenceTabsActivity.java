package com.enroute.enroute;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enroute.enroute.DBHelper.FirebaseHelper;
import com.enroute.enroute.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

public class PreferenceTabsActivity extends AppCompatActivity {

    //preference lib
    private String[] mVals = new String[]
            {"Vegetarian", "BBQ", "Bakery", "Beverages",
                    "Burger", "Cafe","Canadian","Chinese","Desserts",
                    "Fast Food","French", "Ice Cream","Japanese","Thai",
                    "Korean","Mexican","Pizza","Sandwich"};
    public String Preference;

    //ui component
    private TagFlowLayout mFlowLayout;
    Button btn_submit1;
    private TextView username;

    //firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_tabs);

        //get info from activity
        Intent intent = this.getIntent();
        final String Uname = intent.getStringExtra("Uname");
        final String Uphone = intent.getStringExtra("Uphone");
        Log.d("Uname1",Uname);

        Log.d("Uphone1",Uphone);

        //firebase
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user= firebaseAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        //link ui
        mFlowLayout = (TagFlowLayout)findViewById(R.id.id_flowlayout);
        btn_submit1 =(Button)findViewById(R.id.btn_submit1);

        username=(TextView)findViewById(R.id.profile_bar_name);
        username.setText("Welcome "+ user.getEmail());
        final LayoutInflater mInflater = LayoutInflater.from(PreferenceTabsActivity.this);
        final String Uemail=user.getEmail();

        //mFlowLayout.setMaxSelectCount(3);
        mFlowLayout.setAdapter(new TagAdapter<String>(mVals)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                        mFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });

        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {

//                if(position!=positionbuf){
                if(Preference==null){
                    Preference=mVals[position];
                }
                else{
                    Preference=Preference+","+mVals[position];

                }
                view.setVisibility(View.GONE);
                Toast.makeText(PreferenceTabsActivity.this,Preference, Toast.LENGTH_LONG).show();
                return true;
            }
        });

        btn_submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser userInstance= firebaseAuth.getCurrentUser();

                User user=new User(Uemail,Uname,Uphone,Preference);

                databaseReference.child("User").push().setValue(user);

                startActivity(new Intent(getApplicationContext(),UserActivity.class));


                Toast.makeText(PreferenceTabsActivity.this,"Infomation saved",Toast.LENGTH_SHORT).show();
            }
        });

        
    }
}
