package com.pilaipiwang.pui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView

/**
 * 可以监听滚动事件的 ScrollView，并能在滚动回调中获取每次滚动前后的偏移量
 *
 * @author  vitar
 * @date    2019/11/18
 */
open class PUIObservableScrollView : ScrollView {

    private var mScrollOffset: Int = 0

    private val mOnScrollChangedListener: ArrayList<OnScrollChangedListener> = ArrayList()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        mScrollOffset = t
        if (mOnScrollChangedListener.isNotEmpty()) {
            for (listener in mOnScrollChangedListener) {
                listener.onScrollChanged(this, l, t, oldl, oldt)
            }
        }
    }

    fun addOnScrollChangedListener(onScrollChangedListener: OnScrollChangedListener?) {
        if (onScrollChangedListener == null
            || mOnScrollChangedListener.contains(onScrollChangedListener)
        ) {
            return
        }
        mOnScrollChangedListener.add(onScrollChangedListener)
    }

    fun removeOnScrollChangedListener(onScrollChangedListener: OnScrollChangedListener?) {
        if (onScrollChangedListener == null) {
            return
        }
        mOnScrollChangedListener.remove(onScrollChangedListener)
    }

    interface OnScrollChangedListener {
        fun onScrollChanged(
            scrollView: PUIObservableScrollView,
            l: Int,
            t: Int,
            oldl: Int,
            oldt: Int
        )
    }

}