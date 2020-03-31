package com.pilaipiwang.ppuimaster.multi

import android.content.Context
import android.util.AttributeSet
import com.airbnb.lottie.LottieAnimationView
import com.pilaipiwang.pui.widget.multilayout.IPUIMultiStatesLoadAnim

/**
 * @author: vitar
 * @date:   2020/3/26
 */
class PPMultiLoadAnimView : LottieAnimationView, IPUIMultiStatesLoadAnim {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun startLoadingAnim() {
        playAnimation()
    }

    override fun stopLoadingAnim() {
        pauseAnimation()
    }
}