package com.dma.quiz_ph;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;

public class QuizActivity extends AppCompatActivity {

    AppCompatTextView txtQstnNo, txtScore, txtQstnPoint, txtQstn;
    AppCompatButton btnOptionOne, btnOptionTwo, btnOptionThree, btnOptionFour;
    AppCompatImageView imgQstn;

    private long totalQuestionToAnswer = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initViews();
    }

    private void initViews() {
        txtQstnNo = findViewById(R.id.txtQstnNo);
        txtScore = findViewById(R.id.txtScore);
        txtQstnNo = findViewById(R.id.txtQstnNo);
        txtQstnNo = findViewById(R.id.txtQstnNo);

        btnOptionOne = findViewById(R.id.btnOptionOne);
        btnOptionTwo = findViewById(R.id.btnOptionTwo);
        btnOptionThree = findViewById(R.id.btnOptionThree);
        btnOptionFour = findViewById(R.id.btnOptionFour);

        imgQstn = findViewById(R.id.imgQstn);

    }
}