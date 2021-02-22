package com.example.face;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class demo extends AppCompatActivity {
    EditText user_name;
    EditText user_email;
    EditText user_password;
    FirebaseAuth fAut;
    FirebaseFirestore db;
    String name, email, password, manger_id, email_manger,password_manger;
    ImageView image_view_1, image_view_2, image_view_3;
    int id_image ;
    ArrayList <ImageView> list_image_of_user  = null;
    public static final int GALLERY_REQUEST_CODE = 105;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

         user_name = findViewById(R.id.username);
          user_email = findViewById(R.id.user_email);
         user_password = findViewById(R.id.user_password);
        Button bt_save = findViewById(R.id.bt_save);
         fAut = FirebaseAuth.getInstance();
         db = FirebaseFirestore.getInstance();



        FirebaseUser user = fAut.getCurrentUser();
         manger_id = user.getUid();




        image_view_1 = findViewById(R.id.image_view_1);
        image_view_2 = findViewById(R.id.image_view_2);
        image_view_3 = findViewById(R.id.image_view_3);

        image_view_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent In_gl = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(In_gl, GALLERY_REQUEST_CODE);
            }
        });


        /// get the details of  the manger from firestore for sign
        DocumentReference docRef = db.collection("manager").document(manger_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "it is working " );
                          password_manger =document.getData().get("password").toString();
                          email_manger = document.getData().get("email").toString();


                    } else {
                        Log.d("tag", "No such document");
                        startActivity(new Intent(getApplicationContext(), Login.class));

                    }
                } else {
                    Log.d("tag", "get failed with ", task.getException());
                }
            }
        });

        /// finish to get the details of  the manger from firestore

        /// when you click save
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               name = user_name.getText().toString().trim();
                 email = user_email.getText().toString().trim();
                 password = user_password.getText().toString().trim();

// create user and put the details in firestore
                fAut.signOut();
                fAut.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Map<String, Object> data = new HashMap<>();
                            data.put("name", name );
                            data.put("email", email);
                            data.put("password", password);
                            FirebaseUser new_user = fAut.getCurrentUser();
                            String user_id = new_user.getUid();

                            db.collection("manger_id").document(manger_id)
                                    .collection("pupil").document(user_id)
                                    .set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    fAut.signOut();
                                    while(fAut.getCurrentUser() != null){

                                    }
                                    fAut.signInWithEmailAndPassword(email_manger,password_manger);
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                                }
                            });



                        }



            }
        });
            }
        });




    }
}


