package com.pilaipiwang.pui.widget.dialog

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.pilaipiwang.pui.R
import com.pilaipiwang.pui.layout.PUILinearLayout
import com.pilaipiwang.pui.utils.PUIAttrsHelper

/**
 * @author  vitar
 * @date    2019/11/15
 */
class PUIDialogView : PUILinearLayout {

    private var mMinWidth: Int = 0
    private var mMaxWidth: Int = 0
    private var mOnDecorationListener: OnDecorationListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mMinWidth = PUIAttrsHelper.getAttrDimen(context, R.attr.pui_dialog_min_width)
        mMaxWidth = PUIAttrsHelper.getAttrDimen(context, R.attr.pui_dialog_max_width)
    }

    fun setOnDecorationListener(onDecorationListener: OnDecorationListener?) {
        mOnDecorationListener = onDecorationListener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var mWidthMeasureSpec = widthMeasureSpec
        val widthSize = MeasureSpec.getSize(mWidthMeasureSpec)
        val widthMode = MeasureSpec.getMode(mWidthMeasureSpec)
        if (mMaxWidth > 0 && widthSize > mMaxWidth) {
            mWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxWidth, widthMode)
        }
        super.onMeasure(mWidthMeasureSpec, heightMeasureSpec)
        if (widthMode == MeasureSpec.AT_MOST) {
            val measureWidth = measuredWidth
            if (measureWidth < mMinWidth && mMinWidth < widthSize) {
                mWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mMinWidth, MeasureSpec.EXACTLY)
                super.onMeasure(mWidthMeasureSpec, heightMeasureSpec)
            }
        }
    }

    override fun measureChildWithMargins(
        child: View, parentWidthMeasureSpec: Int,
        widthUsed: Int, parentHeightMeasureSpec: Int, heightUsed: Int
    ) {
        super.measureChildWithMargins(
            child,
            parentWidthMeasureSpec,
            widthUsed,
            parentHeightMeasureSpec,
            heightUsed
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            mOnDecorationListener?.onDraw(canvas, this)
        }
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        if (canvas != null) {
            mOnDecorationListener?.onDrawOver(canvas, this)
        }
    }

    fun setMinWidth(minWidth: Int) {
        mMinWidth = minWidth
    }

    fun setMaxWidth(maxWidth: Int) {
        mMaxWidth = maxWidth
    }

    interface OnDecorationListener {
        fun onDraw(canvas: Canvas, view: PUIDialogView)
        fun onDrawOver(canvas: Canvas, view: PUIDialogView)
    }

}