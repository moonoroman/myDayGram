package com.example.roman.myapplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import database.DiaryBaseHelper;
import database.DiaryCursorWrapper;
import database.DiaryDbSchema.DiaryTable;

/**
 * Created by Roman on 2017/4/15.
 */
public class DiaryLab {
    private static DiaryLab sDiaryLab;
    private List<diary> mDiarys;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static DiaryLab get(Context context){
        if (sDiaryLab==null){
            sDiaryLab = new DiaryLab(context);
        }
        return sDiaryLab;
    }

    private DiaryLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new DiaryBaseHelper(mContext).getWritableDatabase();//创建数据库
        mDiarys = new ArrayList<>();
    }

    //插入一条记录到数据库
    public void addDiary(diary d){
        ContentValues values = getContentValues(d);
        mDatabase.insert(DiaryTable.NAME,null,values);
    }

    //获得数据库中某年某月的所有日记
    public List<diary> getDiarys(int year,int month){
        List<diary> diaries = new ArrayList<>();

        DiaryCursorWrapper cursor = queryDiarys(DiaryTable.Cols.YEAR + "=? AND "+DiaryTable.Cols.MONTH + "=?",
                new String[]{String.valueOf(year),String.valueOf(month)});
        try {
            if(cursor.getCount()==0)
                return null;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                diaries.add(cursor.getDiary());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }

        return diaries;
    }

    //获得数据库中某条日记
    public diary getDiary(UUID id){
        DiaryCursorWrapper cursor = queryDiarys(DiaryTable.Cols.UUID+"=?",new String[]{id.toString()});
        try {
            if (cursor.getCount()==0)
                return null;
            cursor.moveToFirst();
            return cursor.getDiary();
        }finally {
            cursor.close();
        }
    }

    //更新一条记录
    public void updateDiary(diary d){
        String uuidString = d.getID().toString();
        ContentValues values = getContentValues(d);
        mDatabase.update(DiaryTable.NAME,values,DiaryTable.Cols.UUID+"=?",new String[]{uuidString});
    }

    private static ContentValues getContentValues(diary d){
        ContentValues values = new ContentValues();
        values.put(DiaryTable.Cols.UUID, d.getID().toString());
        values.put(DiaryTable.Cols.YEAR, d.getDate().getYear()+"");
        values.put(DiaryTable.Cols.MONTH, d.getDate().getMonth()+"");
        values.put(DiaryTable.Cols.DAY, d.getDate().getDay());
        values.put(DiaryTable.Cols.Content, d.getContent());

        return values;
    }

    private DiaryCursorWrapper queryDiarys(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                DiaryTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,//group by
                null,//having
                null
        );
        return new DiaryCursorWrapper(cursor);
    }

}
