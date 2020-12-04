package com.lianka.liankadeamo.mian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lianka.liankadeamo.R;
import com.lianka.liankadeamo.mian.MainActivity;
import com.lianka.liankadeamo.utils.StatusBarUtil;

public class BottomIconAdapter extends RecyclerView.Adapter<BottomIconAdapter.MyViewHolder> {
    private Context context;
    private int Postion;
    private View inflater;
    private String[] name;

    //构造方法，传入数据
    public BottomIconAdapter(Context context, String[] name,int postion) {
        this.context = context;
        this.name = name;
        this.Postion = postion;
    }

    @Override
    public BottomIconAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        inflater = LayoutInflater.from(context).inflate(R.layout.tab_item, parent, false);
        BottomIconAdapter.MyViewHolder myViewHolder = new BottomIconAdapter.MyViewHolder(inflater);
        return myViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(BottomIconAdapter.MyViewHolder holder, int position) {
        holder.tv_tab_item.setText(name[position]);
        if (position == Postion) {
            holder.tv_tab_item.setTextColor(holder.tv_tab_item.getResources().getColor(R.color.theme));
            Glide.with(MainActivity.this)
                    .load(imgres_is[position])
                    .asBitmap()
                    .placeholder(R.color.gray_999999)
                    .error(R.color.gray_999999)
                    .dontAnimate()
                    .into(holder.iv_tab_item);
        } else {
            holder.tv_tab_item.setTextColor(holder.tv_tab_item.getResources().getColor(R.color.bbbccd));
            Glide.with(MainActivity.this)
                    .load(imgres[position])
                    .asBitmap()
                    .placeholder(R.color.gray_999999)
                    .error(R.color.gray_999999)
                    .dontAnimate()
                    .into(holder.iv_tab_item);
        }
        holder.ll_tab_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tab_position == 4) {
                    StatusBarUtil.setStatusBarColor(MainActivity.this,R.color.white_fff)
                }
                iPosition = position;
                viewpager.setCurrentItem(position);
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        //返回Item总条数
        return name.length;
    }

    //内部类，绑定控件
    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_tab_item;
        ImageView iv_tab_item;
        TextView tv_tab_item;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_tab_item = itemView.findViewById(R.id.ll_tab_item);
            iv_tab_item = itemView.findViewById(R.id.iv_tab_item);
            tv_tab_item = itemView.findViewById(R.id.tv_tab_item);

        }

    }
