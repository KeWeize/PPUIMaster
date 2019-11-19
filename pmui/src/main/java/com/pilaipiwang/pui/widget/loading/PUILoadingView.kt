package com.pilaipiwang.pui.widget.loading

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.pilaipiwang.pui.R
import com.pilaipiwang.pui.utils.PUIDisplayHelper

/**
 * 加载控件
 * @author  vitar
 * @date    2019/11/15
 */
class PUILoadingView : View {

    private val TAG: String = "PUILoadingView"

    private var mSize: Int = 0
    private var mPaintColor: Int = 0
    private lateinit var mPaint: Paint
    private var mAnimateValue = 0
    // 动画插值器
    private var mAnimator: ValueAnimator? = null
    // 菊花叶数量
    private val LINE_COUNT = 10
    private val DEGREE_PER_LINE = 360 / LINE_COUNT

    private val mUpdateListener =
        ValueAnimator.AnimatorUpdateListener { animation ->
            mAnimateValue = animation.animatedValue as Int
            invalidate()
        }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttrs(context!!, attrs, defStyleAttr)
        initPaint()
    }

    /**
     * 初始化属性
     */
    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PUILoadingView, defStyleAttr, 0)
        mSize = ta.getDimensionPixelSize(
            R.styleable.PUILoadingView_pui_loading_view_size,
            PUIDisplayHelper.dp2px(context, 28)
        )
        mPaintColor = ta.getColor(
            R.styleable.PUILoadingView_android_color,
            ContextCompat.getColor(context, R.color.pui_config_color_gray_3)
        )
        ta.recycle()
    }

    /**
     * 初始化
     */
    private fun initPaint() {
        mPaint = Paint()
        mPaint.color = mPaintColor
        // 抗锯齿
        mPaint.isAntiAlias = true
        // 圆边切
        mPaint.strokeCap = Paint.Cap.ROUND
    }

    fun setColor(color: Int) {
        mPaintColor = color
        mPaint.color = color
        invalidate()
    }

    fun setSize(size: Int) {
        mSize = size
        requestLayout()
    }

    fun start() {
        if (mAnimator == null) {
            mAnimator = ValueAnimator.ofInt(0, LINE_COUNT - 1)
            mAnimator!!.addUpdateListener(mUpdateListener)
            mAnimator!!.duration = 600
            mAnimator!!.repeatMode = ValueAnimator.RESTART
            mAnimator!!.repeatCount = ValueAnimator.INFINITE
            mAnimator!!.interpolator = LinearInterpolator()
            mAnimator!!.start()
        } else if (!mAnimator!!.isStarted) {
            mAnimator!!.start()
        }
    }

    fun stop() {
        if (mAnimator != null) {
            mAnimator!!.removeUpdateListener(mUpdateListener)
            mAnimator!!.removeAllUpdateListeners()
            mAnimator!!.cancel()
            mAnimator = null
        }
    }

    private fun drawLoading(canvas: Canvas, degrees: Int) {
        val mWidth = mSize / 12
        val mHeight = mSize / 6
        mPaint.strokeWidth = mWidth.toFloat()

        canvas.rotate(degrees.toFloat(), (mSize / 2).toFloat(), (mSize / 2).toFloat())
        canvas.translate((mSize / 2).toFloat(), (mSize / 2).toFloat())

        for (i in 0 until LINE_COUNT) {
            canvas.rotate(DEGREE_PER_LINE.toFloat())
            mPaint.alpha = (255f * (i + 5) / LINE_COUNT).toInt()
            canvas.translate(0f, (mSize / 4 - mHeight / 2).toFloat())
            canvas.drawLine(0f, 0f, 0f, mHeight.toFloat(), mPaint)
            canvas.translate(0f, (-mSize / 4 + mHeight / 2).toFloat())
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(mSize, mSize)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val saveCount = canvas.saveLayer(RectF(0f, 0f, width.toFloat(), height.toFloat()), null)
        drawLoading(canvas, DEGREE_PER_LINE * mAnimateValue)
        canvas.restoreToCount(saveCount)
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stop()
    }

}