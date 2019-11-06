package com.pilaipiwang.pui.layout

import android.view.View
import androidx.annotation.ColorInt

/**
 * @author: vitar
 * @date:   2019/11/5
 */
internal interface IPUILayout {

    /**
     * limit the width of a layout
     *
     * @param widthLimit
     * @return
     */
    fun setWidthLimit(widthLimit: Int): Boolean

    /**
     * limit the height of a layout
     *
     * @param heightLimit
     * @return
     */
    fun setHeightLimit(heightLimit: Int): Boolean

    /**
     * use the shadow elevation from the theme
     */
    fun setUseThemeGeneralShadowElevation()

    /**
     * determine if the outline contain the padding area, usually false
     *
     * @param outlineExcludePadding
     */
    fun setOutlineExcludePadding(outlineExcludePadding: Boolean)

    /**
     * See [android.view.View.setElevation]
     *
     * @param elevation
     */
    fun setShadowElevation(elevation: Int)

    /**
     * See [View.getElevation]
     *
     * @return
     */
    fun getShadowElevation(): Int

    /**
     * set the outline alpha, which will change the shadow
     *
     * @param shadowAlpha
     */
    fun setShadowAlpha(shadowAlpha: Float)

    /**
     * get the outline alpha we set
     *
     * @return
     */
    fun getShadowAlpha(): Float

    /**
     * @param shadowColor opaque color
     * @return
     */
    fun setShadowColor(shadowColor: Int)

    /**
     * @return opaque color
     */
    fun getShadowColor(): Int

    /**
     * set the layout radius
     *
     * @param radius
     */
    fun setRadius(radius: Int)

    /**
     * set the layout radius with one or none side been hidden
     *
     * @param radius
     * @param hideRadiusSide
     */
    fun setRadius(radius: Int, @HideRadiusSide hideRadiusSide: Int)

    /**
     * get the layout radius
     *
     * @return
     */
    fun getRadius(): Int

    /**
     * inset the outline if needed
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    fun setOutlineInset(left: Int, top: Int, right: Int, bottom: Int)

    /**
     * the shadow elevation only work after L, so we provide a downgrading compatible solutions for android 4.x
     * usually we use border, but the border may be redundant for android L+. so will not show border default,
     * if your designer like the border exists with shadow, you can call setShowBorderOnlyBeforeL(false)
     *
     * @param showBorderOnlyBeforeL
     */
    fun setShowBorderOnlyBeforeL(showBorderOnlyBeforeL: Boolean)

    /**
     * in some case, we maybe hope the layout only have radius in one side.
     * but there is no convenient way to write the code like canvas.drawPath,
     * so we take another way that hide one radius side
     *
     * @param hideRadiusSide
     */
    fun setHideRadiusSide(@HideRadiusSide hideRadiusSide: Int)

    /**
     * get the side that we have hidden the radius
     *
     * @return
     */
    fun getHideRadiusSide(): Int

    /**
     * this method will determine the radius and shadow.
     *
     * @param radius
     * @param shadowElevation
     * @param shadowAlpha
     */
    fun setRadiusAndShadow(radius: Int, shadowElevation: Int, shadowAlpha: Float)

    /**
     * this method will determine the radius and shadow with one or none side be hidden
     *
     * @param radius
     * @param hideRadiusSide
     * @param shadowElevation
     * @param shadowAlpha
     */
    fun setRadiusAndShadow(
        radius: Int, @HideRadiusSide hideRadiusSide: Int, shadowElevation: Int,
        shadowAlpha: Float
    )

    /**
     * this method will determine the radius and shadow (support shadowColor if after android 9)with one or none side be hidden
     *
     * @param radius
     * @param hideRadiusSide
     * @param shadowElevation
     * @param shadowColor
     * @param shadowAlpha
     */
    fun setRadiusAndShadow(
        radius: Int, @HideRadiusSide hideRadiusSide: Int, shadowElevation: Int,
        shadowColor: Int, shadowAlpha: Float
    )

    /**
     * border color, if you don not set it, the layout will not draw the border
     *
     * @param borderColor
     */
    fun setBorderColor(@ColorInt borderColor: Int)

    /**
     * border width, default is 1px, usually no need to set
     *
     * @param borderWidth
     */
    fun setBorderWidth(borderWidth: Int)

    /**
     * config the top divider
     *
     * @param topInsetLeft
     * @param topInsetRight
     * @param topDividerHeight
     * @param topDividerColor
     */
    fun updateTopDivider(
        topInsetLeft: Int, topInsetRight: Int, topDividerHeight: Int,
        topDividerColor: Int
    )

    /**
     * config the bottom divider
     *
     * @param bottomInsetLeft
     * @param bottomInsetRight
     * @param bottomDividerHeight
     * @param bottomDividerColor
     */
    fun updateBottomDivider(
        bottomInsetLeft: Int, bottomInsetRight: Int, bottomDividerHeight: Int,
        bottomDividerColor: Int
    )

    /**
     * config the left divider
     *
     * @param leftInsetTop
     * @param leftInsetBottom
     * @param leftDividerWidth
     * @param leftDividerColor
     */
    fun updateLeftDivider(
        leftInsetTop: Int, leftInsetBottom: Int, leftDividerWidth: Int,
        leftDividerColor: Int
    )

    /**
     * config the right divider
     *
     * @param rightInsetTop
     * @param rightInsetBottom
     * @param rightDividerWidth
     * @param rightDividerColor
     */
    fun updateRightDivider(
        rightInsetTop: Int, rightInsetBottom: Int, rightDividerWidth: Int,
        rightDividerColor: Int
    )

    /**
     * show top divider, and hide others
     *
     * @param topInsetLeft
     * @param topInsetRight
     * @param topDividerHeight
     * @param topDividerColor
     */
    fun onlyShowTopDivider(
        topInsetLeft: Int, topInsetRight: Int, topDividerHeight: Int,
        topDividerColor: Int
    )

    /**
     * show bottom divider, and hide others
     *
     * @param bottomInsetLeft
     * @param bottomInsetRight
     * @param bottomDividerHeight
     * @param bottomDividerColor
     */
    fun onlyShowBottomDivider(
        bottomInsetLeft: Int, bottomInsetRight: Int, bottomDividerHeight: Int,
        bottomDividerColor: Int
    )

    /**
     * show left divider, and hide others
     *
     * @param leftInsetTop
     * @param leftInsetBottom
     * @param leftDividerWidth
     * @param leftDividerColor
     */
    fun onlyShowLeftDivider(
        leftInsetTop: Int, leftInsetBottom: Int, leftDividerWidth: Int,
        leftDividerColor: Int
    )

    /**
     * show right divider, and hide others
     *
     * @param rightInsetTop
     * @param rightInsetBottom
     * @param rightDividerWidth
     * @param rightDividerColor
     */
    fun onlyShowRightDivider(
        rightInsetTop: Int, rightInsetBottom: Int, rightDividerWidth: Int,
        rightDividerColor: Int
    )

    /**
     * after config the border, sometimes we need change the alpha of divider with animation,
     * so we provide a method to individually change the alpha
     *
     * @param dividerAlpha [0, 255]
     */
    fun setTopDividerAlpha(dividerAlpha: Int)

    /**
     * @param dividerAlpha [0, 255]
     */
    fun setBottomDividerAlpha(dividerAlpha: Int)

    /**
     * @param dividerAlpha [0, 255]
     */
    fun setLeftDividerAlpha(dividerAlpha: Int)

    /**
     * @param dividerAlpha [0, 255]
     */
    fun setRightDividerAlpha(dividerAlpha: Int)

    /**
     * only available before android L
     *
     * @param color
     */
    fun setOuterNormalColor(color: Int)
}
