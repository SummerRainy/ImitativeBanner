package com.example.gongxuan.banner.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by gongxuan on 2018/1/4.
 */
class ScrollableViewPager : ViewPager {
    constructor(mContext: Context?) : super(mContext)
    constructor(mContext: Context?, attrs: AttributeSet) : super(mContext,attrs)

    private var isScroll:Boolean = true

    fun setIsScroll(isScroll :Boolean) {
        this.isScroll = isScroll
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean = this.isScroll && super.onTouchEvent(ev)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean = this.isScroll && super.onInterceptTouchEvent(ev)
}