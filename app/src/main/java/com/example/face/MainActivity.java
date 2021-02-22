package com.example.face;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity {
    Button bt_logout ,bt_demo;
    FirebaseAuth fAut;
    FirebaseStorage mStorageRef;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStorageRef = FirebaseStorage.getInstance();
        textView = findViewById(R.id.textView);
        //StorageReference ref = mStorageRef.getReferenceFromUrl("gs://facezen-18934.appspot.com/").child("manger/"+fAut.getCurrentUser().getUid()+"/"+"user one.jpg");
       /* File file = null;
        try {
            file = File.createTempFile("image","jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final File finalFile = file;
        ref.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bitmap = BitmapFactory.decodeFile(finalFile.getAbsolutePath());
                image_1.setImageBitmap(bitmap);
            }
        });*/
        bt_demo = findViewById(R.id.bt_demo);
        bt_logout = findViewById(R.id.bt_logout);
        fAut = FirebaseAuth.getInstance();
        textView.setText(fAut.getCurrentUser().getUid());

        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAut.signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));

            }
        });

        bt_demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), demo.class));

            }
        });
    }
    public void add_object (View v){
        startActivity(new Intent(getApplicationContext(), Add_Object.class));

    }
}