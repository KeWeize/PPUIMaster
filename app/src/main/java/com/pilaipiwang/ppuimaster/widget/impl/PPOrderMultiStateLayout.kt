package com.pilaipiwang.ppuimaster.widget.impl

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.pilaipiwang.pui.widget.multilayout.PUIMultiStatesAbstract

/**
 * @author: vitar
 * @date:   2020/3/25
 */
class PPOrderMultiStateLayout : PUIMultiStatesAbstract {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun attachMultiStatesViewProvider(): IMultiStatesViewProvider =
        object : IMultiStatesViewProvider {
            override fun attachedLoadingLayout(): View? {
                val textView = TextView(context)
                textView.text = "我是加载中状态"
                return textView
            }

            override fun attachedEmptyLayout(): View? {
                val textView = TextView(context)
                textView.text = "我是空状态"
                return textView
            }

            override fun attachedExceptionLayout(): View? {
                val textView = TextView(context)
                textView.text = "我是异常状态"
                return textView
            }

        }

}