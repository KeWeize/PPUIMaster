package com.pilaipiwang.pui.widget.multilayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

/**
 * @author  vitar
 * @date    2019/12/17
 */
internal abstract class PUIAbstractMultiStatesView : FrameLayout {

    companion object {
        /**
         * 切换动画时长
         */
        internal const val ANIM_TIME_DELAYED: Long = 300
        /**
         * 各状态标识
         */
        internal const val STATES_CONTENT = 0X01
        internal const val STATES_LOADING = 0X02
        internal const val STATES_EMPTY = 0X03
        internal const val STATES_NETOFF = 0X04
        internal const val STATES_ERROR = 0X05
        internal const val STATES_CUSTOM_EXCEPTION = 0X06
    }

    /**
     * 各状态视图
     */
    protected var mContentView: View? = null
    protected var mLoadingView: View? = null
    protected var mEmptyView: View? = null
    protected var mNetOffView: View? = null
    protected var mErrorView: View? = null
    protected var mCustomExceptionView: View? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    private val mMultiStatesViewProvider: PUIMultiStatesViewProvider = attrchMultiStatesViewProvider()

    /**
     * 由子类实现，返回多状态视图提供器
     */
    protected abstract fun attrchMultiStatesViewProvider(): PUIMultiStatesViewProvider

}