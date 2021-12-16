package com.digitalhain.daipsisearch.Activities.utils

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.digitalhain.daipsisearch.Activities.Subject



class Preferences(private val context: Context) {
    private val sharedPreferences: SharedPreferences

    fun addQuestion(context: Context,question:Subject){
        asked_ques.add(question)

        Toast.makeText(context, asked_ques.toString(),Toast.LENGTH_LONG).show()
    }

    fun removeQues(context: Context,question: Subject){
        for(quest in asked_ques){
            if(quest.ans!!.equals(question)){
                asked_ques.remove(quest)
                Toast.makeText(context, asked_ques.toString(),Toast.LENGTH_LONG).show()
                break
            }
        }
    }

    fun getQuestions(): ArrayList<Subject> {
        return asked_ques
    }



    companion object {

        private var sharedPrefManager: Preferences? = null
        private var asked_ques:ArrayList<Subject> = arrayListOf<Subject>()

        fun getInstance(context: Context): Preferences? {
            if (null == sharedPrefManager) {
                sharedPrefManager = Preferences(context)
            }
            return sharedPrefManager
        }
    }

    init {
        sharedPreferences = context.getSharedPreferences("sharedPreferences_data", Context.MODE_PRIVATE)
    }
}



