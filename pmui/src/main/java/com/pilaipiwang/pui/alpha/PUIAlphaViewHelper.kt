package com.pilaipiwang.pui.alpha

import android.view.View
import androidx.annotation.NonNull
import com.pilaipiwang.pui.R
import com.pilaipiwang.pui.utils.PUIAttrsHelper
import java.lang.ref.WeakReference

/**
 * @author: vitar
 * @date:   2019/11/5
 */
internal class PUIAlphaViewHelper {

    private var mTarget: WeakReference<View>

    private var mChangeAlphaWhenPress = true
    private var mChangeAlphaWhenDisable = true

    private var mNormalAlpha: Float = 1.0f
    private var mPressedAlpha: Float = 0.5f
    private var mDisabledAlpha: Float = 0.5f

    constructor(@NonNull target: View) {
        this.mTarget = WeakReference(target)
        val pressedAlpha =
            PUIAttrsHelper.getAttrFloatValue(target.context, R.attr.pui_alpha_pressed)
        if (pressedAlpha > 0f && pressedAlpha <= 1f) {
            this.mPressedAlpha = pressedAlpha
        }
        val disabledAlpha =
            PUIAttrsHelper.getAttrFloatValue(target.context, R.attr.pui_alpha_disabled)
        if (disabledAlpha > 0f && disabledAlpha <= 1f) {
            this.mDisabledAlpha = disabledAlpha
        }
    }

    constructor(@NonNull target: View, pressedAlpha: Float, disabledAlpha: Float) {
        this.mTarget = WeakReference(target)
        this.mPressedAlpha = pressedAlpha
        this.mDisabledAlpha = disabledAlpha
    }

    /**
     * 在View 的重载函数 setPressed 中调用，通知 helper 更新 alpha
     */
    fun onPressedChanged(current: View, pressed: Boolean) {
        val target: View = mTarget.get() ?: return
        if (current.isEnabled) {
            var alpha: Float = mNormalAlpha
            if (mChangeAlphaWhenPress && pressed && current.isClickable) {
                alpha = mPressedAlpha
            }
            target.alpha = alpha
        }
    }

    /**
     * 在View 的重载函数 setEnabled 中调用，通知 helper 更新 alpha
     */
    fun onEnabledChanged(current: View, enabled: Boolean) {
        val target: View = mTarget.get() ?: return

        val alphaForIsEnable = if (mChangeAlphaWhenDisable && !enabled) {
            mDisabledAlpha
        } else {
            mNormalAlpha
        }
        if (current != target && target.isEnabled != enabled) {
            target.isEnabled = enabled
        }
        target.alpha = alphaForIsEnable
    }

    fun setChangeAlphaWhenPress(changeAlphaWhenPress: Boolean) {
        mChangeAlphaWhenPress = changeAlphaWhenPress
    }

    fun setChangeAlphaWhenDisable(changeAlphaWhenDisable: Boolean) {
        mChangeAlphaWhenDisable = changeAlphaWhenDisable
        val target: View = mTarget.get() ?: return
        onEnabledChanged(target, target.isEnabled)
    }

}