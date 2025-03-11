package com.example.videoplayer.model
import androidx.annotation.RawRes

data class VideoResource(
    val title: String,
    val description: String,
    @RawRes val videoResId: Int
)