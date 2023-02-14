package com.example.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Quiz_Page extends AppCompatActivity {

    TextView questionText, awnser1, awnser2, awnser3, awnser4, time, correct, wrong;
    Button finish, next;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference().child("Questions");

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    DatabaseReference databaseReference2 = database.getReference();

    String question, a,b,c,d,answer, userAnswer;
    int questionCount, number = 1, userCorrect = 0, userWrong =0;
    Boolean timerContinue;

    CountDownTimer countDownTimer;
    private static final long TOTAL_TIME = 25000;
    long leftTime = TOTAL_TIME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);

        questionText = findViewById(R.id.textViewQuestion);
         awnser1 = findViewById(R.id.textViewAwnser1);
         awnser2 = findViewById(R.id.textViewAwnser2);
         awnser3 = findViewById(R.id.textViewAwnser3);
         awnser4 = findViewById(R.id.textViewAwnser4);
         time = findViewById(R.id.textViewTime);
         correct = findViewById(R.id.textViewCorrect);
         wrong = findViewById(R.id.textViewWrong);
         finish = findViewById(R.id.buttonFinish);
         next = findViewById(R.id.buttonNextQuestion);

         game();

         next.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 resetTimer();
                 game();
             }
         });

         finish.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 sendScore();
                 Intent i = new Intent(Quiz_Page.this, Score_Page.class);
                 startActivity(i);
                 finish();

             }
         });

         awnser1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 pauseTimer();

                 userAnswer ="a";

                 if (userAnswer.equals(answer)){
                     userCorrect++;
                     correct.setText(""+ userCorrect);
                 }else {

                     userWrong++;
                     wrong.setText(""+userWrong);

                 }

             }
         });
        awnser2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTimer();

                userAnswer ="b";
                if (userAnswer.equals(answer)){
                    userCorrect++;
                    correct.setText(""+userCorrect);
                }else {

                    userWrong++;
                    wrong.setText(""+userWrong);

                }
            }
        });
        awnser3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTimer();

                userAnswer ="c";
                if (userAnswer.equals(answer)){
                    userCorrect++;
                    correct.setText(""+userCorrect);
                }else {

                    userWrong++;
                    wrong.setText(""+userWrong);

                }
            }
        });
        awnser4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTimer();

                userAnswer ="d";
                if (userAnswer.equals(answer)){
                    userCorrect++;
                    correct.setText(""+userCorrect);
                }else {

                    userWrong++;
                    wrong.setText(""+userWrong);

                }
            }
        });


    }

    public void game(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                startTimer();

                questionCount = (int) dataSnapshot.getChildrenCount();

                question = dataSnapshot.child(String.valueOf(number)).child("q").getValue().toString();
                a = dataSnapshot.child(String.valueOf(number)).child("a").getValue().toString();
                b = dataSnapshot.child(String.valueOf(number)).child("b").getValue().toString();
                c = dataSnapshot.child(String.valueOf(number)).child("c").getValue().toString();
                d = dataSnapshot.child(String.valueOf(number)).child("d").getValue().toString();
                answer = dataSnapshot.child(String.valueOf(number)).child("answer").getValue().toString();
                questionText.setText(question);
                awnser1.setText(a);
                awnser2.setText(b);
                awnser3.setText(c);
                awnser4.setText(d);

                if (number < questionCount){
                    number++;
                }
                else {
                    Toast.makeText(Quiz_Page.this, "thats all the questions", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(DatabaseError error) {

                Toast.makeText(Quiz_Page.this, "error occured", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Quiz_Page.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });
    }
    public void startTimer(){
         countDownTimer = new CountDownTimer(leftTime, 1000) {
             @Override
             public void onTick(long l) {
                 leftTime = l;
                 updateCountDownText();
             }

             @Override
             public void onFinish() {

                 timerContinue = false;
                 pauseTimer();
                 questionText.setText("times up");

             }
         }.start();
         timerContinue = true;
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        timerContinue = false;

    }

    private void updateCountDownText() {
        int second = (int) ((leftTime /1000) %60);
        time.setText(""+ second);
    }

    public void resetTimer(){
        leftTime = TOTAL_TIME;
        updateCountDownText();
    }

    public void sendScore(){
        String uid = user.getUid();
        databaseReference2.child("scores").child(uid).child("correct").setValue(userCorrect).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Quiz_Page.this, "score submitted", Toast.LENGTH_SHORT).show();
            }
        });
        databaseReference2.child("scores").child(uid).child("wrong").setValue(userWrong);
    }
}