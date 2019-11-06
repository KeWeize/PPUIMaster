package com.pilaipiwang.pui.utils

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.core.content.ContextCompat

/**
 * @author: vitar
 * @date:   2019/11/5
 */
internal object PUIAttrsHelper {

    @JvmStatic
    fun getAttrFloatValue(context: Context, attrRes: Int): Float {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attrRes, typedValue, true)
        return typedValue.float
    }

    @JvmStatic
    fun getAttrColor(context: Context, attrRes: Int): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attrRes, typedValue, true)
        return typedValue.data
    }

    @JvmStatic
    fun getAttrColorStateList(context: Context, attrRes: Int): ColorStateList? {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attrRes, typedValue, true)
        return ContextCompat.getColorStateList(context, typedValue.resourceId)
    }

    @JvmStatic
    fun getAttrDrawable(context: Context, attrRes: Int): Drawable? {
        val attrs = intArrayOf(attrRes)
        val ta = context.obtainStyledAttributes(attrs)
        val drawable = getAttrDrawable(context, ta, 0)
        ta.recycle()
        return drawable
    }

    @JvmStatic
    fun getAttrDrawable(context: Context, typedArray: TypedArray, index: Int): Drawable? {
        val value = typedArray.peekValue(index)
        if (value != null) {
            if (value.type != TypedValue.TYPE_ATTRIBUTE && value.resourceId != 0) {
                return PUIDrawableHelper.getVectorDrawable(context, value.resourceId)
            }
        }
        return null
    }

    @JvmStatic
    fun getAttrDimen(context: Context, attrRes: Int): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attrRes, typedValue, true)
        return TypedValue.complexToDimensionPixelSize(
            typedValue.data,
            PUIDisplayHelper.getDisplayMetrics(context)
        )
    }


}