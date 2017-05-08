package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import database.DiaryDbSchema.DiaryTable;

/**
 * 数据库
 * Created by Roman on 2017/4/15.
 */
public class DiaryBaseHelper extends SQLiteOpenHelper{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "diaryBase.db";

    public DiaryBaseHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //创建表
        db.execSQL("create table " + DiaryTable.NAME + "(" +
                "_id integer primary key autoincrement," +
                DiaryTable.Cols.UUID + "," +
                DiaryTable.Cols.YEAR + "," +
                DiaryTable.Cols.MONTH + "," +
                DiaryTable.Cols.DAY + "," +
                DiaryTable.Cols.Content +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
