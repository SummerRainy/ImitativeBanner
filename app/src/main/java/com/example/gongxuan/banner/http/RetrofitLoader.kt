package com.changzhounews.app.http

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.Cookie
import com.example.gongxuan.banner.BuildConfig
import com.google.gson.reflect.TypeToken
import okhttp3.HttpUrl
import okhttp3.CookieJar





/**
 * Created by gongxuan on 2017/6/28.
 * TODO("Retrofit估计要重新封装")
 */
class RetrofitLoader {
    companion object {
        fun initRetrofit(): Retrofit {
            return Retrofit.Builder()
                    .baseUrl(BuildConfig.PATH_URL)
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//支持RxJava
                    .build()
        }
    }
}