package com.asuscomm.yangyinetwork.stylizedcamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jaeyoung on 13/10/2017.
 */

public class ViewImageActivity extends AppCompatActivity {

    public static final String TAG = ViewImageActivity.class.getSimpleName();

    @BindView(R.id.imageView)
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Uri imageUri = intent.getData();
        Picasso.with(this).load(imageUri).into(mImageView);

        saveOnFbStorage(imageUri);
    }

    private void saveOnFbStorage(Uri imageUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://styletransfer-ba06f.appspot.com");
        StorageReference storageRef = storage.getReference();
        String filename = "a.jpg";
        StorageReference fileRef = storageRef.child("upload_images/" + filename);
        UploadTask uploadTask = fileRef.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.d(TAG, "onSuccess() called with: downloadUrl = [" + downloadUrl.toString() + "]");

                saveOnFbDatabase(downloadUrl);
            }
        });
    }

    private void saveOnFbDatabase(Uri downloadUrl) {
        Log.d(TAG, "saveOnFbDatabase: ");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("uploaded_tasks");

        Task task = new Task(downloadUrl.toString());
        myRef.push().setValue(task);
    }
}
