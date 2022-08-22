package com.udacity.asteroidradar.database;

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.domain.Asteroid

@Database(
    entities = [DatabaseAsteroid::class],
    version = 1
)
abstract class AsteroidDataBase : RoomDatabase() {

    abstract fun AsteroidDAO(): AsteroidDAO

    companion object {

        @Volatile
        private var INSTANCE: AsteroidDataBase? = null

        fun getDatabase(context: Context): AsteroidDataBase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            if (INSTANCE == null) {
                synchronized(this) {
                    // Pass the database to the INSTANCE
                    INSTANCE = buildDatabase(context)
                }
            }
            // Return database.
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): AsteroidDataBase {
            return Room.databaseBuilder(
                context.applicationContext,
                AsteroidDataBase::class.java,
                "asteroid_database"
            )
                .build()
        }
    }

}