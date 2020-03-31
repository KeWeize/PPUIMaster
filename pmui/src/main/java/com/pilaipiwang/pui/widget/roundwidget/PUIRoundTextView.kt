package com.pilaipiwang.pui.widget.roundwidget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import com.pilaipiwang.pui.R
import com.pilaipiwang.pui.alpha.PUIAlphaTextView
import com.pilaipiwang.pui.utils.PUIViewHelper

/**
 * @author: vitar
 * @date:   2019/11/5
 */
open class PUIRoundTextView : PUIAlphaTextView {

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context!!, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {

        var mDisableColor: Int
        var mDisableAlpha: Float

        var mOrientationFlag: Int
        val ta =
            context.obtainStyledAttributes(attrs, R.styleable.PUIRoundTextView, defStyleAttr, 0)
        val mBuilder = PUIRoundDrawable.Builder()

        mBuilder.primaryColor =
            ta.getColor(R.styleable.PUIRoundTextView_pui_primaryColor, Color.TRANSPARENT)
        mBuilder.secondaryColor =
            ta.getColor(R.styleable.PUIRoundTextView_pui_secondaryColor, Color.TRANSPARENT)
        mBuilder.borderColor =
            ta.getColor(R.styleable.PUIRoundTextView_pui_borderColor, Color.TRANSPARENT)
        mBuilder.borderWidth =
            ta.getDimensionPixelSize(R.styleable.PUIRoundTextView_pui_borderWidth, 0)
        mOrientationFlag = ta.getInt(R.styleable.PUIRoundTextView_pui_gradientOrientation, 0)
        mBuilder.radius = ta.getDimensionPixelSize(R.styleable.PUIRoundTextView_pui_radius, 0)
        mBuilder.radiusTopLeft =
            ta.getDimensionPixelSize(R.styleable.PUIRoundTextView_pui_radiusTopLeft, 0)
        mBuilder.radiusTopRight =
            ta.getDimensionPixelSize(R.styleable.PUIRoundTextView_pui_radiusTopRight, 0)
        mBuilder.radiusBottomLeft =
            ta.getDimensionPixelSize(R.styleable.PUIRoundTextView_pui_radiusBottomLeft, 0)
        mBuilder.radiusBottomRight =
            ta.getDimensionPixelSize(R.styleable.PUIRoundTextView_pui_radiusBottomRight, 0)
        mBuilder.isRadiusAdjustBounds =
            ta.getBoolean(R.styleable.PUIRoundTextView_pui_isRadiusAdjustBounds, true)

        mDisableColor =
            ta.getColor(R.styleable.PUIRoundTextView_pui_disableColor, 0)
        mDisableAlpha = ta.getFloat(R.styleable.PUIRoundTextView_pui_disableAlpha, -1f)

        ta.recycle()
        mBuilder.gradientOrientation = when (mOrientationFlag) {
            1 ->
                GradientDrawable.Orientation.TOP_BOTTOM
            2 ->
                GradientDrawable.Orientation.BOTTOM_TOP
            3 ->
                GradientDrawable.Orientation.LEFT_RIGHT
            4 ->
                GradientDrawable.Orientation.RIGHT_LEFT
            else ->
                GradientDrawable.Orientation.LEFT_RIGHT
        }
        val drawable = generateDrawable(mBuilder, mDisableAlpha, mDisableColor)
        PUIViewHelper.setBackgroundKeepingPadding(this, drawable)
    }

    private fun generateDrawable(
        builder: PUIRoundDrawable.Builder, disableAlpha: Float, disabledColor: Int
    ): Drawable {

        val mStateListDrawable = StateListDrawable()

        // 生成正常背景
        val normalDrawable = builder.build()
        mStateListDrawable.addState(
            intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_pressed),
            normalDrawable
        )
        mStateListDrawable.addState(
            intArrayOf(android.R.attr.state_enabled, android.R.attr.state_pressed),
            normalDrawable
        )


        // 组件不可用时（enable = false）
        if (disabledColor != 0) {
            builder.primaryColor = disabledColor
            builder.secondaryColor = Color.TRANSPARENT
            val disabledDrawable = builder.build()
            mStateListDrawable.addState(
                intArrayOf(-android.R.attr.state_enabled), disabledDrawable
            )
            // 如果传入了disable的背景色，则取消掉原来半透明的效果
            setChangeAlphaWhenDisable(false)
        } else if (disableAlpha in 0f..1f) {
            setDisableAlpha(disableAlpha)
        } else {
            mStateListDrawable.addState(
                intArrayOf(-android.R.attr.state_enabled), normalDrawable
            )
        }

        // 点击
//        builder.pressedAlpha = pressedAlpha
//        val pressedDrawable = builder.build()
//        mStateListDrawable.addState(
//            intArrayOf(android.R.attr.state_pressed), pressedDrawable
//        )

        // 不可用
//        builder.primaryColor = disabledColor
//        builder.secondaryColor = Color.TRANSPARENT
//        val disabledDrawable = builder.build()
//        mStateListDrawable.addState(
//            intArrayOf(-android.R.attr.state_enabled), disabledDrawable
//        )

        return mStateListDrawable
    }

}