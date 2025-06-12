package com.example.plantin.notes

import android.app.Application
import androidx.room.Room
import com.example.plantin.notes.db.AppDatabase

class MainApplication : Application() {

    companion object {
        lateinit var database: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "plantin-db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
