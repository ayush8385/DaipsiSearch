package com.digitalhain.daipsisearch.Activities.Room

import androidx.lifecycle.LiveData

class QuestionRepository(private val questionDao: QuestionDao) {
    val allQuestions: LiveData<List<QuestionEntity>> = questionDao.getAllQuestion()

    suspend fun insert(questionEntity: QuestionEntity){
        questionDao.insertQuestion(questionEntity)
    }

    suspend fun delete(question: String){
        questionDao.deleteQuestion(question)
    }


//    suspend fun delete(noteEntity: NoteEntity){
//        noteDao.delete(noteEntity)
//    }
}