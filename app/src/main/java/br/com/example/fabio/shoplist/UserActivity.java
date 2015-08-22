package br.com.example.fabio.shoplist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.instinctcoder.sqlitedb.User;
import com.instinctcoder.sqlitedb.UserRepo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class UserActivity extends ActionBarActivity {

    static final int REQUEST_IMAGE_CAPTURE = 100;
    static final int REQUEST_MAP = 110;
    private static final long DOUBLE_PRESS_INTERVAL = 250;
    private ImageView imgUser;
    private int _User_Id;
    private UserRepo repo;
    private User user;
    private Bitmap imageBitmap;
    private long lastPressTime;

    private TextView edtusername;
    private TextView edtuseremail;
    private TextView edtuserlongitude;
    private TextView edtuserlatitude;
    private String userphotopath;
    Boolean saveimage;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStorageDirectory();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }
    private void LoadUserIfExists() throws IOException {
        user = new User();
        repo = new UserRepo(this);
        user = repo.getUserById(0);
        if (user.user_ID != 0){
            edtusername.setText(user.name);
            edtuseremail.setText(user.email);
            edtuserlatitude.setText(String.valueOf(user.latitude));
            edtuserlongitude.setText(String.valueOf(user.longitude));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            imageBitmap = BitmapFactory.decodeFile(user.photo, options);
            imgUser.setImageBitmap(imageBitmap);
            userphotopath = user.photo;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        saveimage = false;
        lastPressTime = 0;

        imgUser = (ImageView)findViewById(R.id.img_user);
        edtuseremail = (TextView)findViewById(R.id.edtUserEmai);
        edtusername = (TextView)findViewById(R.id.edtUserName);
        edtuserlatitude = (TextView)findViewById(R.id.edtUserLatitude);
        edtuserlongitude = (TextView)findViewById(R.id.edtUserLongitude);

        imgUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                long pressTime = System.currentTimeMillis();

                if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
                lastPressTime = pressTime;
            }
        } );


        SetButtonsClick();
        try {
            LoadUserIfExists();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void SimpleMessage(String AText){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(UserActivity.this);
        dlgAlert.setMessage(AText);
        dlgAlert.setTitle(getResources().getString(R.string.msg_title));
        dlgAlert.setPositiveButton(getResources().getString(R.string.btn_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    private void SetButtonsClick() {

        final Button buttonMap = (Button) findViewById(R.id.btn_UserMap);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(UserActivity.this, MapsActivity.class);
                startActivityForResult(i, REQUEST_MAP);
            }
        });
        final Button buttonSave = (Button) findViewById(R.id.btn_UserSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (saveimage) {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    File photofile;
                    try {
                        photofile = createImageFile();
                        userphotopath = photofile.getAbsolutePath();
                        FileOutputStream fo = new FileOutputStream(userphotopath);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                repo = new UserRepo(UserActivity.this);
                //repo.deleteall();
                //_User_Id = 0;
                user = new User();
                user.name= edtusername.getText().toString();
                user.email=edtuseremail.getText().toString();
                user.longitude=Double.parseDouble(edtuserlongitude.getText().toString());
                user.latitude=Double.parseDouble(edtuserlatitude.getText().toString());
                user.photo = userphotopath;
                user.user_ID=_User_Id;
                if (_User_Id==0){
                    try {
                        _User_Id = repo.insert(user);
                    }catch (Exception Ex){
                        SimpleMessage(getResources().getString(R.string.msg_insert_error) + Ex.getMessage());
                    }

                    Toast.makeText(UserActivity.this, getResources().getString(R.string.user_insert), Toast.LENGTH_SHORT).show();
                }else{
                    repo.update(user);
                    Toast.makeText(UserActivity.this, getResources().getString(R.string.user_update), Toast.LENGTH_SHORT).show();
                }
            }
        });
        final Button buttonPhoto = (Button) findViewById(R.id.btn_UserPhoto);
        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_back) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            saveimage = true;
            imageBitmap = (Bitmap) data.getExtras().get("data");
            imgUser.setImageBitmap(imageBitmap);


        }
        if (requestCode == REQUEST_MAP && resultCode == RESULT_OK) {
            double latitude =data.getDoubleExtra("latitude",0);
            double longitude =data.getDoubleExtra("longitude",0);
            edtuserlatitude.setText(String.valueOf(latitude));
            edtuserlongitude.setText(String.valueOf(longitude));
        }
    }
}
