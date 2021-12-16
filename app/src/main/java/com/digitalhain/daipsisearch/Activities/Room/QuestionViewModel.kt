package com.digitalhain.daipsisearch.Activities.Room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuestionViewModel(application: Application): AndroidViewModel(application) {
    val allQuestions: LiveData<List<QuestionEntity>>
    val repository: QuestionRepository

    init {
        val database= QuestionDatabase.getDatabase(application).questionDao()
        repository= QuestionRepository(database)
        allQuestions=repository.allQuestions
    }


    fun inserQuestion(questionEntity: QuestionEntity) =viewModelScope.launch (Dispatchers.IO){
        repository.insert(questionEntity)
    }

    fun deleteQuestion(question: String)=viewModelScope.launch (Dispatchers.IO){
        repository.delete(question)
    }
}