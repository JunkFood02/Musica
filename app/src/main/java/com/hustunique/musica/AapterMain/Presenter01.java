package com.hustunique.musica.AapterMain;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hustunique.musica.R;

public class Presenter01 implements IAdapter01.IPresenter {
    IAdapter01.IView view;
    IAdapter01.IModel model;
    Presenter01(IAdapter01.IView view){
        this.view = view;
        this.model = new Model01();
    }
    @Override
    public void getUI(Adapter01.MyViewHolder holder, int position){
        //从model层获取数据
        //然后修改view层UI
//        holder.TextTime.setText("今天空气橘子");
        holder.PhotoTitle.setBackgroundResource(R.drawable.orange);
    }

}