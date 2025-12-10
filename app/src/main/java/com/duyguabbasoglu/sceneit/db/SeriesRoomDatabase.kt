package com.duyguabbasoglu.sceneit.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.duyguabbasoglu.sceneit.model.Series
import com.duyguabbasoglu.sceneit.util.Constants

@Database(entities = [Series::class], version = 2, exportSchema = false)
abstract class SeriesRoomDatabase : RoomDatabase() {
    abstract fun getDao(): SeriesDAO

    companion object {
        @Volatile
        private var INSTANCE: SeriesRoomDatabase? = null

        fun getDatabase(context: Context): SeriesRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SeriesRoomDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
