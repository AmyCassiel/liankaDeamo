package com.lianka.liankadeamo.mian

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.text.Html
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.lianka.liankadeamo.R
import com.lianka.liankadeamo.base.BaseActivity
import com.lianka.liankadeamo.utils.Api
import com.lianka.liankadeamo.utils.HttpUtil
import com.lianka.liankadeamo.utils.TimerUtil
import com.lianka.liankadeamo.view.HintDialog
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class LoginActivity:BaseActivity() {
    @BindView(R.id.tv_register)
    var tvRegister: TextView? = null

    @BindView(R.id.tv_login)
    var tvLogin: TextView? = null

    @BindView(R.id.et_login_tel)
    var etTelNumber: EditText? = null

    @BindView(R.id.tv_login_erification)
    var tvEft: TextView? = null

    @BindView(R.id.ll_login_erification_code)
    var llEftCode: LinearLayout? = null

    @BindView(R.id.tv_login_verification_code_login)
    var tvVC: EditText? = null

    @BindView(R.id.et_login_password)
    var etPassword: EditText? = null

    @BindView(R.id.et_login_confirm_password)
    var etCp: EditText? = null

    @BindView(R.id.ll_login_confirm_password)
    var llCp: LinearLayout? = null

    @BindView(R.id.tv_login_error)
    var tvError: TextView? = null

    @BindView(R.id.ll_login_error)
    var llError: LinearLayout? = null

    @BindView(R.id.bt_login_register)
    var btRegister: Button? = null

    @BindView(R.id.tv_login_verification_code_login)
    var tvVerificationCode: TextView? = null

    @BindView(R.id.tv_login_forget_the_password)
    var tvForgetThePassword: TextView? = null

    @BindView(R.id.ll_other_login)
    var llOtherLogin: LinearLayout? = null

    @BindView(R.id.tv_login_password_landing)
    var tvPasswordLanding: TextView? = null

    @BindView(R.id.ll_login_user_agreement)
    var llUserAgreement: LinearLayout? = null

    @BindView(R.id.iv_login_wechat)
    var ivWechat: ImageView? = null

    @BindView(R.id.iv_login_qq)
    var ivQq: ImageView? = null

    @BindView(R.id.iv_login_alipay)
    var ivAlipay: ImageView? = null

    @BindView(R.id.ll_login_password)
    var llPassword: LinearLayout? = null

    @BindView(R.id.cb_login_user_agreement)
    var cbUserAgreement: CheckBox? = null

    @BindView(R.id.tv_login_user_agreement_ysxy)
    var tvUserAgreement: TextView? = null

    private var context: Context? = null
    private val sTy = "我已阅读并同意奁卡《用户服务协议》与《隐私协议》"
    private var timer: TimerUtil? = null
    private val TAG =
        LoginActivity::class.java.simpleName
    private var pref: SharedPreferences? = null
    private var editor: Editor? = null
    private val VIDEO_PERMISSIONS_CODE = 1
    private val VIDEO_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)
        context = this@LoginActivity
        pref = PreferenceManager.getDefaultSharedPreferences(this@LoginActivity)
        timer = TimerUtil(tvEft, 60000, 1000)
        initView()
        requestPermission()
    }

    private fun requestPermission() {
        // 当API大于 23 时，才动态申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                this@LoginActivity,
                VIDEO_PERMISSIONS,
                VIDEO_PERMISSIONS_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            VIDEO_PERMISSIONS_CODE ->                 //权限请求失败
                if (grantResults.size == VIDEO_PERMISSIONS.size) {
                    for (result in grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            //弹出对话框引导用户去设置
                            AlertDialog.Builder(this)
                                .setMessage("录像需要相机、录音和读写权限，是否去设置？")
                                .setPositiveButton(
                                    "是"
                                ) { dialog, which ->
                                    dialog.dismiss()
                                    val intent = Intent()
                                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                    val uri = Uri.fromParts(
                                        "package",
                                        packageName,
                                        null
                                    )
                                    intent.data = uri
                                    startActivity(intent)
                                }
                                .setNegativeButton(
                                    "否"
                                ) { dialog, which -> dialog.dismiss() }
                                .setCancelable(false)
                                .show()
                            showToast(context, "请求权限被拒绝")
                            break
                        }
                    }
                } else {
                    Toast.makeText(context, "已授权", Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onStart() {
        super.onStart()
        tvUserAgreement!!.text = Html.fromHtml(sTy)
    }

    private fun initView() {
        llEftCode!!.visibility = View.GONE
        etCp!!.visibility = View.GONE
        llError!!.visibility = View.GONE
        llUserAgreement!!.visibility = View.GONE
        llPassword!!.visibility = View.VISIBLE
        llOtherLogin!!.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        tvUserAgreement!!.setOnClickListener { v: View? ->
            val intent = Intent(context, H5Activity::class.java)
            intent.putExtra("link", "http://lianka.bonnidee.cn/mianze/privacyAgreement.html")
            startActivity(intent)
        }
    }

    @OnClick(
        R.id.tv_register,
        R.id.tv_login,
        R.id.tv_login_erification,
        R.id.bt_login_register,
        R.id.tv_login_verification_code_login,
        R.id.tv_login_forget_the_password,
        R.id.iv_login_wechat,
        R.id.iv_login_qq,
        R.id.iv_login_alipay
    )
    fun onViewClicked(view: View) {
        val sMobile = etTelNumber!!.text.toString()
        val sPassword = etPassword!!.text.toString()
        val sConfirmPassword = etCp!!.text.toString()
        val sCode = tvVC!!.text.toString()
        var intent: Intent
        when (view.id) {
            R.id.tv_register -> {
                initErification()
                llEftCode!!.visibility = View.VISIBLE
                llCp!!.visibility = View.VISIBLE
                llUserAgreement!!.visibility = View.VISIBLE
                llPassword!!.visibility = View.VISIBLE
            }
            R.id.tv_login -> {
                llEftCode!!.visibility = View.GONE
                llCp!!.visibility = View.GONE
                llUserAgreement!!.visibility = View.VISIBLE
                llPassword!!.visibility = View.VISIBLE
            }
            R.id.tv_login_erification -> {
                val sState = "5"
                //                if (code==0) {
//                    sState = "5";
//                } else if (code==1) {
//                    sState = "2";
//                }
                if (sMobile == "") {
                    Hint("请输入手机号！", HintDialog.WARM)
                } else {
                    sSendsms(sMobile, sState)
                    timer!!.start()
                }
            }
            R.id.bt_login_register -> if (tvRegister!!.text.toString() == "注册") {
                if (sMobile == "") {
                    Hint("请输入手机号！", HintDialog.WARM)
                } else if (sCode == "") {
                    Hint("请输入验证码！", HintDialog.WARM)
                } else if (sPassword != sConfirmPassword) {
                    Hint("输入密码不一致！", HintDialog.WARM)
                } else if (!cbUserAgreement!!.isChecked) {
                    Hint("请同意用户协议！", HintDialog.WARM)
                } else {
                    sRegisters(sMobile, sCode, sPassword, "sInvitation")
                }
            } else if (tvRegister!!.text.toString() == "登录") {
                if (tvPasswordLanding!!.visibility == View.VISIBLE) {
                    if (sMobile == "") {
                        Hint("请输入手机号！", HintDialog.WARM)
                    } else if (sCode == "") {
                        Hint("请输入密码！", HintDialog.WARM)
                    } else {
                        sCodeLogin(sMobile, sCode)
                    }
                } else {
                    if (sMobile == "") {
                        Hint("请输入手机号！", HintDialog.WARM)
                    } else if (sPassword == "") {
                        Hint("请输入密码！", HintDialog.WARM)
                    } else {
                        sLogin(sMobile, sPassword)
                    }
                }
            }
            R.id.tv_login_verification_code_login -> {
                initErification()
                llEftCode!!.visibility = View.VISIBLE
                llOtherLogin!!.visibility = View.GONE
                tvPasswordLanding!!.visibility = View.VISIBLE
                llUserAgreement!!.visibility = View.GONE
                llPassword!!.visibility = View.GONE
            }
            R.id.tv_login_forget_the_password -> startActivity(
                Intent(
                    this,
                    ResetPasswordsActivity::class.java
                )
            )
            R.id.tv_login_password_landing -> {
                llEftCode!!.visibility = View.GONE
                llPassword!!.visibility = View.VISIBLE
                tvPasswordLanding!!.visibility = View.GONE
                llPassword!!.visibility = View.VISIBLE
            }
            R.id.iv_login_wechat -> {
            }
            R.id.iv_login_qq -> {
            }
            R.id.iv_login_alipay -> {
            }
        }
    }

    /**
     * 方法名：initErification()
     * 功  能：获取验证码初始化
     */
    private fun initErification() {
        timer!!.cancel()
        timer = TimerUtil(tvEft, 60000, 1000)
        tvEft!!.text = "获取验证码"
        tvEft!!.isClickable = true
        tvEft!!.setBackgroundResource(R.drawable.bg_blue_round_corner_2d3358)
    }

    /**
     * 方法名：sSendsms(String sMobile,String sState)
     * 功    能：用户短信发送接口
     * 参    数：sMobile---手机号码
     * state---登录发送为2 重置密码发送为3 注册为5 设置支付密码为6 商家注册为8
     */
    private fun sSendsms(sMobile: String, sState: String) {
//        hideDialogin();
//        dialogin("");
        val url = Api.sUrl + Api.sSendsms
        val params: MutableMap<String, String> =
            HashMap()
        params["mobile"] = sMobile
        params["state"] = sState
        val responseBody = HttpUtil.mPost(url, params)
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(responseBody)
            val resultMsg = jsonObject.getString("msg")
            val resultCode = jsonObject.getInt("code")
            if (resultCode > 0) {
                Hint(resultMsg, HintDialog.SUCCESS)
            } else {
                Hint(resultMsg, HintDialog.ERROR)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    /**
     * 方法名：sRegisters(String sMobile, String sCode, String sPassword, String sInvitation)
     * 功    能：用户注册
     * 参    数：sMobile---手机号码
     * code---手机验证码
     * password---密码 至少6位
     * invitation---邀请码
     */
    private fun sRegisters(
        sMobile: String,
        sCode: String,
        sPassword: String,
        sInvitation: String
    ) {
//        hideDialogin();
//        dialogin("");
        val url = Api.sUrl + Api.sRegister
        //        RequestQueue requestQueue = Volley.newRequestQueue(this);
        val params: MutableMap<String, String> =
            HashMap()
        params["mobile"] = sMobile
        params["code"] = sCode
        params["password"] = sPassword
        if (sInvitation != "") {
            params["invitation"] = sInvitation
        }
        val responseBody = HttpUtil.mPost(url, params)
        var jsonObject1: JSONObject? = null
        try {
            jsonObject1 = JSONObject(responseBody)
            val resultMsg = jsonObject1.getString("msg")
            val resultCode = jsonObject1.getInt("code")
            if (resultCode > 0) {
                val resultData = jsonObject1.getString("data")
                Hint(resultMsg, HintDialog.SUCCESS)
                sLogin(sMobile, sPassword)
            } else {
                Hint(resultMsg, HintDialog.ERROR)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    /**
     * 方法名：sLogin(String sMobile, String sPassword)
     * 功    能：用户登录
     * 参    数：sMobile---手机号码
     * password---密码 至少6位
     */
    private fun sLogin(sMobile: String, sPassword: String) {
//        hideDialogin();
//        dialogin("");
        val url = Api.sUrl + Api.sLogin
        val params: MutableMap<String, String> =
            HashMap()
        params["mobile"] = sMobile
        params["password"] = sPassword
        val responseBody = HttpUtil.mPost(url, params)
        var jsonObject1: JSONObject? = null
        try {
            jsonObject1 = JSONObject(responseBody)
            val resultMsg = jsonObject1.getString("msg")
            val resultCode = jsonObject1.getInt("code")
            if (resultCode > 0) {
                Hint(resultMsg, HintDialog.SUCCESS)
                main(responseBody)
            } else {
                Hint(resultMsg, HintDialog.ERROR)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun main(data: String) {
        println(data)
        var jsonObject1: JSONObject? = null
        try {
            jsonObject1 = JSONObject(data)
            val resultMsg = jsonObject1.getString("msg")
            val resultCode = jsonObject1.getInt("code")
            if (resultCode > 0) {
                val jsonObjectdate = jsonObject1.getJSONObject("data")
                val user_id = jsonObjectdate.getString("id")
                val mobile = jsonObjectdate.getString("mobile")
                val nickname = jsonObjectdate.getString("nickname")
                val headimgurl = jsonObjectdate.getString("headimgurl")
                val token = jsonObjectdate.getString("token")
                val sex = jsonObjectdate.getString("sex")
                editor = pref!!.edit()
                editor?.putBoolean("user", true)
                editor?.putString("password", user_id)
                editor?.putString("user_id", user_id)
                editor?.putString("mobile", mobile)
                editor?.putString("nickname", nickname)
                editor?.putString("headimgurl", headimgurl)
                editor?.putString("token", token)
                editor?.putString("sex", sex)
                editor?.apply()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                //                hideDialogin();

                //   Hint(resultMsg, HintDialog.SUCCESS);
            } else {
                Hint(resultMsg, HintDialog.ERROR)
                //   Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    /**
     * 方法名：sCodeLogin(String sMobile, String sCode)
     * 功    能：用户登录
     * 参    数：sMobile---手机号码
     * code---	验证码
     */
    private fun sCodeLogin(sMobile: String, sCode: String) {
//        hideDialogin();
//        dialogin("");
        val url = Api.sUrl + Api.codeLogin
        val params: MutableMap<String, String> =
            HashMap()
        params["mobile"] = sMobile
        params["code"] = sCode
        val responseBody = HttpUtil.mPost(url, params)
        var jsonObject1: JSONObject? = null
        try {
            jsonObject1 = JSONObject(responseBody)
            val resultMsg = jsonObject1.getString("msg")
            val resultCode = jsonObject1.getInt("code")
            if (resultCode > 0) {
                Hint(resultMsg, HintDialog.SUCCESS)
                main(responseBody)
            } else {
                Hint(resultMsg, HintDialog.ERROR)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
}