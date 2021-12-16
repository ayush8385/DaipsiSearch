package com.digitalhain.daipsisearch.Activities.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [QuestionEntity::class], version=1,exportSchema = false)
abstract class QuestionDatabase: RoomDatabase() {
    abstract fun questionDao(): QuestionDao

    companion object{
        @Volatile
        private var INSTANCE: QuestionDatabase?=null

        fun getDatabase(context: Context): QuestionDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this){
                val instance=
                    Room.databaseBuilder(context.applicationContext, QuestionDatabase::class.java,"asked-db").allowMainThreadQueries().build()
                INSTANCE =instance
                instance
            }
        }
    }
}