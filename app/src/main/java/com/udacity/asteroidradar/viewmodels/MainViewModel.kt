package com.udacity.asteroidradar.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.utils.Constants.API_KEY
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.api.ApiServices.Companion.AsteroidApi
import com.udacity.asteroidradar.database.AsteroidDataBase
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.utils.ConnectionLiveData
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AsteroidDataBase.getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    private val _imageOfDay = MutableLiveData<PictureOfDay>()
    private val _navigateToDetailScreen = MutableLiveData<Asteroid?>()
    val connectivityLiveData = ConnectionLiveData(application)


    val imageOfDay: LiveData<PictureOfDay>
        get() = _imageOfDay

    val navigateToDetailScreen: LiveData<Asteroid?>
        get() = _navigateToDetailScreen


    val asteroids = asteroidRepository.asteroids
    val todayAsteroids = asteroidRepository.todayAsteroids
    val weekAsteroids = asteroidRepository.weekAsteroids


    fun refreshPicOfDayAndAsteroids() {
        viewModelScope.launch {
            getPictureOfDay()
            asteroidRepository.refreshAsteroids()
        }
    }

    private suspend fun getPictureOfDay() {
        _imageOfDay.value = AsteroidApi.getImageOfDay(API_KEY)
    }

    fun onAsteroidClick(asteroid: Asteroid) {
        _navigateToDetailScreen.value = asteroid
    }

    fun onDetailScreenNavigated() {
        _navigateToDetailScreen.value = null
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}


