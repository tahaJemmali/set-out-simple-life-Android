package tn.esprit.setoutlife.Activities.Forum;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

import tn.esprit.setoutlife.Activities.HomeActivity;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Repository.IRepository;
import tn.esprit.setoutlife.Repository.PostRepository;
import tn.esprit.setoutlife.Repository.UserRepository;
import tn.esprit.setoutlife.entities.Post;

public class AddPostActivity extends AppCompatActivity implements IRepository {

    EditText postTitle, postDescription;
    ImageView postImage;
    Button uploadBtn;

    Toolbar toolbar;

    //permissions constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    //image pick constants
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;

    //permissions array
    String[] cameraPermissions;
    String[] storagePermissions;

    Uri image_uri = null;
    String image = "noImage";
    ProgressDialog pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        PostRepository.getInstance().setiRepository(this);

        postTitle = findViewById(R.id.postTitle);
        postDescription = findViewById(R.id.postDescription);
        postImage = findViewById(R.id.postImage);
        uploadBtn = findViewById(R.id.uploadBtn);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            //w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            w.setStatusBarColor(Color.BLACK);
        }

        pb = new ProgressDialog(this);

        //init permissions arrays
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show image pick
                showImagePickDialog();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = postTitle.getText().toString().trim();
                String description = postDescription.getText().toString().trim();

                if (TextUtils.isEmpty(title)){
                    Toast.makeText(AddPostActivity.this,"Enter title",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(description)){
                    Toast.makeText(AddPostActivity.this,"Enter description",Toast.LENGTH_SHORT).show();
                    return;
                }



                if (image.equals("noImage")){
                    uploadData(title,description,image);
                    System.out.println("image_uri==null11111111111111111");

                }else{
                    uploadData(title,description,image);
                    System.out.println("image_uri==null22222222222222222222");
                }
            }
        });

    }


    private void uploadData(String title, String description, String uri) {
    pb.setMessage("Publishing Post");
    pb.show();

    String time_stamp = String.valueOf(System.currentTimeMillis());
        Post p = new Post();
        p.setUser(HomeActivity.getCurrentLoggedInUser());
        p.setTitle(title);
        p.setDescription(description);
        p.setImage(uri);

        PostRepository.getInstance().addPost(p,this);

/*
        if(!uri.equals("noImage")){
            // post without image
        }else{
            //post with image
        }

        //pb.dismiss();
*/

    }

    void showImagePickDialog() {
        String[] options = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chose Image from");
        //set options to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                if (which == 0) {
                    //camera clicked
                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    }
                 else {
                    pickFromCamera();
                        }
                }
                if (which == 1) {
                    //gallery clicked
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });

        builder.create().show();
    }
    //pickFromGallery
    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);

    }


    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp Pick");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Descr");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    //handle permission results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    System.out.println("cameraAccepted :"+cameraAccepted+"storageAccepted :"+storageAccepted);
                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Camera & Storage both permissions are neccessary", Toast.LENGTH_LONG).show();
                    }

                } else {

                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    System.out.println("storageAccepted :"+storageAccepted);
                    if (storageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Storage permission is necessary", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){

            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                image_uri = data.getData();
                postImage.setImageURI(image_uri);
            }else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                postImage.setImageURI((image_uri));
            }

            try {
                InputStream is = getContentResolver().openInputStream(image_uri);
                Bitmap image = BitmapFactory.decodeStream(is);
                sendImage(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendImage(Bitmap image) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

        this.image = Base64.encodeToString(outputStream.toByteArray(),Base64.DEFAULT);
        //this.image =
        //return base64String;
        /*
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("image", base64String);
            //webSocket.send(jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void showLoadingButton() {

    }

    @Override
    public void doAction() {
        pb.dismiss();
        finish();
    }
}
