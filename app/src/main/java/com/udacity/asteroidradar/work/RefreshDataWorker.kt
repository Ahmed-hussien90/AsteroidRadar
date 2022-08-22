package com.udacity.asteroidradar.work

import android.content.Context
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDataBase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshDataWorker(context: Context, prams: WorkerParameters) :
    CoroutineWorker(context, prams) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }


    override suspend fun doWork(): Result {
        val database = AsteroidDataBase.getDatabase(applicationContext)
        val repository = AsteroidRepository(database)

        return try {
            Toast.makeText(applicationContext,"workereeeeeeeeerer",Toast.LENGTH_SHORT).show()
            repository.refreshAsteroids()
            Result.success()
        }catch (e :HttpException){
            Result.retry()
        }

    }
}