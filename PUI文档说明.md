#### 一. PUIAlphaXX 系列控件

1. 继承自各常用系统控件，如 PUIAlphaButton，继承于 AppCompatButton。支持**点击**和**disable**时动态设置半透明度，半透明度通过全局主题设置属性，是 PUI 中最基础的组件。

   > ```kotlin
   > <!-- alpha widget 全局属性 -->
   > <item name="pui_alpha_pressed">0.8</item>
   > <item name="pui_alpha_disabled">0.8</item>
   > ```

#### 二. PUIXX 系列控件(layout)

继承各常用 ViewGroup 系统控件，可用于圆角下划线的布局

1. 对外暴漏包含:

   > * PUIFrameLayout
   > * PUILinearLayout
   > * PUIRelativeLayout
   
2. 使用：

   > ```kotlin
   > <attr name="android:maxWidth" />    			// 最大宽度
   > <attr name="android:maxHeight" />				// 最大高度
   > <attr name="android:minWidth" />				// 最小宽度
   > <attr name="android:minHeight" />				// 最小高度
   > <attr name="pui_bottomDividerHeight" />			// 下分割线高度
   > <attr name="pui_bottomDividerColor" />			// 下分割线颜色
   > <attr name="pui_bottomDividerInsetLeft" />		// 下分割线绘制时左留白宽度
   > <attr name="pui_bottomDividerInsetRight" />		// 下分割线绘制时右留白宽度
   > <attr name="pui_topDividerHeight" />			// 上分割线高度
   > <attr name="pui_topDividerColor" />				// 上分割线颜色
   > <attr name="pui_topDividerInsetLeft" />			// 上分割线绘制时左留白宽度
   > <attr name="pui_topDividerInsetRight" />		// 上分割线绘制时右留白宽度
   > <attr name="pui_leftDividerWidth" />			// 左分割线宽度
   > <attr name="pui_leftDividerColor" />			// 左分割线颜色
   > <attr name="pui_leftDividerInsetTop" />			// 左分割线绘制时上留白高度
   > <attr name="pui_leftDividerInsetBottom" />		// 左分割线绘制时下留白高度
   > <attr name="pui_rightDividerWidth" />			// 右分割线宽度
   > <attr name="pui_rightDividerColor" />			// 右分割线颜色
   > <attr name="pui_rightDividerInsetTop" />		// 右分割线绘制时上留白高度
   > <attr name="pui_rightDividerInsetBottom" />		// 右分割线绘制时下留白高度
   > <attr name="pui_borderColor" />					// 描边颜色
   > <attr name="pui_borderWidth" />					// 描边宽度
   > <attr name="pui_radius" />						// 圆角大小
   > <attr name="pui_hideRadiusSide" format="enum">	// 圆角隐藏标识
   > 	<enum name="none" value="0" />				// 默认 不隐藏圆角
   >    	<enum name="top" value="1" />				// 隐藏上边圆角
   >    	<enum name="right" value="2" />				// 隐藏右边圆角
   >    	<enum name="bottom" value="3" />			// 隐藏下边圆角
   >    	<enum name="left" value="4" />				// 隐藏左边圆角
   >    </attr>
   > <attr name="pui_shadowElevation" />				// 阴影高度
   > <attr name="pui_shadowAlpha" />					// 阴影透明度
   > <attr name="pui_useThemeGeneralShadowElevation" /> // 是否使用默认全局shadowAlpha和puiShadowElevation，默认false
   > ```

#### 三. PUIRoundTextView

1. 继承自 PUIAlphaTextView，所以默认情况下支持 PUIAlphaTextView 的点击效果和 disable 效果，支持各种姿势的圆角/描边/渐变，PUIRoundTextView 拓展属性如下：

   > ```kotlin
   > <declare-styleable name="PUIRoundTextView">
   >     <attr name="pui_borderColor" />			// 描边颜色
   >     <attr name="pui_borderWidth" />			// 描边宽度
   >     <attr name="pui_isRadiusAdjustBounds" />  	// 是否自适应圆角（圆角大小为1/2短边）
   >     <attr name="pui_radius" />					// 圆角大小
   >     <attr name="pui_radiusTopLeft" />			// 左上圆角大小（会覆盖 pui_radius）
   >     <attr name="pui_radiusTopRight" />			// 右上圆角大小（会覆盖 pui_radius）
   >     <attr name="pui_radiusBottomLeft" />		// 左下圆角大小（会覆盖 pui_radius）
   >     <attr name="pui_radiusBottomRight" />		// 右下圆角大小（会覆盖 pui_radius）
   >     <attr name="pui_gradientOrientation" />	// 渐变角度（主色 primaryColor 和辅色 secondaryColor 同时存在时才生效，默认从左到右）
   >     <attr name="pui_primaryColor" />			// 背景主色
   >     <attr name="pui_secondaryColor" />			// 背景辅色
   > 	<attr name="pui_disableAlpha" />		// disable 时半透明度
   >     <attr name="pui_disableColor" />			// disable 时背景色，会覆盖 disableAlpha
   > </declare-styleable>
   > ```

#### 四. PUITopBar

顶部状态栏控件，继承于 ReleaseLayout。

![TopBar](C:\Users\vitar\Desktop\TopBar.png)

```kotlin
<declare-styleable name="PUITopBar">
	// 背景色
    <attr name="android:background" />	
	// 标题居左|居中
    <attr name="pui_topbar_title_gravity" format="enum">
        <enum name="left_center" value="19" />
        <enum name="center" value="17" />
    </attr>
    // 底部分隔线颜色，只有在 backgroud 设置而了纯色时才会生效
	<attr name="pui_topbar_separator_color" format="color" />
    // 底部分隔线高度
	<attr name="pui_topbar_separator_height" format="dimension" />
	// 标题容器水平内间距padding 
    <attr name="pui_topbar_title_container_padding_hor" format="dimension" />
	// 标题文案
    <attr name="pui_topbar_title_text" format="string" />
	// 标题容器标题字体大小
    <attr name="pui_topbar_title_text_size" format="dimension" />
    // 标题容器标题字体颜色
	<attr name="pui_topbar_title_text_color" format="color" />
	// 存在副标题时标题文字字体大小
    <attr name="pui_topbar_title_with_subtitle_text_size" format="dimension" />
    // 副标题字体大小    
	<attr name="pui_topbar_subtitle_text_size" format="dimension" />
    // 副标题字体颜色
	<attr name="pui_topbar_subtitle_text_color" format="color" />
    // 返回按钮是否显示    
	<attr name="pui_topbar_left_back_need_show" format="boolean" />
	// 返回按钮默认icon
	<attr name="pui_topbar_left_back_drawable_id" format="reference" />		
	// 两边容器LinearLayout水平内间距padding
	<attr name="pui_topbar_side_container_padding_hor" format="dimension" />
	// 边容器ImageView宽度
	<attr name="pui_topbar_side_imagebutton_width" format="dimension" />
	// 边容器ImageView高度
	<attr name="pui_topbar_side_imagebutton_height" format="dimension" />
	// 边容器ImageView的padding值
	<attr name="pui_topbar_side_imagebutton_padding" format="dimension" />
	// 边容器ImageView的水平margin
	<attr name="pui_topbar_side_imagebutton_margin_hor" format="dimension" />
	// 边容器TextView字体大小
	<attr name="pui_topbar_side_textview_text_size" format="dimension" />
	// 边容器TextView字体颜色
	<attr name="pui_topbar_side_textview_text_color" format="color" />
	// 边容器TextView水平内间距
	<attr name="pui_topbar_side_textview_padding_hor" format="dimension" />
	// 自定义View布局id（如搜索栏中间部分，可通过该属性植入TopBar）
    <attr name="pui_topbar_custom_view_layout_id" format="reference" />
	// 自定义View水平内间距
    <attr name="pui_topbar_custom_view_padding_hor" format="dimension" />
</declare-styleable>
```

#### 五.PUIMultiStatusLayout

1. 多状态布局，布局类型包括：

   a. 内容视图； b. 加载中； c. 空状态视图；d. 网络异常视图；e. 服务器错误异常视图；

异常视图UI规范如下：

![MultiStateLayout](C:\Users\vitar\Desktop\PUI文档说明\MultiStateLayout.png)

```kotlin
<declare-styleable name="PUITopBar">
    // 图标icon宽度
    <attr name="pui_multistates_icon_width" format="dimension" />
    // 图标icon高度
    <attr name="pui_multistates_icon_height" format="dimension" />
    // 主标题text的字体大小
    <attr name="pui_multistates_text_size" format="dimension" />
    // 主标题text的字体颜色
    <attr name="pui_multistates_text_color" format="color" />
    // 副标题text的字体大小
    <attr name="pui_multistates_subtext_size" format="dimension" />
    // 副标题text的字体颜色
    <attr name="pui_multistates_subtext_color" format="color" />
    // 底部点击按钮文本内容
    <attr name="pui_multistates_action_text" format="boolean" />
    // 加载布局的resId
    <attr name="pui_multistates_loading_view_layoutId" format="reference" />
    // 空状态下icon图片id
    <attr name="pui_multistates_empty_resId" format="reference" />
    // 空状态下主标题text文案
    <attr name="pui_multistates_empty_text" format="string" />
    // 空状态下副标题subtext文案
    <attr name="pui_multistates_empty_subtext" format="string" />
    // 空状态下是否显示底部点击按钮
    <attr name="pui_multistates_empty_is_action_show" format="boolean" />
    // 网络异常状态下icon图片id
    <attr name="pui_multistates_netoff_resId" format="reference" />
    // 网络异常状态下主标题text文案
    <attr name="pui_multistates_netoff_text" format="string" />
    // 网络异常状态下副标题subtext文案
    <attr name="pui_multistates_netoff_subtext" format="string" />
    // 网络异常状态下是否显示底部点击按钮
    <attr name="pui_multistates_netoff_is_action_show" format="boolean" />
    // 错误状态下icon图片id
    <attr name="pui_multistates_error_resId" format="reference" />
    // 错误状态下主标题text文案
    <attr name="pui_multistates_error_text" format="string" />
    // 错误状态下副标题subtext文案
    <attr name="pui_multistates_error_subtext" format="string" />
    // 错误状态下是否显示底部点击按钮
    <attr name="pui_multistates_error_is_action_show" format="boolean" />
</declare-styleable>
```

#### 六.弹窗控件

1. 通用样式的弹窗控件。

![](C:\Users\vitar\Desktop\PUI文档说明\PUIDialog.png)

通过全局主题定制样式如下：

```kotlin
// dialog 背景半透明度
<attr name="pui_dialog_background_dim_amount" format="float" />
// dialog 最小宽度
<attr name="pui_dialog_min_width" format="dimension" />
// dialog 最大宽度
<attr name="pui_dialog_max_width" format="dimension" />
// 标题Title样式
<attr name="pui_dialog_title_style" format="reference" />
// dialog 下 MessageDialog 中内容文本样式
<attr name="pui_dialog_message_content_style" format="reference" />
// 底部按钮容器Action container样式
<attr name="pui_dialog_action_container_style" format="reference" />
// 底部普通按钮样式
<attr name="pui_dialog_action_style" format="reference" />
// 底部消极按钮(PUIRoundTextView)样式
<attr name="pui_dialog_action_nevative_style" format="reference" />
// 底部积极按钮(PUIRoundTextView)样式
<attr name="pui_dialog_action_position_style" format="reference" />
```