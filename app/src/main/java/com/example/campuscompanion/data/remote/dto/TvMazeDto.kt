package com.example.campuscompanion.data.remote.dto

data class TvMazeSearchDto(
    val show: TvMazeShowDto
)

data class TvMazeShowDto(
    val id: Int,
    val name: String,
    val summary: String?,
    val image: TvMazeImageDto?
)

data class TvMazeImageDto(
    val original: String?
)
