package com.lianka.liankadeamo.base;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lianka.liankadeamo.R;
import com.lianka.liankadeamo.view.HintDialog;
import com.lianka.liankadeamo.view.LoadingDialog;

public abstract class OnBaseActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.white_fff));
        }
        setContentView(R.layout.activity_onbase);
        initView();
    }
    /**
     * 获取要显示内容的布局文件的资源id
     * @return 显示的内容界面的资源id
     */
    protected abstract int getContentView();

    protected abstract int setBarBackground();
    protected abstract int setTitle();
//    protected abstract int setQueryButton();
    protected abstract void init();

    private void initView() {
        // 绑定控件
        FrameLayout toolbar = findView(R.id.toolbar);
        ImageView iv_back = findView(R.id.iv_back);
        TextView tv_title = findView(R.id.tv_title);
//        ImageView iv_query = findView(R.id.iv_query);
        FrameLayout container = findView(R.id.container);
        // 初始化设置Toolbar
        toolbar.setBackgroundColor(setBarBackground());
        tv_title.setText(setTitle());
        iv_back.setImageResource(R.mipmap.img_back);
//        iv_query.setImageResource(setQueryButton());
        iv_back.setOnClickListener(v -> finish());
        // 将继承了BaseActivity的布局文件解析到 container 中，这样 BaseActivity 就能显示 MainActivity 的布局文件了
        LayoutInflater.from(this).inflate(getContentView(), container);
    }

}
