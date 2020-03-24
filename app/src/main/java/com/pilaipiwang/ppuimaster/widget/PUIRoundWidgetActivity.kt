package com.pilaipiwang.ppuimaster.widget

import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import com.pilaipiwang.ppuimaster.R
import com.pilaipiwang.ppuimaster.base.BaseActivity

/**
 * @author: vitar
 * @date:   2019/11/5
 */
class PUIRoundWidgetActivity : BaseActivity() {

    @BindView(R.id.btn_submit)
    lateinit var mSubmitBtn: TextView
//    @BindView(R.id.tv_submit)
//    lateinit var mSubmitTv: TextView
//    @BindView(R.id.fl_container)
//    lateinit var mContainerFl: View
//    @BindView(R.id.ll_container)
//    lateinit var mContainerLl: View
//    @BindView(R.id.rl_container)
//    lateinit var mContainerRl: View
//    @BindView(R.id.tv_setting)
//    lateinit var mSettingTv: TextView

    @BindView(R.id.btn_enable)
    lateinit var mEnableBtn: Button

    override fun initView() {
        mEnableBtn.tag = true
    }

    override fun attrLayoutId(): Int = R.layout.activity_round_widget

    @OnClick(R.id.btn_submit, R.id.btn_enable)
    fun onViewClick(view: View) {
        when (view.id) {
            R.id.btn_submit -> {
                Toast.makeText(this, "点击了确定", Toast.LENGTH_SHORT).show()
//            }
            }

            R.id.btn_enable -> {
                if (mEnableBtn.tag as Boolean) {
                    mEnableBtn.text = "enable"
                    mEnableBtn.tag = false
                    mSubmitBtn.isEnabled = false
//                    mSubmitTv.isEnabled = false
//                    mContainerRl.isEnabled = false
//                    mContainerLl.isEnabled = false
//                    mContainerFl.isEnabled = false
//                    mSettingTv.isEnabled = false
                } else {
                    mEnableBtn.text = "disable"
                    mEnableBtn.tag = true
                    mSubmitBtn.isEnabled = true
//                    mSubmitTv.isEnabled = true
//                    mContainerRl.isEnabled = true
//                    mContainerLl.isEnabled = true
//                    mContainerFl.isEnabled = true
//                    mSettingTv.isEnabled = true
                }
            }

        }

    }
}