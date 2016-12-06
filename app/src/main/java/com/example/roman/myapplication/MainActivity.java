package com.example.roman.myapplication;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
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
    private ArrayList<Object> mDiary = null;//日记列表
    private ImageButton btn_add = null;//加号按钮
    private ImageButton btn_scanner = null;//长条按钮
    private Spinner spin1;//月份选择框
    private Spinner spin2;//年份选择框
    private final int requestCode1 = 1;
    private final int requestCode2 = 2;
    private int month;       //当前日期时间
    private int day;
    private int week;
    private int year;
    private int month_selected;     //选择的日期时间
    private int year_selected;
    private int day_selected;
    private int week_selected;

    private ArrayList<diary> DiaryList = null;    //已写的日记列表
    private monthAdapter monAdapter = null;
    private final int TAG_MAIN = 3;//主界面的标记
    private final int TAG_THISMONTH = 4;//当月日记浏览画面的标记
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialdate();//初始化日期变量为当前日期

        //从文件中获取
        mDiary = new ArrayList<Object>();
        if (getDiary(year,month+1)==null){
            refreshDiary(Initialize(mDiary),year,month);
        }
        mDiary = getDiary(year,month+1);

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
        btn_add.setOnClickListener(new View.OnClickListener() {    //设置加号按钮点击事件
            @Override
            public void onClick(View v) {
                mDiary = getDiary(year,month+1);
                year_selected = year;
                month_selected = month;
                day_selected = day;
                week_selected = week;
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                Date date = new Date();
                String s = getDate(date);
                intent.putExtra("date",s);
                startActivityForResult(intent,requestCode1);
            }
        });

        type = TAG_MAIN;
        btn_scanner = (ImageButton)findViewById(R.id.imageButton2);
        btn_scanner.setOnClickListener(new View.OnClickListener() {   //设置长条按钮点击事件
            @Override
            public void onClick(View v) {
                if (type == TAG_MAIN){//判断当前是否为主界面
                    ArrayList<Object> thisDiary = getDiary(year_selected,month_selected + 1);
                    if (thisDiary == null){
                        refreshDiary(Initialize(new ArrayList<Object>()),year_selected,month_selected + 1);
                        thisDiary = getDiary(year_selected, month_selected + 1);
                    }
                    showMonthDiary(thisDiary);
                    type = TAG_THISMONTH;
                }else if (type == TAG_THISMONTH){//判断当前是否为当月日记浏览界面
                    list_one.setAdapter(mAdapter);
                    type = TAG_MAIN;
                }
            }
        });

        //设置列表项点击事件
        list_one.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (type == TAG_MAIN){
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    day_selected = position+1;
                    year_selected = spin2.getSelectedItemPosition()+2000;
                    month_selected = spin1.getSelectedItemPosition();
                    Calendar cal = Calendar.getInstance();
                    cal.set(year_selected,month_selected,day_selected);//这里的月份是从0开始算起的
                    Date dt = cal.getTime();
                    cal.setTime(dt);
                    week_selected = cal.get(Calendar.DAY_OF_WEEK)-1;
                    String s1 = getDayOfDate(week_selected)+"DAY / "+getMonthOfDate(month_selected)+" "
                            +Integer.toString(day_selected)+" / "+ Integer.toString(year_selected);
                    intent.putExtra("date",s1);
                    if (mDiary.get(position) instanceof diary){
                        System.out.print(mDiary);
                        intent.putExtra("content",((diary) mDiary.get(position)).getContent());
                    }else {
                        intent.putExtra("content","");
                    }
                    startActivityForResult(intent,requestCode2);
                }else if (type == TAG_THISMONTH){
                    return;
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?>parent,View view,int position,long id){
        switch (parent.getId()){
            case R.id.spinner1:
                month_selected = position;
                mDiary = getDiary(year_selected,month_selected+1);
                if (mDiary==null){
                    refreshDiary(Initialize(new ArrayList<Object>()),year_selected,month_selected+1);
                    mDiary = getDiary(year_selected,month_selected+1);
                }
                mAdapter = new MyAdapter(mDiary,MainActivity.this);
                if(type == TAG_MAIN) {
                    list_one.setAdapter(mAdapter);
                }else if(type == TAG_THISMONTH) {
                    showMonthDiary(mDiary);
                }
                break;
            case R.id.spinner2:
                year_selected = 2000+position;
                mDiary = getDiary(year_selected,month_selected+1);
                if (mDiary==null){
                    refreshDiary(Initialize(new ArrayList<Object>()),year_selected,month_selected+1);
                    mDiary = getDiary(year_selected,month_selected+1);
                }
                mAdapter = new MyAdapter(mDiary,MainActivity.this);
                if(type == TAG_MAIN) {
                    list_one.setAdapter(mAdapter);
                }else if(type == TAG_THISMONTH) {
                    showMonthDiary(mDiary);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    //初始化日记列表
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

    //显示当月日记界面
    public void showMonthDiary(ArrayList<Object> mDiary){
        DiaryList = new ArrayList<diary>();
        for(int i=0;i<mDiary.size();i++){
            Object obj = mDiary.get(i);
            if (obj!=null && obj instanceof diary){
                DiaryList.add((diary)obj);
            }
        }
        monAdapter = new monthAdapter(DiaryList,MainActivity.this);
        list_one.setAdapter(monAdapter);
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
                d.setMonth(month_selected+1);
                d.setContent(rc);
                d.setNum(day_selected);
                d.setDay(getDayOfDate(week_selected).substring(0,3));

                mDiary.set(day_selected-1,d);
                refreshDiary(mDiary,d.getYear(),d.getMonth());
                mAdapter = new MyAdapter(mDiary,MainActivity.this);

                if(type == TAG_MAIN) {
                    list_one.setAdapter(mAdapter);
                }else if(type == TAG_THISMONTH) {
                    showMonthDiary(mDiary);
                }
                if (year!=spin2.getSelectedItemPosition()||month!=spin1.getSelectedItemPosition()){
                    spin1.setSelection(month_selected);
                    spin2.setSelection(year_selected-2000);
                }
                break;
            }
            default:
                break;
        }
    }

    //更新本地数据
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

    //获取本地数据
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
}
