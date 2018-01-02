package com.example.gongxuan.banner

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList
import com.youth.banner.BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE
import android.text.method.TextKeyListener.clear
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import com.bumptech.glide.Glide
import com.changzhounews.app.http.RetrofitLoader
import com.example.gongxuan.banner.bean.NewsRecommendList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.example.gongxuan.banner.http.RetrofitService
import com.example.gongxuan.banner.listener.OnBannerListener
import com.youth.banner.BannerConfig


class MainActivity : AppCompatActivity() {
    private lateinit var recommendImages: ArrayList<String?>
    private lateinit var recommendTitles: ArrayList<String?>
    private var recommendList: ArrayList<NewsRecommendList.RecommendListBean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recommendImages = ArrayList()
        recommendTitles = ArrayList()
        recommendList = ArrayList()
        getRecommendList()
    }

    /**网络请求*/
    fun getRecommendList() {
        val service = RetrofitLoader.initRetrofit().create(RetrofitService::class.java)
        val observable = service.getNewsRecommendList("435")
        observable.subscribeOn(Schedulers.io())//请求数据的事件发生在io线程
                .observeOn(AndroidSchedulers.mainThread())//请求完成后在主线程更新UI
                //return observable
                .subscribe(
                        { result ->
                            recommendList = result.recommend_list
                            recommendList?.forEach {
                                recommendImages.add(it.thumb_pic?.url)
                                recommendTitles.add(it.subject)
                            }
                            Log.i("MyLog", "轮播图 Url： $recommendImages")
                            banner.apply {
                                setImages(recommendImages)
                                setTitles(recommendTitles)
                                setOnBannerListener(bannerListener)
                                start()
                            }
                        },
                        { error ->
                            error.printStackTrace()
                        }
                )

    }

    private val bannerListener = object : OnBannerListener {
        override fun onBannerClick(position: Int) {
            Log.i("MyLog", "当前点击pid： ${recommendList?.get(position)?.pid};title: ${recommendList?.get(position)?.subject}")
        }
    }
}
