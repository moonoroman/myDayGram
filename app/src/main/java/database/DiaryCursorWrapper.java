package database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.roman.myapplication.diary;

import java.util.Date;
import java.util.UUID;

import database.DiaryDbSchema.DiaryTable;

/**
 * Created by Roman on 2017/4/15.
 */
public class DiaryCursorWrapper extends CursorWrapper {
    public DiaryCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public diary getDiary(){
        String uuidString = getString(getColumnIndex(DiaryTable.Cols.UUID));
        int year = Integer.parseInt(getString(getColumnIndex(DiaryTable.Cols.YEAR)));
        int month = Integer.parseInt(getString(getColumnIndex(DiaryTable.Cols.MONTH)));
        int day = getInt(getColumnIndex(DiaryTable.Cols.DAY));
        String content = getString(getColumnIndex(DiaryTable.Cols.Content));

        diary d = new diary(UUID.fromString(uuidString));
        d.setDate(year,month,day);
        d.setContent(content);

        return d;
    }
}
