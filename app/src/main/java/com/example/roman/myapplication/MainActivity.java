package com.example.roman.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import MyDate.*;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ListView list_one;
    private MyAdapter mAdapter = null;
    private monthAdapter monAdapter = null;

    private ImageButton btn_add = null;//加号按钮
    private ImageButton btn_scanner = null;//长条按钮
    private Spinner spin1;//月份选择框
    private Spinner spin2;//年份选择框

    private Date currentDate;   //当前日期
    private int year;
    private int month;
    private int year_selected;
    private int month_selected;

    private final int MAIN_REQUEST = 1;
    private final int THISMONTH_REQUEST = 2;
    private static final int TAG_MAIN = 3;//主界面的标记
    private static final int TAG_THISMONTH = 4;//当月日记浏览画面的标记
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialdate();//初始化日期变量为当前日期

        list_one = (ListView)findViewById(R.id.listView);
        initView();

        updateUI(year,month);
    }

    //初始化日记列表
    private void InitializeDiary(){
        DiaryLab diaryLab = DiaryLab.get(this);
        int listSize = Dateformat.getDaynumOfMonth(year_selected,month_selected);
        for (int i=0;i<listSize;i++){
            diary d = new diary();
            d.setDate(year_selected,month_selected,i+1);
            diaryLab.addDiary(d);
        }
    }

    //更新UI
    private void updateUI(int year,int month){
        DiaryLab diaryLab = DiaryLab.get(this);
        List<diary> diaries = diaryLab.getDiarys(year,month);
        if (diaries==null){
            InitializeDiary();
            diaries = diaryLab.getDiarys(year,month);
        }
        if(type == TAG_MAIN) {
            showMainList(diaries);
        }else if(type == TAG_THISMONTH) {
            showMonthDiary(diaries);
        }
    }

    //显示日记缩略
    private void showMainList(List<diary> diaries){
        if (mAdapter==null){
            mAdapter = new MyAdapter(diaries,this);
            list_one.setAdapter(mAdapter);
        }else {
            mAdapter.setDiaries(diaries);
            mAdapter.notifyDataSetChanged();
        }
    }

    //显示当月日记界面
    private void showMonthDiary(List<diary> mDiary){
        List<diary> monthDiary = new ArrayList<>();
        for (diary d:mDiary){
            if (!d.isEmpty())
                monthDiary.add(d);
        }
        if (monAdapter==null){
            monAdapter = new monthAdapter(monthDiary,MainActivity.this);
            list_one.setAdapter(monAdapter);
        }else {
            monAdapter.setDiaries(monthDiary);
            monAdapter.notifyDataSetChanged();
        }
    }

    public void initView(){
        spin1 = (Spinner)findViewById(R.id.spinner1);
        spin2 = (Spinner)findViewById(R.id.spinner2);

        ArrayList<String> spin2_items = new ArrayList<>();
        for (int i=2000;i<=year;i++){
            spin2_items.add(Integer.toString(i));
        }
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spin2_items);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(spinAdapter);

        spin1.setOnItemSelectedListener(this);
        spin2.setOnItemSelectedListener(this);
        spin1.setSelection(month);
        spin2.setSelection(year-2000);

        btn_add = (ImageButton)findViewById(R.id.imageButton);
        btn_add.setOnClickListener(new View.OnClickListener() {    //设置加号按钮点击事件
            @Override
            public void onClick(View v) {
                DiaryLab diaryLab = DiaryLab.get(MainActivity.this);
                diary todayDiary = diaryLab.getDiarys(year,month).get(currentDate.getDay()-1);
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("uuid",todayDiary.getID());
                startActivityForResult(intent,MAIN_REQUEST);
            }
        });

        type = TAG_MAIN;
        btn_scanner = (ImageButton)findViewById(R.id.imageButton2);
        btn_scanner.setOnClickListener(new View.OnClickListener() {   //设置长条按钮点击事件
            @Override
            public void onClick(View v) {
                DiaryLab diaryLab = DiaryLab.get(MainActivity.this);
                List<diary> diaries = diaryLab.getDiarys(year_selected,month_selected);
                if (type == TAG_MAIN){//判断当前是否为主界面
                    showMonthDiary(diaries);
                    if (list_one.getAdapter()==mAdapter)
                        list_one.setAdapter(monAdapter);
                    type = TAG_THISMONTH;
                }else if (type == TAG_THISMONTH){//判断当前是否为当月日记浏览界面
                    showMainList(diaries);
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
                    DiaryLab diaryLab = DiaryLab.get(MainActivity.this);
                    diary itemDiary = diaryLab.getDiarys(year_selected,month_selected).get(position);
                    intent.putExtra("uuid",itemDiary.getID());
                    startActivityForResult(intent,THISMONTH_REQUEST);
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
                updateUI(year_selected,month_selected);
                break;
            case R.id.spinner2:
                year_selected = 2000+position;
                updateUI(year_selected,month_selected);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    //设置初始日期
    public void Initialdate(){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        year_selected = year;
        month_selected = month;
        currentDate = new Date(year,month,calendar.get(Calendar.DATE));//当前日期
    }

    /**
     * 接收当前Activity跳转后，目标Activity关闭后的回传值
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode!= Activity.RESULT_OK){
            return;
        }
        switch (requestCode){
            case MAIN_REQUEST:
                updateUI(year,month);
                break;
            case THISMONTH_REQUEST:
                updateUI(year_selected,month_selected);
                break;
            default:
                break;
        }
    }
}
