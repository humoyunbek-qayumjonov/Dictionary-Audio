package com.humoyunbek.dictionary.RoomUtils

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WordDb::class], version = 1)
abstract class AppDatabase:RoomDatabase() {
    abstract fun wordDao():MyWordDao

    companion object{
        private var instance:AppDatabase? = null

        @Synchronized
        fun getInstance(context:Context?):AppDatabase{
            if (instance == null){
                instance = Room.databaseBuilder(context!!,AppDatabase::class.java,"words_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}