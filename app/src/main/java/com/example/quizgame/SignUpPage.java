package com.example.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpPage extends AppCompatActivity {

    EditText mail, password;
    Button signUp;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        mail = findViewById(R.id.editTextSingupEmail);
        password = findViewById(R.id.editTextSingupPassword);
        signUp = findViewById(R.id.buttonSignup);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signUp.setClickable(false);

                String userMail = mail.getText().toString();
                String userPassword = mail.getText().toString();

                signUpFirebase(userMail,userPassword);

            }
        });

    }
    public void signUpFirebase(String userMail, String userPassword){

        auth.createUserWithEmailAndPassword(userMail,userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    finish();
                }else {
                    Toast.makeText(SignUpPage.this, "error something went wrong", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

}