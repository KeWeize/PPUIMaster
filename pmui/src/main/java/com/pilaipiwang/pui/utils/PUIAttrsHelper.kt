package com.pilaipiwang.pui.utils

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.TypedValue
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.pilaipiwang.pui.R

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

    /**
     * 动态设置TextView样式
     */
    @JvmStatic
    fun assignTextViewWithAttr(textView: TextView, attrRes: Int) {
        val ta = textView.context.obtainStyledAttributes(
            null,
            R.styleable.PUITextViewCommAttr, attrRes, 0
        )
        var paddingLeft = textView.paddingLeft
        var paddingRight = textView.paddingRight
        var paddingTop = textView.paddingTop
        var paddingBottom = textView.paddingBottom

        for (i in 0 until ta.indexCount) {
            when (val attr = ta.getIndex(i)) {
                R.styleable.PUITextViewCommAttr_android_gravity ->
                    textView.gravity = ta.getInt(attr, -1)

                R.styleable.PUITextViewCommAttr_android_textColor ->
                    textView.setTextColor(ta.getColorStateList(attr))

                R.styleable.PUITextViewCommAttr_android_textSize ->
                    textView.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        ta.getDimensionPixelSize(attr, 0).toFloat()
                    )

                R.styleable.PUITextViewCommAttr_android_paddingLeft ->
                    paddingLeft = ta.getDimensionPixelSize(attr, 0)
                R.styleable.PUITextViewCommAttr_android_paddingTop ->
                    paddingTop = ta.getDimensionPixelSize(attr, 0)
                R.styleable.PUITextViewCommAttr_android_paddingRight ->
                    paddingRight = ta.getDimensionPixelSize(attr, 0)
                R.styleable.PUITextViewCommAttr_android_paddingBottom ->
                    paddingBottom = ta.getDimensionPixelSize(attr, 0)


                R.styleable.PUITextViewCommAttr_android_singleLine ->
                    textView.isSingleLine = ta.getBoolean(attr, false)

                R.styleable.PUITextViewCommAttr_android_ellipsize -> {
                    when (ta.getInt(attr, 3)) {
                        1 -> textView.ellipsize = TextUtils.TruncateAt.START
                        2 -> textView.ellipsize = TextUtils.TruncateAt.MIDDLE
                        3 -> textView.ellipsize = TextUtils.TruncateAt.END
                        4 -> textView.ellipsize = TextUtils.TruncateAt.MARQUEE
                    }
                }

                R.styleable.PUITextViewCommAttr_android_maxLines ->
                    textView.maxLines = ta.getInt(attr, -1)

                R.styleable.PUITextViewCommAttr_android_background ->
                    PUIViewHelper.setBackgroundKeepingPadding(textView, ta.getDrawable(attr))

                R.styleable.PUITextViewCommAttr_android_lineSpacingExtra ->
                    textView.setLineSpacing(ta.getDimensionPixelSize(attr, 0).toFloat(), 1f)

                R.styleable.PUITextViewCommAttr_android_drawablePadding ->
                    textView.compoundDrawablePadding = ta.getDimensionPixelSize(attr, 0)

                R.styleable.PUITextViewCommAttr_android_textColorHint ->
                    textView.setHintTextColor(ta.getColor(attr, 0))

                R.styleable.PUITextViewCommAttr_android_textStyle -> {
                    val styleIndex = ta.getInt(attr, -1)
                    textView.setTypeface(null, styleIndex)
                }
            }
        }
        ta.recycle()
        textView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    }

}