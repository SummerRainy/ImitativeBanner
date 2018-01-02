package com.example.gongxuan.banner.http

import com.changzhounews.app.http.MyPath
import com.example.gongxuan.banner.bean.NewsRecommendList
import io.reactivex.Observable
import retrofit2.http.*
/**
 * Created by gongxuan on 2017/6/28.
 */
interface RetrofitService {

    @GET(MyPath.newsRecommendList)
    fun getNewsRecommendList(@Query("fid") fid:String): Observable<NewsRecommendList>
}