package com.example.findmytutor.dataClasses.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.findmytutor.dataClasses.DoubtInfo
import com.example.findmytutor.dataClasses.dao.DoubtDao

@Database(entities = [DoubtInfo::class], version = 1, exportSchema = false)
abstract class FindMyTutorDb:RoomDatabase() {


        abstract val doubtDao:DoubtDao


        companion object {

            @Volatile
            private var INSTANCE: FindMyTutorDb? = null

            private fun getInstance(context: Context): FindMyTutorDb {
                synchronized(this) {
                    var instance = INSTANCE

                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            FindMyTutorDb::class.java,
                            "database"
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                        INSTANCE = instance
                    }

                    return instance
                }
            }

            fun initDatabase(context: Context): FindMyTutorDb {
                return getInstance(context)
            }

    }
}