package com.digitalhain.daipsisearch.Activities.Room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuestion(questionEntity: QuestionEntity)

    @Query("DELETE FROM asked WHERE question = :ques")
    fun deleteQuestion(ques: String)

    @Query("SELECT * FROM asked")
    fun getAllQuestion(): LiveData<List<QuestionEntity>>
//
//    @Query("SELECT EXISTS(SELECT * FROM calls WHERE id = :id)")
//    fun isUserExist(id : String) : Boolean

//    @Query("SELECT * FROM contacts where contact_number=:number")
//    fun getRestaurantsbyId(number:String):ContactEntity
}