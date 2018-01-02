package com.example.gongxuan.banner.widget

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.gongxuan.banner.R
import com.example.gongxuan.banner.listener.OnBannerListener
import com.example.gongxuan.banner.loadPic
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.layout_banner.view.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by gongxuan on 2017/11/23.
 */
class Banner: FrameLayout, ViewPager.OnPageChangeListener {
    private var bannerContext:Context
    private var titles: MutableList<String?>
    private var imageUrls: MutableList<String?>
    private var imageViews: MutableList<View>
    private var indicatorImages: MutableList<ImageView>
    private var dm: DisplayMetrics
    private var indicatorSize : Int
    private var count = 0
    private var currentItem: Int = 0
    private var mIndicatorSelectedResId = R.drawable.gray_radius
    private var mIndicatorUnselectedResId = R.drawable.white_radius
    private var scaleType = ImageView.ScaleType.CENTER_CROP
    private var adapter:BannerPagerAdapter? = null
    private var listener : OnBannerListener? = null
    private lateinit var onPageChangeListener: ViewPager.OnPageChangeListener
    private lateinit var viewPager:ViewPager
    private var previousPosition = 1
    //private lateinit var indicator : LinearLayout

    constructor(context: Context) :this(context,null)

    constructor(context: Context, attrs: AttributeSet?) : this(context,attrs,0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle) {
        bannerContext = context
        titles = ArrayList()
        imageUrls = ArrayList()
        imageViews = ArrayList()

        indicatorImages = ArrayList()
        dm = context.resources.displayMetrics
        indicatorSize = dm.widthPixels / 80
        initView(context, attrs)
    }

    private fun initView(context: Context,attrs: AttributeSet?) {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_banner, this, true)
        //indicator = view.findViewById(com.youth.banner.R.id.circleIndicator)
        viewPager = view.findViewById(R.id.bannerViewPager)
        handleTypedArray(context, attrs)

    }

    private fun handleTypedArray(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        //val typedArray = context.obtainStyledAttributes(attrs, R.styleable.)
        //typedArray.recycle()
    }

    /** setData such as images、title、imageLoader*/
    fun setImages(imageUrls :MutableList<String?>): Banner {
        this.imageUrls = imageUrls
        return this
    }

    fun setTitles(titles :MutableList<String?>): Banner {
        this.titles = titles
        return this
    }

    fun setOnBannerListener(listener: OnBannerListener) :Banner{
        this.listener = listener
        return this
    }

    fun start(): Banner {
        //data check
        if(imageUrls.isEmpty()) {
            Log.e("Banner","[Banner]-->lack of imageUrls")
        }

        if(imageUrls.size != titles.size) {
            throw RuntimeException("[Banner] --> The number of titles and images is different")
        }

        count = imageUrls.size

        setBannerStyleUI()
        setImageList(imageUrls)
        setOtherData()
        return this
    }

    private fun setBannerStyleUI() {
        //set indicator
        indicatorImages.clear()
        indicatorInside.removeAllViews()

        var visibility = if(count >1 ) View.VISIBLE else View.GONE
        indicatorInside.visibility = visibility

        setTitleStyleUI()


    }

    private fun setTitleStyleUI() {
        //set title
        if(titles.isNotEmpty()) {
            bannerTitle.text = titles[0]
            bannerTitle.visibility = View.VISIBLE
            titleView.visibility = View.VISIBLE
        }
    }

    private fun setImageList(imageUrls: MutableList<String?>) {
        createIndicator()

        //init images
        for(i in 0..count+1) {
            var imageView = ImageView(context)
            imageView.scaleType = scaleType

            var url:String? = when(i) {
                0 -> imageUrls[count-1]
                count +1 -> imageUrls[0]
                else -> imageUrls[i-1]
            }
            imageViews.add(imageView)
            imageView.loadPic(context, url)
        }
    }

    //构造 indicator
    private fun createIndicator() {
        //init ImageViews
        imageViews.clear()

        indicatorImages.clear()
        //indicator.removeAllViews()
        indicatorInside.removeAllViews()
        for(i in 0 until count) {
            //init indicator
            val imageView = ImageView(context)
            imageView.scaleType = scaleType
            val params = LinearLayout.LayoutParams(indicatorSize, indicatorSize)
            params.leftMargin = BannerConfig.PADDING_SIZE
            params.rightMargin = BannerConfig.PADDING_SIZE
            if (i == 0) {
                imageView.setImageResource(mIndicatorSelectedResId)
            } else {
                imageView.setImageResource(mIndicatorUnselectedResId)
            }
            indicatorImages.add(imageView)
            indicatorInside.addView(imageView,params)
        }
    }

    private fun setOtherData() {
        currentItem = 1
        adapter?:let{
            adapter = BannerPagerAdapter()
            viewPager.addOnPageChangeListener(this)
        }
        viewPager.apply {
            adapter = this@Banner.adapter
            isFocusable = true
            currentItem = 1
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
        currentItem = viewPager.currentItem
        when(state) {
            0 -> {
                when(currentItem) {
                    0 -> viewPager.setCurrentItem(count,false)
                    count+1 -> viewPager.setCurrentItem(1,false)
                }
            }
            1-> {
                when(currentItem) {
                    0 -> viewPager.setCurrentItem(count,false)
                    count+1 -> viewPager.setCurrentItem(1,false)
                }
            }
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        //设置 indicator image(将preIndicator = unselected,currentIndicator = selected)
        indicatorImages[(previousPosition -1 + count) % count].setImageResource(mIndicatorUnselectedResId)
        indicatorImages[(position -1 +count) % count].setImageResource(mIndicatorSelectedResId)
        previousPosition = position

        //设置标题
        var currentPosition = if(position == 0) { count }else if(position > count){ 1 }else{ position }
        bannerTitle.text = titles[currentPosition -1]
    }

    /**
     * 返回真实的位置
     *
     * @param position
     * @return 下标从0开始
     */
    fun toRealPosition(position: Int): Int {
        var realPosition = (position - 1) % count
        if (realPosition < 0)
            realPosition += count
        return realPosition
    }
    /***/
    inner class BannerPagerAdapter:PagerAdapter() {
        override fun isViewFromObject(view: View?, `object`: Any?): Boolean = view == `object`

        override fun getCount(): Int = imageViews.size

        override fun instantiateItem(container: ViewGroup?, position: Int): Any {
            var view = imageViews[position]
            container?.addView(imageViews[position])
            listener?.let {
                view.setOnClickListener {
                    listener?.OnBannerClick(toRealPosition(position))
                }
            }
            return view
        }

        override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
            container?.removeView(`object` as View)
        }
    }

}