package MyDate;

import java.util.Calendar;

/**
 * Created by Roman on 2017/4/16.
 */
public class Date {

    int year;
    int month;
    int day;

    public Date(){}

    public Date(int year,int month,int day){
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
