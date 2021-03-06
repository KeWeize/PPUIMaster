package com.pilaipiwang.pui.layout

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.pilaipiwang.pui.R
import com.pilaipiwang.pui.alpha.PUIAlphaFrameLayout

/**
 * @author:  vitar5
 * @date:    2019/9/28
 */
open class PUIFrameLayout : PUIAlphaFrameLayout, IPUILayout {

    private lateinit var mLayoutHelper: PUILayoutHelper

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {

        val mBuilder = PUILayoutHelper.Builder(context, this)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.PUIFrameLayout, defStyleAttr, 0)
        val count = ta.indexCount
        for (i in 0 until count) {
            when (val index = ta.getIndex(i)) {
                // width height
                R.styleable.PUIFrameLayout_android_maxWidth ->
                    mBuilder.mWidthLimit = ta.getDimensionPixelSize(index, 0)
                R.styleable.PUIFrameLayout_android_maxHeight ->
                    mBuilder.mHeightLimit = ta.getDimensionPixelSize(index, 0)
                R.styleable.PUIFrameLayout_android_minWidth ->
                    mBuilder.mWidthMini = ta.getDimensionPixelSize(index, 0)
                R.styleable.PUIFrameLayout_android_minHeight ->
                    mBuilder.mHeightMini = ta.getDimensionPixelSize(index, 0)
                // top divider
                R.styleable.PUIFrameLayout_pui_topDividerColor ->
                    mBuilder.mTopDividerColor = ta.getColor(
                        index,
                        ContextCompat.getColor(context, R.color.pui_config_color_separator)
                    )
                R.styleable.PUIFrameLayout_pui_topDividerHeight ->
                    mBuilder.mTopDividerHeight = ta.getDimensionPixelSize(index, 0)
                R.styleable.PUIFrameLayout_pui_topDividerInsetLeft ->
                    mBuilder.mTopDividerInsetLeft = ta.getDimensionPixelSize(index, 0)
                R.styleable.PUIFrameLayout_pui_topDividerInsetRight ->
                    mBuilder.mTopDividerInsetRight = ta.getDimensionPixelSize(index, 0)
                // bottom divider
                R.styleable.PUIFrameLayout_pui_bottomDividerColor ->
                    mBuilder.mBottomDividerColor = ta.getColor(
                        index,
                        ContextCompat.getColor(context, R.color.pui_config_color_separator)
                    )
                R.styleable.PUIFrameLayout_pui_bottomDividerHeight ->
                    mBuilder.mBottomDividerHeight = ta.getDimensionPixelSize(index, 0)
                R.styleable.PUIFrameLayout_pui_bottomDividerInsetLeft ->
                    mBuilder.mBottomDividerInsetLeft = ta.getDimensionPixelSize(index, 0)
                R.styleable.PUIFrameLayout_pui_bottomDividerInsetRight ->
                    mBuilder.mBottomDividerInsetRight = ta.getDimensionPixelSize(index, 0)
                // left divider
                R.styleable.PUIFrameLayout_pui_leftDividerColor ->
                    mBuilder.mLeftDividerColor = ta.getColor(
                        index,
                        ContextCompat.getColor(context, R.color.pui_config_color_separator)
                    )
                R.styleable.PUIFrameLayout_pui_leftDividerWidth ->
                    mBuilder.mLeftDividerWidth = ta.getDimensionPixelSize(index, 0)
                R.styleable.PUIFrameLayout_pui_leftDividerInsetTop ->
                    mBuilder.mLeftDividerInsetTop = ta.getDimensionPixelSize(index, 0)
                R.styleable.PUIFrameLayout_pui_leftDividerInsetBottom ->
                    mBuilder.mLeftDividerInsetBottom = ta.getDimensionPixelSize(index, 0)
                // right divider
                R.styleable.PUIFrameLayout_pui_rightDividerColor ->
                    mBuilder.mRightDividerColor = ta.getColor(
                        index,
                        ContextCompat.getColor(context, R.color.pui_config_color_separator)
                    )
                R.styleable.PUIFrameLayout_pui_rightDividerWidth ->
                    mBuilder.mRightDividerWidth = ta.getDimensionPixelSize(index, 0)
                R.styleable.PUIFrameLayout_pui_rightDividerInsetTop ->
                    mBuilder.mRightDividerInsetTop = ta.getDimensionPixelSize(index, 0)
                R.styleable.PUIFrameLayout_pui_rightDividerInsetBottom ->
                    mBuilder.mRightDividerInsetBottom = ta.getDimensionPixelSize(index, 0)
                // border & radius & shadow
                R.styleable.PUIFrameLayout_pui_borderColor ->
                    mBuilder.mBorderColor = ta.getColor(index, 0)
                R.styleable.PUIFrameLayout_pui_borderWidth ->
                    mBuilder.mBorderWidth = ta.getDimensionPixelSize(index, 1)
                R.styleable.PUIFrameLayout_pui_radius ->
                    mBuilder.radius = ta.getDimensionPixelSize(index, 0)
                R.styleable.PUIFrameLayout_pui_outerNormalColor ->
                    mBuilder.mOuterNormalColor = ta.getColor(index, 0)
                R.styleable.PUIFrameLayout_pui_hideRadiusSide ->
                    mBuilder.mHideRadiusSide = ta.getInteger(index, HIDE_RADIUS_SIDE_NONE)
                R.styleable.PUIFrameLayout_pui_shadowElevation ->
                    mBuilder.shadow = ta.getDimensionPixelSize(index, 0)
                R.styleable.PUIFrameLayout_pui_shadowAlpha ->
                    mBuilder.mShadowAlpha = ta.getFloat(index, 0f)
                R.styleable.PUIFrameLayout_pui_useThemeGeneralShadowElevation ->
                    mBuilder.useThemeGeneralShadowElevation = ta.getBoolean(index, false)
            }
        }
        ta.recycle()

        // build
        mLayoutHelper = mBuilder.build()
        setChangeAlphaWhenPress(false)
        setChangeAlphaWhenDisable(false)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var mWidthMeasureSpec = mLayoutHelper.getMeasuredWidthSpec(widthMeasureSpec)
        var mHeightMeasureSpec = mLayoutHelper.getMeasuredHeightSpec(heightMeasureSpec)
        super.onMeasure(mWidthMeasureSpec, mHeightMeasureSpec)
        val minW = mLayoutHelper.handleMiniWidth(mWidthMeasureSpec, measuredWidth)
        val minH = mLayoutHelper.handleMiniHeight(mHeightMeasureSpec, measuredHeight)
        if (mWidthMeasureSpec != minW || mHeightMeasureSpec != minH) {
            super.onMeasure(minW, minH)
        }
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        if (canvas != null) {
            mLayoutHelper.drawDividers(canvas, width, height)
            mLayoutHelper.dispatchRoundBorderDraw(canvas)
        }
    }

    override fun setWidthLimit(widthLimit: Int): Boolean {
        if (mLayoutHelper.setWidthLimit(widthLimit)) {
            requestLayout()
            invalidate()
        }
        return true
    }

    override fun setHeightLimit(heightLimit: Int): Boolean {
        if (mLayoutHelper.setHeightLimit(heightLimit)) {
            requestLayout()
            invalidate()
        }
        return true
    }

    override fun setUseThemeGeneralShadowElevation() =
        mLayoutHelper.setUseThemeGeneralShadowElevation()

    override fun setShadowElevation(elevation: Int) =
        mLayoutHelper.setShadowElevation(elevation)

    override fun getShadowElevation(): Int = mLayoutHelper.getShadowElevation()

    override fun setShadowAlpha(shadowAlpha: Float) = mLayoutHelper.setShadowAlpha(shadowAlpha)

    override fun getShadowAlpha(): Float = mLayoutHelper.getShadowAlpha()

    override fun setShadowColor(shadowColor: Int) = mLayoutHelper.setShadowColor(shadowColor)

    override fun getShadowColor(): Int = mLayoutHelper.getShadowColor()

    override fun setRadius(radius: Int) = mLayoutHelper.setRadius(radius)

    override fun setRadius(radius: Int, hideRadiusSide: Int) =
        mLayoutHelper.setRadius(radius, hideRadiusSide)

    override fun getRadius(): Int = mLayoutHelper.getRadius()

    override fun setHideRadiusSide(hideRadiusSide: Int) =
        mLayoutHelper.setHideRadiusSide(hideRadiusSide)

    override fun getHideRadiusSide(): Int = mLayoutHelper.getHideRadiusSide()

    override fun setRadiusAndShadow(radius: Int, shadowElevation: Int, shadowAlpha: Float) =
        mLayoutHelper.setRadiusAndShadow(radius, shadowElevation, shadowAlpha)

    override fun setRadiusAndShadow(
        radius: Int,
        hideRadiusSide: Int,
        shadowElevation: Int,
        shadowAlpha: Float
    ) = mLayoutHelper.setRadiusAndShadow(radius, hideRadiusSide, shadowElevation, shadowAlpha)

    override fun setRadiusAndShadow(
        radius: Int,
        hideRadiusSide: Int,
        shadowElevation: Int,
        shadowColor: Int,
        shadowAlpha: Float
    ) = mLayoutHelper.setRadiusAndShadow(
        radius,
        hideRadiusSide,
        shadowElevation,
        shadowColor,
        shadowAlpha
    )

    override fun setBorderColor(borderColor: Int) {
        mLayoutHelper.setBorderColor(borderColor)
        invalidate()
    }

    override fun setBorderWidth(borderWidth: Int) {
        mLayoutHelper.setBorderWidth(borderWidth)
        invalidate()
    }

    override fun updateTopDivider(
        topInsetLeft: Int,
        topInsetRight: Int,
        topDividerHeight: Int,
        topDividerColor: Int
    ) {
        mLayoutHelper.updateTopDivider(
            topInsetLeft,
            topInsetRight,
            topDividerHeight,
            topDividerColor
        )
        invalidate()
    }

    override fun updateBottomDivider(
        bottomInsetLeft: Int,
        bottomInsetRight: Int,
        bottomDividerHeight: Int,
        bottomDividerColor: Int
    ) {
        mLayoutHelper.updateBottomDivider(
            bottomInsetLeft,
            bottomInsetRight,
            bottomDividerHeight,
            bottomDividerColor
        )
        invalidate()
    }

    override fun updateLeftDivider(
        leftInsetTop: Int,
        leftInsetBottom: Int,
        leftDividerWidth: Int,
        leftDividerColor: Int
    ) {
        mLayoutHelper.updateLeftDivider(
            leftInsetTop,
            leftInsetBottom,
            leftDividerWidth,
            leftDividerColor
        )
        invalidate()
    }

    override fun updateRightDivider(
        rightInsetTop: Int,
        rightInsetBottom: Int,
        rightDividerWidth: Int,
        rightDividerColor: Int
    ) {
        mLayoutHelper.updateRightDivider(
            rightInsetTop,
            rightInsetBottom,
            rightDividerWidth,
            rightDividerColor
        )
        invalidate()
    }

    override fun onlyShowTopDivider(
        topInsetLeft: Int,
        topInsetRight: Int,
        topDividerHeight: Int,
        topDividerColor: Int
    ) {
        mLayoutHelper.onlyShowTopDivider(
            topInsetLeft,
            topInsetRight,
            topDividerHeight,
            topDividerColor
        )
        invalidate()
    }

    override fun onlyShowBottomDivider(
        bottomInsetLeft: Int,
        bottomInsetRight: Int,
        bottomDividerHeight: Int,
        bottomDividerColor: Int
    ) {
        mLayoutHelper.onlyShowBottomDivider(
            bottomInsetLeft,
            bottomInsetRight,
            bottomDividerHeight,
            bottomDividerColor
        )
        invalidate()
    }

    override fun onlyShowLeftDivider(
        leftInsetTop: Int,
        leftInsetBottom: Int,
        leftDividerWidth: Int,
        leftDividerColor: Int
    ) {
        mLayoutHelper.onlyShowLeftDivider(
            leftInsetTop,
            leftInsetBottom,
            leftDividerWidth,
            leftDividerColor
        )
        invalidate()
    }

    override fun onlyShowRightDivider(
        rightInsetTop: Int,
        rightInsetBottom: Int,
        rightDividerWidth: Int,
        rightDividerColor: Int
    ) {
        mLayoutHelper.onlyShowRightDivider(
            rightInsetTop,
            rightInsetBottom,
            rightDividerWidth,
            rightDividerColor
        )
        invalidate()
    }

    override fun setTopDividerAlpha(dividerAlpha: Int) {
        mLayoutHelper.setTopDividerAlpha(dividerAlpha)
        invalidate()
    }

    override fun setBottomDividerAlpha(dividerAlpha: Int) {
        mLayoutHelper.setBottomDividerAlpha(dividerAlpha)
        invalidate()
    }

    override fun setLeftDividerAlpha(dividerAlpha: Int) {
        mLayoutHelper.setLeftDividerAlpha(dividerAlpha)
        invalidate()
    }

    override fun setRightDividerAlpha(dividerAlpha: Int) {
        mLayoutHelper.setRightDividerAlpha(dividerAlpha)
        invalidate()
    }

    override fun setOuterNormalColor(color: Int) = mLayoutHelper.setOuterNormalColor(color)

}