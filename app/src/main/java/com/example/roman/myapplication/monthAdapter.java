package com.example.roman.myapplication;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Roman on 2016/12/6.
 */
public class monthAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<diary> monDiary = null;

    public monthAdapter(){}

    public monthAdapter(ArrayList<diary> monDiary, Context mContext){
        this.mContext = mContext;
        this.monDiary = monDiary;
    }

    @Override
    public int getCount(){
        return monDiary.size();
    }

    @Override
    public Object getItem(int position){
        return null;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holeder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.diarylist,parent,false);
            holeder = new ViewHolder();
            holeder.txt = (TextView)convertView.findViewById(R.id.diaryItem);
            convertView.setTag(holeder);
        }else{
            holeder = (ViewHolder)convertView.getTag();
        }
        String s = Integer.toString(monDiary.get(position).getNum()) + " " +
                getDayOfDate(monDiary.get(position).getNumofDay()) + "DAY / " +
                monDiary.get(position).getContent();
        holeder.txt.setText(s);
        Pattern pattern = Pattern.compile("(\\d+\\D*)/([\\s\\S]*)");
        Matcher m = pattern.matcher(s);
        if (m.find()){
            SpannableStringBuilder style = new SpannableStringBuilder(s);
            style.setSpan(new StyleSpan(Typeface.BOLD),0, m.group(1).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if(monDiary.get(position).getNumofDay()==0) {
                style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.sundaycolor)),
                        0, m.group(1).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);     //设置指定位置文字的颜色
            }
            holeder.txt.setText(style);
        }
        return convertView;
    }

    private class ViewHolder{
        TextView txt;
    }

    //获取星期
    private String getDayOfDate(int week){
        String[] weekDays = {"SUN","MON","TUES","WEDENS","THURS","FRI","SATUR"};
        return weekDays[week];
    }
}
