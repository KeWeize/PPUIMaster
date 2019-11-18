package com.pilaipiwang.pui.widget

import android.content.Context
import android.util.AttributeSet

/**
 * 高度随内容自适应，但被 maxHeight 限制的ScrollView
 * @author  vitar
 * @date    2019/11/18
 */
class PUIWrapContentScrollView : PUIObservableScrollView {

    private var mMaxHeight = Integer.MAX_VALUE shr 2

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(context: Context?, mMaxHeight: Int) : super(context) {
        this.mMaxHeight = mMaxHeight
    }

    fun setMaxHeight(maxHeight: Int) {
        if (mMaxHeight != maxHeight) {
            mMaxHeight = maxHeight
            requestLayout()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val expandSpec = if (layoutParams.height in 1..mMaxHeight) {
            MeasureSpec.makeMeasureSpec(layoutParams.height, MeasureSpec.EXACTLY)
        } else {
            MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST)
        }
        super.onMeasure(widthMeasureSpec, expandSpec)
    }
}