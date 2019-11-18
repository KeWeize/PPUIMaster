package com.pilaipiwang.pui.widget.dialog

import android.content.Context
import android.content.res.ColorStateList
import android.util.TypedValue
import androidx.annotation.IntDef
import com.pilaipiwang.pui.R
import com.pilaipiwang.pui.alpha.PUIAlphaButton
import com.pilaipiwang.pui.alpha.PUIAlphaTextView
import com.pilaipiwang.pui.utils.PUIViewHelper

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
    private var mActionView: PUIAlphaTextView? = null
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
    fun buildActionView(dialog: PUIDialog, index: Int) {
        mActionView = generateActionButton(dialog.context, mActionText, mIconResId)
        mActionView?.setOnClickListener {
            mOnClickListener?.onClick(dialog, index)
        }
    }

    /**
     * 生成对话框底部行为按钮
     */
    private fun generateActionButton(context: Context, charSequence: CharSequence?, iconRes: Int)
            : PUIAlphaTextView {
        val textView = PUIAlphaTextView(context)
        PUIViewHelper.setBackground(textView, null)
        textView.minHeight = 0
        textView.minimumHeight = 0
        textView.setChangeAlphaWhenDisable(true)
        textView.setChangeAlphaWhenPress(true)

        val ta = context.obtainStyledAttributes(
            null, R.styleable.PUIDialogActionStyleDef,
            R.attr.pui_dialog_action_style, 0
        )
        var paddingHor = 0
        var iconSpace = 0
        var positiveTextColor: ColorStateList? = null
        var negativeTextColor: ColorStateList? = null
        for (i in 0 until ta.indexCount) {
            when (val attr = ta.getIndex(i)) {
                R.styleable.PUIDialogActionStyleDef_android_gravity ->
                    textView.gravity = ta.getInt(attr, -1)
                R.styleable.PUIDialogActionStyleDef_android_textColor ->
                    textView.setTextColor(ta.getColorStateList(attr))
                R.styleable.PUIDialogActionStyleDef_android_textSize ->
                    textView.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        ta.getDimensionPixelSize(attr, 0).toFloat()
                    )
                R.styleable.PUIDialogActionStyleDef_pui_dialog_action_button_padding_horizontal ->
                    paddingHor = ta.getDimensionPixelSize(attr, 0)
                R.styleable.PUIDialogActionStyleDef_pui_dialog_action_icon_space ->
                    iconSpace = ta.getDimensionPixelSize(attr, 0)
                R.styleable.PUIDialogActionStyleDef_android_background ->
                    PUIViewHelper.setBackground(textView, ta.getDrawable(attr))
                R.styleable.PUIDialogActionStyleDef_android_minWidth -> {
                    val minWidth = ta.getDimensionPixelSize(attr, 0)
                    textView.minWidth = minWidth
                    textView.minimumWidth = minWidth
                }
                R.styleable.PUIDialogActionStyleDef_pui_dialog_positive_action_text_color ->
                    positiveTextColor = ta.getColorStateList(attr)
                R.styleable.PUIDialogActionStyleDef_pui_dialog_negative_action_text_color ->
                    negativeTextColor = ta.getColorStateList(attr)
                R.styleable.PUIDialogActionStyleDef_android_textStyle ->
                    textView.setTypeface(null, ta.getInt(attr, -1))
            }
        }
        ta.recycle()
        textView.setPadding(paddingHor, 0, paddingHor, 0)
        if (iconRes <= 0) {
            textView.text = charSequence
        } else {
            // todo 生成带 icon 的按钮
        }
        textView.isClickable = true
        textView.isEnabled = mIsEnable

        if (mActionLevel == ACTION_LEVEL_NEGATIVE) {
            textView.setTextColor(negativeTextColor)
        } else if (mActionLevel == ACTION_LEVEL_POSITIVE) {
            textView.setTextColor(positiveTextColor)
        }
        return textView
    }

    /**
     * Dialog 行为按钮点击回调
     */
    interface ActionListener {
        fun onClick(dialog: PUIDialog, index: Int)
    }

}