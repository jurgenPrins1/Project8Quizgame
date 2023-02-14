package com.example.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password extends AppCompatActivity {

    EditText mail;
    Button button;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mail = findViewById(R.id.editTextPasswordEmail);
        button = findViewById(R.id.buttonPasswordContinue);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userMail = mail.getText().toString();
                resetPassword(userMail);

            }
        });
    }

    public void resetPassword(String userMail){

        auth.sendPasswordResetEmail(userMail).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if( task.isSuccessful()){
                    Intent i = new Intent(Forgot_Password.this, Login_Page.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Toast.makeText(Forgot_Password.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}