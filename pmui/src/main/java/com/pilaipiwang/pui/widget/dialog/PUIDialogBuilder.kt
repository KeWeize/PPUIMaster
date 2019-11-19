package com.pilaipiwang.pui.widget.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.annotation.IntDef
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.pilaipiwang.pui.R
import com.pilaipiwang.pui.alpha.PUIAlphaTextView
import com.pilaipiwang.pui.layout.PUILinearLayout
import com.pilaipiwang.pui.utils.PUIAttrsHelper
import com.pilaipiwang.pui.utils.PUIDisplayHelper

/**
 * @author  vitar
 * @date    2019/11/15
 */
abstract class PUIDialogBuilder<T : PUIDialogBuilder<T>> {

    @IntDef(HORIZONTAL, VERTICAL)
    annotation class Orientation

    companion object {
        const val HORIZONTAL = 0
        const val VERTICAL = 1
    }

    private lateinit var mContext: Context
    protected lateinit var mDialog: PUIDialog
    protected var mTitle: String? = null
    private var mCancelable = true
    private var mCanceledOnTouchOutside = true

    protected lateinit var mRootView: LinearLayout
    protected lateinit var mDialogView: PUIDialogView
    protected var mAnchorTopView: View? = null
    protected var mAnchorBottomView: View? = null
    protected val mActions: ArrayList<PUIDialogAction> = ArrayList()

    private var mOnDecorationListener: PUIDialogView.OnDecorationListener? = null

    protected var mTitleView: TextView? = null

    /**
     * 内容布局最大高度
     */
    private var mContentAreaMaxHeight = -1

    @Orientation
    private var mActionContainerOrientation = HORIZONTAL
    protected var mActionContainer: PUILinearLayout? = null

    /**
     * A global theme provider, use to distinguish theme from different builder type
     */
    private var sOnProvideDefaultTheme: OnProvideDefaultTheme? = null

    constructor(context: Context) {
        this.mContext = context
    }

    protected fun getContentAreaMaxHeight(): Int {
        return if (mContentAreaMaxHeight < 0) {
            // 默认最大高度为 屏幕高度 - title&action 大概占用高度
            (PUIDisplayHelper.getScreenHeight(mContext) * 0.85
                    - PUIDisplayHelper.dp2px(mContext, 120))
                .toInt()
        } else {
            mContentAreaMaxHeight
        }
    }

    fun getBaseContext() = mContext

    /**
     * 设置中间内容区域最大高度
     */
    fun setContentAreaMaxHeight(contentAreaMaxHeight: Int): T {
        this.mContentAreaMaxHeight = contentAreaMaxHeight
        return this as T
    }

    /**
     * 设置对话框顶部的标题文字
     */
    fun setTitle(@StringRes resId: Int): T {
        return setTitle(getBaseContext().getString(resId))
    }

    /**
     * 设置对话框顶部的标题文字
     */
    fun setTitle(title: String?): T {
        if (!title.isNullOrEmpty()) {
            this.mTitle = title
        }
        return this as T
    }

    fun addAction(
        charSequence: CharSequence, @PUIDialogAction.Level level: Int,
        listener: PUIDialogAction.ActionListener
    ): T {
        val action = PUIDialogAction(mContext, charSequence, level, listener)
        return addAction(action)
    }

    fun addAction(action: PUIDialogAction?): T {
        if (action != null) {
            mActions.add(action)
        }
        return this as T
    }

    /**
     * 判断对话框是否需要显示 Title
     */
    protected fun hasTitle() = !mTitle.isNullOrBlank()

    fun show(): PUIDialog {
        val dialog: PUIDialog = create()
        dialog.show()
        return dialog
    }

    /**
     * 只产生一个 Dialog, 不显示出来
     *
     * @see .create
     */
    fun create(): PUIDialog {
        val theme = sOnProvideDefaultTheme?.getThemeForBuilder(this)
        if (theme != null) {
            return create(theme)
        }
        return create(R.style.PUI_Dialog)
    }

    /**
     * 产生一个主题样式的 Dialog, 不显示出来
     */
    fun create(@StyleRes style: Int): PUIDialog {
        mDialog = PUIDialog(mContext, style)
        val dialogContext = mDialog.context

        mRootView = LayoutInflater.from(dialogContext).inflate(R.layout.pui_dialog_layout, null)
                as LinearLayout
        // dialog 内容容器
        mDialogView = mRootView.findViewById(R.id.dialog_view)
        mDialogView.setOnDecorationListener(mOnDecorationListener)
        mAnchorTopView = mRootView.findViewById(R.id.view_anchor_top)
        mAnchorBottomView = mRootView.findViewById(R.id.view_anchor_bottom)

        // title
        onCreateTitle(mDialog, mDialogView, dialogContext)

        // content
        onCreateContent(mDialog, mDialogView, dialogContext)

        // 操作 action
        onCreateHandlerBar(mDialog, mDialogView, dialogContext)

        mDialog.addContentView(
            mRootView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        mDialog.setCancelable(mCancelable)
        mDialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside)
        return mDialog
    }

    /**
     * 创建顶部的标题区域
     */
    protected fun onCreateTitle(dialog: PUIDialog, parent: ViewGroup, context: Context) {
        if (hasTitle()) {
            mTitleView = TextView(context)
            mTitleView!!.text = mTitle
            PUIAttrsHelper.assignTextViewWithAttr(mTitleView!!, R.attr.pui_dialog_title_style)
            onConfigTitleView(mTitleView!!)
            mTitleView!!.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            parent.addView(mTitleView)
        }
    }

    protected open fun onConfigTitleView(titleView: TextView) {
    }


    /**
     * 创建中间的区域，由子类去实现
     */
    protected abstract fun onCreateContent(dialog: PUIDialog, parent: ViewGroup, context: Context)

    /**
     * 创建底部的操作区域
     */
    protected fun onCreateHandlerBar(dialog: PUIDialog, parent: ViewGroup, context: Context) {
        val size = mActions.size
        if (size > 0) {
            val ta = context.obtainStyledAttributes(
                null, R.styleable.PUIDialogActionContainerCustomDef,
                R.attr.pui_dialog_action_container_style, 0
            )
            var justifyContent = 1
            var spaceCustomIndex = 0
            var actionHeight = -1
            var actionSpace = 0

            for (i in 0 until ta.indexCount) {
                when (val attr = ta.getIndex(i)) {
                    R.styleable.PUIDialogActionContainerCustomDef_pui_dialog_action_container_justify_content ->
                        justifyContent = ta.getInteger(attr, justifyContent)

                    R.styleable.PUIDialogActionContainerCustomDef_pui_dialog_action_container_custom_space_index ->
                        spaceCustomIndex = ta.getInteger(attr, 0)

                    R.styleable.PUIDialogActionContainerCustomDef_pui_dialog_action_space ->
                        actionSpace = ta.getDimensionPixelSize(attr, 0)

                    R.styleable.PUIDialogActionContainerCustomDef_pui_dialog_action_height ->
                        actionHeight = ta.getDimensionPixelSize(attr, 0)
                }
            }
            ta.recycle()
            var spaceInsertPos = -1
            if (mActionContainerOrientation == VERTICAL) {
                spaceInsertPos = -1
            } else if (justifyContent == 0) {
                spaceInsertPos = size
            } else if (justifyContent == 1) {
                spaceInsertPos = 0
            } else if (justifyContent == 3) {
                spaceInsertPos = spaceCustomIndex
            }

            mActionContainer =
                PUILinearLayout(context, null, R.attr.pui_dialog_action_container_style)
            mActionContainer!!.orientation = if (mActionContainerOrientation == VERTICAL) {
                LinearLayout.VERTICAL
            } else {
                LinearLayout.HORIZONTAL
            }
            mActionContainer!!.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            for (i in 0 until size) {
                if (spaceInsertPos == i) {
                    mActionContainer?.addView(createActionContainerSpace(context))
                }
                val action = mActions[i]
                val actionLp: LinearLayout.LayoutParams
                if (mActionContainerOrientation == VERTICAL) {
                    actionLp =
                        LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, actionHeight)
                } else {
                    actionLp =
                        LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, actionHeight)
                    if (spaceInsertPos >= 0) {
                        if (i >= spaceInsertPos) {
                            actionLp.leftMargin = actionSpace
                        } else {
                            actionLp.rightMargin = actionSpace
                        }
                    }
                    if (justifyContent == 2) {
                        actionLp.weight = 1f
                    }
                }
                val mActionView = action.buildActionView(mDialog, i)
                mActionView.setChangeAlphaWhenDisable(true)
                mActionView.setChangeAlphaWhenPress(true)
                mActionContainer!!.addView(mActionView, actionLp)
            }

            if (mActionContainerOrientation == HORIZONTAL) {
                mActionContainer!!.addOnLayoutChangeListener(View.OnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                    val width = right - left
                    val childCount = mActionContainer!!.childCount
                    if (childCount > 0) {
                        val lastChild = mActionContainer!!.getChildAt(childCount - 1)
                        // 如果ActionButton的宽度过宽，则减小padding
                        if (lastChild!!.right > width) {
                            val childPaddingHor = Math.max(
                                0,
                                lastChild!!.paddingLeft - PUIDisplayHelper.dp2px(mContext, 3)
                            )
                            for (i in 0 until childCount) {
                                mActionContainer!!.getChildAt(i)
                                    .setPadding(childPaddingHor, 0, childPaddingHor, 0)
                            }
                        }
                    }
                })
            }
            parent.addView(mActionContainer)

        }
    }

    private fun createActionContainerSpace(context: Context): View {
        val space = Space(context)
        val spaceLp = LinearLayout.LayoutParams(0, 0)
        spaceLp.weight = 1f
        space.layoutParams = spaceLp
        return space
    }

    interface OnProvideDefaultTheme {
        fun getThemeForBuilder(builder: PUIDialogBuilder<*>): Int
    }

}