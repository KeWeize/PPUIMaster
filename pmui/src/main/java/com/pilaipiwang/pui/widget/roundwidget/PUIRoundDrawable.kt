package com.pilaipiwang.pui.widget.roundwidget

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.annotation.NonNull
import kotlin.math.min

/**
 * @author: vitar
 * @date:   2019/11/5
 */
internal class PUIRoundDrawable : GradientDrawable {

    private var mRadiusAdjustBounds = true
    private var mStrokeWidth = 0
    private var mFillColors: ColorStateList? = null
    private var mStrokeColors: ColorStateList? = null

    private constructor(builder: Builder) : super() {
        if (builder.mGradientOrientation != null) {
            orientation = builder.mGradientOrientation
            super.setColors(intArrayOf(builder.mGradientColorStart, builder.mGradientColorEnd))
        } else {
            setBgColor(builder.colorBg)
        }
        setStrokeData(builder.borderWidth, builder.colorBorder)
        if (builder.mRadiusTopLeft > 0 || builder.mRadiusTopRight > 0
            || builder.mRadiusBottomLeft > 0 || builder.mRadiusBottomRight > 0
        ) {
            val radii = floatArrayOf(
                builder.mRadiusTopLeft, builder.mRadiusTopLeft,
                builder.mRadiusTopRight, builder.mRadiusTopRight,
                builder.mRadiusBottomRight, builder.mRadiusBottomRight,
                builder.mRadiusBottomLeft, builder.mRadiusBottomLeft
            )
            cornerRadii = radii
            builder.isRadiusAdjustBounds = false
        } else {
            cornerRadius = builder.mRadius.toFloat()
            if (builder.mRadius > 0) {
                builder.isRadiusAdjustBounds = false
            }
        }
        setIsRadiusAdjustBounds(builder.isRadiusAdjustBounds)
    }

    override fun onStateChange(stateSet: IntArray?): Boolean {
        var superRet = super.onStateChange(stateSet)
        if (mFillColors != null) {
            val color = mFillColors!!.getColorForState(stateSet, Color.TRANSPARENT)
            setColor(color)
            superRet = true
        }
        if (mStrokeColors != null) {
            val color = mStrokeColors!!.getColorForState(stateSet, Color.TRANSPARENT)
            setStroke(mStrokeWidth, color)
            superRet = true
        }
        return superRet
    }

    override fun isStateful(): Boolean {
        return (mFillColors != null && mFillColors!!.isStateful
                || mStrokeColors != null && mStrokeColors!!.isStateful
                || super.isStateful())
    }

    override fun onBoundsChange(r: Rect?) {
        super.onBoundsChange(r)
        if (mRadiusAdjustBounds && r != null) {
            // 修改圆角为短边的一半
            cornerRadius = min(r.width(), r.height()).toFloat() / 2
        }
    }

    /**
     * 设置背景颜色（只支持纯色，不支持 BItmap 或 Drawable）
     */
    private fun setBgColor(@NonNull colors: ColorStateList?) {
        if (hasNativeStateListAPI()) {
            super.setColor(colors)
        } else {
            mFillColors = colors
            val currentColor = if (colors == null) {
                Color.TRANSPARENT
            } else {
                colors.getColorForState(state, Color.TRANSPARENT)
            }
            setColor(currentColor)
        }
    }

    /**
     * 设置按钮的描边大小和颜色
     */
    private fun setStrokeData(width: Int, @NonNull colors: ColorStateList?) {
        if (hasNativeStateListAPI()) {
            super.setStroke(width, colors)
        } else {
            mStrokeWidth = width
            mStrokeColors = colors
            val currentColor = if (colors == null) {
                Color.TRANSPARENT
            } else {
                colors.getColorForState(state, 0)
            }
            setStroke(width, currentColor)
        }
    }

    /**
     * 圆角大小是否自适应为自身高度的一半
     */
    fun setIsRadiusAdjustBounds(isRadiusAdjustBounds: Boolean) {
        this.mRadiusAdjustBounds = isRadiusAdjustBounds
    }

    private fun hasNativeStateListAPI(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    internal class Builder {
        var colorBg: ColorStateList? = null
        var colorBorder: ColorStateList? = null
        var borderWidth: Int = 0
        var isRadiusAdjustBounds = true
        var mRadius = 0
        var mRadiusTopLeft = 0f
        var mRadiusTopRight = 0f
        var mRadiusBottomLeft = 0f
        var mRadiusBottomRight = 0f

        var mGradientOrientation: Orientation? = null
        var mGradientColorStart = Color.TRANSPARENT
        var mGradientColorEnd = Color.TRANSPARENT

        fun build(): PUIRoundDrawable = PUIRoundDrawable(this)
    }

}