package com.pilaipiwang.pui.layout

import androidx.annotation.IntDef

/**
 * @author: vitar
 * @date:   2019/11/5
 */
const val HIDE_RADIUS_SIDE_NONE = 0
const val HIDE_RADIUS_SIDE_TOP = 1
const val HIDE_RADIUS_SIDE_RIGHT = 2
const val HIDE_RADIUS_SIDE_BOTTOM = 3
const val HIDE_RADIUS_SIDE_LEFT = 4

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    value = [HIDE_RADIUS_SIDE_NONE,
        HIDE_RADIUS_SIDE_TOP,
        HIDE_RADIUS_SIDE_RIGHT,
        HIDE_RADIUS_SIDE_BOTTOM,
        HIDE_RADIUS_SIDE_LEFT]
)
annotation class HideRadiusSide