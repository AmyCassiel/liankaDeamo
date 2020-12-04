package com.lianka.liankadeamo.base;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.lianka.liankadeamo.R;
import com.lianka.liankadeamo.utils.NullTranslator;
import com.lianka.liankadeamo.view.HintDialog;
import com.lianka.liankadeamo.view.LoadingDialog;


public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
    public void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }
/**
 * 消息提示*/
    protected void Hint(String sHint, int type) {
        try {
            new HintDialog(BaseActivity.this, R.style.dialog, sHint, type, true).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    LoadingDialog loadingDialog;

    public void dialogin(String msg) {
        loadingDialog = new LoadingDialog(BaseActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
        loadingDialog.show();
    }

    public void hideDialogin() {
        if (!NullTranslator.isNullEmpty(loadingDialog)) {
            loadingDialog.dismiss();
        }
    }
    public void error(Throwable error) {
        Log.e(getAttributionTag(), error.getMessage(), error);
        if (error != null) {
            if (error instanceof TimeoutError) {
                // Toast.makeText(mActivity,"网络请求超时，请重试！",Toast.LENGTH_SHORT).show();
                Hint("网络请求超时，请重试！", HintDialog.ERROR);
                return;
            }
            if (error instanceof ServerError) {
                //  Toast.makeText(mActivity,"服务器异常",Toast.LENGTH_SHORT).show();
                Hint("服务器异常", HintDialog.ERROR);
                return;
            }
            if (error instanceof NetworkError) {
                // Toast.makeText(mActivity,"请检查网络",Toast.LENGTH_SHORT).show();
                Hint("请检查网络", HintDialog.ERROR);
                return;
            }
            if (error instanceof ParseError) {
                Hint("数据格式错误", HintDialog.ERROR);
                //  Toast.makeText(mActivity,"数据格式错误",Toast.LENGTH_SHORT).show();
                return;
            }

            Log.e(getAttributionTag(), error.getMessage(), error);
        }
    }
}
