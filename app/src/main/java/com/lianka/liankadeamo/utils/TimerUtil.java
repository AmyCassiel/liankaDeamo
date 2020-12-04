package com.lianka.liankadeamo.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.lianka.liankadeamo.R;

public class TimerUtil extends CountDownTimer {
    TextView view;
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public TimerUtil(TextView view, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.view = view;
    }

    @Override
    public void onTick(long millisUntilFinished) {
//        view.setBackgroundResource(R.drawable.bg_blue_round_corner_6f57ff);
        view.setClickable(false);
        view.setText("" + millisUntilFinished / 1000 + "秒后重新发送");
    }

    @Override
    public void onFinish() {
        view.setText("重新获取验证码");
        view.setClickable(true);
//        view.setBackgroundResource(R.drawable.bg_blue_round_corner_6f57ff);
    }
}
