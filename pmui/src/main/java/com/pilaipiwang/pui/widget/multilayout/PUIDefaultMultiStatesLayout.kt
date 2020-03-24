package com.pilaipiwang.pui.widget.multilayout

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.pilaipiwang.pui.R
import com.pilaipiwang.pui.widget.loading.PUILoadingView

/**
 * @author: vitar
 * @date:   2019/11/11
 */
class PUIDefaultMultiStatesLayout : PUIMultiStatesLayout {

    private var mMultiStateViewProvider: PUIMultiStatesViewProvider? = null

    private var mCommExceptionView: View? = null
    private var mExceptionIconIv: ImageView? = null
    private var mExceptionTextTv: TextView? = null
    private var mExceptionActionBtn: TextView? = null

    /**
     * 属性
     */
    private var mEmptyResId = 0
    private var mNetOffResId = 0
    private var mErrorResId = 0
    private var mEmptyText = ""
    private var mNetOffText = ""
    private var mErrorText = ""
    private var mActionBtnText = ""
    private var mIsActionBtnShow = false

    /**
     * 底部行为按钮点击回调
     */
    private var mOnMultiStatesActionListener: OnDefaultMultiStatesActionListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttrs(context, attrs, defStyleAttr)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val ta = context.obtainStyledAttributes(
            attrs, R.styleable.PUIDefaultMultiStatesLayout, defStyleAttr, 0
        )
        mEmptyResId = ta.getResourceId(
            R.styleable.PUIDefaultMultiStatesLayout_pui_default_multistates_empty_resId,
            R.drawable.pui_default_ic_multi_states_empty
        )
        mNetOffResId = ta.getResourceId(
            R.styleable.PUIDefaultMultiStatesLayout_pui_default_multistates_netoff_resId,
            R.drawable.pui_default_ic_multi_states_netoff
        )
        mErrorResId = ta.getResourceId(
            R.styleable.PUIDefaultMultiStatesLayout_pui_default_multistates_error_resId,
            R.drawable.pui_default_ic_multi_states_error
        )
        mEmptyText =
            ta.getString(R.styleable.PUIDefaultMultiStatesLayout_pui_default_multistates_empty_text)
                ?: context.getString(R.string.pui_default_multi_states_empty_text)
        mErrorText =
            ta.getString(R.styleable.PUIDefaultMultiStatesLayout_pui_default_multistates_error_text)
                ?: context.getString(R.string.pui_default_multi_states_error_text)
        mNetOffText =
            ta.getString(R.styleable.PUIDefaultMultiStatesLayout_pui_default_multistates_netoff_text)
                ?: context.getString(R.string.pui_default_multi_states_netoff_text)
        mActionBtnText =
            ta.getString(R.styleable.PUIDefaultMultiStatesLayout_pui_default_multistates_action_text)
                ?: context.getString(R.string.pui_default_multi_states_action_text)
        mIsActionBtnShow =
            ta.getBoolean(
                R.styleable.PUIDefaultMultiStatesLayout_pui_default_multistates_is_action_show,
                false
            )
        ta.recycle()
    }

    /**
     * 显示空状态布局
     */
    fun showEmptyLayout(isShowActionBtn: Boolean) {
        updateExceptionLayout(mEmptyResId, mEmptyText, mActionBtnText, isShowActionBtn)
        super.showEmptyLayout()
    }

    /**
     * 显示空状态布局
     */
    override fun showEmptyLayout() {
        updateExceptionLayout(mEmptyResId, mEmptyText, mActionBtnText, mIsActionBtnShow)
        super.showEmptyLayout()
    }

    /**
     * 显示网络异常布局
     */
    fun showNetOffLayout(isShowActionBtn: Boolean) {
        updateExceptionLayout(mNetOffResId, mNetOffText, mActionBtnText, isShowActionBtn)
        super.showNetOffLayout()
    }

    /**
     * 显示网络异常布局
     */
    override fun showNetOffLayout() {
        updateExceptionLayout(mNetOffResId, mNetOffText, mActionBtnText, mIsActionBtnShow)
        super.showNetOffLayout()
    }

    /**
     * 显示服务器错误布局
     */
    fun showErrorLayout(isShowActionBtn: Boolean) {
        updateExceptionLayout(mErrorResId, mErrorText, mActionBtnText, isShowActionBtn)
        super.showErrorLayout()
    }

    /**
     * 显示服务器错误布局
     */
    override fun showErrorLayout() {
        updateExceptionLayout(mErrorResId, mErrorText, mActionBtnText, mIsActionBtnShow)
        super.showErrorLayout()
    }

    /**
     * 显示自定义异常布局
     */
    fun showCustomExceptionLayout(
        @DrawableRes resId: Int, exceptionText: CharSequence,
        actionText: CharSequence, isShowActionBtn: Boolean
    ) {
        updateExceptionLayout(resId, exceptionText, actionText, isShowActionBtn)
        super.showCustomExceptionLayout()
    }

    override fun showGoalView(view: View?): Boolean {
        val result = super.showGoalView(view)
        if (result && view == mLoadingView) {
            mLoadingAnimView?.start()
        } else {
            mLoadingAnimView?.stop()
        }
        return result
    }

    override fun getMultiStateViewProvider(): PUIMultiStatesViewProvider? {
        mMultiStateViewProvider = object : PUIMultiStatesViewProvider {

            override fun attachedLoadingLayout(context: Context): View? {
                return makeSureLoadingView(context)
            }

            override fun attachedEmptyLayout(context: Context): View? {
                return makeSureExceptionView(context)
            }

            override fun attachedNetOffLayout(context: Context): View? {
                return makeSureExceptionView(context)
            }

            override fun attachedErrorLayout(context: Context): View? {
                return makeSureExceptionView(context)
            }

            override fun attachedExceptionLayout(context: Context): View? {
                return makeSureExceptionView(context)
            }

        }
        return mMultiStateViewProvider
    }

    /**
     * 显示异常界面
     */
    private fun updateExceptionLayout(
        resId: Int, charSequence: CharSequence,
        actionText: CharSequence, showActionBtn: Boolean
    ) {
        resetAllExceptionViews()
        resetAllExceptionViews()
        mExceptionIconIv?.setImageResource(resId)
        mExceptionTextTv?.text = charSequence
        mExceptionActionBtn?.text = actionText
        mExceptionActionBtn?.visibility = if (showActionBtn) View.VISIBLE else GONE
        mExceptionActionBtn?.setOnClickListener {
            when (mCurStates) {
                STATES_EMPTY -> mOnMultiStatesActionListener?.onClick(
                    it,
                    MultiStatesType.STATES_EMPTY
                )
                STATES_ERROR -> mOnMultiStatesActionListener?.onClick(
                    it,
                    MultiStatesType.STATES_ERROR
                )
                STATES_NETOFF -> mOnMultiStatesActionListener?.onClick(
                    it,
                    MultiStatesType.STATES_NETOFF
                )
                STATES_CUSTOM_EXCEPTION -> mOnMultiStatesActionListener?.onClick(
                    it,
                    MultiStatesType.STATES_CUSTOM
                )
            }
        }
    }

    /**
     * 重置异常布局控件
     */
    private fun resetAllExceptionViews() {
        mExceptionIconIv?.setImageBitmap(null)
        mExceptionTextTv?.text = ""
        mExceptionActionBtn?.text = ""
        mExceptionActionBtn?.visibility = GONE
    }

    /**
     * 返回异常视图
     */
    private fun makeSureExceptionView(context: Context): View {
        if (mCommExceptionView == null) {
            mCommExceptionView = LayoutInflater.from(context)
                .inflate(R.layout.pui_include_default_multistates_exception_layout, null)
            mExceptionIconIv = mCommExceptionView!!.findViewById(R.id.iv_exception_icon)
            mExceptionTextTv = mCommExceptionView!!.findViewById(R.id.tv_exception_text)
            mExceptionActionBtn = mCommExceptionView!!.findViewById(R.id.btn_exception_action)
        }
        return mCommExceptionView!!
    }

    private var mLoadingAnimView: PUILoadingView? = null

    /**
     * 返回加载视图
     */
    private fun makeSureLoadingView(context: Context): View {
        if (mLoadingView == null) {
            mLoadingView = LayoutInflater.from(context)
                .inflate(R.layout.pui_include_default_multistates_loading_layout, null)
            mLoadingAnimView =
                mLoadingView!!.findViewById(R.id.v_pui_default_multistates_loading_view)
        }
        return mLoadingView!!
    }

    /**
     * 设置行为按钮回调。目前所有类型异常都会使用同一个回调
     */
    fun setDefaultMultiStatesActionListener(listener: OnDefaultMultiStatesActionListener?) {
        this.mOnMultiStatesActionListener = listener
    }

    /**
     * 底部行为按钮点击回调
     */
    interface OnDefaultMultiStatesActionListener {
        fun onClick(view: View, type: MultiStatesType)
    }

    /**
     * 枚举类型，用于点击回调时返回当前的异常类型
     */
    enum class MultiStatesType {
        STATES_EMPTY, STATES_ERROR, STATES_NETOFF, STATES_CUSTOM
    }

}