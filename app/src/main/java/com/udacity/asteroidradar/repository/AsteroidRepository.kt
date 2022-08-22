package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.ApiServices.Companion.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDataBase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.asDatabaseModel
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.utils.Constants.API_KEY
import com.udacity.asteroidradar.utils.getDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDataBase) {

    private val startDate = getDay(0)
    private val endDate = getDay(7)


    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.AsteroidDAO().getAsteroids()) {
            it.asDomainModel()
        }

    val todayAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.AsteroidDAO().getTodayAsteroids(startDate)) {
            it.asDomainModel()
        }

    val weekAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.AsteroidDAO().getWeekAsteroids(startDate, endDate)) {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val result = AsteroidApi.getAsteroids(API_KEY)
            val asteroids = parseAsteroidsJsonResult(JSONObject(result))
            database.AsteroidDAO().insertAsteroid(asteroids.asDatabaseModel())

        }
    }

}