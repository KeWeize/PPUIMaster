package com.pilaipiwang.pui.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Message
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.StringRes
import com.pilaipiwang.pui.R
import com.pilaipiwang.pui.utils.PUIAttrsHelper
import com.pilaipiwang.pui.widget.PUIWrapContentScrollView

/**
 * @author  vitar
 * @date    2019/11/15
 */
class PUIDialog : Dialog {

    private var mCancelable = true
    private var mCanceledOnTouchOutside = true
    private lateinit var mBaseContext: Context

    constructor(context: Context) : super(context, R.style.PUI_Dialog)
    constructor(context: Context, themeResId: Int) : super(context, themeResId) {
        mBaseContext = context
        init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDialog()
    }

    private fun init() {
        setCancelable(true)
        setCanceledOnTouchOutside(true)
    }

    private fun initDialog() {
        if (window == null) {
            return
        }
        val wmlp: WindowManager.LayoutParams = window!!.attributes
        wmlp.width = ViewGroup.LayoutParams.MATCH_PARENT
        wmlp.gravity = Gravity.CENTER
        window!!.attributes = wmlp
    }

    override fun setCancelable(flag: Boolean) {
        super.setCancelable(flag)
        mCancelable = flag
    }

    override fun setCanceledOnTouchOutside(cancel: Boolean) {
        super.setCanceledOnTouchOutside(cancel)
        if (cancel && !mCancelable) {
            mCancelable = true
        }
        mCanceledOnTouchOutside = cancel
    }

    /**
     * 消息类型对话框。通过它可以生成一个带标题、文本消息、按钮的对话框
     */
    class MessageDialogBuilder(context: Context) : PUIDialogBuilder<MessageDialogBuilder>(context) {

        protected var mMessage: CharSequence? = null
        private var mScrollContainer: PUIWrapContentScrollView? = null
        private var mTextView: TextView? = null

        /**
         * 设置对话框消息文本
         */
        fun setMessage(@StringRes resId: Int): MessageDialogBuilder {
            return setMessage(getBaseContext().getString(resId))
        }

        /**
         * 设置对话框消息文本
         */
        fun setMessage(message: CharSequence): MessageDialogBuilder {
            this.mMessage = message
            return this
        }

        /**
         * 获取消息文本 TextView
         */
        fun getMessageTextView(): TextView? = mTitleView

        /**
         * 实现中间内容区域
         */
        override fun onCreateContent(dialog: PUIDialog, parent: ViewGroup, context: Context) {
            if (!mMessage.isNullOrBlank()) {
                mTextView = TextView(context)
                assignMessageTvWithAttr(
                    mTextView!!, hasTitle(),
                    R.attr.pui_dialog_message_content_style
                )
                mTextView!!.text = mMessage

                mScrollContainer = PUIWrapContentScrollView(context, getContentAreaMaxHeight())
                mScrollContainer!!.isVerticalScrollBarEnabled = false
                mScrollContainer!!.addView(mTextView)
                parent.addView(mScrollContainer)
            }
        }

        override fun onConfigTitleView(titleView: TextView) {
            super.onConfigTitleView(titleView)
            if (mMessage.isNullOrBlank()) {
                val ta = titleView.context.obtainStyledAttributes(
                    null,
                    R.styleable.PUIDialogTitleTvCustomDef, R.attr.pui_dialog_title_style, 0
                )
                val count = ta.indexCount
                for (i in 0 until count) {
                    val attr = ta.getIndex(i)
                    if (attr == R.styleable.PUIDialogTitleTvCustomDef_pui_paddingBottomWhenNotContent) {
                        titleView.setPadding(
                            titleView.paddingLeft,
                            titleView.paddingTop,
                            titleView.paddingRight,
                            ta.getDimensionPixelSize(attr, titleView.paddingBottom)
                        )
                    }
                }
                ta.recycle()
            }
        }

    }

    companion object {
        /**
         * 设置 Message textView 样式
         */
        @JvmStatic
        private fun assignMessageTvWithAttr(messageTv: TextView, hasTitle: Boolean, defAttr: Int) {
            PUIAttrsHelper.assignTextViewWithAttr(messageTv, defAttr)
            if (!hasTitle) {
                val ta = messageTv.context.obtainStyledAttributes(
                    null, R.styleable.PUIDialogMessageTvCustomDef,
                    defAttr, 0
                )
                val count = ta.indexCount
                for (i in 0 until count) {
                    val attr = ta.getIndex(i)
                    if (attr == R.styleable.PUIDialogMessageTvCustomDef_pui_paddingTopWhenNotTitle) {
                        messageTv.setPadding(
                            messageTv.paddingLeft,
                            ta.getDimensionPixelSize(attr, messageTv.paddingTop),
                            messageTv.paddingRight,
                            messageTv.paddingBottom
                        )
                    }
                }
                ta.recycle()
            }
        }
    }


}