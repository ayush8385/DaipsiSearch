package com.digitalhain.daipsisearch.Activities.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asked")
data class QuestionEntity(
    @ColumnInfo(name = "course") val course : String,
    @ColumnInfo(name = "question") val question : String,
    @PrimaryKey(autoGenerate = true) var id:Int=0
)