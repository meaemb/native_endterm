package com.example.campuscompanion.data.remote.api

import com.example.campuscompanion.data.remote.dto.TvMazeSearchDto
import com.example.campuscompanion.data.remote.dto.TvMazeShowDto
import retrofit2.http.GET
import retrofit2.http.Query

interface EventsApi {

    // FEED pagination: https://api.tvmaze.com/shows?page=0,1,2...
    @GET("shows")
    suspend fun getShows(
        @Query("page") page: Int
    ): List<TvMazeShowDto>

    // SEARCH: https://api.tvmaze.com/search/shows?q=...
    @GET("search/shows")
    suspend fun searchShows(
        @Query("q") query: String
    ): List<TvMazeSearchDto>
}
