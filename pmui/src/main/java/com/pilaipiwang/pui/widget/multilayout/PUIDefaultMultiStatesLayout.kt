package com.pilaipiwang.pui.widget.multilayout

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.pilaipiwang.pui.R

/**
 * @author: vitar
 * @date:   2019/11/11
 */
class PUIDefaultMultiStatesLayout : PUIMultiStatesLayout {

    private val mMultiStateViewProvider = object : PUIMultiStatesViewProvider {

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
    }

    private var mCommExceptionView: View? = null
    private var mExceptionIconIv: ImageView? = null
    private var mExceptionTextTv: TextView? = null
    private var mExceptionActionBtn: Button? = null

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

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setMultiStatesViewProvider(mMultiStateViewProvider)
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
//        mEmptyText =
//            ta.getString(R.styleable.PUIDefaultMultiStatesLayout_pui_default_multistates_error_text)
//                ?: context.getString(R.string.default_multistatus_empty_text)
//        mErrorText =
//            ta.getString(R.styleable.PUIMultiStatusLayout_pp_multistatus_default_error_text)
//                ?: context.getString(R.string.default_multistatus_error_text)
//        mNetOffText =
//            ta.getString(R.styleable.PUIMultiStatusLayout_pp_multistatus_default_netoff_text)
//                ?: context.getString(R.string.default_multistatus_netoff_text)
//        mActionBtnText =
//            ta.getString(R.styleable.PUIMultiStatusLayout_pp_multistatus_default_action_text)
//                ?: context.getString(R.string.default_multistatus_action_text)
//        mIsActionBtnShow =
//            ta.getBoolean(
//                R.styleable.PUIMultiStatusLayout_pp_multistatus_default_action_show,
//                false
//            )
        ta.recycle()
    }

    override fun showEmptyLayout() {
        mExceptionTextTv?.text = "暂无相关数据"
        super.showEmptyLayout()
    }

    override fun showNetOffLayout() {
        mExceptionTextTv?.text = "网络异常，请重试"
        super.showNetOffLayout()
    }

    override fun showErrorLayout() {
        mExceptionTextTv?.text = "服务器错误，请重试"
        super.showErrorLayout()
    }

    /**
     * 返回异常视图
     */
    private fun makeSureExceptionView(context: Context): View {
        if (mCommExceptionView == null) {
            mCommExceptionView = LayoutInflater.from(context)
                .inflate(R.layout.pui_include_default_multistatus_exception_layout, null)
            mExceptionIconIv = mCommExceptionView!!.findViewById(R.id.iv_exception_icon)
            mExceptionTextTv = mCommExceptionView!!.findViewById(R.id.tv_exception_text)
            mExceptionActionBtn = mCommExceptionView!!.findViewById(R.id.btn_exception_action)
        }
        return mCommExceptionView!!
    }

    /**
     * 返回加载视图
     */
    private fun makeSureLoadingView(context: Context): View {
        if (mLoadingView == null) {
            mLoadingView = LayoutInflater.from(context)
                .inflate(R.layout.pui_include_default_multistatus_loading_layout, null)
        }
        return mLoadingView!!
    }

}