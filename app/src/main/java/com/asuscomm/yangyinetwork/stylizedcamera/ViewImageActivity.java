package com.asuscomm.yangyinetwork.stylizedcamera;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jaeyoung on 13/10/2017.
 */

public class ViewImageActivity extends AppCompatActivity {

    public static final String TAG = ViewImageActivity.class.getSimpleName();

    @BindView(R.id.imageView)
    ImageView mImageView;
    private String styleName;

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
        String filename = imageUri.getLastPathSegment();
        StorageReference fileRef = storageRef.child("upload_images/" + filename);

//        com.google.android.gms.tasks.Task<AuthResult> authResultTask = FirebaseAuth.getInstance().signInAnonymously();
//        authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
//                Log.d(TAG, "onComplete: ");
//            }
//        });

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
                String path = taskSnapshot.getMetadata().getPath();
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.d(TAG, "onSuccess() called with: downloadUrl = [" + downloadUrl.toString() + "]");

                saveOnFbDatabase(downloadUrl, path);
            }
        });
    }

    private void saveOnFbDatabase(final Uri downloadUrl, final String path) {
        Log.d(TAG, "saveOnFbDatabase: ");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("uploaded_tasks");

        getStyleName(new OnFinishListener() {
            @Override
            public void onFinish(String style) {
                Task task = new Task(downloadUrl.toString(), path, style);
                myRef.push().setValue(task);
            }
        });
    }

    interface OnFinishListener {
        void onFinish(String style);
    }

    public String getStyleName(final OnFinishListener finished) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("styles");
        myRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (int idx = 0; idx < dataSnapshot.getChildrenCount(); idx++) {
                            DataSnapshot item = dataSnapshot.getChildren().iterator().next();
                            if (idx == 0) {
                                item.getKey();
                            }
                        }
                        for (DataSnapshot item:
                                dataSnapshot.getChildren()) {
                            finished.onFinish(item.getKey());
                            break;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        return styleName;
    }
}
