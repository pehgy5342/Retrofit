package com.example.retrofit.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.retrofit.ApiRepository
import com.example.retrofit.R
import com.example.retrofit.model.AlbumsData
import com.example.retrofit.model.Repo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var apiRepository: ApiRepository
    lateinit var user: EditText
    lateinit var search: Button
    lateinit var githubShow: TextView
    lateinit var idShow: TextView
    lateinit var noContent: TextView
    lateinit var id: EditText
    lateinit var go: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        apiRepository = ApiRepository()
        user = findViewById(R.id.et_user)
        search = findViewById(R.id.btn_search)
        githubShow = findViewById(R.id.tv_githubShow)
        idShow = findViewById(R.id.tv_idShow)
        noContent = findViewById(R.id.tv_noContent)
        id = findViewById(R.id.et_id)
        go = findViewById(R.id.btn_go)

        //github
        search.setOnClickListener {
            val name = user.text.toString()
            getGithubAsync(name)

        }

        //jsonId
        go.setOnClickListener {
            val number = id.text.toString()
            if (number.equals("") || number == null) {
                Toast.makeText(this, "請輸入數字", Toast.LENGTH_SHORT).show()
            } else if (number.toInt() <= 0 || number.toInt() >= 101) {
                idShow.visibility = View.GONE
                Toast.makeText(this, "輸入數字必須介於1~100之間", Toast.LENGTH_SHORT).show()
            } else {
                val m = Integer.parseInt(number)
                getAlbumAsync(m)
                getAlbumSync(m)
            }
        }
    }

    //非同步執行
    private fun getAlbumAsync(id: Int) {
        //getJsonId
        apiRepository.getAlbumById(id).enqueue(object : Callback<AlbumsData> {
            override fun onResponse(call: Call<AlbumsData>, response: Response<AlbumsData>) {
                Log.d("AsyncSuccess", response.body().toString())

                noContent.visibility = View.GONE
                githubShow.visibility = View.GONE
                idShow.visibility = View.VISIBLE
                idShow.text = response.body().toString()
            }

            override fun onFailure(call: Call<AlbumsData>, t: Throwable) {
                Log.d("Fail", "is Failed")
            }
        })
    }


    //非同步執行
    private fun getGithubAsync(name: String) {
        //getGithubRepo
        apiRepository.getGitHub(name).enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                Log.d("github", response.body().toString())

                if (response.body().isNullOrEmpty()) {
                    githubShow.visibility = View.GONE
                    idShow.visibility = View.GONE
                    noContent.visibility = View.VISIBLE

                } else {
                    noContent.visibility = View.GONE
                    idShow.visibility = View.GONE
                    githubShow.visibility = View.VISIBLE
                    githubShow.text = response.body().toString()
                }
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                Log.d("Fail", "is Failed")
            }
        })

        //callAdapter方法
        apiRepository.getGitHubByCallAdapter(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("list", it.toString())
                githubShow.text = it.toString()
                idShow.visibility = View.GONE
            }, {
                Log.d("error", it.toString())
                noContent.visibility = View.VISIBLE
            })

        //logging方法
        apiRepository.getGitHubByLog(name).enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                if (response.body().isNullOrEmpty()) {
                    githubShow.visibility = View.GONE
                    idShow.visibility = View.GONE
                    noContent.visibility = View.VISIBLE
                } else {
                    noContent.visibility = View.GONE
                    idShow.visibility = View.GONE
                    githubShow.visibility = View.VISIBLE
                    githubShow.text = response.body().toString()
                }
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                Log.d("Fail", "is error")
            }
        })
    }

    //同步執行
    private fun getAlbumSync(id: Int) {
        Thread {
            try {
                val response = apiRepository.getAlbumById(id).execute()
                if (response.isSuccessful) {
                    Log.d("Success", response.body().toString())
                } else {
                    Log.d("Fail", "fail")
                }

            } catch (e: Exception) {
                Log.d("Fail", "is error")
            }
        }.start()
    }
}