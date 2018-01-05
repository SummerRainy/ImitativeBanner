package com.example.gongxuan.banner.widget

import android.content.Context
import android.widget.Scroller

/**
 * Created by gongxuan on 2018/1/4.
 */
class BannerScroller(context: Context?) : Scroller(context) {
    var mDuration: Int= BannerConfig.DURATION

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        super.startScroll(startX, startY, dx, dy, mDuration)
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, mDuration)
    }
}