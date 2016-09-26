package com.example.roman.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ListView list_one;
    private MyAdapter mAdapter = null;
    private ArrayList<Object> mDiary = null;
    private ImageButton btn_add = null;
    private final int requestCode1 = 1;
    private final int requestCode2 = 2;
    private Spinner spin1;
    private Spinner spin2;
    private int month;
    private int day;
    private int week;
    private int year;
    private int month_selected;
    private int year_selected;
    private int day_selected;
    private int week_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialdate();

        mDiary = new ArrayList<Object>();
        if (getDiary(year,month)==null){
            refreshDiary(Initialize(mDiary),year,month);
        }
        mDiary = getDiary(year,month);

        list_one = (ListView)findViewById(R.id.listView);
        initView();

    }

    public void initView(){
        spin1 = (Spinner)findViewById(R.id.spinner1);
        spin2 = (Spinner)findViewById(R.id.spinner2);
        spin1.setOnItemSelectedListener(this);
        spin2.setOnItemSelectedListener(this);
        spin1.setSelection(month);
        spin2.setSelection(year-2000);

        btn_add = (ImageButton)findViewById(R.id.imageButton);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDiary = getDiary(year,month+1);
                year_selected = year;
                month_selected = month+1;
                day_selected = day;
                week_selected = week;
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                Date date = new Date();
                String s = getDate(date);
                intent.putExtra("date",s);
                startActivityForResult(intent,requestCode1);
            }
        });

        list_one.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                day_selected = position+1;
                Calendar cal = Calendar.getInstance();
                cal.set(year_selected,month_selected-1,day_selected);//这里的月份是从0开始算起的
                Date dt = cal.getTime();
                cal.setTime(dt);
                week_selected = cal.get(Calendar.DAY_OF_WEEK)-1;
                String s1 = getDayOfDate(week_selected)+"DAY / "+getMonthOfDate(month_selected-1)+" "
                        +Integer.toString(day_selected)+" / "+ Integer.toString(year_selected);
                intent.putExtra("date",s1);
                if (mDiary.get(position) instanceof diary){
                    intent.putExtra("content",((diary) mDiary.get(position)).getContent());
                }else {
                    intent.putExtra("content","");
                }
                startActivityForResult(intent,requestCode2);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?>parent,View view,int position,long id){
        switch (parent.getId()){
            case R.id.spinner1:
                month_selected = position+1;
                mDiary = getDiary(year_selected,month_selected);
                if (mDiary==null){
                    refreshDiary(Initialize(new ArrayList<Object>()),year_selected,month_selected);
                    mDiary = getDiary(year_selected,month_selected);
                }
                mAdapter = new MyAdapter(mDiary,MainActivity.this);
                list_one.setAdapter(mAdapter);
                break;
            case R.id.spinner2:
                year_selected = 2000+position;
                mDiary = getDiary(year_selected,month_selected);
                if (mDiary==null){
                    refreshDiary(Initialize(new ArrayList<Object>()),year_selected,month_selected);
                    mDiary = getDiary(year_selected,month_selected);
                }
                mAdapter = new MyAdapter(mDiary,MainActivity.this);
                list_one.setAdapter(mAdapter);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private ArrayList<Object> Initialize(ArrayList<Object> mDiary){
        for(int i=0;i<31;i++){
            mDiary.add(new noDiary());
        }
        return mDiary;
    }

    //设置初始日期
    public void Initialdate(){
        Date dt = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        week = cal.get(Calendar.DAY_OF_WEEK)-1;
        if (week<0) week=0;
        month = cal.get(Calendar.MONTH);
        if (month<0) month=0;
        day = cal.get(Calendar.DATE);
        if (day<0) day=0;
        year = cal.get(Calendar.YEAR);
        if (year<0) year=0;
        week_selected = week;
        month_selected = month;
        year_selected = year;
        day_selected = day;
    }

    //获取星期
    public String getDayOfDate(int week){
        String[] weekDays = {"SUN","MON","TUES","WEDENS","THURS","FRI","SATUR"};
        return weekDays[week];
    }

    //获取月份
    public String getMonthOfDate(int month){
        String[] months = this.getResources().getStringArray(R.array.months);
        return months[month];
    }

    //获得当前日期
    public String getDate(Date dt){
        String date = getDayOfDate(week)+"DAY / "+getMonthOfDate(month)+" "
                +Integer.toString(day)+" / "+ Integer.toString(year);
        return date;
    }

    /**
     * 接收当前Activity跳转后，目标Activity关闭后的回传值
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (resultCode){
            case RESULT_OK:{
                Bundle bundle = data.getExtras();
                String rc = bundle.getString("rc");

                diary d = new diary();
                d.setYear(year_selected);
                d.setMonth(month_selected);
                d.setContent(rc);
                d.setNum(day_selected);
                d.setDay(getDayOfDate(week_selected).substring(0,3));
                mDiary.set(day_selected-1,d);
                refreshDiary(mDiary,d.getYear(),d.getMonth());
                mAdapter = new MyAdapter(mDiary,MainActivity.this);
                list_one.setAdapter(mAdapter);

                if (year_selected!=year||month_selected!=month+1){
                    spin1.setSelection(month_selected-1);
                    spin2.setSelection(year_selected-2000);
                }
                break;
            }
            default:
                break;
        }
    }

    private void refreshDiary(ArrayList<Object> obj,int key1,int key2){
        for (int i=2000;i<=year;i++){
            if (key1 == i){
                for (int j=1;j<=12;j++){
                    if (key2 == j){
                        saveList(Integer.toString(i)+"-"+Integer.toString(j),obj);
                        return;
                    }
                }
            }
        }
    }
    private ArrayList<Object> getDiary(int key1,int key2){
        ArrayList<Object> obj = null;
        for (int i=2000;i<=year;i++){
            if (key1 == i){
                for (int j=1;j<=12;j++){
                    if (key2 == j){
                        obj = (ArrayList<Object>) getList(Integer.toString(i)+"-"+Integer.toString(j));
                        return obj;
                    }
                }
            }
        }
        return null;
    }

    private void saveList(String name, ArrayList<Object> list){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = this.openFileOutput(name, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
        } catch (Exception e) {
            e.printStackTrace();
            //这里是保存文件产生异常
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    //fos流关闭异常
                    e.printStackTrace();
                }
            }
            if (oos != null){
                try {
                    oos.close();
                } catch (IOException e) {
                    //oos流关闭异常
                    e.printStackTrace();
                }
            }
        }
    }

    private Object getList(String name){
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = this.openFileInput(name);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            //这里是读取文件产生异常
        } finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    //fis流关闭异常
                    e.printStackTrace();
                }
            }
            if (ois != null){
                try {
                    ois.close();
                } catch (IOException e) {
                    //ois流关闭异常
                    e.printStackTrace();
                }
            }
        }
        //读取产生异常，返回null
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
