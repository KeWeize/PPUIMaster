package com.pilaipiwang.pui.widget.multilayout

import android.content.Context
import android.util.AttributeSet
import android.view.InflateException
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.FrameLayout

/**
 * @author: vitar
 * @date:   2019/11/11
 */
abstract class PUIMultiStatesLayout : FrameLayout {

    private val TAG = PUIMultiStatesLayout::class.java.simpleName

    companion object {
        /**
         * 切换动画状态
         */
        private const val ANIM_TIME_DELAYED: Long = 300
        /**
         * 内容布局状态
         */
        internal const val STATES_CONTENT = 0x01
        /**
         * 加载中状态
         */
        internal const val STATES_LOADING = 0x02
        /**
         * 空状态
         */
        internal const val STATES_EMPTY = 0x03
        /**
         * 网络异常
         */
        internal const val STATES_NETOFF = 0x04
        /**
         * 错误状态
         */
        internal const val STATES_ERROR = 0x05
        /**
         * 其他自定义状态
         */
        internal const val STATES_CUSTOM_EXCEPTION = 0x06
    }

    /**
     * 内容视图
     */
    protected var mContentView: View? = null
    /**
     * 加载视图
     */
    protected var mLoadingView: View? = null
    /**
     * 空视图
     */
    protected var mEmptyView: View? = null
    /**
     * 异常视图
     */
    protected var mNetOffView: View? = null
    /**
     * 错误视图
     */
    protected var mErrorView: View? = null
    /**
     * 自定义异常视图
     */
    protected var mCustomExceptionView: View? = null

    protected var mCurStates = -1
    private val DEFAULT_LAYOUT_PARAMS =
        LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

    private var mPUIMultiStatesViewProvider: PUIMultiStatesViewProvider? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        mPUIMultiStatesViewProvider = getMultiStateViewProvider()
        if (mPUIMultiStatesViewProvider == null) {
            throw InflateException(
                "${PUIMultiStatesLayout::class.java.simpleName}'s mPUIMultiStatesViewProvider " +
                        "should be assign value before onAttachedToWindow"
            )
        }
        mLoadingView = mPUIMultiStatesViewProvider!!.attachedLoadingLayout(context)
        mEmptyView = mPUIMultiStatesViewProvider!!.attachedEmptyLayout(context)
        mNetOffView = mPUIMultiStatesViewProvider!!.attachedNetOffLayout(context)
        mErrorView = mPUIMultiStatesViewProvider!!.attachedErrorLayout(context)
        mCustomExceptionView = mPUIMultiStatesViewProvider!!.attachedExceptionLayout(context)
        addViewByCheckLegal(mLoadingView)
        addViewByCheckLegal(mEmptyView)
        addViewByCheckLegal(mNetOffView)
        addViewByCheckLegal(mErrorView)
        addViewByCheckLegal(mCustomExceptionView)

        // 初始化显示内容视图
        showContentLayout()
    }

    /**
     * 注入 PUIMultiStatesViewProvider
     */
    fun setMultiStatesViewProvider(provider: PUIMultiStatesViewProvider) {
        this.mPUIMultiStatesViewProvider = provider
    }

    /**
     * 显示内容视图
     */
    open fun showContentLayout() {
        if (mCurStates == STATES_CONTENT) {
            return
        }
        if (showGoalView(mContentView)) {
            mCurStates = STATES_CONTENT
        }
    }

    /**
     * 显示正在加载中视图
     */
    open fun showLoadingLayout() {
        if (mCurStates == STATES_LOADING) {
            return
        }
        if (showGoalView(mLoadingView)) {
            mCurStates = STATES_LOADING
        }
    }

    /**
     * 显示空状态视图
     */
    open fun showEmptyLayout() {
        if (mCurStates == STATES_EMPTY) {
            return
        }
        if (showGoalView(mEmptyView)) {
            mCurStates = STATES_EMPTY
        }
    }

    /**
     * 显示网络异常视图
     */
    open fun showNetOffLayout() {
        if (mCurStates == STATES_NETOFF) {
            return
        }
        if (showGoalView(mNetOffView)) {
            mCurStates = STATES_NETOFF
        }

    }

    open fun showErrorLayout() {
        if (mCurStates == STATES_ERROR) {
            return
        }
        if (showGoalView(mErrorView)) {
            mCurStates = STATES_ERROR
        }
    }

    open fun showCustomExceptionLayout() {
        if (mCurStates == STATES_CUSTOM_EXCEPTION) {
            return
        }
        if (showGoalView(mCustomExceptionView)) {
            mCurStates = STATES_CUSTOM_EXCEPTION
        }
    }

    /**
     * 显示当前层级下指定的子 View，并且隐藏其他子 View
     * @return 切换显示是否成功，如果目标 view 为 null，或者目标 view 不存在与当前视图中返回 false
     */
    protected open fun showGoalView(view: View?): Boolean {
        if (view == null || !checkIsInContainer(view)) {
            return false
        }
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child === view) {
                child.visibility = View.VISIBLE
                showDisplayAnimation(view)
            } else {
                child.visibility = View.GONE
            }
        }
        return true
    }

    /**
     * 执行View动画
     */
    private fun showDisplayAnimation(inView: View?) {
        if (inView == mLoadingView) {
            // 过滤 LoadingView 的进场动画，避免加载时间过短，导致进场动画还没开始执行就被切换掉
            return
        }
        val alpha = AlphaAnimation(0f, 1f)
        alpha.duration = ANIM_TIME_DELAYED
        inView?.startAnimation(alpha)
    }

    /**
     * 用于检查添加的视图是否是内容视图
     */
    private fun checkContentView(view: View?) {
        // 添加的是加载视图或内部异常视图
        if (view === mLoadingView || view === mEmptyView || view === mNetOffView || view === mErrorView) {
            return
        }
        if (mContentView != null && view !== mContentView) {
            // 多布局控件只允许包裹一个子View内容
            throw InflateException("${PUIMultiStatesLayout::class.java.simpleName} only one child view is allowed")
        }
        mContentView = view
    }

    /**
     * 检查目标 view 是否存在于当前布局中
     * 如果不存在则添加
     */
    private fun addViewByCheckLegal(view: View?) {
        for (i in 0 until childCount) {
            if (view == null || view === getChildAt(i)) {
                return
            }
        }
        addView(view, DEFAULT_LAYOUT_PARAMS)
    }

    /**
     * 检查目标 view 是否存在于当前布局中
     */
    private fun checkIsInContainer(view: View?): Boolean {
        for (i in 0 until childCount) {
            if (view === getChildAt(i)) {
                return true
            }
        }
        return false
    }

    abstract fun getMultiStateViewProvider(): PUIMultiStatesViewProvider?

    /**================== 重写addView方法，控制添加子内容View数量 ======================*/
    override fun addView(child: View?) {
        checkContentView(child)
        super.addView(child)
    }

    override fun addView(child: View?, index: Int) {
        checkContentView(child)
        super.addView(child, index)
    }

    override fun addView(child: View?, width: Int, height: Int) {
        checkContentView(child)
        super.addView(child, width, height)
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        checkContentView(child)
        super.addView(child, params)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        checkContentView(child)
        super.addView(child, index, params)
    }

}