package com.lianka.liankadeamo.mian

import android.os.Bundle
import android.os.Handler
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import com.lianka.liankadeamo.R
import com.lianka.liankadeamo.base.OnBaseActivity
import com.lianka.liankadeamo.utils.Api
import com.lianka.liankadeamo.utils.TimerUtil
import com.lianka.liankadeamo.view.HintDialog
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class ResetPasswordsActivity : OnBaseActivity() {
    private val s = ResetPasswordsActivity::class.java.simpleName
    @BindView(R.id.et_czmm_phone)
    var etCzmmPhone: EditText? = null

    @BindView(R.id.et_czmm_verification_code)
    var etCzmmVerificationCode: EditText? = null

    @BindView(R.id.tv_czmm_verification)
    var tvCzmmVerification: TextView? = null

    @BindView(R.id.et_czmm_password)
    var etCzmmPassword: EditText? = null

    @BindView(R.id.et_czmm_confirm_password)
    var etCzmmConfirmPassword: EditText? = null

    private var time: TimerUtil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ButterKnife.bind(this)
        time = TimerUtil(tvCzmmVerification, 60000, 1000)
        etCzmmPassword!!.transformationMethod = PasswordTransformationMethod.getInstance() //密码隐藏
        etCzmmConfirmPassword!!.transformationMethod =
            PasswordTransformationMethod.getInstance() //密码隐藏
    }

    override fun getContentView(): Int {
        return R.layout.activity_reset_passwords
    }

    override fun setBarBackground(): Int {
        return 0
    }

    override fun setTitle(): Int {
        return R.string.title_reset_password
    }

    override fun init() {}

    /**
     * 方法名：yzmcz()
     * 功  能：获取验证码初始化
     */
    private fun yzmcz() {
        time!!.cancel()
        time = TimerUtil(tvCzmmVerification, 60000, 1000)
        tvCzmmVerification!!.text = "获取验证码"
        tvCzmmVerification!!.isClickable = true
        tvCzmmVerification!!.setBackgroundResource(R.drawable.bg_blue_round_corner_6f57ff)
    }

    @OnClick(R.id.tv_czmm_verification, R.id.tv_czmm_save)
    fun onViewClicked(view: View) {
        val sMobile = etCzmmPhone!!.text.toString()
        val sCode = etCzmmVerificationCode!!.text.toString()
        val sPassword = etCzmmPassword!!.text.toString()
        val sConfirmPassword = etCzmmConfirmPassword!!.text.toString()
        when (view.id) {
            R.id.tv_czmm_verification -> if (sMobile == "") {
                Hint("请输入手机号！", HintDialog.WARM)
            } else {
                time!!.start()
                sendsms(sMobile, "3")
            }
            R.id.tv_czmm_save -> if (sMobile == "") {
                Hint("请输入手机号！", HintDialog.WARM)
            } else if (sCode == "") {
                Hint("请输入验证码！", HintDialog.WARM)
            } else if (sPassword != sConfirmPassword) {
                Hint("输入密码不一致！", HintDialog.WARM)
            } else {
                repwd(sMobile, sCode, sPassword)
            }
        }
    }

    /**
     * 方法名：sSendsms(String sMobile,String sState)
     * 功    能：用户短信发送接口
     * 参    数：sMobile---手机号码
     * state---登录发送为2 重置密码发送为3 注册为5 设置支付密码为6 商家注册为8
     */
    private fun sendsms(sMobile: String, sState: String) {
        hideDialogin()
        dialogin("")
        val url = Api.sUrl + Api.sSendsms
        val requestQueue = Volley.newRequestQueue(this)
        val params: MutableMap<String?, String?> = HashMap()
        params["mobile"] = sMobile
        params["state"] = sState
        val jsonObject = JSONObject(params as Map<*, *>)
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        val jsonRequest: JsonRequest<JSONObject> =
            JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                Response.Listener { response ->
                    hideDialogin()
                    val sDate = response.toString()
                    println(sDate)
                    var jsonObject1: JSONObject? = null
                    try {
                        jsonObject1 = JSONObject(sDate)
                        val resultMsg = jsonObject1.getString("msg")
                        val resultCode = jsonObject1.getInt("code")
                        if (resultCode > 0) {
                            val resultData = jsonObject1.getString("data")
                            Hint(resultMsg, HintDialog.SUCCESS)
                        } else {
                            Hint(resultMsg, HintDialog.ERROR)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    Log.d(s, "response -> $response")
                    //   Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                }, Response.ErrorListener { error ->
                    hideDialogin()
                    error(error)
                })
        requestQueue.add(jsonRequest)
    }


    /**
     * 方法名：sRepwd(String sMobile, String sCode, String sPassword)
     * 功    能：密码重置
     * 参    数：sMobile---手机号码
     * code---	验证码
     * password---	密码
     */
    private fun repwd(
        sMobile: String,
        sCode: String,
        sPassword: String
    ) {
        hideDialogin()
        dialogin("")
        val url = Api.sUrl + Api.sRepwd
        val requestQueue = Volley.newRequestQueue(this)
        val params: MutableMap<String?, String?> = HashMap()
        params["mobile"] = sMobile
        params["code"] = sCode
        params["password"] = sPassword
        val jsonObject = JSONObject(params as Map<*, *>)
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        val jsonRequest: JsonRequest<JSONObject> =
            JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                Response.Listener { response ->
                    hideDialogin()
                    val sDate = response.toString()
                    println(sDate)
                    var jsonObject1: JSONObject? = null
                    try {
                        jsonObject1 = JSONObject(sDate)
                        val resultMsg = jsonObject1.getString("msg")
                        val resultCode = jsonObject1.getInt("code")
                        if (resultCode > 0) {
                            val resultData = jsonObject1.getString("data")
                            Hint(resultMsg, HintDialog.SUCCESS)
                            Handler().postDelayed({
                                try {
                                    finish()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }, 1500)
                        } else {
                            Hint(resultMsg, HintDialog.ERROR)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    Log.d(s, "response -> $response")
                    //   Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                }, Response.ErrorListener { error ->
                    hideDialogin()
                    error(error)
                })
        requestQueue.add(jsonRequest)
    }
}