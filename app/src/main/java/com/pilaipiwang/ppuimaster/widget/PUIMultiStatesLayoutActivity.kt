package com.pilaipiwang.ppuimaster.widget

import android.view.View
import butterknife.BindView
import butterknife.OnClick
import com.pilaipiwang.ppuimaster.R
import com.pilaipiwang.ppuimaster.base.BaseActivity
import com.pilaipiwang.pui.widget.multilayout.PUIMultiStatesLayout

/**
 * @author: vitar
 * @date:   2019/11/11
 */
class PUIMultiStatesLayoutActivity : BaseActivity() {

    @BindView(R.id.stateslayout)
    lateinit var mStatesLayout: PUIMultiStatesLayout

    override fun initView() {
    }

    override fun attrLayoutId(): Int = R.layout.activity_multistates

    @OnClick(R.id.btn_loading, R.id.btn_content, R.id.btn_empty, R.id.btn_netoff, R.id.btn_error)
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
        }
    }

}