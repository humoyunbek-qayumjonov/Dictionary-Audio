package com.humoyunbek.dictionary.RoomUtils

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MyWordDao {
    @Insert
    fun addWord(wordDb: WordDb)

    @Query("select*from worddb")
    fun getAllMyWord():List<WordDb>

    @Update
    fun updateWord(wordDb: WordDb)
}