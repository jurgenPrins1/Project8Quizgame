package com.example.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Score_Page extends AppCompatActivity {

    TextView correct,wrong;
    Button again,exit;
    String userCorrect, userWrong;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference().child("scores");

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_page);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uid = user.getUid();
                userCorrect = snapshot.child(uid).child("correct").getValue().toString();
                userWrong = snapshot.child(uid).child("Wrong").getValue().toString();

                correct.setText(""+ userCorrect);
                wrong.setText(""+ userWrong);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        correct = findViewById(R.id.textViewScoreCorrect);
        wrong = findViewById(R.id.textViewScoreWrong);
        again = findViewById(R.id.buttonPlayAgain);
        exit = findViewById(R.id.buttonExit);

        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Score_Page.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}