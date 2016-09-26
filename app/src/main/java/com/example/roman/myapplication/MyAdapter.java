package com.example.roman.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Roman on 2016/9/20.
 */
public class MyAdapter extends BaseAdapter{
    private static final int TYPE_CONTENT = 0;
    private static final int TYPE_NOCONTENT = 1;
    private Context mContext;
    private ArrayList<Object> mDiary = null;

    public MyAdapter(){}

    public MyAdapter(ArrayList<Object> mDiary,Context mContext){
        this.mContext = mContext;
        this.mDiary = mDiary;
    }

    @Override
    public int getCount(){
        return mDiary.size();
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
    public int getItemViewType(int position){
        if (mDiary.get(position) instanceof diary){
            return TYPE_CONTENT;
        }else if (mDiary.get(position) instanceof noDiary){
            return TYPE_NOCONTENT;
        }else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public int getViewTypeCount(){
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        int type = getItemViewType(position);
        ViewHolder2 holder2 = null;
        if (convertView == null){
            switch (type){
                case TYPE_NOCONTENT:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.empty_list,parent,false);
                    break;
                case TYPE_CONTENT:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);
                    holder2 = new ViewHolder2();
                    holder2.day_icon = (TextView) convertView.findViewById(R.id.day_icon);
                    holder2.num_icon = (TextView) convertView.findViewById(R.id.num_icon);
                    holder2.txt_content = (TextView) convertView.findViewById(R.id.txt_content);
                    convertView.setTag(R.id.Tag_CONTENT,holder2);
                    break;
            }
        }else{
            if (type == TYPE_CONTENT){
                holder2 = (ViewHolder2)convertView.getTag(R.id.Tag_CONTENT);
            }
        }
        Object obj = mDiary.get(position);
        if (type == TYPE_CONTENT){
            diary d = (diary) obj;
            if(d!=null){
                holder2.day_icon.setText(d.getDay());
                holder2.num_icon.setText(d.getNum()+"");
                holder2.txt_content.setText(d.getContent());
            }
        }
        return convertView;
    }

    private class ViewHolder2{
        TextView day_icon;
        TextView num_icon;
        TextView txt_content;
    }

    public void add(diary data) {
        if (mDiary == null) {
            mDiary = new ArrayList<>();
        }
        mDiary.add(data);
        notifyDataSetChanged();
    }
}
