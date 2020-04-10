package com.pilaipiwang.pui.widget.roundwidget

import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import kotlin.math.min

/**
 * @author: vitar
 * @date:   2019/11/8
 */
internal class PUIRoundDrawable : GradientDrawable {

    private var mRadiusAdjustBounds = true

    private constructor(builder: Builder) : super() {
        // 设置颜色
        orientation = builder.gradientOrientation
        if (builder.primaryColor != Color.TRANSPARENT && builder.secondaryColor != Color.TRANSPARENT) {
            val colors = intArrayOf(builder.primaryColor, builder.secondaryColor)
            setColors(colors)
        } else if (builder.primaryColor != Color.TRANSPARENT) {
            val color = builder.primaryColor
            setColor(color)
        } else {
            val color = builder.secondaryColor
            setColor(color)
        }
        alpha = (builder.pressedAlpha * 255).toInt()
        // 设置边线
        setStroke(builder.borderWidth, builder.borderColor)
        if (builder.radiusTopLeft > 0 || builder.radiusTopRight > 0
            || builder.radiusBottomLeft > 0 || builder.radiusBottomRight > 0
        ) {
            val radii = floatArrayOf(
                builder.radiusTopLeft.toFloat(), builder.radiusTopLeft.toFloat(),
                builder.radiusTopRight.toFloat(), builder.radiusTopRight.toFloat(),
                builder.radiusBottomRight.toFloat(), builder.radiusBottomRight.toFloat(),
                builder.radiusBottomLeft.toFloat(), builder.radiusBottomLeft.toFloat()
            )
            cornerRadii = radii
            builder.isRadiusAdjustBounds = false
        } else {
            cornerRadius = builder.radius.toFloat()
            if (builder.radius > 0) {
                builder.isRadiusAdjustBounds = false
            }
        }
        setIsRadiusAdjustBounds(builder.isRadiusAdjustBounds)
    }

    override fun isStateful(): Boolean {
        return false
    }

    override fun onBoundsChange(r: Rect?) {
        super.onBoundsChange(r)
        if (mRadiusAdjustBounds && r != null) {
            // 修改圆角为短边的一半
            cornerRadius = min(r.width(), r.height()).toFloat() / 2
        }
    }

    /**
     * 圆角大小是否自适应为自身高度的一半
     */
    fun setIsRadiusAdjustBounds(isRadiusAdjustBounds: Boolean) {
        this.mRadiusAdjustBounds = isRadiusAdjustBounds
    }

    /**
     * 用于构建 PUIRoundDrawable
     */
    internal class Builder : Cloneable {

        var radius = 0
        var radiusTopLeft = 0
        var radiusTopRight = 0
        var radiusBottomLeft = 0
        var radiusBottomRight = 0
        var isRadiusAdjustBounds = true

        var gradientOrientation = Orientation.LEFT_RIGHT
        var primaryColor = Color.TRANSPARENT
        var secondaryColor = Color.TRANSPARENT
        var pressedAlpha = 1f

        var borderColor = Color.TRANSPARENT
        var borderWidth = 0

        fun build(): GradientDrawable {
            return PUIRoundDrawable(this)
        }

        fun copy() : Builder = this.clone() as Builder

    }

}