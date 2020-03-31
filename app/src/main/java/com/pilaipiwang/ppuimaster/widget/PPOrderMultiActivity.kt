package com.pilaipiwang.ppuimaster.widget

import android.os.Handler
import android.view.View
import butterknife.BindView
import butterknife.OnClick
import com.pilaipiwang.ppuimaster.R
import com.pilaipiwang.ppuimaster.base.BaseActivity
import com.pilaipiwang.pui.widget.multilayout.PUIMultiStatesAbstract

/**
 * @author: vitar
 * @date:   2020/3/25
 */
class PPOrderMultiActivity : BaseActivity() {

    @BindView(R.id.statuelayout)
    lateinit var mStateLayout: PUIMultiStatesAbstract

    override fun attrLayoutId(): Int = R.layout.activity_pp_order_multi

    override fun initView() {
        mStateLayout.showLoadingLayout()
        Handler().postDelayed({
            mStateLayout.showContentLayout()
        }, 2000)
    }

    @OnClick(R.id.btn_loading, R.id.btn_content, R.id.btn_empty, R.id.btn_exception)
    fun onViewClick(view: View?) {
        when (view?.id) {
            R.id.btn_loading -> mStateLayout.showLoadingLayout()
            R.id.btn_content -> mStateLayout.showContentLayout()
            R.id.btn_empty -> mStateLayout.showEmptyLayout()
            R.id.btn_exception -> mStateLayout.showExceptionLayout()
        }
    }

}