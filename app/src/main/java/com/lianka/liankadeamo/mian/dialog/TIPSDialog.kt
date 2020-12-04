package com.lianka.liankadeamo.mian.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import com.lianka.liankadeamo.R

class TIPSDialog(context: Context) : Dialog(context, R.style.ActionSheetDialogStyle) {
    private val mContext:Context = context
    private var tv : TextView? = null
    private var tvTipsCancel : TextView? = null
    private var tvTipsOK : TextView? = null
    private var onClickBottomListener: OnClickBottomListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_tips)
        val dialogWindow = window
        dialogWindow!!.setGravity(Gravity.CENTER)
        val lp = dialogWindow.attributes
        lp.y = 80
        dialogWindow.attributes = lp
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        initView()
        initEvent()
    }

    private fun initEvent() {
        tvTipsCancel?.setOnClickListener {_->
            if ( onClickBottomListener!= null) {
                onClickBottomListener!!.onNegtiveClick()
            }
        }
        tvTipsOK?.setOnClickListener { _->
            if ( onClickBottomListener!= null) {
                onClickBottomListener!!.onPositiveClick()

            }
        }
    }

    private fun initView() {
        tv = findViewById(R.id.tv_tips_text)
        tvTipsCancel = findViewById(R.id.tv_tips_cancel)
        tvTipsOK = findViewById(R.id.tv_tips_okay)
    }

    override fun show() {
        super.show()
    }

    override fun dismiss() {
        super.dismiss()
    }
    /**
     * 设置确定取消按钮的回调
     */
    fun setOnClickBottomListener(onClickBottomListener: OnClickBottomListener?): TIPSDialog? {
        this.onClickBottomListener = onClickBottomListener
        return this
    }

    interface OnClickBottomListener {
        /**
         * 点击确定按钮事件
         */
        fun onPositiveClick()
        /**
         * 点击取消按钮事件
         */
        fun onNegtiveClick()
    }
}