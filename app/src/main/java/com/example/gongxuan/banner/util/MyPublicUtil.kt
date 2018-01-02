package com.example.gongxuan.banner

import android.app.Activity
import android.app.Application
import android.app.Fragment
import android.content.Context
import android.graphics.Rect
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


import java.util.regex.Pattern
import android.view.WindowManager
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import com.example.gongxuan.banner.constants.MyPublicData


/**
 * Created by gongxuan on 2017/7/4.
 * 公共工具类
 */

/**
 * 顶级函数
 * Toast 工具
 */

fun Context.toast(message:String, length : Int = Toast.LENGTH_SHORT){
    Toast.makeText(this,message,length).show()
}


fun <T : Context> ImageView.loadPic(context:T, url : String?, options: RequestOptions = MyPublicData.options) {
    Glide.with(context)
            .load(url)
            .apply(options)
            .into(this)
    }

//TODO(以后做成扩展函数)
object MyPublicUtil {
    /**
     * 局部函数
     * 得到状态栏高度
     */
    fun getStatusHeight(activity: Activity): Int {
        var statusHeight = 0
        val localRect = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(localRect)
        statusHeight = localRect.top
        if (0 == statusHeight) {
            val localClass: Class<*>
            try {
                localClass = Class.forName("com.android.internal.R\$dimen")
                val localObject = localClass.newInstance()
                val i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString())
                statusHeight = activity.resources.getDimensionPixelSize(i5)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: Fragment.InstantiationException) {
                e.printStackTrace()
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: SecurityException) {
                e.printStackTrace()
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: java.lang.InstantiationException) {
                e.printStackTrace()
            }

        }
        return statusHeight
    }

    /**
     * 检测是否是手机号
     */
    fun isMobileNO(mobiles: String): Boolean {
        val p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$")
        val m = p.matcher(mobiles)
        return m.matches()
    }

    //base64加密
    //在这里使用的是encode方式，返回的是byte类型加密数据，可使用new String转为String类型
    fun encode(s: String): String {
        val string = String(Base64.encode(s.toByteArray(), Base64.DEFAULT))
        return string
    }

    //base64解密
    //传入base64字符串
    fun decode(s: String): String {
        val string = String(Base64.decode(s.toByteArray(), Base64.DEFAULT))
        return string
    }

    fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is LinearLayout) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.height
            val right = left + v.width
            return !(event.x > left && event.x < right
                    && event.y > top && event.y < bottom)
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false
    }

/*    *//**
     * 调整窗口的透明度
     *//*
    fun dimBackground(activity: Activity, view:View, isDark: Boolean= false) {
        val window = activity.window
        val params = window.attributes
        if(isDark) {
            params.alpha = 0.5f
            view.alpha = 1.0f
        } else {
            params.alpha = 1.0f
            view.alpha = 0.5f
        }
        window.attributes= params
    }*/

    @Suppress("DEPRECATION")
            /**
     * 调整窗口的透明度
     */
    fun dimBackground(root: View, change: View, isDark: Boolean= false) {
        if(isDark) {
            root.setBackgroundColor(Color.BLACK)
            change.alpha = 0.5f
        } else {
            root.setBackgroundColor(Color.WHITE)
            change.alpha = 1.0f
        }
    }
}