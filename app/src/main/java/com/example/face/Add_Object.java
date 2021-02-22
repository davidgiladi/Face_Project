package com.example.face;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.HashMap;
import java.util.Map;

public class Add_Object extends AppCompatActivity {

    int count_im = 1;
    //Uri [] arr_imageUri = new Uri[3];
    FirebaseAuth fAut =FirebaseAuth.getInstance();
    String userID ;
    FirebaseFirestore db;
    Button bt_save;
    ImageButton bt_camere ;
    ImageView image_view_1, image_view_2, image_view_3;

    EditText ed_Fname , ed_email, ed_password;
    ProgressBar progressBar;
    int number_of_error ;
    String Fname, email, password;
    StorageReference mStorageRef;

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__object);



        mStorageRef = FirebaseStorage.getInstance().getReference();
        ed_Fname = findViewById(R.id.username);
        ed_email = findViewById(R.id.user_email);
        ed_password = findViewById(R.id.user_password);
        progressBar = findViewById(R.id.progressBar);
        bt_save = findViewById(R.id.bt_save);
        bt_camere = findViewById(R.id.bt_camera);
        image_view_1 = findViewById(R.id.image_view_1);
        image_view_2 = findViewById(R.id.image_view_2);
        image_view_3 = findViewById(R.id.image_view_3);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String user_ID = user.getUid();


        db = FirebaseFirestore.getInstance();
        image_view_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count_im = 1;



                Intent In_gl = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(In_gl, GALLERY_REQUEST_CODE);
            }
        });
        image_view_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count_im = 2;

                Intent In_gl = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(In_gl, GALLERY_REQUEST_CODE);
            }
        });
        image_view_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count_im = 3;

                Intent In_gl = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(In_gl, GALLERY_REQUEST_CODE);
            }
        });

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number_of_error = 0;
                Fname = ed_Fname.getText().toString().trim();
                email = ed_email.getText().toString().trim();
                password = ed_password.getText().toString().trim();

                if (TextUtils.isEmpty(Fname)) {
                    ed_Fname.setError(" Please enter your name");
                    number_of_error++;
                }
                if (TextUtils.isEmpty(email)) {
                    ed_email.setError(" Please enter your email");
                    number_of_error++;

                }
                if (TextUtils.isEmpty(password)) {
                    ed_password.setError("Please enter password");
                    number_of_error++;
                } else {
                    if (password.length() < 6) {
                        ed_password.setError("The password must contain at least 6 character\"");
                        number_of_error++;
                    }
                }

                if (number_of_error > 0) {
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                fAut.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
///                            UpLoadImageFirebase(arr_imageUri,fAut.getCurrentUser().getUid(),userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fname",Fname);
                            user.put("email",email);
                            user.put("password",password);


                            db.collection("manager_id").document(user_ID)
                                    .collection("pupil").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Add_Object.this, email + password , Toast.LENGTH_LONG).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Add_Object.this, "erorre" , Toast.LENGTH_LONG).show();

                                        }
                                    });



                            progressBar.setVisibility(View.INVISIBLE);
                            fAut.signOut();

                            DocumentReference docRef = db.collection("manager").document(userID);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Map<String, Object> m_user = document.getData();
                                            password = (m_user.get("password").toString());
                                            email = (m_user.get("email").toString());
                                            fAut.signInWithEmailAndPassword(email, password);

                                        } else {
                                        }
                                    } else {
                                    }
                                }
                            });


                            progressBar.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(Add_Object.this, "Error " + task.getException(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);


                        }
                    }
                });

            }

        });


    }
   /* protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:

                break;
            case GALLERY_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {

                    Uri imageUri = data.getData();
                    switch (count_im) {
                        case 1:
                            image_view_1.setImageURI(imageUri);
                            arr_imageUri[0] = imageUri;
                            break;
                        case 2:
                            image_view_2.setImageURI(imageUri);
                            arr_imageUri[1] = imageUri;

                            break;
                        case 3:
                            image_view_3.setImageURI(imageUri);
                            arr_imageUri[2] = imageUri;

                            break;
                    }

                }
                break;

        }


    }*/

   /* private void UpLoadImageFirebase(Uri[] arr_imageUri ,String uid ,String mid) {

        for (int i = 0; i < 3; i++) {
            final StorageReference[] ref = {mStorageRef.child("manger/" + mid + "/" + "user/" + uid + "/" + (i + 1) + ".jpg")};
            ref[0].putFile(arr_imageUri[i]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                }
            });
        }*/

    }












