package com.pilaipiwang.pui.alpha

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * @author: vitar
 * @date:   2019/11/5
 */
open class PUIAlphaFrameLayout : FrameLayout, IPUIAlphaView {

    private var mAlphaViewHelper: PUIAlphaViewHelper? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun setPressed(pressed: Boolean) {
        super.setPressed(pressed)
        getAlphaViewHelper()?.onPressedChanged(this, pressed)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        getAlphaViewHelper()?.onEnabledChanged(this, enabled)
    }

    override fun setChangeAlphaWhenPress(changeAlphaWhenPress: Boolean) {
        getAlphaViewHelper()?.setChangeAlphaWhenPress(changeAlphaWhenPress)
    }

    override fun setChangeAlphaWhenDisable(changeAlphaWhenDisable: Boolean) {
        getAlphaViewHelper()?.setChangeAlphaWhenDisable(changeAlphaWhenDisable)
    }

    override fun setPressedAlpha(alpha: Float) {
        getAlphaViewHelper()?.setPressedAlpha(alpha)
    }

    override fun setDisableAlpha(alpha: Float) {
        getAlphaViewHelper()?.setDisableAlpha(alpha)
    }

    private fun getAlphaViewHelper(): PUIAlphaViewHelper? {
        if (mAlphaViewHelper == null) {
            mAlphaViewHelper = PUIAlphaViewHelper(this)
        }
        return mAlphaViewHelper
    }

}