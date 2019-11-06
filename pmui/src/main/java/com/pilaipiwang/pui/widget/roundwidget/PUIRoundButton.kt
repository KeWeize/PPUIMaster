package com.pilaipiwang.pui.widget.roundwidget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import com.pilaipiwang.pui.R
import com.pilaipiwang.pui.alpha.PUIAlphaButton
import com.pilaipiwang.pui.utils.PUIViewHelper

/**
 * @author: vitar
 * @date:   2019/11/5
 */
open class PUIRoundButton : PUIAlphaButton {

    constructor(context: Context?) : super(context) {
        init(context!!, null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs,
        R.attr.PUIButtonStyle
    ) {
        init(context!!, attrs, R.attr.PUIButtonStyle)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context!!, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PUIRoundButton, defStyleAttr, 0)
        val count = ta.indexCount
        val mBuilder = PUIRoundDrawable.Builder()
        var orientationFlag = 0
        for (i in 0 until count) {
            when (val index = ta.getIndex(i)) {
                R.styleable.PUIRoundButton_pui_backgroundColor ->
                    mBuilder.colorBg = ta.getColorStateList(index)
                R.styleable.PUIRoundButton_pui_borderColor ->
                    mBuilder.colorBorder = ta.getColorStateList(index)
                R.styleable.PUIRoundButton_pui_borderWidth ->
                    mBuilder.borderWidth = ta.getDimensionPixelSize(index, 0)
                R.styleable.PUIRoundButton_pui_isRadiusAdjustBounds ->
                    mBuilder.isRadiusAdjustBounds = ta.getBoolean(index, true)
                R.styleable.PUIRoundButton_pui_radius ->
                    mBuilder.mRadius = ta.getDimensionPixelSize(index, 0)
                R.styleable.PUIRoundButton_pui_radiusTopLeft ->
                    mBuilder.mRadiusTopLeft = ta.getDimensionPixelSize(index, 0).toFloat()
                R.styleable.PUIRoundButton_pui_radiusTopRight ->
                    mBuilder.mRadiusTopRight = ta.getDimensionPixelSize(index, 0).toFloat()
                R.styleable.PUIRoundButton_pui_radiusBottomLeft ->
                    mBuilder.mRadiusBottomLeft = ta.getDimensionPixelSize(index, 0).toFloat()
                R.styleable.PUIRoundButton_pui_radiusBottomRight ->
                    mBuilder.mRadiusBottomRight = ta.getDimensionPixelSize(index, 0).toFloat()
                R.styleable.PUIRoundButton_pui_gradientOrientation ->
                    orientationFlag =
                        ta.getInt(R.styleable.PUIRoundButton_pui_gradientOrientation, 0)
                R.styleable.PUIRoundButton_pui_gradientStartColor ->
                    mBuilder.mGradientColorStart = ta.getColor(
                        R.styleable.PUIRoundButton_pui_gradientStartColor,
                        Color.TRANSPARENT
                    )
                R.styleable.PUIRoundButton_pui_gradientEndColor ->
                    mBuilder.mGradientColorEnd = ta.getColor(
                        R.styleable.PUIRoundButton_pui_gradientEndColor,
                        Color.TRANSPARENT
                    )
            }
        }
        ta.recycle()
        mBuilder.mGradientOrientation = when (orientationFlag) {
            1 ->
                GradientDrawable.Orientation.TOP_BOTTOM
            2 ->
                GradientDrawable.Orientation.BOTTOM_TOP
            3 ->
                GradientDrawable.Orientation.LEFT_RIGHT
            4 ->
                GradientDrawable.Orientation.RIGHT_LEFT
            else ->
                null
        }
        if (mBuilder.colorBg == null) {
            mBuilder.colorBg = ColorStateList.valueOf(Color.TRANSPARENT)
        }
        val bg = mBuilder.build()
        PUIViewHelper.setBackgroundKeepingPadding(this, bg)
        setChangeAlphaWhenDisable(false)
        setChangeAlphaWhenPress(false)
    }
}