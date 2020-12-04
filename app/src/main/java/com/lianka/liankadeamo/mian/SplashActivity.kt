package com.lianka.liankadeamo.mian

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.lianka.liankadeamo.R
import com.lianka.liankadeamo.base.BaseActivity
import com.lianka.liankadeamo.mian.dialog.TIPSDialog
import com.lianka.liankadeamo.mian.dialog.TIPSDialog.OnClickBottomListener

class SplashActivity : BaseActivity() {
    private var mContext: Context? = null
    private val TAG = "SplashActivity.class"
    private var pref: SharedPreferences? = null
    private var sPassword: String? = ""
    private var sTy: String? = ""
    private var sAppId: String? = ""
    private var sUser: String? = ""
    private var editor: Editor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE) //去掉标题栏
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        ) //全屏显示
        val params = window.attributes
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        setContentView(R.layout.activity_splash)
        mContext = this@SplashActivity
        initData()
        if (sTy == "1") {
            onAnimationEnd()
        } else {
            tipsDialog()
        }
    }

    private fun tipsDialog() {
        val dialog = TIPSDialog(mContext!!)
        dialog.setOnClickBottomListener(object : OnClickBottomListener {
            override fun onPositiveClick() {
                finish()
            }

            override fun onNegtiveClick() {
                dialog.dismiss()
                editor = pref!!.edit()
                editor?.putString("ty", "1")
                editor?.apply()
                onAnimationEnd()
            }
        })
        dialog.show()
    }

    private fun initData() {
        pref = PreferenceManager.getDefaultSharedPreferences(mContext)
        val isRemember = pref!!.getBoolean("user", false)
        sUser = pref!!.getString("account", "")
        sPassword = pref!!.getString("password", "")
        sTy = pref!!.getString("ty", "")
        sAppId = pref!!.getString("AppId", "")
        editor = pref!!.edit()
        editor?.putString("shangchen", "2")
        editor?.putString("zhixun", "1")
        editor?.apply()
    }

    private fun onAnimationEnd() {
        Handler().postDelayed({
            var intent: Intent
            //                if (sPassword.equals("")) {
            intent = Intent(mContext, LoginActivity::class.java)
            //                } else {
            intent = Intent(mContext, MainActivity::class.java)
            //                }
            startActivity(intent)
            finish()
        }, 1000)
    }
}