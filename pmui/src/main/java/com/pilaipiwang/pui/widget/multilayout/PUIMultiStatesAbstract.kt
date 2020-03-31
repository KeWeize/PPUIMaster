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
 * @date:   2020/3/25
 *
 * 1. 继承自 FrameLayout。定义了多状态布局的状态
 *      1.加载中； 2.内容； 3.空状态； 4.异常状态；
 * 以及这些状态布局的装载和切换逻辑等；
 * 2. PUIMultiStatesAbstract本身并不关心多状态布局的具体实现，子类需要重写 attachMultiStatesViewProvider() 方法
 * 向上提供各个状态的布局内容；
 */
abstract class PUIMultiStatesAbstract : FrameLayout {

    private val TAG: String = PUIMultiStatesAbstract::class.java.simpleName

    protected companion object {

        const val ANIM_TIME_DELAYED: Long = 300

        /**
         * 内容布局状态
         */
        const val STATES_CONTENT = 0x01
        /**
         * 加载中状态
         */
        const val STATES_LOADING = 0x02
        /**
         * 空状态
         */
        const val STATES_EMPTY = 0x03
        /**
         * 错误状态
         */
        const val STATES_EXCEPTION = 0x04
    }

    /**
     * 缓存内容视图
     */
    private var mContentView: View? = null
    /**
     * 加载视图
     */
    private var mLoadingView: View? = null

    private val mLoadViewsMap: ArrayList<IPUIMultiStatesLoadAnim> = arrayListOf()

    /**
     * 空视图
     */
    private var mEmptyView: View? = null
    /**
     * 异常视图
     */
    private var mExceptionView: View? = null

    /**
     * 当前状态类型
     */
    protected var mCurStates = -1

    /**
     * 默认 LAYOUT_PARAMS 实例
     */
    private val DEFAULT_LAYOUT_PARAMS =
        LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

    /**
     * 状态视图提供者
     */
    private var mMultiStatesViewProvider: IMultiStatesViewProvider? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    /**
     * 初始化操作
     */
    private fun init() {
        mMultiStatesViewProvider = attachMultiStatesViewProvider()
        if (mMultiStatesViewProvider == null) {
            throw InflateException(
                "$TAG 's mPUIMultiStatesViewProvider " +
                        "should be assign value before onAttachedToWindow"
            )
        }
        // 初始化显示内容视图
        showContentLayout()
    }

    /**
     * 由子类实现，提供一个多状态布局的视图提供者
     */
    abstract fun attachMultiStatesViewProvider(): IMultiStatesViewProvider

    /**
     * 定义多布局提供者行为，用于子类提供各个状态的布局内容
     */
    interface IMultiStatesViewProvider {

        /**
         * 提供加载中状态视图
         */
        fun attachedLoadingLayout(): View?

        /**
         * 提供空状态视图
         */
        fun attachedEmptyLayout(): View?

        /**
         * 提供异常视图
         */
        fun attachedExceptionLayout(): View?

    }

    /**
     * 显示内容视图
     */
    fun showContentLayout() {
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
        if (mLoadingView == null) {
            mLoadingView = mMultiStatesViewProvider!!.attachedLoadingLayout()
            findMultiStatesLoadView(mLoadingView!!)
            addViewByCheckLegal(mLoadingView)
        }
        if (showGoalView(mLoadingView)) {
            mCurStates = STATES_LOADING
        }
    }

    /**
     * 通过递归把 loadView 视图中所有实现了 IPUIMultiStatesLaodView 接口的视图缓存下来
     */
    private fun findMultiStatesLoadView(view: View) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                findMultiStatesLoadView(child)
            }
        } else {
            if (view is IPUIMultiStatesLoadAnim) {
                mLoadViewsMap.add(view)
            }
        }
    }

    /**
     * 执行缓存中的动画对象的加载动画行为
     */
    private fun startLoadingAnim() {
        for (loadAnim in mLoadViewsMap) {
            loadAnim.startLoadingAnim()
        }
    }

    /**
     * 停止缓存中的动画对象的加载动画行为
     */
    private fun stopLoadingAnim() {
        for (loadAnim in mLoadViewsMap) {
            loadAnim.stopLoadingAnim()
        }
    }

    /**
     * 显示空状态视图
     */
    open fun showEmptyLayout() {
        if (mCurStates == STATES_EMPTY) {
            return
        }
        if (mEmptyView == null) {
            mEmptyView = mMultiStatesViewProvider!!.attachedEmptyLayout()
            addViewByCheckLegal(mEmptyView)
        }
        if (showGoalView(mEmptyView)) {
            mCurStates = STATES_EMPTY
        }
    }

    /**
     * 显示异常视图
     */
    open fun showExceptionLayout() {
        if (mCurStates == STATES_EXCEPTION) {
            return
        }
        if (mExceptionView == null) {
            mExceptionView = mMultiStatesViewProvider!!.attachedExceptionLayout()
            addViewByCheckLegal(mExceptionView)
        }
        if (showGoalView(mExceptionView)) {
            mCurStates = STATES_EXCEPTION
        }
    }

    /**
     * 显示当前层级下指定的子 View，并且隐藏其他子 View
     * @return 切换显示是否成功，如果目标 view 为 null，或者目标 view 不存在与当前视图中返回 false
     */
    protected fun showGoalView(view: View?): Boolean {
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
        if (view == mLoadingView) {
            startLoadingAnim()
        } else {
            stopLoadingAnim()
        }
        return true
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

    /**
     * 执行 View 的进入动画
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
     * 检查目标 view 是否存在于当前布局中
     * 如果不存在则添加
     */
    private fun addViewByCheckLegal(view: View?) {
        if (checkIsInContainer(view)) {
            return
        }
        addView(view, DEFAULT_LAYOUT_PARAMS)
    }

    /**================== 重写 addView 方法，用于获取 contentView视图 ======================*/
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

    /**
     * 用于检查添加的视图是否是内容视图
     */
    private fun checkContentView(view: View?) {
        // 添加的是加载视图或内部异常视图
        if (view === mLoadingView || view === mEmptyView || view === mExceptionView || view === mContentView) {
            return
        }
        if (mContentView != null && view !== mContentView) {
            // 多布局控件只允许包裹一个子View内容
            throw InflateException("$TAG only one child view is allowed")
        }
        mContentView = view
    }
}