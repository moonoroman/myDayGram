package com.example.roman.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Roman on 2016/9/20.
 */
public class EditActivity extends AppCompatActivity {

    private TextView data_result = null;
    private ImageButton btn_done = null;
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

        btn_done = (ImageButton)findViewById(R.id.done);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle2 = new Bundle();
                String str = editText.getText().toString();
                if(null == str)
                    return;
                bundle2.putString("rc",str);
                data.putExtras(bundle2);

                EditActivity.this.setResult(RESULT_OK,data);
                EditActivity.this.finish();
            }
        });
    }
}
