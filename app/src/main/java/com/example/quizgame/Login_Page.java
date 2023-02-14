package com.example.quizgame;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login_Page extends AppCompatActivity {
    EditText mail, password;
    Button signIn, signUp, forgotPassword;

    GoogleSignInClient signInClient;

    SignInButton signInGoogle;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        registerActivityForGoogleSignin();

        mail = findViewById(R.id.editTextLoginEmail);
        password = findViewById(R.id.editTextLoginPassword);
        signIn = findViewById(R.id.buttonSignin);
        signInGoogle = findViewById(R.id.buttonLoginGoogleSignin);
        signUp = findViewById(R.id.LoginSignup);
        forgotPassword = findViewById(R.id.LoginForgotPassword);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userMail = mail.getText().toString();
                String userPassword = password.getText().toString();

                signInWithFirebase(userMail, userPassword);


            }
        });
        signInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSignInGoogle();

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Login_Page.this, SignUpPage.class);
                startActivity(i);

            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Login_Page.this, Forgot_Password.class);
                startActivity(i);

            }
        });

    }

    public void signInWithFirebase(String userMail, String userPassword) {


        auth.signInWithEmailAndPassword(userMail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Intent i = new Intent(Login_Page.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(Login_Page.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            Intent i = new Intent(Login_Page.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void setSignInGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("883500894740-c5d315si112ic9hg6hfbo3e6v4gvt8a0.apps.googleusercontent.com").requestEmail().build();

        signInClient = GoogleSignIn.getClient(this, gso);
        signIn();
    }

    public void registerActivityForGoogleSignin() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                int resultCode = result.getResultCode();
                Intent data = result.getData();

                if (resultCode == RESULT_OK && data != null) {

                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    firebaseSignInWithGoogle(task);

                }
            }
        });
    }

    private void firebaseSignInWithGoogle(Task<GoogleSignInAccount> task) {

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(this, "signed in", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Login_Page.this, MainActivity.class);
            startActivity(i);
            finish();
            firebaseGoogleAccount(account);
        } catch (ApiException e) {
            Toast.makeText(this, "went wrong", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);

        }

    }

    private void firebaseGoogleAccount(GoogleSignInAccount account) {

        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(authCredential)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                        } else {

                        }
                    }
                });


    }

    public void signIn() {
        Intent i = signInClient.getSignInIntent();
        activityResultLauncher.launch(i);
    }
}