package com.example.roman.myapplication;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;
import java.util.UUID;

import MyDate.*;


/**
 * Created by Roman on 2016/9/20.
 */
public class EditActivity extends AppCompatActivity {

    private diary mDiary;
    private TextView data_result = null;
    private ImageButton btn_done = null;
    private ImageButton btn_clock = null;
    private EditText editText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_edit);

        UUID diaryId = (UUID)getIntent().getSerializableExtra("uuid");
        mDiary = DiaryLab.get(this).getDiary(diaryId);
        initView();
    }

    private void initView(){
        data_result = (TextView)findViewById(R.id.date);
        editText = (EditText)findViewById(R.id.editText);

        data_result.setText(getDateText(mDiary.getDate()));
        editText.setText(mDiary.getContent());

        if(Dateformat.getDayOfWeek(mDiary.getDate()).equals("SUN")){
            SpannableStringBuilder style = new SpannableStringBuilder(getDateText(mDiary.getDate()));
            style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this,R.color.sundaycolor)),0,6,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);     //设置指定位置文字的颜色
            data_result.setText(style);
        }

        if (mDiary.getContent()!=null)
            editText.setSelection(mDiary.getContent().length());

        btn_done = (ImageButton)findViewById(R.id.done);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDiary.setContent(editText.getText().toString());
                EditActivity.this.setResult(RESULT_OK);
                EditActivity.this.finish();
            }
        });

        btn_clock = (ImageButton)findViewById(R.id.clock);
        btn_clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                long time = System.currentTimeMillis();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(time);
                int mHour = calendar.get(Calendar.HOUR_OF_DAY);
                int mMinutes = calendar.get(Calendar.MINUTE);
                String ss = "am";
                if (mHour>12){
                    mHour = mHour%12;
                    ss = "pm";
                }
                s = s + Integer.toString(mHour) + ":" + Integer.toString(mMinutes) + ss + " ";
                editText.setText(s);
                editText.setSelection(s.length());
            }
        });
    }

    //获得日期标题
    public String getDateText(Date dt){
        return Dateformat.getDayOfWeek(dt)+"DAY / "+Dateformat.getMonthString(dt.getMonth())+" "
                + dt.getDay() + " / " + dt.getYear();
    }

    @Override
    protected void onPause(){
        super.onPause();
        DiaryLab.get(this).updateDiary(mDiary);
    }
}
