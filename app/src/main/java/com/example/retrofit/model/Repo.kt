package com.example.retrofit.model

import com.google.gson.annotations.SerializedName

data class Repo(
    val id: Int, val name: String, val owner: User,
    @SerializedName("stargazers_count")//SerializedName是Gson註解，用來對應變數命名
    val starCount: Int//給予變數新的命名
)

data class User(
    val id: Int,
    @SerializedName("login")
    val name: String
)
