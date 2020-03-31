package com.pilaipiwang.pui.widget.multilayout

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.pilaipiwang.pui.R
import com.pilaipiwang.pui.widget.loading.PUILoadingView

/**
 * @author: vitar
 * @date:   2020/3/25
 *
 * 拓展自 PUIMultiStatesAbstract，提供了具体的多状态布局和拓展的api方法
 */
class PUIMultiStatesLayout : PUIMultiStatesAbstract {

    /**
     * 加载状态视图
     */
    private var mLoadingView: View? = null
    private var mLoadingAnimView: PUILoadingView? = null
    /**
     * 异常状态视图
     */
    private var mExceptionView: View? = null
    private var mExceptionIconIv: ImageView? = null
    private var mExceptionTextTv: TextView? = null
    private var mExceptionSubTextTv: TextView? = null
    private var mExceptionActionBtn: TextView? = null
    /**
     * 属性
     */
    private var mIconWidth: Int = 0
    private var mIconHeight: Int = 0
    private var mEmptyResId = 0
    private var mNetOffResId = 0
    private var mErrorResId = 0

    private var mTextColor: Int = 0
    private var mTextSize: Int = 0
    private var mEmptyText: String? = null
    private var mNetOffText: String? = null
    private var mErrorText: String? = null

    private var mSubTextColor: Int = 0
    private var mSubTextSize: Int = 0
    private var mEmptySubText: String? = null
    private var mNetOffSubText: String? = null
    private var mErrorSubText: String? = null

    private var mEmptyIsActionBtnShow = false
    private var mErrorIsActionBtnShow = false
    private var mNetoffIsActionBtnShow = false
    private var mActionBtnText = ""

    @LayoutRes
    private var mLoadingViewLayoutId: Int? = 0

    /**
     * 底部按钮点击回调
     */
    private var mOnMultiStatesActionListener: OnMultiStatesActionListener? = null

    /**
     * 记录当前异常布局类型
     */
    private var mExceptionType: MultiStatesType? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        R.attr.pui_multistateslayout_style
    )

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttrs(context, attrs, defStyleAttr)
    }

    fun setOnMultiStatesActionListener(listener: OnMultiStatesActionListener) {
        this.mOnMultiStatesActionListener = listener
    }

    /**
     * 初始化属性
     */
    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val ta = context.obtainStyledAttributes(
            attrs, R.styleable.PUIMultiStatesLayout, defStyleAttr, 0
        )
        // 异常状态 icon
        mIconWidth =
            ta.getDimensionPixelSize(R.styleable.PUIMultiStatesLayout_pui_multistates_icon_width, 0)
        mIconHeight = ta.getDimensionPixelSize(
            R.styleable.PUIMultiStatesLayout_pui_multistates_icon_height,
            0
        )
        mEmptyResId = ta.getResourceId(
            R.styleable.PUIMultiStatesLayout_pui_multistates_empty_resId,
            R.drawable.pui_default_ic_multi_states_empty
        )
        mNetOffResId = ta.getResourceId(
            R.styleable.PUIMultiStatesLayout_pui_multistates_netoff_resId,
            R.drawable.pui_default_ic_multi_states_netoff
        )
        mErrorResId = ta.getResourceId(
            R.styleable.PUIMultiStatesLayout_pui_multistates_error_resId,
            R.drawable.pui_default_ic_multi_states_error
        )
        // 异常状态主文本
        mTextColor = ta.getColor(R.styleable.PUIMultiStatesLayout_pui_multistates_text_color, 0)
        mTextSize =
            ta.getDimensionPixelSize(R.styleable.PUIMultiStatesLayout_pui_multistates_text_size, 0)
        mEmptyText =
            ta.getString(R.styleable.PUIMultiStatesLayout_pui_multistates_empty_text)
                ?: context.getString(R.string.pui_default_multi_states_empty_text)
        mErrorText =
            ta.getString(R.styleable.PUIMultiStatesLayout_pui_multistates_error_text)
                ?: context.getString(R.string.pui_default_multi_states_error_text)
        mNetOffText =
            ta.getString(R.styleable.PUIMultiStatesLayout_pui_multistates_netoff_text)
                ?: context.getString(R.string.pui_default_multi_states_netoff_text)
        // 异常状态副文本
        mSubTextColor =
            ta.getColor(R.styleable.PUIMultiStatesLayout_pui_multistates_subtext_color, 0)
        mSubTextSize = ta.getDimensionPixelSize(
            R.styleable.PUIMultiStatesLayout_pui_multistates_subtext_size,
            0
        )
        mEmptySubText = ta.getString(R.styleable.PUIMultiStatesLayout_pui_multistates_empty_subtext)
        mErrorSubText = ta.getString(R.styleable.PUIMultiStatesLayout_pui_multistates_error_subtext)
        mNetOffSubText =
            ta.getString(R.styleable.PUIMultiStatesLayout_pui_multistates_netoff_subtext)

        mActionBtnText =
            ta.getString(R.styleable.PUIMultiStatesLayout_pui_multistates_action_text)
                ?: context.getString(R.string.pui_default_multi_states_action_text)
        mEmptyIsActionBtnShow =
            ta.getBoolean(
                R.styleable.PUIMultiStatesLayout_pui_multistates_empty_is_action_show,
                false
            )
        mErrorIsActionBtnShow =
            ta.getBoolean(
                R.styleable.PUIMultiStatesLayout_pui_multistates_error_is_action_show,
                false
            )
        mNetoffIsActionBtnShow =
            ta.getBoolean(
                R.styleable.PUIMultiStatesLayout_pui_multistates_netoff_is_action_show,
                false
            )
        mLoadingViewLayoutId = ta.getResourceId(
            R.styleable.PUIMultiStatesLayout_pui_multistates_loading_view_layoutId,
            R.layout.pui_include_default_multistates_loading_layout
        )
        ta.recycle()
    }


    /**
     * 显示空状态
     */
    override fun showEmptyLayout() {
        super.showExceptionLayout()
        mExceptionType = MultiStatesType.STATES_EMPTY
        updateExceptionLayout(
            mEmptyResId,
            mEmptyText,
            mEmptySubText,
            mActionBtnText,
            mEmptyIsActionBtnShow
        )
    }

    /**
     * 显示网络异常布局
     */
    fun showNetOffLayout() {
        super.showExceptionLayout()
        mExceptionType = MultiStatesType.STATES_NETOFF
        updateExceptionLayout(
            mNetOffResId,
            mNetOffText,
            mNetOffSubText,
            mActionBtnText,
            mNetoffIsActionBtnShow
        )
    }

    /**
     * 显示服务器错误布局
     */
    fun showErrorLayout() {
        super.showExceptionLayout()
        mExceptionType = MultiStatesType.STATES_ERROR
        updateExceptionLayout(
            mErrorResId,
            mErrorText,
            mErrorSubText,
            mActionBtnText,
            mErrorIsActionBtnShow
        )
    }

    /**
     * 显示异常界面
     */
    private fun updateExceptionLayout(
        resId: Int, text: CharSequence?, subText: CharSequence?,
        actionText: CharSequence, showActionBtn: Boolean
    ) {
        resetAllExceptionViews()
        mExceptionIconIv?.setImageResource(resId)
        mExceptionTextTv?.text = text
        mExceptionTextTv?.visibility = if (mExceptionTextTv?.text.isNullOrBlank()) GONE else VISIBLE
        mExceptionSubTextTv?.text = subText
        mExceptionSubTextTv?.visibility =
            if (mExceptionSubTextTv?.text.isNullOrBlank()) GONE else VISIBLE
        mExceptionActionBtn?.text = actionText
        mExceptionActionBtn?.visibility = if (showActionBtn) VISIBLE else GONE
    }

    /**
     * 提供默认的多状态视图提供者
     */
    override fun attachMultiStatesViewProvider(): IMultiStatesViewProvider =
        object : IMultiStatesViewProvider {

            override fun attachedLoadingLayout(): View? = makeSureLoadingView()

            /**
             * 空状态跟异常状态公用一套布局
             */
            override fun attachedEmptyLayout(): View? = makeSureExceptionView()

            override fun attachedExceptionLayout(): View? = makeSureExceptionView()
        }

    /**
     * 返回异常视图
     */
    private fun makeSureExceptionView(): View {
        if (mExceptionView == null) {
            mExceptionView = LayoutInflater.from(context)
                .inflate(R.layout.pui_include_default_multistates_exception_layout, null)
            mExceptionIconIv = mExceptionView!!.findViewById(R.id.iv_exception_icon)
            mExceptionTextTv = mExceptionView!!.findViewById(R.id.tv_exception_text)
            mExceptionSubTextTv = mExceptionView!!.findViewById(R.id.tv_exception_subtext)
            mExceptionActionBtn = mExceptionView!!.findViewById(R.id.btn_exception_action)
            // 根据属性动态设置 icon 宽高
            if (mIconWidth > 0 && mIconHeight > 0) {
                val vlp = mExceptionIconIv!!.layoutParams
                vlp.width = mIconWidth
                vlp.height = mIconHeight
            }
            mExceptionTextTv!!.setTextColor(mTextColor)
            mExceptionTextTv!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize.toFloat())
            mExceptionSubTextTv!!.setTextColor(mSubTextColor)
            mExceptionSubTextTv!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSubTextSize.toFloat())
            mExceptionActionBtn!!.setOnClickListener {
                if (mCurStates == STATES_EXCEPTION) {
                    mOnMultiStatesActionListener?.onClick(mExceptionActionBtn!!, mExceptionType!!)
                }
            }
        }
        return mExceptionView!!
    }

    /**
     * 返回加载视图
     */
    private fun makeSureLoadingView(): View {
        if (mLoadingView == null) {
            mLoadingView = LayoutInflater.from(context)
                .inflate(mLoadingViewLayoutId!!, null)
            mLoadingAnimView =
                mLoadingView!!.findViewById(R.id.v_pui_default_multistates_loading_view)
        }
        return mLoadingView!!
    }

    /**
     * 重置异常布局控件
     */
    private fun resetAllExceptionViews() {
        mExceptionIconIv?.setImageBitmap(null)
        mExceptionTextTv?.text = ""
        mExceptionSubTextTv?.text = ""
        mExceptionActionBtn?.text = ""
        mExceptionActionBtn?.visibility = GONE
    }

    /**
     * 底部行为按钮点击回调
     */
    interface OnMultiStatesActionListener {
        fun onClick(view: View, type: MultiStatesType)
    }

    /**
     * 枚举类型，用于点击回调时返回当前的异常类型
     */
    enum class MultiStatesType {
        STATES_EMPTY, STATES_ERROR, STATES_NETOFF
    }

}