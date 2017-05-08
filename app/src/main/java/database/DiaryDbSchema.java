package database;

/**
 * 数据表
 * Created by Roman on 2017/4/15.
 */
public class DiaryDbSchema {
    public static final class DiaryTable{
        public static final String NAME = "diarys";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String YEAR = "year";
            public static final String MONTH = "month";
            public static final String DAY = "day";
            public static final String Content = "content";
        }
    }
}
