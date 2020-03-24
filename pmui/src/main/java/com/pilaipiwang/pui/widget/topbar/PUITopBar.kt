package com.pilaipiwang.pui.widget.topbar

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.pilaipiwang.pui.R
import com.pilaipiwang.pui.alpha.PUIAlphaImageButton
import com.pilaipiwang.pui.alpha.PUIAlphaTextView
import com.pilaipiwang.pui.layout.PUIRelativeLayout
import com.pilaipiwang.pui.utils.PUIDisplayHelper
import com.pilaipiwang.pui.utils.PUIDrawableHelper
import com.pilaipiwang.pui.utils.PUIViewHelper
import kotlin.math.max

/**
 * @author: vitar
 * @date:   2019/11/7
 */
class PUITopBar : PUIRelativeLayout {

    companion object {
        const val SIDE_LEFT = 0
        const val SIDE_RIGHT = 1
    }

    @IntDef(SIDE_LEFT, SIDE_RIGHT)
    annotation class SideFlag

    private var mTopBarSeparatorColor: Int = Color.TRANSPARENT
    private var mTopBarBgColor: Int = Color.TRANSPARENT
    private var mTopBarSeparatorHeight = 0
    private var mTopBarBgWidthSeparatorDrawableCache: Drawable? = null

    /**
     * title & subTitle
     */
    private var mTitleContainerLl: LinearLayout? = null
    private var mTitleTv: TextView? = null
    private var mSubTitleTv: TextView? = null
    private var mTitleGravity: Int = Gravity.CENTER
    private var mTitleContainerPaddingHor = 0
    private var mTitleDefaultText = ""
    /**
     * title & subtitle property value
     */
    private var mTitleTextSize = 0
    private var mTitleWidthSubTitleTextSize = 0
    private var mTitleTextColor = Color.TRANSPARENT
    private var mSubTitleTextSize = 0
    private var mSubTitleTextColor = Color.TRANSPARENT
    /**
     * left right side
     */
    private var mLeftContainerLl: LinearLayout? = null
    private var mRightContainerLl: LinearLayout? = null
    private var mLeftBackIb: ImageButton? = null
    /**
     * left right side property value
     */
    private var mLeftBackNeedShow: Boolean = false
    private var mLeftBackDrawableId: Int = 0
    private var mSideImageButtonWidth = 0
    private var mSideImageButtonHeight = 0
    private var mSideImageButtonPadding = 0
    private var mSideImageButtonMarginHor = 0
    private var mSideTextViewTextSize = 0
    private var mSideTextViewTextColor = 0
    private var mSideTextViewPaddingHor = 0

    /**
     * custom center view
     */
    private var mCustomView: View? = null
    private var mCustomViewContainerFl: FrameLayout? = null
    @LayoutRes
    private var mCustomLayoutId: Int = 0
    private var mCustomPaddingHor: Int = 0

    /**
     * 默认返回按钮的点击回调
     */
    private val mDefaultLeftBackOnClickListener = OnClickListener {
        if (context is Activity) {
            (context as Activity).finish()
        }
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, R.attr.pui_topbar_style)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    ) {
        initAttrs(context!!, attrs, R.attr.pui_topbar_style)
        // 如果存在背景色，并且存在分割线颜色，则设置设置带分隔线背景
        if (mTopBarSeparatorColor != Color.TRANSPARENT && mTopBarBgColor != Color.TRANSPARENT && mTopBarSeparatorHeight > 0) {
            setBackgroundWithDivider(mTopBarBgColor, mTopBarSeparatorHeight, mTopBarSeparatorColor)
        }
        // 初始化显示
        if (mLeftBackNeedShow) {
            addLeftBackImageButton()
        }
        if (mCustomLayoutId != 0) {
            // 显示自定义布局
            val view = LayoutInflater.from(context)
                .inflate(mCustomLayoutId, makeSureRightContainerLl(), false)
            setCustomView(view)
        } else if (mTitleDefaultText.isNotBlank()) {
            // 显示 title
            setTitle(mTitleDefaultText)
        }
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyleAttrs: Int) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PUITopBar, defStyleAttrs, 0)
        // 背景色
        val bgDrawable = ta.getDrawable(R.styleable.PUITopBar_android_background)
        if (bgDrawable is ColorDrawable) {
            mTopBarBgColor = bgDrawable.color
        }
        mTopBarSeparatorColor =
            ta.getColor(R.styleable.PUITopBar_pui_topbar_separator_color, Color.TRANSPARENT)
        mTopBarSeparatorHeight =
            ta.getDimensionPixelSize(R.styleable.PUITopBar_pui_topbar_separator_height, 0)
        // 标题
        mTitleGravity = ta.getInt(R.styleable.PUITopBar_pui_topbar_title_gravity, Gravity.CENTER)
        mTitleContainerPaddingHor = ta.getDimensionPixelSize(
            R.styleable.PUITopBar_pui_topbar_title_container_padding_hor,
            0
        )
        mTitleDefaultText = ta.getString(R.styleable.PUITopBar_pui_topbar_title_text) ?: ""
        mTitleTextColor = ta.getColor(
            R.styleable.PUITopBar_pui_topbar_title_text_color,
            ContextCompat.getColor(context, R.color.pui_config_color_gray_1)
        )
        mTitleTextSize = ta.getDimensionPixelSize(
            R.styleable.PUITopBar_pui_topbar_title_text_size,
            PUIDisplayHelper.sp2px(context, 17)
        )
        mTitleWidthSubTitleTextSize = ta.getDimensionPixelSize(
            R.styleable.PUITopBar_pui_topbar_title_with_subtitle_text_size,
            PUIDisplayHelper.sp2px(context, 15)
        )
        mSubTitleTextColor = ta.getColor(
            R.styleable.PUITopBar_pui_topbar_subtitle_text_color,
            ContextCompat.getColor(context, R.color.pui_config_color_gray_3)
        )
        mSubTitleTextSize = ta.getDimensionPixelSize(
            R.styleable.PUITopBar_pui_topbar_subtitle_text_size,
            PUIDisplayHelper.sp2px(context, 12)
        )
        // 两侧 view
        mSideImageButtonWidth = ta.getDimensionPixelSize(
            R.styleable.PUITopBar_pui_topbar_side_imagebutton_width,
            PUIDisplayHelper.dp2px(context, 35)
        )
        mSideImageButtonHeight = ta.getDimensionPixelSize(
            R.styleable.PUITopBar_pui_topbar_side_imagebutton_height,
            PUIDisplayHelper.dp2px(context, 35)
        )
        mSideImageButtonPadding = ta.getDimensionPixelSize(
            R.styleable.PUITopBar_pui_topbar_side_imagebutton_padding,
            PUIDisplayHelper.dp2px(context, 5)
        )
        mSideImageButtonMarginHor = ta.getDimensionPixelSize(
            R.styleable.PUITopBar_pui_topbar_side_imagebutton_margin_hor,
            PUIDisplayHelper.dp2px(context, 3)
        )
        mSideTextViewTextSize = ta.getDimensionPixelSize(
            R.styleable.PUITopBar_pui_topbar_side_textview_text_size,
            PUIDisplayHelper.sp2px(getContext(), 16)
        )
        mSideTextViewTextColor = ta.getColor(
            R.styleable.PUITopBar_pui_topbar_side_textview_text_color,
            ContextCompat.getColor(context, R.color.pui_config_color_gray_3)
        )
        mSideTextViewPaddingHor = ta.getDimensionPixelSize(
            R.styleable.PUITopBar_pui_topbar_side_textview_padding_hor,
            PUIDisplayHelper.dp2px(getContext(), 3)
        )
        mLeftBackDrawableId = ta.getResourceId(
            R.styleable.PUITopBar_pui_topbar_left_back_drawable_id,
            R.drawable.pui_default_left_back_drawable
        )
        mLeftBackNeedShow =
            ta.getBoolean(R.styleable.PUITopBar_pui_topbar_left_back_need_show, false)
        mCustomLayoutId =
            ta.getResourceId(R.styleable.PUITopBar_pui_topbar_custom_view_layout_id, 0)
        mCustomPaddingHor =
            ta.getDimensionPixelSize(R.styleable.PUITopBar_pui_topbar_custom_view_padding_hor, 0)
        ta.recycle()
    }

    /**
     * 设置PUITopBar背景
     * @param isNeedSeparator  是否带底部分割线
     */
    private fun setBackgroundWithDivider(bgColor: Int, separtorHeight: Int, separtorColor: Int) {
        if (mTopBarBgWidthSeparatorDrawableCache == null) {
            mTopBarBgWidthSeparatorDrawableCache = PUIDrawableHelper.createItemSeparatorBg(
                separtorColor, bgColor, separtorHeight, false
            )
        }
        PUIViewHelper.setBackgroundKeepingPadding(this, mTopBarBgWidthSeparatorDrawableCache!!)
    }

    /******************************** title & subtitle *******************************/

    /**
     * 添加标题
     */
    fun setTitle(@StringRes resId: Int): TextView {
        return setTitle(context.getString(resId))
    }

    fun setTitle(charSequence: CharSequence): TextView {
        val mTextView = getTitleTv()
        mTextView.text = charSequence
        mTextView.visibility = if (charSequence.isNullOrBlank()) {
            GONE
        } else {
            VISIBLE
        }
        updateCustomViewShowStatus()
        return mTextView
    }

    /**
     * 添加副标题
     */
    fun setSubTitle(@StringRes resId: Int): TextView {
        return setSubTitle(context.getString(resId))
    }

    fun setSubTitle(charSequence: CharSequence): TextView {
        val mTextView = getSubTitleTv()
        mTextView.text = charSequence
        mTextView.visibility = if (charSequence.isNullOrBlank()) {
            GONE
        } else {
            VISIBLE
        }
        updateTitleTvStyle()
        updateCustomViewShowStatus()
        return mTextView
    }

    /**
     * 获取主标题视图
     */
    private fun getTitleTv(): TextView {
        if (mTitleTv == null) {
            mTitleTv = TextView(context)
            mTitleTv!!.gravity = Gravity.CENTER
            mTitleTv!!.isSingleLine = true
            mTitleTv!!.ellipsize = TextUtils.TruncateAt.MIDDLE
            mTitleTv!!.setTextColor(mTitleTextColor)
            // 更新标题字体大小
            updateTitleTvStyle()
            val llp =
                LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            llp.gravity = mTitleGravity
            makeSureTitleContainerLl().addView(mTitleTv, llp)
        }
        return mTitleTv!!
    }

    /**
     * 获取副标题视图
     */
    private fun getSubTitleTv(): TextView {
        if (mSubTitleTv == null) {
            mSubTitleTv = TextView(context)
            mSubTitleTv!!.gravity = Gravity.CENTER
            mSubTitleTv!!.isSingleLine = true
            mSubTitleTv!!.ellipsize = TextUtils.TruncateAt.MIDDLE
            mSubTitleTv!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSubTitleTextSize.toFloat())
            mSubTitleTv!!.setTextColor(mSubTitleTextColor)
            val llp =
                LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            llp.gravity = mTitleGravity
            makeSureTitleContainerLl().addView(mSubTitleTv, llp)
        }
        return mSubTitleTv!!
    }

    /**
     * 更新 titile Size （副标题影响主标题样式）
     */
    private fun updateTitleTvStyle() {
        if (mSubTitleTv == null || mSubTitleTv!!.text.isNullOrBlank()) {
            // 副标题为空
            mTitleTv?.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize.toFloat())
        } else {
            mTitleTv?.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleWidthSubTitleTextSize.toFloat())
        }
    }

    /**
     * 获取标题布局
     */
    private fun makeSureTitleContainerLl(): LinearLayout {
        if (mTitleContainerLl == null) {
            mTitleContainerLl = LinearLayout(context)
            mTitleContainerLl!!.orientation = LinearLayout.VERTICAL
            mTitleContainerLl!!.gravity = Gravity.CENTER
            mTitleContainerLl!!.setPadding(
                mTitleContainerPaddingHor, 0,
                mTitleContainerPaddingHor, 0
            )
            addView(
                mTitleContainerLl,
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            )
        }
        return mTitleContainerLl!!
    }

    /******************************* side view ****************************/

    /**
     * 向左侧添加 TextView
     */
    fun addLeftTextView(@StringRes resId: Int): TextView {
        return addLeftTextView(context.getString(resId))
    }

    /**
     * 向左侧添加 TextView
     */
    fun addLeftTextView(charSequence: CharSequence): TextView {
        val mTextView = generateSideTextView(charSequence)
        addLeftView(mTextView)
        return mTextView
    }

    /**
     * 添加左侧返回按钮
     */
    fun addLeftBackImageButton(): ImageButton {
        if (mLeftBackIb == null) {
            mLeftBackIb = generateSideImageButton(mLeftBackDrawableId, SIDE_LEFT)
            // 设置默认点击回调
            mLeftBackIb!!.setOnClickListener(mDefaultLeftBackOnClickListener)
        }
        addLeftView(mLeftBackIb!!)
        return mLeftBackIb!!
    }

    /**
     * 向左侧添加 ImageButton
     */
    fun addLeftImageButton(@DrawableRes resId: Int): ImageButton {
        val mImageButton = generateSideImageButton(resId, SIDE_LEFT)
        addLeftView(mImageButton)
        return mImageButton
    }

    /**
     * 向左边添加视图
     */
    fun addLeftView(view: View) {
        val vlp = view.layoutParams
        val llp: LinearLayout.LayoutParams = if (vlp == null || vlp !is LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        } else {
            vlp
        }
        if (view == mLeftBackIb) {
            if (view.parent == null) {
                // 确保返回按钮只添加一次
                makeSureLeftContainerLl().addView(view, 0, llp)
            }
        } else {
            makeSureLeftContainerLl().addView(view, llp)
        }
    }

    /**
     * 向右边添加 TextView
     */
    fun addRightTextView(@StringRes resId: Int): TextView {
        return addRightTextView(context.getString(resId))
    }

    /**
     * 向右边添加 TextView
     */
    fun addRightTextView(charSequence: CharSequence): TextView {
        val mTextView = generateSideTextView(charSequence)
        addRightView(mTextView)
        return mTextView
    }

    /**
     * 向右边添加 ImageButton
     */
    fun addRightImageButton(@DrawableRes resId: Int): ImageButton {
        val mImageButton = generateSideImageButton(resId, SIDE_RIGHT)
        addRightView(mImageButton)
        return mImageButton
    }

    /**
     * 向右边添加视图
     */
    fun addRightView(view: View) {
        val vlp = view.layoutParams
        val llp: LinearLayout.LayoutParams = if (vlp == null || vlp !is LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        } else {
            vlp
        }
        makeSureRightContainerLl().addView(view, 0, llp)
    }

    /**
     * 获取 left 布局容器
     */
    private fun makeSureLeftContainerLl(): LinearLayout {
        if (mLeftContainerLl == null) {
            mLeftContainerLl = LinearLayout(context)
            mLeftContainerLl!!.gravity = Gravity.CENTER_VERTICAL
            mLeftContainerLl!!.orientation = LinearLayout.HORIZONTAL
            val rlp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            rlp.addRule(ALIGN_PARENT_LEFT)
            rlp.addRule(CENTER_VERTICAL)
            addView(mLeftContainerLl, rlp)
        }
        return mLeftContainerLl!!
    }

    /**
     * 获取 right 布局容器
     */
    private fun makeSureRightContainerLl(): LinearLayout {
        if (mRightContainerLl == null) {
            mRightContainerLl = LinearLayout(context)
            mRightContainerLl!!.gravity = Gravity.CENTER_VERTICAL
            mRightContainerLl!!.orientation = LinearLayout.HORIZONTAL
            val rlp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            rlp.addRule(ALIGN_PARENT_RIGHT)
            rlp.addRule(CENTER_VERTICAL)
            addView(mRightContainerLl, rlp)
        }
        return mRightContainerLl!!
    }

    /**
     * 生成两侧 ImageButton
     */
    private fun generateSideImageButton(@DrawableRes resId: Int, @SideFlag side: Int): ImageButton {
        val mImageButton = PUIAlphaImageButton(context)
        mImageButton.setBackgroundColor(Color.TRANSPARENT)
        mImageButton.setImageResource(resId)
        mImageButton.scaleType = ImageView.ScaleType.FIT_CENTER
        val llp = LinearLayout.LayoutParams(mSideImageButtonWidth, mSideImageButtonHeight)
        if (side == SIDE_LEFT) {
            llp.rightMargin = mSideImageButtonMarginHor
        } else {
            llp.leftMargin = mSideImageButtonMarginHor
        }
        mImageButton.layoutParams = llp
        mImageButton.setPadding(
            mSideImageButtonPadding, mSideImageButtonPadding,
            mSideImageButtonPadding, mSideImageButtonPadding
        )
        return mImageButton
    }

    /**
     * 生成两侧 TextView
     */
    private fun generateSideTextView(charSequence: CharSequence): TextView {
        val mTextView = PUIAlphaTextView(context)
        mTextView.text = charSequence
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSideTextViewTextSize.toFloat())
        mTextView.setTextColor(mSideTextViewTextColor)
        mTextView.setPadding(mSideTextViewPaddingHor, 0, mSideTextViewPaddingHor, 0)
        mTextView.isSingleLine = true
        mTextView.ellipsize = TextUtils.TruncateAt.MIDDLE
        mTextView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return mTextView
    }

    /***************** custom center view ********************/
    fun setCustomView(view: View?) {
        if (view == null || mCustomView == view) {
            return
        }
        // 设置自定义View之后，隐藏title
        if (mCustomView != null) {
            makeSureCustomViewContainerFl().removeView(mCustomView)
        }
        mCustomView = view
        val vlp = mCustomView!!.layoutParams
        val params: FrameLayout.LayoutParams = if (vlp == null) {
            FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_VERTICAL
            )
        } else if (vlp !is FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams(vlp.width, vlp.height, Gravity.CENTER_VERTICAL)
        } else {
            vlp.gravity = Gravity.CENTER_VERTICAL
            vlp
        }
        makeSureCustomViewContainerFl().addView(mCustomView, params)
    }


    /**
     * 获取 right 布局容器
     */
    private fun makeSureCustomViewContainerFl(): FrameLayout {
        if (mCustomViewContainerFl == null) {
            mCustomViewContainerFl = FrameLayout(context)
            mCustomViewContainerFl!!.setPadding(mCustomPaddingHor, 0, mCustomPaddingHor, 0)
            val rlp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            rlp.addRule(CENTER_IN_PARENT)
            addView(mCustomViewContainerFl, rlp)
        }
        return mCustomViewContainerFl!!
    }

    /**
     * 更新 customView 和 title 的显示状态
     */
    private fun updateCustomViewShowStatus() {
        if (mCustomView != null) {
            mCustomViewContainerFl?.visibility = View.VISIBLE
            mTitleContainerLl?.visibility = View.GONE
        } else {
            mCustomViewContainerFl?.visibility = View.GONE
            mTitleContainerLl?.visibility = View.VISIBLE
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 测量title容器布局
        if (mTitleContainerLl != null) {
            val isleftEmpty = mLeftContainerLl == null || mLeftContainerLl!!.childCount == 0
            val isRightEmpty = mRightContainerLl == null || mRightContainerLl!!.childCount == 0
            // 因为两侧容器有 paddingHor 存在，所以就算不存在子View也会有宽度。
            val leftViewWidth = if (isleftEmpty) 0 else mLeftContainerLl!!.measuredWidth
            val rightViewWidth = if (isRightEmpty) 0 else mRightContainerLl!!.measuredWidth
            // 计算 titleContainer 的最大宽度
            val titleContainerWidth: Int
            titleContainerWidth = if (mTitleGravity == Gravity.CENTER) {
                // 标题水平居中
                MeasureSpec.getSize(widthMeasureSpec) - max(
                    leftViewWidth,
                    rightViewWidth
                ) * 2 - paddingLeft - paddingRight
            } else {
                // 标题非水平居中，左右两侧的占位按实际计算即可
                MeasureSpec.getSize(widthMeasureSpec) - leftViewWidth - rightViewWidth - paddingLeft - paddingRight
            }
            val titleContainerWidthMeasureSpec =
                MeasureSpec.makeMeasureSpec(titleContainerWidth, MeasureSpec.EXACTLY)
            mTitleContainerLl!!.measure(titleContainerWidthMeasureSpec, heightMeasureSpec)
        }
        // 测量自定义容器布局
        if (mCustomView != null) {
            // 当TopBar存在中间自定义视图时，则隐藏掉title内容。所以不需要测量 mTitleContainerLl
            val isleftEmpty = mLeftContainerLl == null || mLeftContainerLl!!.childCount == 0
            val isRightEmpty = mRightContainerLl == null || mRightContainerLl!!.childCount == 0
            // 因为两侧容器有 paddingHor 存在，所以就算不存在子View也会有宽度。
            val leftViewWidth = if (isleftEmpty) 0 else mLeftContainerLl!!.measuredWidth
            val rightViewWidth = if (isRightEmpty) 0 else mRightContainerLl!!.measuredWidth
            val customViewContainerWidth: Int =
                MeasureSpec.getSize(widthMeasureSpec) - leftViewWidth - rightViewWidth - paddingLeft - paddingRight
            val titleContainerWidthMeasureSpec =
                MeasureSpec.makeMeasureSpec(customViewContainerWidth, MeasureSpec.EXACTLY)
            mCustomViewContainerFl!!.measure(titleContainerWidthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        // 布局title容器
        if (mTitleContainerLl != null && mCustomView == null) {
            val titleContainerViewTop = (b - t - mTitleContainerLl!!.measuredHeight) / 2
            var titleContainerViewLeft = paddingLeft
            if (mTitleGravity and Gravity.HORIZONTAL_GRAVITY_MASK == Gravity.CENTER_HORIZONTAL) {
                // 标题水平居中
                titleContainerViewLeft = (r - l - mTitleContainerLl!!.measuredWidth) / 2
            } else {
                // 标题非水平居中
                // 计算左侧 View 的总宽度
                val isLeftEmpty = mLeftContainerLl == null || mLeftContainerLl!!.childCount == 0
                val left = if (isLeftEmpty) 0 else mLeftContainerLl!!.measuredWidth
                titleContainerViewLeft += left
            }
            mTitleContainerLl!!.layout(
                titleContainerViewLeft, titleContainerViewTop,
                titleContainerViewLeft + mTitleContainerLl!!.measuredWidth,
                titleContainerViewTop + mTitleContainerLl!!.measuredHeight
            )
        } else if (mCustomView != null) {
            val customViewContainerTop = (b - t - mCustomViewContainerFl!!.measuredHeight) / 2
            var customViewContainerLeft = paddingLeft
            val isLeftEmpty = mLeftContainerLl == null || mLeftContainerLl!!.childCount == 0
            val left = if (isLeftEmpty) 0 else mLeftContainerLl!!.measuredWidth
            customViewContainerLeft += left
            mCustomViewContainerFl!!.layout(
                customViewContainerLeft, customViewContainerTop,
                customViewContainerLeft + mCustomViewContainerFl!!.measuredWidth,
                customViewContainerTop + mCustomViewContainerFl!!.measuredHeight
            )
        }
    }

}