package com.pilaipiwang.ppuimaster.widget

import android.view.View
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import com.pilaipiwang.ppuimaster.R
import com.pilaipiwang.ppuimaster.base.BaseActivity
import com.pilaipiwang.pui.widget.multilayout.PUIDefaultMultiStatesLayout

/**
 * @author: vitar
 * @date:   2019/11/11
 */
class PUIMultiStatesLayoutActivity : BaseActivity() {

    @BindView(R.id.stateslayout)
    lateinit var mStatesLayout: PUIDefaultMultiStatesLayout

    override fun initView() {
        mStatesLayout.setDefaultMultiStatesActionListener(object :
            PUIDefaultMultiStatesLayout.OnDefaultMultiStatesActionListener {
            override fun onClick(view: View, type: PUIDefaultMultiStatesLayout.MultiStatesType) {
                when (type) {
                    PUIDefaultMultiStatesLayout.MultiStatesType.STATES_EMPTY ->
                        Toast.makeText(
                            this@PUIMultiStatesLayoutActivity,
                            "空状态请重试",
                            Toast.LENGTH_SHORT
                        ).show()

                    PUIDefaultMultiStatesLayout.MultiStatesType.STATES_ERROR ->
                        Toast.makeText(
                            this@PUIMultiStatesLayoutActivity,
                            "错误状态请重试",
                            Toast.LENGTH_SHORT
                        ).show()

                    PUIDefaultMultiStatesLayout.MultiStatesType.STATES_NETOFF ->
                        Toast.makeText(
                            this@PUIMultiStatesLayoutActivity,
                            "网络异常请重试",
                            Toast.LENGTH_SHORT
                        ).show()
                    else ->
                        Toast.makeText(
                            this@PUIMultiStatesLayoutActivity,
                            "其他异常请重试",
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }
        })
    }

    override fun attrLayoutId(): Int = R.layout.activity_multistates

    @OnClick(R.id.btn_loading, R.id.btn_content, R.id.btn_empty, R.id.btn_netoff, R.id.btn_error, R.id.btn_custom)
    fun onViewClick(view: View) {
        when (view.id) {
            R.id.btn_loading ->
                mStatesLayout.showLoadingLayout()
            R.id.btn_content ->
                mStatesLayout.showContentLayout()
            R.id.btn_empty ->
                mStatesLayout.showEmptyLayout()
            R.id.btn_netoff ->
                mStatesLayout.showNetOffLayout()
            R.id.btn_error ->
                mStatesLayout.showErrorLayout()
            R.id.btn_custom ->
                mStatesLayout.showCustomExceptionLayout()
        }
    }

}