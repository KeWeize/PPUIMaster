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

    @BindView(R.id.topbar2)
    lateinit var mTopBar2: PUITopBar

    @BindView(R.id.topbar3)
    lateinit var mTopBar3: PUITopBar

    override fun attrLayoutId(): Int = R.layout.activity_topbar

    override fun initView() {
        mTopBar.setTitle("我的订单")
        mTopBar.addLeftBackImageButton().setOnClickListener {
            onBackPressed()
        }
        mTopBar.addRightTextView("编辑").setOnClickListener {
            Toast.makeText(this, "点击了编辑", Toast.LENGTH_SHORT).show()
        }

        mTopBar2.setTitle("个人中心")
        mTopBar2.addLeftBackImageButton().setOnClickListener {
            onBackPressed()
        }
        mTopBar2.addRightImageButton(R.drawable.ic_more_gray).setOnClickListener {
            Toast.makeText(this, "点击了更多", Toast.LENGTH_SHORT).show()
        }
        mTopBar2.addRightImageButton(R.drawable.ic_setting_gray).setOnClickListener {
            Toast.makeText(this, "点击了设置", Toast.LENGTH_SHORT).show()
        }

        mTopBar3.addLeftBackImageButton().setOnClickListener {
            onBackPressed()
        }
        mTopBar3.setTitle("进货车")
        val mTopBar3RightTv = mTopBar3.addRightTextView("完成")
        mTopBar3RightTv.setOnClickListener {
            val tag: Int = if (mTopBar3RightTv.tag == null) {
                0
            } else {
                mTopBar3RightTv.tag as Int
            }
            mTopBar3RightTv.tag = (tag + 1) % 2
            mTopBar3RightTv.text = if (tag == 0) {
                "完成"
            } else {
                "编辑"
            }
        }

    }

}