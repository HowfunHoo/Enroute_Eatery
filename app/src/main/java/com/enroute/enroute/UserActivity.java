package com.enroute.enroute;

/**
 * Created by youranzhang on 2018-03-30.
 */


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.enroute.enroute.DBHelper.FirebaseHelper;
import com.enroute.enroute.interfaces.UserCallbacks;
import com.enroute.enroute.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.File;
import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    private static final String TAG = "UserActivity";
    private Context mcontext=UserActivity.this;
    private static final int ACTIVITY_NUM=3;

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private ImageView iv_personal_icon;
    private PicPopup menuWindow;

    private FirebaseAuth firebaseAuth;
    private TextView username;
    TextView tv_name, tv_email, tv_phone, tv_prefer;
    DatabaseReference db;
    FirebaseHelper firebasehelper;
    private int count = 0;
    private String Uemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //Initialize Firebase Database
        db= FirebaseDatabase.getInstance().getReference();

        //firebase
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser= firebaseAuth.getCurrentUser();

        Uemail = currentUser.getEmail();

        //LOG
        Log.d("Uemail", Uemail);

        //initialize firebasehelper
        firebasehelper=new FirebaseHelper(db);

        //set welcome +user name
        username=(TextView)findViewById(R.id.profile_bar_name);
        username.setText("Welcome "+ currentUser.getEmail());
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_email = (TextView)findViewById(R.id.tv_email);
        tv_phone = (TextView)findViewById(R.id.tv_phone);
        tv_prefer = (TextView)findViewById(R.id.tv_preference);

        //set ui
        setupBottomNavigationView();
        setupToolBar();

//        if not login,jup to login activity
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, UserLoginActivity.class));
        }

        firebasehelper.retrieveUser(Uemail, new UserCallbacks() {
            @Override
            public void onUserCallback(User user) {
                tv_name.setText(user.getUname());
                tv_email.setText(user.getUemail());
                tv_phone.setText(user.getUphone());
                tv_prefer.setText(user.getUpreference());
            }
        });


        File destDir = new File(Environment.getExternalStorageDirectory() + "/AndroidPersonal_icon");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        iv_personal_icon = (ImageView) findViewById(R.id.iv_personal_icon);
        iv_personal_icon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                menuWindow = new PicPopup(UserActivity.this, itemsOnClick);

                menuWindow.showAtLocation(UserActivity.this.findViewById(R.id.main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });


        if (destDir.exists() && destDir.isDirectory()) {
            if (destDir.list().length > 0) {
                Bitmap bitmap = BitmapFactory.decodeFile(destDir.toString() + "/image_icon.png");
                iv_personal_icon.setImageBitmap(bitmap);
            } else {
                iv_personal_icon.setBackgroundResource(R.drawable.default_personal_image);
            }
        }
    }


    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.Layout_take_photo:
                    Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    tempUri = Uri.fromFile(
                            new File(Environment.getExternalStorageDirectory() + "/AndroidPersonal_icon", "image.jpg"));
                    Log.d("11111111", tempUri.toString());

                    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                    startActivityForResult(openCameraIntent, TAKE_PICTURE);
                    break;
                case R.id.Layout_pick_photo:
                    Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
                    openAlbumIntent.setType("image/*");
                    startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                    break;
                default:
                    break;
            }

        }

    };

    //determine the statemant of picture.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri);
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData());
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data);
                    }
                    break;
            }
        }
    }


    //cut put picture
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        intent.putExtra("crop", "true");

        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    //save the changed picture
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = Utils.toRoundBitmap(photo, tempUri);
            iv_personal_icon.setImageBitmap(photo);
            uploadPic(photo);
        }
    }

    private void uploadPic(Bitmap bitmap) {

        String imagePath = Utils.savePhoto(bitmap,
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/AndroidPersonal_icon", "image_icon");
        Log.d("imagePath", imagePath + "");
        if (imagePath != null) {

        }
    }



    private void setupToolBar(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.profile_Toolbar);
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Log.d(TAG, "onMenuItemClick: clicked menu item"+item);

                switch (item.getItemId()){
                    case R.id.profile_edit:
                        Log.d(TAG, "onMenuItemClick: edit the profile");
                        Intent editintent=new Intent(getApplicationContext(),UserEditActivity.class);
                        startActivity(editintent);
                        break;
                    case R.id.profile_signout:
                        Log.d(TAG, "onMenuItemClick: signout");
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(getApplicationContext(),UserLoginActivity.class));
                        break;
                }

                return false;
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }
}
