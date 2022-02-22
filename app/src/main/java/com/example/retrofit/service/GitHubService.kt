package com.example.retrofit.service

import com.example.retrofit.model.Repo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {
    @GET("user/{user}/repos")
    fun listRepos(
        @Path("user") user: String,
        @Query("type") type: String? = null,
        @Query("sort") sort: String? = null
    ): Call<List<Repo>>
}