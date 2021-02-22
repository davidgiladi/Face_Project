package com.example.face;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    FirebaseAuth fAut;
    EditText ed_email, ed_password;
    ProgressBar progressBar;
    Button bt_login;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAut = FirebaseAuth.getInstance();
        if(fAut.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }


        db = FirebaseFirestore.getInstance();
        ed_email = findViewById(R.id.user_email);
        ed_password = findViewById(R.id.user_password);
        progressBar = findViewById(R.id.progressBar);
        bt_login = findViewById(R.id.bt_login);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ed_email.getText().toString().trim();
                String password =  ed_password.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    ed_email.setError(" Please enter your email");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    ed_password.setError("Please enter password");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                fAut.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            DocumentReference docRef = db.collection("manager").document(fAut.getCurrentUser().getUid());
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            Toast.makeText(Login.this,"Logged In Successfully",Toast.LENGTH_LONG).show();

                                        } else {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            startActivity(new Intent(getApplicationContext(), Pupil.class));
                                            Toast.makeText(Login.this,"Logged In Successfully",Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                    }
                                }
                            });

                        }else{
                            ed_password.setError("Error please enter the correct password");
                            progressBar.setVisibility(View.INVISIBLE);

                        }

                    }
                });

            }
        });

    }
}
