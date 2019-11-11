package com.pilaipiwang.ppuimaster.widget

import android.view.View
import android.widget.Toast
import butterknife.BindView
import com.pilaipiwang.ppuimaster.R
import com.pilaipiwang.ppuimaster.base.BaseActivity
import com.pilaipiwang.pui.widget.topbar.PUITopBar

/**
 * @author: vitar
 * @date:   2019/11/7
 */
class PUITopBarActivity : BaseActivity() {

    @BindView(R.id.topbar)
    lateinit var mTopBar: PUITopBar

    override fun attrLayoutId(): Int = R.layout.activity_topbar

    override fun initView() {
        mTopBar.setTitle("我的订单我的订单我的订单")
        mTopBar.addLeftBackImageButton().setOnClickListener {
            Toast.makeText(this, "点击了返回", Toast.LENGTH_SHORT).show()
        }
        mTopBar.addLeftTextView("返回")

        mTopBar.addRightTextView("发布")
        mTopBar.addRightImageButton(R.drawable.ic_setting)
        mTopBar.addRightImageButton(R.drawable.ic_wechat)

    }

}