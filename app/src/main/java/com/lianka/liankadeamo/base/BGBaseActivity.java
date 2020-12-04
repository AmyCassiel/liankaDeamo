package com.lianka.liankadeamo.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lianka.liankadeamo.R;

public abstract class BGBaseActivity extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_bg_base);
        initView();

    }
    private void initView() {
        // 绑定控件
        ImageView iv_bg= findView(R.id.iv_bg);
        FrameLayout toolbar = findView(R.id.toolbar);
        ImageView iv_back = findView(R.id.iv_back);
        TextView tv_title = findView(R.id.tv_title);
        FrameLayout container = findView(R.id.container);
        // 初始化设置Toolbar
        iv_bg.setImageResource(setBG());
        toolbar.setBackgroundColor(setBarBackground());
        tv_title.setText(setTitle());
        iv_back.setImageResource(setBackButton());
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 将继承了BaseActivity的布局文件解析到 container 中，这样 BaseActivity 就能显示 MainActivity 的布局文件了
        LayoutInflater.from(this).inflate(getContentView(), container);
    }

    /**
     * 获取要显示内容的布局文件的资源id
     * @return 显示的内容界面的资源id
     */
    protected abstract int getContentView();
    /**
     * 设置标题
     * @return 要显示的标题名称
     */
    protected abstract int setBG();
    protected abstract int setBackButton();
    protected abstract int setBarBackground();
    protected abstract int setTitle();
    protected abstract void init();

    LoadingDialog loadingDialog;

    public void dialogin(String msg) {
        loadingDialog = new LoadingDialog(this, R.style.dialog, msg, LoadingDialog.WAITING_C);
        loadingDialog.show();
    }

    public void hideDialogin() {
        if (!NullTranslator.isNullEmpty(loadingDialog)) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 消息提示
     */
    protected void Hint(String sHint, int type) {
        try {
            new HintDialog(this, R.style.dialog, sHint, type, true).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void error(Throwable error) {
        Log.e("", error.getMessage(), error);
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
        }
    }
}
