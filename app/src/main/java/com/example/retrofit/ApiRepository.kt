package com.example.retrofit

import com.example.retrofit.model.AlbumsData
import com.example.retrofit.model.Repo
import com.example.retrofit.service.JsonApi
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call

class ApiRepository {
    val jsonApi = RetrofitManager().getJsonRetrofit().create(JsonApi::class.java)
    val gitHubApi = RetrofitManager().getGithubRetrofit().create(JsonApi::class.java)
    val gitHubCallAdapterApi = RetrofitManager().getGithubByCallAdapter().create(JsonApi::class.java)
    val gitHubLogApi = RetrofitManager().getGitHubByLog().create(JsonApi::class.java)

    fun getAlbumById(id: Int): Call<AlbumsData> {
        return jsonApi.getAlbumsById(id)
    }

    fun getGitHub(name: String): Call<List<Repo>> {
        return gitHubApi.getRepos(name)
    }

    fun getGitHubByCallAdapter(name: String): Observable<List<Repo>> {
        return gitHubCallAdapterApi.getReposByCallAdapter(name)
    }

    fun getGitHubByLog(name: String): Call<List<Repo>> {
        return gitHubLogApi.getRepos(name)
    }
}