package com.pilaipiwang.pui.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager
import java.lang.Exception

/**
 * 屏幕相关
 * @author: vitar
 * @date:   2019/11/5
 */
internal object PUIDisplayHelper {

    private val TAG = PUIDisplayHelper.javaClass.simpleName

    /**
     * 屏幕密度
     */
    @JvmField
    val DENSITY = Resources.getSystem().displayMetrics.density

    /**
     * 获取 DisplayMetrics
     */
    @JvmStatic
    fun getDisplayMetrics(context: Context): DisplayMetrics {
        return context.resources.displayMetrics
    }


    /**
     * 把以 dp 为单位得值转化为 px 为单位
     */
    @JvmStatic
    fun dpToPx(dpValue: Int): Int = (dpValue * DENSITY + 0.5f).toInt()

    /**
     * 把以 px 为单位得值转化为 dp 为单位
     */
    @JvmStatic
    fun pxToDp(pxValue: Float): Int = (pxValue / DENSITY + 0.5f).toInt()

    @JvmStatic
    fun getDensity(context: Context) = context.resources.displayMetrics.density

    @JvmStatic
    fun getFontDensity(context: Context): Float = context.resources.displayMetrics.scaledDensity

    /**
     * 获取屏幕宽度
     */
    @JvmStatic
    fun getScreenWidth(context: Context) = getDisplayMetrics(context).widthPixels

    /**
     * 获取屏幕高度
     */
    @JvmStatic
    fun getScreenHeight(context: Context): Int {
        val screenHeight = getDisplayMetrics(context).heightPixels
        if (PUIDeviceHelper.isXiaomi() && xiaomiNavigationGestureEnabled(context)) {
            return screenHeight + getResourceNavHeight(context)
        }
        return screenHeight
    }

    private fun getResourceNavHeight(context: Context): Int {
        // 小米4没有nav bar, 而 navigation_bar_height 有值
        val resourceId =
            context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else -1
    }

    /**
     * 获取屏幕真实宽高
     */
    fun getRealScreenSize(context: Context) = doGetRealScreenSize(context)

    /**
     * 获取屏幕真实宽高
     */
    private fun doGetRealScreenSize(context: Context): IntArray {
        val size = IntArray(2)
        var widthPixels = 0
        var heightPixels = 0
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val d: Display = wm.defaultDisplay
        val metrics = DisplayMetrics()
        d.getMetrics(metrics)
        // since SDK_INT = 1
        widthPixels = metrics.widthPixels
        heightPixels = metrics.heightPixels

        try {
            // used when 17 > SDK_INT >= 14; includes window decorations (statusbar bar/menu bar)
            // 通过反射获取原始宽高
            widthPixels = Display::class.java.getMethod("getRawWidth").invoke(d) as Int
            heightPixels = Display::class.java.getMethod("getRawHeight").invoke(d) as Int
        } catch (ignored: Exception) {
        }
        if (Build.VERSION.SDK_INT >= 17) {
            try {
                // used when SDK_INT >= 17; includes window decorations (statusbar bar/menu bar)
                val realSize = Point()
                d.getRealSize(realSize)
                // 通过反射获取真实宽高
                Display::class.java.getMethod("getRealSize", Point::class.java).invoke(d, realSize)
                widthPixels = realSize.x
                heightPixels = realSize.y
            } catch (ignored: Exception) {
            }
        }

        size[0] = widthPixels
        size[1] = heightPixels
        return size
    }


    /**##### 单位转换 ######*/

    /**
     * 单位转换 dp -> px
     */
    @JvmStatic
    fun dp2px(context: Context, dp: Int): Int = (getDensity(context) * dp + 0.5).toInt()

    /**
     * 单位转换 sp -> px
     */
    @JvmStatic
    fun sp2px(context: Context, sp: Int): Int = (getFontDensity(context) * sp + 0.5).toInt()

    /**
     * 单位转换 px -> dp
     */
    @JvmStatic
    fun px2dp(context: Context, px: Int) = (px / getDensity(context) + 0.5).toInt()

    /**
     * 单位转换 sp - dp
     */
    @JvmStatic
    fun px2sp(context: Context, sp: Int) = (sp / getFontDensity(context) + 0.5).toInt()


    // ====================== Setting ===========================
    private const val VIVO_NAVIGATION_GESTURE = "navigation_gesture_on"
    private const val HUAWAI_DISPLAY_NOTCH_STATUS = "display_notch_status"
    private const val XIAOMI_DISPLAY_NOTCH_STATUS = "force_black"
    private const val XIAOMI_FULLSCREEN_GESTURE = "force_fsg_nav_bar"

    /**
     * 获取vivo手机设置中的"navigation_gesture_on"值，判断当前系统是使用导航键还是手势导航操作
     *
     * @param context app Context
     * @return false 表示使用的是虚拟导航键(NavigationBar)， true 表示使用的是手势， 默认是false
     */
    @JvmStatic
    fun vivoNavigationGestureEnabled(context: Context): Boolean {
        val value = Settings.Secure.getInt(context.contentResolver, VIVO_NAVIGATION_GESTURE, 0)
        return value != 0
    }

    @TargetApi(17)
    fun xiaomiNavigationGestureEnabled(context: Context): Boolean {
        val value = Settings.Global.getInt(context.contentResolver, XIAOMI_FULLSCREEN_GESTURE, 0)
        return value != 0
    }

    @JvmStatic
    fun huaweiIsNotchSetToShowInSetting(context: Context): Boolean {
        // 0: 默认
        // 1: 隐藏显示区域
        val result = Settings.Secure.getInt(context.contentResolver, HUAWAI_DISPLAY_NOTCH_STATUS, 0)
        return result == 0
    }

    @JvmStatic
    @TargetApi(17)
    fun xiaomiIsNotchSetToShowInSetting(context: Context): Boolean {
        return Settings.Global.getInt(context.contentResolver, XIAOMI_DISPLAY_NOTCH_STATUS, 0) == 0
    }

}