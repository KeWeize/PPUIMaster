package com.pilaipiwang.ppuimaster.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.Unbinder

/**
 * @author: vitar
 * @date:   2019/11/5
 */
abstract class BaseActivity : AppCompatActivity() {

    private var mUnbinder: Unbinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(attrLayoutId())
        mUnbinder = ButterKnife.bind(this)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mUnbinder?.unbind()
    }

    abstract fun attrLayoutId(): Int

    abstract fun initView()

}