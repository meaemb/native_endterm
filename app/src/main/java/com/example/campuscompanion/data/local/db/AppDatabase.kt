package com.example.campuscompanion.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.campuscompanion.data.local.dao.EventDao
import com.example.campuscompanion.data.local.entity.EventEntity

@Database(
    entities = [EventEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    companion object {
        fun get(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "campus_db")
                .fallbackToDestructiveMigration()
                .build()
    }
}
