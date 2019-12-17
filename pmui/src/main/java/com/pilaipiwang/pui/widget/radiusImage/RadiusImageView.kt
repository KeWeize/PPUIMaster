package com.pilaipiwang.pui.widget.radiusImage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * @author  vitar
 * @date    2019/11/19
 */
class RadiusImageView : AppCompatImageView {

    private val DEFAULT_BORDER_COLOR = Color.GRAY
    private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
    private val COLOR_DRAWABLE_DIMEN = 2


    private var mCornerRadius = 0
    private var mBorderWidth: Int = 0
    private var mBorderColor = Color.TRANSPARENT
    private var mIsCircle = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context!!, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
    }

    private fun setupBitmap() {
    }

    private fun getBitmap(): Bitmap? {
        val drawable = getDrawable() ?: return null

        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap ?: return null
            val bmWidth = bitmap.width
            val bmHeight = bitmap.height
            if (bmWidth == 0 || bmHeight == 0) {
                return null
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                val minScaleX = minimumWidth / bmWidth
                val minScaleY = minimumHeight / bmHeight
                if (minScaleX > 1 || minScaleY > 1) {
                    val scale = Math.max(minScaleX, minScaleY)
                    val matrix = Matrix()
                    matrix.postScale(scale.toFloat(), scale.toFloat())

                    return Bitmap.createBitmap(bitmap, 0, 0, bmWidth, bmHeight, matrix, false)
                }
            }
            return bitmap
        }

        try {
            val bitmap = if (drawable is ColorDrawable) {
                Bitmap.createBitmap(COLOR_DRAWABLE_DIMEN, COLOR_DRAWABLE_DIMEN, BITMAP_CONFIG)
            } else {
                Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    BITMAP_CONFIG
                )
            }
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        } catch (e : Exception) {
            e.printStackTrace()
            return null
        }
    }

}