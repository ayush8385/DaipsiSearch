package com.digitalhain.daipsisearch.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.digitalhain.daipsisearch.R

class AnswerView : AppCompatActivity() {
    lateinit var question:TextView
    lateinit var answer:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer_view)

        question=findViewById(R.id.questionn)
        answer = findViewById(R.id.answerr)

        question.text = intent.getStringExtra("Ques")
        answer.text = intent.getStringExtra("Ans")

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}