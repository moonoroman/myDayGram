package com.example.roman.myapplication;

import android.content.Intent;
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

/**
 * Created by Roman on 2016/9/20.
 */
public class EditActivity extends AppCompatActivity {

    private TextView data_result = null;
    private ImageButton btn_done = null;
    private ImageButton btn_clock = null;
    private EditText editText = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_edit);
        initView();
    }

    private void initView(){
        final Intent data = getIntent();
        data_result = (TextView)findViewById(R.id.date);
        editText = (EditText)findViewById(R.id.editText);
        Bundle bundle1 = data.getExtras();
        String s = bundle1.getString("date");
        String s1 = bundle1.getString("content");
        data_result.setText(s);
        editText.setText(s1);

        if(s!=null&&s.substring(0,3).equals("SUN")){
            SpannableStringBuilder style = new SpannableStringBuilder(s);
            style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this,R.color.sundaycolor)),0,6,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);     //设置指定位置文字的颜色
            data_result.setText(style);
        }

        if (s1!=null)
            editText.setSelection(s1.length());

        btn_done = (ImageButton)findViewById(R.id.done);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle2 = new Bundle();
                String str = editText.getText().toString();
                bundle2.putString("rc",str);
                data.putExtras(bundle2);

                EditActivity.this.setResult(RESULT_OK,data);
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
}
