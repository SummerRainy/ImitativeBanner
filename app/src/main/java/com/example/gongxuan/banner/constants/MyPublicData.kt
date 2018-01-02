package com.example.gongxuan.banner.constants

import android.app.Activity
import android.app.Fragment
import android.graphics.Rect
import com.bumptech.glide.request.RequestOptions
import com.example.gongxuan.banner.R

/**
 * Created by gongxuan on 2017/6/27.
 */
object MyPublicData {
    /**Glide 默认配置*/
    var options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
}