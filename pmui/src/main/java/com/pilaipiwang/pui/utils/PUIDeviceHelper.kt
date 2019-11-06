package com.pilaipiwang.pui.utils

import android.os.Build
import android.os.Environment
import android.text.TextUtils
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.lang.reflect.Method
import java.util.*

/**
 * @author: vitar
 * @date:   2019/11/5
 */
internal object PUIDeviceHelper {

    val TAG = PUIDeviceHelper::class.java.simpleName

    private const val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"
    private const val KEY_FLYME_VERSION_NAME = "ro.build.display.id"

    private const val FLYME = "flyme"
    private val MEIZUBOARD = arrayOf("m9", "M9", "mx", "MX")

    private var sMiuiVersionName: String? = null
    private var sFlymeVersionName: String? = null

    /**
     * 品牌
     */
    private val BRAND = Build.BRAND.toLowerCase()

    init {
        val properties = Properties()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // android 8.0，读取 /system/uild.prop 会报 permission denied
            var fileInputStream: FileInputStream? = null
            try {
                fileInputStream =
                    FileInputStream(File(Environment.getRootDirectory(), "build.prop"))
                properties.load(fileInputStream)
            } catch (e: Exception) {
                // todo log
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        var clzSystemProperties: Class<*>? = null
        try {
            clzSystemProperties = Class.forName("android.os.SystemProperties")
            val getMethod = clzSystemProperties.getDeclaredMethod("get", String::class.java)
            // miui
            sMiuiVersionName = getLowerCaseName(properties, getMethod, KEY_MIUI_VERSION_NAME)
            //flyme
            sFlymeVersionName = getLowerCaseName(properties, getMethod, KEY_FLYME_VERSION_NAME)
        } catch (e: Exception) {
            // todo log
        }

    }


    private fun isPhone(boards: Array<String>): Boolean {
        val board = android.os.Build.BOARD ?: return false
        for (board1 in boards) {
            if (board == board1) {
                return true
            }
        }
        return false
    }

    private fun getLowerCaseName(p: Properties, get: Method, key: String): String? {
        var name: String? = p.getProperty(key)
        if (name == null) {
            try {
                name = get.invoke(null, key) as String
            } catch (ignored: Exception) {
            }

        }
        if (name != null) name = name.toLowerCase()
        return name
    }

    /**#### 判断系统 #####*/

    /**
     * 判断是否是flyme系统
     */
    @JvmStatic
    fun isFlyme(): Boolean {
        return !TextUtils.isEmpty(sFlymeVersionName) && sFlymeVersionName!!.contains(FLYME)
    }

    /**
     * 判断是否是MIUI系统
     */
    @JvmStatic
    fun isMIUI(): Boolean {
        return !TextUtils.isEmpty(sMiuiVersionName)
    }

    @JvmStatic
    fun isMIUIV5(): Boolean {
        return "v5" == sMiuiVersionName
    }

    @JvmStatic
    fun isMIUIV6(): Boolean {
        return "v6" == sMiuiVersionName
    }

    @JvmStatic
    fun isMIUIV7(): Boolean {
        return "v7" == sMiuiVersionName
    }

    @JvmStatic
    fun isMIUIV8(): Boolean {
        return "v8" == sMiuiVersionName
    }

    @JvmStatic
    fun isMIUIV9(): Boolean {
        return "v9" == sMiuiVersionName
    }

    /**#### 判断品牌厂商 #####*/

    /**
     * 判断是否为小米
     */
    @JvmStatic
    fun isXiaomi() = Build.MANUFACTURER.toLowerCase() == "xiaomi"

    /**
     * 是否是魅族
     */
    @JvmStatic
    fun isMeizu(): Boolean {
        return isPhone(MEIZUBOARD) || isFlyme()
    }

    /**
     * 是否为 vivo
     */
    @JvmStatic
    fun isVivo() = (BRAND == "vivo" || BRAND == "bbk")

    /**
     * 是否为 oppo
     */
    @JvmStatic
    fun isOppo() = BRAND == "oppo"

    /**
     * 是否为华为
     */
    @JvmStatic
    fun isHuawei() = (BRAND == "huawei" || BRAND == "honor")

}