package com.pilaipiwang.pui.widget.roundwidget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
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
        var mDisableColor: Int
        var mPressedAlpha: Float
        var mOrientationFlag: Int
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PUIRoundButton, defStyleAttr, 0)
        val mBuilder = PUIRoundDrawable.Builder()

        mBuilder.primaryColor =
            ta.getColor(R.styleable.PUIRoundButton_pui_primaryColor, Color.TRANSPARENT)
        mBuilder.secondaryColor =
            ta.getColor(R.styleable.PUIRoundButton_pui_secondaryColor, Color.TRANSPARENT)
        mBuilder.borderColor =
            ta.getColor(R.styleable.PUIRoundButton_pui_borderColor, Color.TRANSPARENT)
        mBuilder.borderWidth =
            ta.getDimensionPixelSize(R.styleable.PUIRoundButton_pui_borderWidth, 0)
        mDisableColor = ta.getColor(R.styleable.PUIRoundButton_pui_disableColor, Color.TRANSPARENT)
        mPressedAlpha = ta.getFloat(R.styleable.PUIRoundButton_pui_pressedAlpha, 1f)
        mOrientationFlag = ta.getInt(R.styleable.PUIRoundButton_pui_gradientOrientation, 0)

        mBuilder.radius = ta.getDimensionPixelSize(R.styleable.PUIRoundButton_pui_radius, 0)
        mBuilder.radiusTopLeft =
            ta.getDimensionPixelSize(R.styleable.PUIRoundButton_pui_radiusTopLeft, 0)
        mBuilder.radiusTopRight =
            ta.getDimensionPixelSize(R.styleable.PUIRoundButton_pui_radiusTopRight, 0)
        mBuilder.radiusBottomLeft =
            ta.getDimensionPixelSize(R.styleable.PUIRoundButton_pui_radiusBottomLeft, 0)
        mBuilder.radiusBottomRight =
            ta.getDimensionPixelSize(R.styleable.PUIRoundButton_pui_radiusBottomRight, 0)
        mBuilder.isRadiusAdjustBounds =
            ta.getBoolean(R.styleable.PUIRoundButton_pui_isRadiusAdjustBounds, true)

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
        mPressedAlpha = if (mPressedAlpha < 0) {
            0f
        } else if (mPressedAlpha > 1) {
            1f
        } else {
            mPressedAlpha
        }

        val drawable = generateDrawable(mBuilder, mPressedAlpha, mDisableColor)
        PUIViewHelper.setBackgroundKeepingPadding(this, drawable)
        setChangeAlphaWhenDisable(false)
        setChangeAlphaWhenPress(false)
    }

    private fun generateDrawable(
        builder: PUIRoundDrawable.Builder, pressedAlpha: Float, disabledColor: Int
    ): Drawable {

        val mStateListDrawable = StateListDrawable()

        val normalDrawable = builder.build()
        mStateListDrawable.addState(
            intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_pressed),
            normalDrawable
        )

        builder.pressedAlpha = pressedAlpha
        val pressedDrawable = builder.build()
        mStateListDrawable.addState(
            intArrayOf(android.R.attr.state_pressed), pressedDrawable
        )

        builder.primaryColor = disabledColor
        builder.secondaryColor = Color.TRANSPARENT
        val disabledDrawable = builder.build()
        mStateListDrawable.addState(
            intArrayOf(-android.R.attr.state_enabled), disabledDrawable
        )
        return mStateListDrawable
    }

}