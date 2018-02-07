package com.example.hp.firebaserecycle;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Firebase fb;
    Button b1,b2;
    ImageView img;
    File actualImage;
    static final int PICK_IMAGE_REQUEST = 1;
    Uri selectedImageUri;
    String url="https://recyclerexample-71934.firebaseio.com/";
    Date currentTime ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1=(Button)findViewById(R.id.button);
        b2=(Button)findViewById(R.id.button2);
        img=(ImageView) findViewById(R.id.imageView2);
        Firebase.setAndroidContext(getApplicationContext());
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTime= Calendar.getInstance().getTime();

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fb=new Firebase(url);
                StorageReference sr= FirebaseStorage.getInstance().getReference().child("RECYCLER").child(currentTime.toString());
                sr.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloaduri=taskSnapshot.getDownloadUrl();
                        System.out.println("bowbow"+taskSnapshot.getStorage());
                        // Uri ndi=taskSnapshot.getUploadSessionUri();
                        // System.out.println("BOW"+ndi.toString());
                        String DownloadUri=downloaduri.toString();
                        ImgAdapter img = new ImgAdapter();
                        img.setUrl(DownloadUri);
                        fb.child("RECYCLER").child(currentTime.toString()).setValue(img);
                    }
                });

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(i);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            try {
                actualImage = FileUtil.from(this, data.getData());
                selectedImageUri=data.getData();
                img.setImageBitmap(BitmapFactory.decodeFile(actualImage.getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
