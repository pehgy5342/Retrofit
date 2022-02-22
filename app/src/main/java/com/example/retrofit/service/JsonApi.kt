package com.example.retrofit.service

import com.example.retrofit.model.AlbumsData
import com.example.retrofit.model.Repo
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JsonApi {

    //https://jsonplaceholder.typicode.com/
    @GET("albums/{id}")
    fun getAlbumsById(@Path("id") Id: Int): Call<AlbumsData>

    //https://api.github.com/
    @GET("users/{user}/repos")
    fun getRepos(
        @Path("user") user: String,
        @Query("type") type: String? = null,
        @Query("sort") sort: String? = null
    ): Call<List<Repo>>

    //https://api.github.com/
    @GET("users/{user}/repos")
    fun getReposByCallAdapter(
        @Path("user") user: String,
        @Query("type") type: String? = null,
        @Query("sort") sort: String? = null
    ): Observable<List<Repo>>
}