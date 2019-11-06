package com.pilaipiwang.pui.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import com.pilaipiwang.pui.PUILog

/**
 * @author: vitar
 * @date:   2019/11/5
 */
internal object PUIDrawableHelper {

    private val TAG = PUIDrawableHelper::class.java.simpleName

    /**
     * 创建一个层级 Drawable 带上分割线或下分割线
     */
    @JvmStatic
    fun createItemSeparatorBg(
        @ColorInt separatorColor: Int,
        @ColorInt bgColor: Int, separatorHeight: Int,
        top: Boolean
    ): LayerDrawable {
        val separator = ShapeDrawable()
        separator.paint.style = Paint.Style.FILL
        separator.paint.color = separatorColor

        val bg = ShapeDrawable()
        bg.paint.style = Paint.Style.FILL
        bg.paint.color = bgColor

        val layers = arrayOf<Drawable>(separator, bg)
        val layerDrawable = LayerDrawable(layers)

        layerDrawable.setLayerInset(
            1, 0, if (top) separatorHeight else 0,
            0, if (top) 0 else separatorHeight
        )
        return layerDrawable
    }


    /////////////// VectorDrawable /////////////////////

    @JvmStatic
    fun getVectorDrawable(context: Context, @DrawableRes resVector: Int): Drawable? {
        return try {
            AppCompatResources.getDrawable(context, resVector)
        } catch (e: Exception) {
            PUILog.d(
                TAG,
                "Error in getVectorDrawable. resVector=" + resVector
                        + ", resName=" + context.resources.getResourceName(
                    resVector
                ) + e.message
            )
            null
        }

    }

}