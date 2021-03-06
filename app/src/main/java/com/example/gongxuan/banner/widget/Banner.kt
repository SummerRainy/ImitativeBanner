package com.example.gongxuan.banner.widget

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.gongxuan.banner.R
import com.example.gongxuan.banner.listener.OnBannerListener
import com.example.gongxuan.banner.loadPic
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_banner.view.*
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * Created by gongxuan on 2017/11/23.
 */
class Banner: FrameLayout, ViewPager.OnPageChangeListener {
    private lateinit var viewPager :ScrollableViewPager
    private lateinit var scroller: BannerScroller
    private var isScroll = BannerConfig.IS_SCROLL
    private var isAutoPlay = BannerConfig.IS_AUTO_PLAY
    private var delayInterval = BannerConfig.DELAY_INTERVAL
    private var scrollDuration = BannerConfig.SCROLL_DURATION
    private var disposable:Disposable? =null

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
    private var previousPosition = 0
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
        viewPager = view.findViewById(R.id.scrollViewPager)
        handleTypedArray(context, attrs)

        initViewPagerScroll()
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
        /*for(i in 0 until count) {
            var imageView = ImageView(context)
            imageView.scaleType = scaleType

            var url:String? = imageUrls[i]
            imageViews.add(imageView)
            imageView.loadPic(context, url)
        }*/
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

            if (isScroll && count > 1) {
                viewPager.setIsScroll(true)
            } else {
                viewPager.setIsScroll(false)
            }
        }

        if (isAutoPlay)
            startAutoPlay()
    }

    private fun initViewPagerScroll() {
        val field = ViewPager::class.java.getDeclaredField("mScroller")
        field.isAccessible = true
        scroller = BannerScroller(viewPager.context)
        scroller.mDuration = scrollDuration
        field.set(viewPager, scroller)
    }

    private fun startAutoPlay() {
        Log.i("Banner","开始自动轮播")
        if (count >1 && isAutoPlay) {
            var observable = Observable.interval(delayInterval.toLong(), delayInterval.toLong(), TimeUnit.MILLISECONDS)
            disposable =  observable.observeOn(AndroidSchedulers.mainThread()).subscribe {
                Log.i("Banner","currentItem: "+currentItem)
                currentItem = if(currentItem == count) {
                    currentItem+1 % (count+1) + 1
                }else {
                    currentItem % (count+1) + 1
                }
                viewPager.currentItem = currentItem
            }
        }
    }

    private fun stopAutoPlay() {
        Log.i("Banner","停止自动轮播")
        disposable?.dispose()
    }

    //当Bannerwindow 中看不见时候，应该停止轮播
    @SuppressLint("SwitchIntDef")
    override fun onWindowVisibilityChanged(visibility: Int) {
        when(visibility) {
            View.VISIBLE -> {
                startAutoPlay()
            }
            else -> {
                stopAutoPlay()
            }
        }
        super.onWindowVisibilityChanged(visibility)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if(isAutoPlay) {
            var action = ev?.action
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                startAutoPlay()
            } else if (action == MotionEvent.ACTION_DOWN) {
                stopAutoPlay()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onPageScrollStateChanged(state: Int) {
        var currentItem = viewPager.currentItem
        when(state) {
            ViewPager.SCROLL_STATE_IDLE -> {
                if (currentItem == count+1) {
                    viewPager.setCurrentItem(1,false)
                }

                if (currentItem == 0) {
                    viewPager.setCurrentItem(count,false)
                }
            }
            ViewPager.SCROLL_STATE_DRAGGING-> {
            }
            ViewPager.SCROLL_STATE_SETTLING-> {
            }
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        //设置 indicator image(将preIndicator = unselected,currentIndicator = selected)
        var currentPosition = toRealPosition(position)
        indicatorImages[toRealPosition(previousPosition)].setImageResource(mIndicatorUnselectedResId)
        indicatorImages[currentPosition
                ].setImageResource(mIndicatorSelectedResId)
        previousPosition = position

        //设置标题
        bannerTitle.text = titles[currentPosition]
    }

    /**
     * 返回真实的位置
     * ∵imageViews.size = data.size+2(即=0的情况和=count+1的情况，因为是轮回首位相接的)
     * ∴realDataPosition = when(position) {
     *  0 -> data.count -1     (0 = 数据的最后一个)
     *  imageViews.size-1 ->0 //imageViews-1 -1  = count（最后一个 = 数据的第一个）
     *  else { it -1 } （其他时候为 -1 为当前数据位置）
     * }
     *
     * @param position
     * @return 下标从0开始
     */
    fun toRealPosition(position: Int): Int = (position - 1+ count) % count

    /***/
    inner class BannerPagerAdapter:PagerAdapter() {
        override fun isViewFromObject(view: View?, `object`: Any?): Boolean = view == `object`

        override fun getCount(): Int = imageViews.size

        override fun instantiateItem(container: ViewGroup?, position: Int): Any {
            var view = imageViews[position]
            container?.addView(imageViews[position])
            listener?.let {
                view.setOnClickListener {
                    listener?.onBannerClick(toRealPosition(position))
                }
            }
            return view
        }

        override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
            container?.removeView(`object` as View)
        }
    }
}