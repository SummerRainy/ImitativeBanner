package com.changzhounews.app.http

import com.example.gongxuan.banner.BuildConfig

/**
 * Created by gongxuan on 2017/6/28.
 */
object MyPath {
    //数据接口 basePath
    var path = BuildConfig.PATH_URL

    //网络请求（使用 retrofit有 baseURL了）
    const val newsRecommendList = "thread/recommend.php"
}