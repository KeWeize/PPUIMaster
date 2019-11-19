package com.pilaipiwang.pui.widget.dialog

import android.content.Context
import androidx.annotation.IntDef
import com.pilaipiwang.pui.R
import com.pilaipiwang.pui.widget.roundwidget.PUIRoundTextView

/**
 * @author  vitar
 * @date    2019/11/18
 */
class PUIDialogAction {

    companion object {
        const val ACTION_LEVEL_NEGATIVE = 0
        const val ACTION_LEVEL_NEUTRAL = 1
        const val ACTION_LEVEL_POSITIVE = 2
    }

    @IntDef(ACTION_LEVEL_NEGATIVE, ACTION_LEVEL_NEUTRAL, ACTION_LEVEL_POSITIVE)
    annotation class Level

    private var mContext: Context? = null
    private var mActionText: CharSequence? = null
    private var mIconResId: Int = 0
    @Level
    private var mActionLevel: Int? = null

    private var mOnClickListener: ActionListener? = null
    private var mActionView: PUIRoundTextView? = null
    private var mIsEnable: Boolean = true

    constructor(context: Context?, iconResId: Int, onClickListener: ActionListener?)
            : this(context, null, iconResId, ACTION_LEVEL_NEGATIVE, onClickListener)

    constructor(context: Context?, actionText: CharSequence, onClickListener: ActionListener?)
            : this(context, actionText, 0, ACTION_LEVEL_NEGATIVE, onClickListener)

    constructor(
        context: Context?,
        iconResId: Int,
        actionLevel: Int?,
        onClickListener: ActionListener?
    ) : this(context, null, iconResId, actionLevel, onClickListener)

    constructor(
        context: Context?,
        actionText: CharSequence?,
        actionLevel: Int?,
        onClickListener: ActionListener?
    ) : this(context, actionText, 0, actionLevel, onClickListener)

    constructor(
        context: Context?,
        actionText: CharSequence?,
        iconResId: Int,
        @Level actionLevel: Int?,
        onClickListener: ActionListener?
    ) {
        this.mContext = context
        this.mActionText = actionText
        this.mIconResId = iconResId
        this.mActionLevel = actionLevel
        this.mOnClickListener = onClickListener
    }

    fun setOnClickListener(onClickListener: ActionListener) {
        mOnClickListener = onClickListener
    }

    fun setEnable(enabled: Boolean) {
        mIsEnable = enabled
        mActionView?.isEnabled = enabled
    }

    /**
     * 生成行为按钮 PUIAlphaTextView
     */
    fun buildActionView(dialog: PUIDialog, index: Int): PUIRoundTextView {
        mActionView = generateActionButton(dialog.context, mActionText, mIconResId)
        mActionView?.setOnClickListener {
            mOnClickListener?.onClick(dialog, index)
        }
        return mActionView!!
    }

    /**
     * 生成对话框底部行为按钮
     */
    private fun generateActionButton(context: Context, charSequence: CharSequence?, iconRes: Int)
            : PUIRoundTextView {
        val textView = when (mActionLevel) {
            ACTION_LEVEL_POSITIVE ->
                PUIRoundTextView(context, null, R.attr.pui_dialog_action_position_style)
            ACTION_LEVEL_NEGATIVE ->
                PUIRoundTextView(context, null, R.attr.pui_dialog_action_nevative_style)
            else ->
                PUIRoundTextView(context, null, R.attr.pui_dialog_action_style)
        }
        textView.minHeight = 0
        textView.minimumHeight = 0
        textView.setChangeAlphaWhenDisable(true)
        textView.setChangeAlphaWhenPress(true)
        textView.isClickable = true
        textView.isEnabled = mIsEnable
        textView.text = charSequence
        return textView
    }

    /**
     * Dialog 行为按钮点击回调
     */
    interface ActionListener {
        fun onClick(dialog: PUIDialog, index: Int)
    }

}