package com.pilaipiwang.pui.alpha

/**
 * @author: vitar
 * @date:   2019/11/5
 */
internal interface IPUIAlphaView {

    /**
     * 设置是否要在 press 时改变透明度onPressedChanged
     *
     * @param changeAlphaWhenPress 是否要在 press 时改变透明度
     */
    fun setChangeAlphaWhenPress(changeAlphaWhenPress: Boolean)

    /**
     * 设置是否要在 disabled 时改变透明度
     *
     * @param changeAlphaWhenDisable 是否要在 disabled 时改变透明度
     */
    fun setChangeAlphaWhenDisable(changeAlphaWhenDisable: Boolean)

}