package com.pilaipiwang.ppuimaster.widget

import android.view.View
import butterknife.OnClick
import com.pilaipiwang.ppuimaster.R
import com.pilaipiwang.ppuimaster.base.BaseActivity
import com.pilaipiwang.pui.widget.dialog.PUIDialog

/**
 * @author  vitar
 * @date    2019/11/15
 */
class DialogActivity : BaseActivity() {

    override fun attrLayoutId(): Int = R.layout.activity_dialog

    override fun initView() {
    }

    @OnClick(R.id.btn_message)
    fun onViewClick(view: View) {
        when (view.id) {

            R.id.btn_message -> showMessageDialog()

        }
    }

    private fun showMessageDialog() {
        PUIDialog.MessageDialogBuilder(this)
            .setTitle("提示")
            .setMessage("这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容这里显示文本内容")
            .show()
    }

}