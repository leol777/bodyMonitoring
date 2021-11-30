package liang.junxuan.bodymonitoring.contentProvider;

import android.net.Uri;
import android.provider.BaseColumns;

public class provider {
    public static final String AUTHORITY = "liang.junxuan.bodymonitor.data.provider";

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.liang.bodymonitor";

    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.liang.bodymonitor";

    //Uric Acid
    public static final class UricAcidColumms implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/uric_acid");
        public static final String _ID = "id";
        public static final String TABLE_NAME = "UricAcid";
        public static final String DATE_TIME = "dateTime";
        public static final String URIC_ACID = "uricAcid";
        public static final String BLOOD_SUGAR = "bloodSugar";
    }

    public static final class BloodPressureColumms implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/blood_pressure");
        public static final String _ID = "id";
        public static final String TABLE_NAME = "BloodPressure";
        public static final String DATE_TIME = "dateTime";
        public static final String UPPER_BLOOD_PRESSURE = "upperBP";
        public static final String LOWER_BLOOD_PRESSURE = "lowerBP";
        public static final String HEART_BEAT = "heartBeat";
    }
}
