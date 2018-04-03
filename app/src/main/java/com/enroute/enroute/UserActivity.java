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

    protected static final int choosephone = 0;
    protected static final int takephone = 1;
    private static final int editpic = 2;
    protected static Uri uriuri;
    private ImageView personalpic;
    private PicPopup menuWindow;

    private static final String TAG = "UserActivity";
    private Context mcontext=UserActivity.this;
    private static final int ACTIVITY_NUM=3;

    private FirebaseAuth firebaseAuth;
    private TextView username;
    TextView profile_name;
    TextView profile_phone;
    DatabaseReference db;
    FirebaseHelper firebasehelper;
    private int count = 0;
    private String Uemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //Initialize Firebase Database
        db= FirebaseDatabase.getInstance().getReference();

        //firebase
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser= firebaseAuth.getCurrentUser();

        Uemail = currentUser.getEmail();

        //initialize firebasehelper
        firebasehelper=new FirebaseHelper(db);

        //set welcome +user name
        username=(TextView)findViewById(R.id.profile_bar_name);
        username.setText("Welcome "+ currentUser.getEmail());
        profile_name = (TextView)findViewById(R.id.profile_name);
        profile_phone = (TextView)findViewById(R.id.profole_phone);
        //set ui
        setupBottomNavigationView();
        setupToolBar();

        //if not login,jup to login activity
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, UserLoginActivity.class));
        }

        Log.d(TAG, "onCreate: started");

//        firebasehelper.retrieveUser(Uemail, new UserCallbacks() {
//            @Override
//            public void onUserCallback(User user) {
//                profile_name.setText(user.getUname());
//                //TODO
//            }
//        });

//        firebasehelper.retrieveUser(Uemail, new UserCallbacks() {
//            @Override
//            public void onUserCallback(ArrayList<User> users) {
//                profile_name.setText("");
//                if (count >= users.size()) {
//                    profile_name.setText("nop");
//                } else {
//                    profile_name.setText(users.get(0).getUname());
//
//                }
//            }
//        });
        File tempdoc = Environment.getExternalStorageDirectory();
        File direpic = new File(tempdoc, "picture");
        if (!direpic.exists()) {
            direpic.mkdirs();
        }

        personalpic = (ImageView) findViewById(R.id.iv_personal_icon);
        personalpic.setOnClickListener(new View.OnClickListener() {
            //use popup window
            @Override
            public void onClick(View v) {

                menuWindow = new PicPopup(UserActivity.this, itemsOnClick);

                menuWindow.showAtLocation(UserActivity.this.findViewById(R.id.main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

        // load picture from last time
        if (direpic.exists() && direpic.isDirectory()) {
            if (direpic.list().length > 0) {
                /*Log.d("test", destDir.toString() + "/image_icon.png");*/
                Bitmap bitmap = BitmapFactory.decodeFile(direpic.toString() + "/image_icon.png");
                personalpic.setImageBitmap(bitmap);
            } else {
                personalpic.setBackgroundResource(R.drawable.default_personal_image);
            }
        }

    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.Layout_take_photo:
                    Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    uriuri = Uri.fromFile(
                            new File(Environment.getExternalStorageDirectory() + "picture", "image.jpg"));
                   /* Log.d("11111111", tempUri.toString());*/
                    startActivityForResult(openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriuri), takephone);
//                    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
//                    startActivityForResult(openCameraIntent, TAKE_PICTURE);
                    break;
                case R.id.Layout_pick_photo:
                    Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
                    openAlbumIntent.setType("image/*");
                    startActivityForResult(openAlbumIntent, choosephone);
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
                case takephone:
                    startPhotoZoom(uriuri);
                    break;
                case choosephone:
                    startPhotoZoom(data.getData());
                    break;
                case editpic:
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
        uriuri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        intent.putExtra("crop", "true");

        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, editpic);
    }

    //save the changed picture
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = Utils.toRoundBitmap(photo, uriuri);
            personalpic.setImageBitmap(photo);
            uploadPic(photo);
        }
    }
    //upload picture
    private void uploadPic(Bitmap bitmap) {


        String imagePath = Utils.savePhoto(bitmap,
                Environment.getExternalStorageDirectory().getAbsolutePath() + "picture", "image_icon");
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
