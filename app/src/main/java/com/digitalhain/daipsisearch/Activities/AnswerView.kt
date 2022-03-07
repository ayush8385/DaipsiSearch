package com.digitalhain.daipsisearch.Activities

import android.app.NotificationManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.digitalhain.daipsisearch.Activities.Room.QuestionViewModel
import com.digitalhain.daipsisearch.Activities.utils.Preferences
import com.digitalhain.daipsisearch.R

class AnswerView : AppCompatActivity() {
    lateinit var question:TextView
    lateinit var answer:TextView
    lateinit var sharedPreferences:Preferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer_view)

        question=findViewById(R.id.questionn)
        answer = findViewById(R.id.answerr)

        sharedPreferences= Preferences.getInstance(applicationContext)!!

        supportActionBar!!.title=intent.getStringExtra("course")
        question.text = intent.getStringExtra("Ques")
        answer.text = intent.getStringExtra("Ans")

        val notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()

        //sharedPreferences.removeQues(applicationContext,Subject(supportActionBar!!.title.toString(), question.text.toString()))

        QuestionViewModel(application).deleteQuestion(question.text.toString())

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}