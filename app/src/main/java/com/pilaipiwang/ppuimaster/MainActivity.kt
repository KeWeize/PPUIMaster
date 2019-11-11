package com.pilaipiwang.ppuimaster

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.pilaipiwang.ppuimaster.base.BaseActivity
import com.pilaipiwang.ppuimaster.widget.PUIRoundWidgetActivity
import com.pilaipiwang.ppuimaster.widget.LayoutActivity
import com.pilaipiwang.ppuimaster.widget.PUITopBarActivity
import com.pilaipiwang.pui.widget.topbar.PUITopBar

class MainActivity : BaseActivity() {

    @BindView(R.id.recyclerview)
    lateinit var mRecyclerView: RecyclerView

    private val mAdapter = MainAdapter()
    private val mDataList = arrayListOf("Layout", "Round Widget", "PUITopBar")

    override fun attrLayoutId(): Int = R.layout.activity_main

    override fun initView() {
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val item = adapter!!.getItem(position) as String
            when (item) {
                "Layout" ->
                    startActivity(Intent(this@MainActivity, LayoutActivity::class.java))
                "Round Widget" ->
                    startActivity(Intent(this@MainActivity, PUIRoundWidgetActivity::class.java))
                "PUITopBar" ->
                    startActivity(Intent(this@MainActivity, PUITopBarActivity::class.java))
            }
        }
        mAdapter.setNewData(mDataList)
    }

    inner class MainAdapter :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_simple_text, null) {
        override fun convert(helper: BaseViewHolder, item: String?) {
            helper.addOnClickListener(R.id.tv_content)
            helper.setText(R.id.tv_content, item ?: "")
        }
    }

}
