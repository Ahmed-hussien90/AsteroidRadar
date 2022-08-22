package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.domain.PictureOfDay
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidService {


    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("api_key") apiKey: String
    ): String


    @GET("planetary/apod")
    suspend fun getImageOfDay(@Query("api_key") apiKey: String): PictureOfDay
}