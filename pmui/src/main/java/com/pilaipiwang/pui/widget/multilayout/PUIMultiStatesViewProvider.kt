package com.pilaipiwang.pui.widget.multilayout

import android.content.Context
import android.view.View

/**
 * @author: vitar
 * @date:   2019/11/11
 */
interface PUIMultiStatesViewProvider {

    /**
     * 提供加载中状态视图
     */
    fun attachedLoadingLayout(context: Context): View?

    /**
     * 提供空状态视图
     */
    fun attachedEmptyLayout(context: Context): View?

    /**
     * 提供网络异常视图
     */
    fun attachedNetOffLayout(context: Context): View?

    /**
     * 提供错误状态视图
     */
    fun attachedErrorLayout(context: Context): View?

}