package com.example.retrofit

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitManager {
    val json = "https://jsonplaceholder.typicode.com/"
    val gitHub = "https://api.github.com"

    //Json
    fun getJsonRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(json)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkhttp())
            .build()
    }

    //Github
    fun getGithubRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(gitHub)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkhttp())
            .build()
    }

    //CallAdapter
    fun getGithubByCallAdapter(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(gitHub)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(getOkhttp())
            .build()
    }

    //Logging
    fun getGitHubByLog(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(gitHub)
            .client(getOkhttpByLog())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //建立Okhttp連線
    fun getOkhttp(): OkHttpClient {
        val token = "RestfulApiToken"
        val okHttpBuilder = OkHttpClient.Builder()
        //OkHttp攔截器
        okHttpBuilder.addInterceptor { chain ->
            val apiRequest = chain.request()
            apiRequest.newBuilder().addHeader("Authorization", "Bearer" + token)
            val apiResponse = chain.proceed(apiRequest)
            when (apiResponse.code) {
                200 -> Log.d("200", "success")
                404 -> Log.d("404", "fail")
            }
            apiResponse
        }
        return okHttpBuilder.build()
    }

    //建立Logging攔截器
    fun getOkhttpByLog(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val newRequest = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .build()

                    return chain.proceed(newRequest)
                }
            })
            .addInterceptor(logging)
            .build()

        return client
    }
}

