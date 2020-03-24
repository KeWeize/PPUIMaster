package com.pilaipiwang.pui.layout

import android.content.Context
import android.graphics.*
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import com.pilaipiwang.pui.R
import com.pilaipiwang.pui.utils.PUIAttrsHelper
import java.lang.ref.WeakReference
import kotlin.math.floor

/**
 * @author:  vitar
 * @date:    2019/9/28
 */
internal class PUILayoutHelper : IPUILayout {

    private var mContext: Context

    // size
    private var mWidthLimit = 0
    private var mHeightLimit = 0
    private var mWidthMini = 0
    private var mHeightMini = 0

    // divider
    private var mTopDividerHeight = 0
    private var mTopDividerInsetLeft = 0
    private var mTopDividerInsetRight = 0
    private var mTopDividerColor: Int = Color.TRANSPARENT
    private var mTopDividerAlpha = 255

    private var mBottomDividerHeight = 0
    private var mBottomDividerInsetLeft = 0
    private var mBottomDividerInsetRight = 0
    private var mBottomDividerColor: Int = Color.TRANSPARENT
    private var mBottomDividerAlpha = 255

    private var mLeftDividerWidth = 0
    private var mLeftDividerInsetTop = 0
    private var mLeftDividerInsetBottom = 0
    private var mLeftDividerColor = Color.TRANSPARENT
    private var mLeftDividerAlpha = 255

    private var mRightDividerWidth = 0
    private var mRightDividerInsetTop = 0
    private var mRightDividerInsetBottom = 0
    private var mRightDividerColor = Color.TRANSPARENT
    private var mRightDividerAlpha = 255

    private var mDividerPaint: Paint? = null

    // round
    private var mClipPaint: Paint
    private var mMode: PorterDuffXfermode
    private var mRadiusArray: FloatArray? = null
    private var mBorderRect: RectF
    private var mOwner: WeakReference<View>
    private var mPath = Path()
    private var mRadius = 0
    @HideRadiusSide
    private var mHideRadiusSide = HIDE_RADIUS_SIDE_NONE
    private var mBorderColor = Color.TRANSPARENT
    private var mBorderWidth = 1
    private var mOuterNormalColor = Color.TRANSPARENT

    // shadow
    private var mShadowElevation = 0
    private var mShadowAlpha = 0f
    private var mShadowColor = Color.BLACK

    private constructor(context: Context, builder: Builder, owner: View) {
        mContext = context
        mOwner = WeakReference(owner)
        mMode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        mClipPaint = Paint()
        mClipPaint.isAntiAlias = true

        mBorderRect = RectF()

        var radius = 0
        var shadow = 0
        var useThemeGeneralShadowElevation = false

        mWidthLimit = builder.mWidthLimit
        mHeightLimit = builder.mHeightLimit
        mWidthMini = builder.mWidthMini
        mHeightMini = builder.mHeightMini
        mTopDividerColor = builder.mTopDividerColor
        mTopDividerHeight = builder.mTopDividerHeight
        mTopDividerInsetLeft = builder.mTopDividerInsetLeft
        mTopDividerInsetRight = builder.mTopDividerInsetRight
        mBottomDividerColor = builder.mBottomDividerColor
        mBottomDividerHeight = builder.mBottomDividerHeight
        mBottomDividerInsetLeft = builder.mBottomDividerInsetLeft
        mBottomDividerInsetRight = builder.mBottomDividerInsetRight
        mLeftDividerColor = builder.mLeftDividerColor
        mLeftDividerWidth = builder.mLeftDividerWidth
        mLeftDividerInsetTop = builder.mLeftDividerInsetTop
        mLeftDividerInsetBottom = builder.mLeftDividerInsetBottom
        mRightDividerColor = builder.mRightDividerColor
        mRightDividerWidth = builder.mRightDividerWidth
        mRightDividerInsetTop = builder.mRightDividerInsetTop
        mRightDividerInsetBottom = builder.mRightDividerInsetBottom
        mBorderColor = builder.mBorderColor
        mBorderWidth = builder.mBorderWidth
        radius = builder.radius
        mOuterNormalColor = builder.mOuterNormalColor
        mHideRadiusSide = builder.mHideRadiusSide
        shadow = builder.shadow
        mShadowAlpha = builder.mShadowAlpha
        useThemeGeneralShadowElevation = builder.useThemeGeneralShadowElevation

        if (shadow == 0 && useThemeGeneralShadowElevation) {
            shadow = PUIAttrsHelper.getAttrDimen(context, R.attr.pui_general_shadow_elevation)
            mShadowAlpha = PUIAttrsHelper.getAttrFloatValue(context, R.attr.pui_general_shadow_alpha)
        }
        setRadiusAndShadow(radius, mHideRadiusSide, shadow, mShadowAlpha)
    }


    override fun setWidthLimit(widthLimit: Int): Boolean {
        if (mWidthLimit != widthLimit) {
            mWidthLimit = widthLimit
            return true
        }
        return false
    }

    override fun setHeightLimit(heightLimit: Int): Boolean {
        if (mHeightLimit != heightLimit) {
            mHeightLimit = heightLimit
            return true
        }
        return false
    }

    override fun setUseThemeGeneralShadowElevation() {
        mShadowElevation =
            PUIAttrsHelper.getAttrDimen(mContext, R.attr.pui_general_shadow_elevation)
        setRadiusAndShadow(mRadius, mHideRadiusSide, mShadowElevation, mShadowAlpha)
    }

    override fun setShadowElevation(elevation: Int) {
        if (mShadowElevation == elevation) {
            return
        }
        mShadowElevation = elevation
        invalidate()
    }

    override fun getShadowElevation(): Int = mShadowElevation

    override fun setShadowAlpha(shadowAlpha: Float) {
        if (mShadowAlpha == shadowAlpha) {
            return
        }
        mShadowAlpha = shadowAlpha
        invalidate()
    }

    override fun getShadowAlpha(): Float = mShadowAlpha

    override fun setShadowColor(shadowColor: Int) {
        if (mShadowColor == shadowColor) {
            return
        }
        mShadowColor = shadowColor
        setShadowColorInner(mShadowColor)
    }

    override fun getShadowColor(): Int = mShadowColor

    override fun setRadius(radius: Int) {
        if (mRadius != radius) {
            setRadiusAndShadow(radius, mShadowElevation, mShadowAlpha)
        }
    }

    override fun setRadius(radius: Int, hideRadiusSide: Int) {
        if (mRadius == radius && hideRadiusSide == mHideRadiusSide) {
            return
        }
        setRadiusAndShadow(radius, hideRadiusSide, mShadowElevation, mShadowAlpha)
    }

    override fun getRadius(): Int = mRadius

    override fun setHideRadiusSide(hideRadiusSide: Int) {
        if (mHideRadiusSide == hideRadiusSide) {
            return
        }
        setRadiusAndShadow(mRadius, hideRadiusSide, mShadowElevation, mShadowAlpha)
    }

    override fun getHideRadiusSide(): Int = mHideRadiusSide

    override fun setRadiusAndShadow(radius: Int, shadowElevation: Int, shadowAlpha: Float) {
        setRadiusAndShadow(radius, mHideRadiusSide, shadowElevation, shadowAlpha)
    }

    override fun setRadiusAndShadow(
        radius: Int,
        hideRadiusSide: Int,
        shadowElevation: Int,
        shadowAlpha: Float
    ) {
        setRadiusAndShadow(radius, hideRadiusSide, shadowElevation, mShadowColor, shadowAlpha)
    }


    override fun setRadiusAndShadow(
        radius: Int,
        hideRadiusSide: Int,
        shadowElevation: Int,
        shadowColor: Int,
        shadowAlpha: Float
    ) {
        val owner = mOwner.get() ?: return

        mRadius = radius
        mHideRadiusSide = hideRadiusSide

        if (mRadius > 0) {
            val mRadiusFloat: Float = mRadius.toFloat()
            when (hideRadiusSide) {
                HIDE_RADIUS_SIDE_TOP ->
                    mRadiusArray =
                        floatArrayOf(
                            0f,
                            0f,
                            0f,
                            0f,
                            mRadiusFloat,
                            mRadiusFloat,
                            mRadiusFloat,
                            mRadiusFloat
                        )

                HIDE_RADIUS_SIDE_RIGHT ->
                    mRadiusArray =
                        floatArrayOf(
                            mRadiusFloat,
                            mRadiusFloat,
                            0f,
                            0f,
                            0f,
                            0f,
                            mRadiusFloat,
                            mRadiusFloat
                        )

                HIDE_RADIUS_SIDE_BOTTOM ->
                    mRadiusArray =
                        floatArrayOf(
                            mRadiusFloat,
                            mRadiusFloat,
                            mRadiusFloat,
                            mRadiusFloat,
                            0f,
                            0f,
                            0f,
                            0f
                        )

                HIDE_RADIUS_SIDE_LEFT ->
                    mRadiusArray =
                        floatArrayOf(
                            0f,
                            0f,
                            mRadiusFloat,
                            mRadiusFloat,
                            mRadiusFloat,
                            mRadiusFloat,
                            0f,
                            0f
                        )

                else ->
                    mRadiusArray = null
            }

        }

        mShadowElevation = shadowElevation
        mShadowAlpha = shadowAlpha
        mShadowColor = shadowColor
        if (useFeature()) {
            if (mShadowElevation == 0 || isRadiusWithSideHidden()) {
                owner.elevation = 0f
            } else {
                owner.elevation = mShadowElevation.toFloat()
            }
            setShadowColorInner(mShadowColor)

            owner.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View?, outline: Outline?) {
                    if (view == null) {
                        return
                    }
                    val w = view.width
                    val h = view.height
                    if (w == 0 || h == 0) {
                        return
                    }
                    if (isRadiusWithSideHidden()) {
                        var left = 0
                        var top = 0
                        var right = w
                        var bottom = h
                        when (mHideRadiusSide) {
                            HIDE_RADIUS_SIDE_LEFT -> left -= mRadius
                            HIDE_RADIUS_SIDE_TOP -> top -= mRadius
                            HIDE_RADIUS_SIDE_RIGHT -> right += mRadius
                            HIDE_RADIUS_SIDE_BOTTOM -> bottom += mRadius
                        }
                        outline?.setRoundRect(
                            left, top,
                            right, bottom, mRadius.toFloat()
                        )
                        return
                    }

                    var top = 0
                    var bottom = Math.max(top + 1, h)
                    var left = 0
                    var right = w

                    var shadowAlpha = mShadowAlpha
                    if (mShadowElevation == 0) {
                        // outline.setAlpha will work even if shadowElevation == 0
                        shadowAlpha = 1f
                    }

                    outline?.alpha = shadowAlpha

                    if (mRadius <= 0) {
                        outline?.setRect(left, top, right, bottom)
                    } else {
                        outline?.setRoundRect(left, top, right, bottom, mRadius.toFloat())
                    }
                }
            }
            owner.clipToOutline = mRadius > 0
        }
        owner.invalidate()

    }

    override fun setBorderColor(borderColor: Int) {
        this.mBorderColor = borderColor
    }

    override fun setBorderWidth(borderWidth: Int) {
        this.mBorderWidth = borderWidth
    }

    override fun updateTopDivider(
        topInsetLeft: Int,
        topInsetRight: Int,
        topDividerHeight: Int,
        topDividerColor: Int
    ) {
        mTopDividerInsetLeft = topInsetLeft
        mTopDividerInsetRight = topInsetRight
        mTopDividerHeight = topDividerHeight
        mTopDividerColor = topDividerColor
    }

    override fun updateBottomDivider(
        bottomInsetLeft: Int,
        bottomInsetRight: Int,
        bottomDividerHeight: Int,
        bottomDividerColor: Int
    ) {
        mBottomDividerInsetLeft = bottomInsetLeft
        mBottomDividerInsetRight = bottomInsetRight
        mBottomDividerColor = bottomDividerColor
        mBottomDividerHeight = bottomDividerHeight
    }

    override fun updateLeftDivider(
        leftInsetTop: Int,
        leftInsetBottom: Int,
        leftDividerWidth: Int,
        leftDividerColor: Int
    ) {
        mLeftDividerInsetTop = leftInsetTop
        mLeftDividerInsetBottom = leftInsetBottom
        mLeftDividerWidth = leftDividerWidth
        mLeftDividerColor = leftDividerColor
    }

    override fun updateRightDivider(
        rightInsetTop: Int,
        rightInsetBottom: Int,
        rightDividerWidth: Int,
        rightDividerColor: Int
    ) {
        mRightDividerInsetTop = rightInsetTop
        mRightDividerInsetBottom = rightInsetBottom
        mRightDividerWidth = rightDividerWidth
        mRightDividerColor = rightDividerColor
    }

    override fun onlyShowTopDivider(
        topInsetLeft: Int,
        topInsetRight: Int,
        topDividerHeight: Int,
        topDividerColor: Int
    ) {
        updateTopDivider(topInsetLeft, topInsetRight, topDividerHeight, topDividerColor)
        mLeftDividerWidth = 0
        mRightDividerWidth = 0
        mBottomDividerHeight = 0
    }

    override fun onlyShowBottomDivider(
        bottomInsetLeft: Int,
        bottomInsetRight: Int,
        bottomDividerHeight: Int,
        bottomDividerColor: Int
    ) {
        updateBottomDivider(
            bottomInsetLeft,
            bottomInsetRight,
            bottomDividerHeight,
            bottomDividerColor
        )
        mLeftDividerWidth = 0
        mRightDividerWidth = 0
        mTopDividerHeight = 0
    }

    override fun onlyShowLeftDivider(
        leftInsetTop: Int,
        leftInsetBottom: Int,
        leftDividerWidth: Int,
        leftDividerColor: Int
    ) {
        updateLeftDivider(leftInsetTop, leftInsetBottom, leftDividerWidth, leftDividerColor)
        mRightDividerWidth = 0
        mTopDividerHeight = 0
        mBottomDividerHeight = 0
    }

    override fun onlyShowRightDivider(
        rightInsetTop: Int,
        rightInsetBottom: Int,
        rightDividerWidth: Int,
        rightDividerColor: Int
    ) {
        updateRightDivider(rightInsetTop, rightInsetBottom, rightDividerWidth, rightDividerColor);
        mLeftDividerWidth = 0;
        mTopDividerHeight = 0;
        mBottomDividerHeight = 0;
    }

    override fun setTopDividerAlpha(dividerAlpha: Int) {
        this.mTopDividerAlpha = dividerAlpha
    }

    override fun setBottomDividerAlpha(dividerAlpha: Int) {
        this.mBottomDividerAlpha = dividerAlpha

    }

    override fun setLeftDividerAlpha(dividerAlpha: Int) {
        this.mLeftDividerAlpha = dividerAlpha
    }

    override fun setRightDividerAlpha(dividerAlpha: Int) {
        this.mRightDividerAlpha = dividerAlpha
    }

    override fun setOuterNormalColor(color: Int) {
        mOuterNormalColor = color
        mOwner.get()?.invalidate()
    }

    private fun isRadiusWithSideHidden(): Boolean {
        return mRadius > 0 && mHideRadiusSide != HIDE_RADIUS_SIDE_NONE
    }

    private fun setShadowColorInner(shadowColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val owner = mOwner.get() ?: return
            owner.outlineAmbientShadowColor = shadowColor
            owner.outlineSpotShadowColor = shadowColor
        }
    }

    /**
     * 版本大于 21
     */
    private fun useFeature(): Boolean {
        return Build.VERSION.SDK_INT >= 21
    }

    private fun invalidate() {
        if (useFeature()) {
            val owner = mOwner.get() ?: return
            if (mShadowElevation == 0) {
                owner.elevation = 0f
            } else {
                owner.elevation = mShadowElevation.toFloat()
            }
            owner.invalidateOutline()
        }
    }

    /**
     * 绘制分割线
     *
     * @param canvas
     * @param w
     * @param h
     */
    fun drawDividers(canvas: Canvas, w: Int, h: Int) {
        if (mDividerPaint == null && (mTopDividerHeight > 0 || mBottomDividerHeight > 0 || mLeftDividerWidth > 0 || mRightDividerWidth > 0)) {
            mDividerPaint = Paint()
        }
        if (mTopDividerHeight > 0) {
            mDividerPaint!!.setStrokeWidth(mTopDividerHeight.toFloat())
            mDividerPaint!!.setColor(mTopDividerColor)
            if (mTopDividerAlpha < 255) {
                mDividerPaint!!.setAlpha(mTopDividerAlpha)
            }
            val y = mTopDividerHeight * 1f / 2
            canvas.drawLine(
                mTopDividerInsetLeft.toFloat(),
                y,
                (w - mTopDividerInsetRight).toFloat(),
                y,
                mDividerPaint!!
            )
        }

        if (mBottomDividerHeight > 0) {
            mDividerPaint!!.setStrokeWidth(mBottomDividerHeight.toFloat())
            mDividerPaint!!.setColor(mBottomDividerColor)
            if (mBottomDividerAlpha < 255) {
                mDividerPaint!!.alpha = mBottomDividerAlpha
            }
            val y = floor((h - mBottomDividerHeight * 1f / 2).toDouble()).toFloat()
            canvas.drawLine(
                mBottomDividerInsetLeft.toFloat(),
                y,
                (w - mBottomDividerInsetRight).toFloat(),
                y,
                mDividerPaint!!
            )
        }

        if (mLeftDividerWidth > 0) {
            mDividerPaint!!.strokeWidth = mLeftDividerWidth.toFloat()
            mDividerPaint!!.color = mLeftDividerColor
            if (mLeftDividerAlpha < 255) {
                mDividerPaint!!.alpha = mLeftDividerAlpha
            }
            canvas.drawLine(
                0f,
                mLeftDividerInsetTop.toFloat(),
                0f,
                (h - mLeftDividerInsetBottom).toFloat(),
                mDividerPaint!!
            )
        }

        if (mRightDividerWidth > 0) {
            mDividerPaint!!.strokeWidth = mRightDividerWidth.toFloat()
            mDividerPaint!!.color = mRightDividerColor
            if (mRightDividerAlpha < 255) {
                mDividerPaint!!.alpha = mRightDividerAlpha
            }
            canvas.drawLine(
                w.toFloat(),
                mRightDividerInsetTop.toFloat(),
                w.toFloat(),
                (h - mRightDividerInsetBottom).toFloat(),
                mDividerPaint!!
            )
        }
    }

    /**
     * 绘制圆角
     *
     * @param canvas
     */
    fun dispatchRoundBorderDraw(canvas: Canvas) {
        val owner = mOwner.get() ?: return
        if (mBorderColor == 0 && (mRadius == 0 || mOuterNormalColor == 0)) {
            return
        }

        val width = canvas.width
        val height = canvas.height
        // react
        mBorderRect.set(1f, 1f, (width - 1).toFloat(), (height - 1).toFloat())

        if (mRadius == 0 || !useFeature() && mOuterNormalColor == 0) {
            mClipPaint.style = Paint.Style.STROKE
            mClipPaint.color = mBorderColor
            canvas.drawRect(mBorderRect, mClipPaint)
            return
        }

        // 圆角矩形
        if (!useFeature()) {
            val layerId = canvas.saveLayer(
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                null,
                Canvas.ALL_SAVE_FLAG
            )
            canvas.drawColor(mOuterNormalColor)
            mClipPaint.color = mOuterNormalColor
            mClipPaint.style = Paint.Style.FILL
            mClipPaint.xfermode = mMode
            if (mRadiusArray == null) {
                canvas.drawRoundRect(mBorderRect, mRadius.toFloat(), mRadius.toFloat(), mClipPaint)
            } else {
                drawRoundRect(canvas, mBorderRect, mRadiusArray!!, mClipPaint)
            }
            mClipPaint.xfermode = null
            canvas.restoreToCount(layerId)
        }

        mClipPaint.color = mBorderColor
        mClipPaint.strokeWidth = mBorderWidth.toFloat()
        mClipPaint.style = Paint.Style.STROKE
        if (mRadiusArray == null) {
            canvas.drawRoundRect(mBorderRect, mRadius.toFloat(), mRadius.toFloat(), mClipPaint)
        } else {
            drawRoundRect(canvas, mBorderRect, mRadiusArray!!, mClipPaint)
        }
    }

    private fun drawRoundRect(canvas: Canvas, rect: RectF, radiusArray: FloatArray, paint: Paint) {
        mPath.reset()
        mPath.addRoundRect(rect, radiusArray, Path.Direction.CW)
        canvas.drawPath(mPath, paint)
    }

    internal fun getMeasuredWidthSpec(widthMeasureSpec: Int): Int {
        var widthMeasureSpec = widthMeasureSpec
        if (mWidthLimit > 0) {
            val size = View.MeasureSpec.getSize(widthMeasureSpec)
            if (size > mWidthLimit) {
                val mode = View.MeasureSpec.getMode(widthMeasureSpec)
                if (mode == View.MeasureSpec.AT_MOST) {
                    widthMeasureSpec =
                        View.MeasureSpec.makeMeasureSpec(mWidthLimit, View.MeasureSpec.AT_MOST)
                } else {
                    widthMeasureSpec =
                        View.MeasureSpec.makeMeasureSpec(mWidthLimit, View.MeasureSpec.EXACTLY)
                }

            }
        }
        return widthMeasureSpec
    }

    internal fun getMeasuredHeightSpec(heightMeasureSpec: Int): Int {
        var heightMeasureSpec = heightMeasureSpec
        if (mHeightLimit > 0) {
            val size = View.MeasureSpec.getSize(heightMeasureSpec)
            if (size > mHeightLimit) {
                val mode = View.MeasureSpec.getMode(heightMeasureSpec)
                if (mode == View.MeasureSpec.AT_MOST) {
                    heightMeasureSpec =
                        View.MeasureSpec.makeMeasureSpec(mWidthLimit, View.MeasureSpec.AT_MOST)
                } else {
                    heightMeasureSpec =
                        View.MeasureSpec.makeMeasureSpec(mWidthLimit, View.MeasureSpec.EXACTLY)
                }
            }
        }
        return heightMeasureSpec
    }

    internal fun handleMiniWidth(widthMeasureSpec: Int, measuredWidth: Int): Int {
        return if (View.MeasureSpec.getMode(widthMeasureSpec) != View.MeasureSpec.EXACTLY && measuredWidth < mWidthMini) {
            View.MeasureSpec.makeMeasureSpec(mWidthMini, View.MeasureSpec.EXACTLY)
        } else widthMeasureSpec
    }

    fun handleMiniHeight(heightMeasureSpec: Int, measuredHeight: Int): Int {
        return if (View.MeasureSpec.getMode(heightMeasureSpec) != View.MeasureSpec.EXACTLY && measuredHeight < mHeightMini) {
            View.MeasureSpec.makeMeasureSpec(mHeightMini, View.MeasureSpec.EXACTLY)
        } else heightMeasureSpec
    }


    /**
     * 构建类
     */
    internal class Builder {

        private var context: Context
        private var owner: View

        internal var mWidthLimit = 0
        internal var mWidthMini = 0
        internal var mHeightLimit = 0
        internal var mHeightMini = 0

        internal var mTopDividerColor = Color.TRANSPARENT
        internal var mTopDividerHeight = 0
        internal var mTopDividerInsetLeft = 0
        internal var mTopDividerInsetRight = 0

        internal var mBottomDividerColor = Color.TRANSPARENT
        internal var mBottomDividerHeight = 0
        internal var mBottomDividerInsetLeft = 0
        internal var mBottomDividerInsetRight = 0

        internal var mLeftDividerColor = Color.TRANSPARENT
        internal var mLeftDividerWidth = 0
        internal var mLeftDividerInsetTop = 0
        internal var mLeftDividerInsetBottom = 0

        internal var mRightDividerColor = Color.TRANSPARENT
        internal var mRightDividerWidth = 0
        internal var mRightDividerInsetTop = 0
        internal var mRightDividerInsetBottom = 0

        internal var mBorderColor = Color.TRANSPARENT
        internal var mBorderWidth = 0
        internal var radius = 0
        internal var mOuterNormalColor = Color.TRANSPARENT
        internal var mHideRadiusSide = HIDE_RADIUS_SIDE_NONE
        internal var shadow = 0
        internal var mShadowAlpha = 0f
        internal var useThemeGeneralShadowElevation = false

        constructor(context: Context, owner: View) {
            this.context = context
            this.owner = owner
        }

        internal fun build(): PUILayoutHelper {
            return PUILayoutHelper(context, this, owner)
        }

        override fun toString(): String {
            return "Builder(mWidthLimit=$mWidthLimit, mWidthMini=$mWidthMini," +
                    " mHeightLimit=$mHeightLimit, mHeightMini=$mHeightMini," +
                    " mTopDividerColor=$mTopDividerColor, mTopDividerHeight=$mTopDividerHeight," +
                    " mTopDividerInsetLeft=$mTopDividerInsetLeft, mTopDividerInsetRight=$mTopDividerInsetRight, " +
                    "mBottomDividerColor=$mBottomDividerColor, mBottomDividerHeight=$mBottomDividerHeight, " +
                    "mBottomDividerInsetLeft=$mBottomDividerInsetLeft, mBottomDividerInsetRight=$mBottomDividerInsetRight, " +
                    "mLeftDividerColor=$mLeftDividerColor, mLeftDividerWidth=$mLeftDividerWidth, mLeftDividerInsetTop=$mLeftDividerInsetTop," +
                    " mLeftDividerInsetBottom=$mLeftDividerInsetBottom, mRightDividerColor=$mRightDividerColor, " +
                    "mRightDividerWidth=$mRightDividerWidth, mRightDividerInsetTop=$mRightDividerInsetTop, " +
                    "mRightDividerInsetBottom=$mRightDividerInsetBottom, mBorderColor=$mBorderColor, " +
                    "mBorderWidth=$mBorderWidth, radius=$radius, mOuterNormalColor=$mOuterNormalColor," +
                    " mHideRadiusSide=$mHideRadiusSide," +
                    "shadow=$shadow, mShadowAlpha=$mShadowAlpha, useThemeGeneralShadowElevation=$useThemeGeneralShadowElevation"
        }

    }

}