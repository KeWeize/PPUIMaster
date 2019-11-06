package com.pilaipiwang.pui.utils

import android.annotation.TargetApi
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View

/**
 * @author: vitar
 * @date:   2019/11/5
 */
object PUIViewHelper {

    @JvmStatic
    fun setBackgroundKeepingPadding(view: View, drawable: Drawable) {
        val padding =
            intArrayOf(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)
        setBackground(view, drawable)
        view.setPadding(padding[0], padding[1], padding[2], padding[3])
    }

    @JvmStatic
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun setBackground(view: View, drawable: Drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.background = drawable
        } else {
            view.setBackgroundDrawable(drawable)
        }
    }

}