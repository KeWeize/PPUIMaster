package com.pilaipiwang.ppuimaster.widget

import android.view.View
import android.widget.TextView
import butterknife.OnClick
import com.pilaipiwang.ppuimaster.R
import com.pilaipiwang.ppuimaster.base.BaseActivity
import com.pilaipiwang.pui.widget.dialog.PUIDialog
import com.pilaipiwang.pui.widget.dialog.PUIDialogAction

/**
 * @author  vitar
 * @date    2019/11/15
 */
class DialogActivity : BaseActivity() {

    override fun attrLayoutId(): Int = R.layout.activity_dialog

    override fun initView() {
    }

    @OnClick(R.id.btn_message, R.id.btn_custom)
    fun onViewClick(view: View) {
        when (view.id) {

            R.id.btn_message -> showMessageDialog()

            R.id.btn_custom -> showCustomDialog()

        }
    }

    private fun showMessageDialog() {
        PUIDialog.MessageDialogBuilder(this)
            .setTitle("您有新版本需要更新")
            .setMessage("版本：1.2.0\n更新内容：\n1.修复诺干BUG，优化用户体验；\n2.新增用户模块")
            .addNegativeAction(
                "取消",
                object : PUIDialogAction.ActionListener {
                    override fun onClick(dialog: PUIDialog, index: Int) {
                        dialog.dismiss()
                    }
                })
            .addPositionAction(
                "确定",
                object : PUIDialogAction.ActionListener {
                    override fun onClick(dialog: PUIDialog, index: Int) {
                        dialog.dismiss()
                    }
                })
            .show()
    }

    private fun showCustomDialog() {
        PUIDialog.CustomDialogBuilder(this, R.layout.custom_dialog)
            .setOnCustomViewInflateListener(object :
                PUIDialog.CustomDialogBuilder.OnCustomViewInflateListener {
                override fun onInflate(view: View) {
                    val mContentTv = view.findViewById<TextView>(R.id.tv_content)
                    mContentTv.text = "版本：1.2.0\n" +
                            "更新内容：\n" +
                            "1.修复诺干BUG，优化用户体验；\n" +
                            "2.新增用户模块"
                }
            })
            .addNegativeAction(
                "取消",
                object : PUIDialogAction.ActionListener {
                    override fun onClick(dialog: PUIDialog, index: Int) {
                        dialog.dismiss()
                    }

                })
            .addPositionAction(
                "确定",
                object : PUIDialogAction.ActionListener {
                    override fun onClick(dialog: PUIDialog, index: Int) {
                        dialog.dismiss()
                    }
                })
            .show()
    }

}