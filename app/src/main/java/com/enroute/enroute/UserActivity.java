package com.enroute.enroute;

/**
 * Created by youranzhang on 2018-03-30.
 */


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.enroute.enroute.DBHelper.FirebaseHelper;
import com.enroute.enroute.interfaces.UserCallbacks;
import com.enroute.enroute.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Activity
 * @author:YouranZhang,HaoyuSun
 */
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
    FirebaseUser currentUser;

    private FirebaseAuth firebaseAuth;
    private TextView username;
    TextView tv_name, tv_email, tv_phone, tv_prefer;
    DatabaseReference db;
    FirebaseHelper firebasehelper;
    private int count = 0;
    private String Uemail;


    FirebaseStorage storage;
    StorageReference storageReference;

    //Shaerpreferences
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

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

        //get current user
         currentUser= firebaseAuth.getCurrentUser();

        sharedPreferences = getSharedPreferences("cur_user",0);

        //storage of firebase
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://enroute-eatery.appspot.com/images");


        //if not login,jup to login activity
        if(currentUser == null){
            Intent intent = new Intent(UserActivity.this, UserLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else {
            Uemail = currentUser.getEmail();
            //set welcome +user name
            username=(TextView)findViewById(R.id.profile_bar_name);
            username.setText("Welcome "+ currentUser.getEmail());
        }

        //initialize firebasehelper
        firebasehelper=new FirebaseHelper(db);


        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_email = (TextView)findViewById(R.id.tv_email);
        tv_phone = (TextView)findViewById(R.id.tv_phone);
        tv_prefer = (TextView)findViewById(R.id.tv_preference);

        //set ui
        setupBottomNavigationView();
        setupToolBar();

        firebasehelper.retrieveUser(Uemail, new UserCallbacks() {
            @Override
            public void onUserCallback(User user) {
                tv_name.setText(user.getUname());
                tv_email.setText(user.getUemail());
                tv_phone.setText(user.getUphone());
                tv_prefer.setText(user.getUpreference());

                //store current user's information in sharedPreference
                editor = sharedPreferences.edit();
                editor.putString("cur_uemail", Uemail);
                editor.putString("cur_uprefer", user.getUpreference());
                editor.apply();
            }
        });


          File   destDir = new File(Environment.getExternalStorageDirectory() + "/AndroidPersonal_icon");
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
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageReference.child(currentUser.getUid()).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    iv_personal_icon.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e ) {}

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

    /**
     * Save the changed picture
      * @param data
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = Utils.toRoundBitmap(photo, tempUri);
            iv_personal_icon.setImageBitmap(photo);
            uploadPic(photo);
            uploadImage();
        }
    }

    private void uploadPic(Bitmap bitmap) {

        String imagePath = Utils.savePhoto(bitmap,
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/AndroidPersonal_icon", "image_icon");

        Log.d("imagePath", imagePath + "");
        if (imagePath != null) {

        }
    }

    private void uploadImage( ) {

        if(tempUri != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child(currentUser.getUid().toString() );
            ref.putFile(tempUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(UserActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UserActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

//    static void deleteAllFiles(File root) {
//        File files[] = root.listFiles();
//        if (files != null)
//            for (File f : files) {
//                if (f.isDirectory()) {
//                    deleteAllFiles(f);
//                    try {
//                        f.delete();
//                    } catch (Exception e) {
//                    }
//                } else {
//                    if (f.exists()) {
//                        deleteAllFiles(f);
//                        try {
//                            f.delete();
//                        } catch (Exception e) {
//                        }
//                    }
//                }
//            }
//    }
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
//                        deleteAllFiles(new File(Environment.getExternalStorageDirectory()+"/AndroidPersonal_icon"));
                        firebaseAuth.signOut();
                        sharedPreferences.edit().clear().apply();
                        finish();
                        startActivity(new Intent(getApplicationContext(),UserLoginActivity.class));
                        break;
                }

                return false;
            }
        });
    }

    /**
     * A method to set up navigation view bar for each activity
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

    /**
     *
     * @param menu
     * @return the toolbar menu for the users
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }
}
