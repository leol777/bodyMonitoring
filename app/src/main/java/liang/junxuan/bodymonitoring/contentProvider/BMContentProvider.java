package liang.junxuan.bodymonitoring.contentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.security.Provider;
import java.util.HashMap;
import java.util.Objects;

import liang.junxuan.bodymonitoring.dataBase.BodyMonitordbHelper;

public class BMContentProvider extends ContentProvider {
    private static HashMap<String, String> projectionMap;
    private BodyMonitordbHelper dbHelper;
    private static final String TAG = "BMContentProvider";

    public static final int URIC_ACIDS = 0;
    public static final int URIC_ACID_ID = 1;
    public static final int BLOOD_PRESSURES = 2;
    public static final int BLOOD_PRESSURE_ID = 3;

    private static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(provider.AUTHORITY, "uricAcids", URIC_ACIDS);
        uriMatcher.addURI(provider.AUTHORITY, "uricAcid/#", URIC_ACID_ID);

        uriMatcher.addURI(provider.AUTHORITY, "bloodPressures", BLOOD_PRESSURES);
        uriMatcher.addURI(provider.AUTHORITY, "bloodPressure/#", BLOOD_PRESSURE_ID);

        projectionMap = new HashMap<>();
        projectionMap.put(provider.UricAcidColumms._ID, provider.UricAcidColumms._ID);
        projectionMap.put(provider.UricAcidColumms.URIC_ACID, provider.UricAcidColumms.URIC_ACID);
        projectionMap.put(provider.UricAcidColumms.BLOOD_SUGAR, provider.UricAcidColumms.BLOOD_SUGAR);
        projectionMap.put(provider.UricAcidColumms.DATE_TIME, provider.UricAcidColumms.DATE_TIME);

        projectionMap.put(provider.BloodPressureColumms._ID, provider.BloodPressureColumms._ID);
        projectionMap.put(provider.BloodPressureColumms.DATE_TIME, provider.BloodPressureColumms.DATE_TIME);
        projectionMap.put(provider.BloodPressureColumms.HEART_BEAT, provider.BloodPressureColumms.HEART_BEAT);
        projectionMap.put(provider.BloodPressureColumms.UPPER_BLOOD_PRESSURE, provider.BloodPressureColumms.UPPER_BLOOD_PRESSURE);
        projectionMap.put(provider.BloodPressureColumms.LOWER_BLOOD_PRESSURE, provider.BloodPressureColumms.LOWER_BLOOD_PRESSURE);
    }


    @Override
    public boolean onCreate() {
        dbHelper = new BodyMonitordbHelper(getContext(), 1);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)){
            case URIC_ACIDS:
            case URIC_ACID_ID:
                qb.setTables(provider.UricAcidColumms.TABLE_NAME);
                break;
            case BLOOD_PRESSURES:
            case BLOOD_PRESSURE_ID:
                qb.setTables(provider.BloodPressureColumms.TABLE_NAME);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        switch (uriMatcher.match(uri)){
            case URIC_ACIDS:
            case BLOOD_PRESSURES:
                qb.setProjectionMap(projectionMap);
                break;
            case URIC_ACID_ID:
                qb.setProjectionMap(projectionMap);
                qb.appendWhere(provider.UricAcidColumms._ID + "=" + uri.getPathSegments().get(1));
                break;

            case BLOOD_PRESSURE_ID:
                qb.setProjectionMap(projectionMap);
                qb.appendWhere(provider.BloodPressureColumms._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case URIC_ACIDS:
            case BLOOD_PRESSURES:
                return provider.CONTENT_TYPE;
            case URIC_ACID_ID:
            case BLOOD_PRESSURE_ID:
                return provider.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues initialValues) {
        ContentValues values;
        if (initialValues != null){
            values = new ContentValues(initialValues);
        }else {
            values = new ContentValues();
        }

        String tableName = "";
        String nullColumn = "";
        switch (uriMatcher.match(uri)){
            case URIC_ACIDS:
                tableName = provider.UricAcidColumms.TABLE_NAME;
                nullColumn = provider.UricAcidColumms.DATE_TIME;
                break;
            case BLOOD_PRESSURES:
                tableName = provider.BloodPressureColumms.TABLE_NAME;
                nullColumn = provider.BloodPressureColumms.DATE_TIME;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = db.insert(tableName, nullColumn, values);
        if (rowId > 0){
            Uri noteUri = ContentUris.withAppendedId(uri, rowId);
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }
        throw new SQLException("Failed to insert row into" + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)){
            case URIC_ACIDS:
                count = db.delete(provider.UricAcidColumms.TABLE_NAME, selection, selectionArgs);
                break;
            case URIC_ACID_ID:
                String uaID = uri.getPathSegments().get(1);
                count = db.delete(provider.UricAcidColumms.TABLE_NAME, provider.UricAcidColumms._ID
                        +"=" + uaID
                        +(!TextUtils.isEmpty(selection) ? "AND (" + selection + ')':""), selectionArgs);
                break;
            case BLOOD_PRESSURES:
                count = db.delete(provider.BloodPressureColumms.TABLE_NAME, selection, selectionArgs);
                break;
            case BLOOD_PRESSURE_ID:
                String bpID = uri.getPathSegments().get(1);
                count = db.delete(provider.BloodPressureColumms.TABLE_NAME, provider.BloodPressureColumms._ID
                        +"=" + bpID
                        +(!TextUtils.isEmpty(selection) ? "AND (" + selection + ')':""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)){
            case URIC_ACIDS:
                count = db.update(provider.UricAcidColumms.TABLE_NAME, values ,selection, selectionArgs);
                break;
            case URIC_ACID_ID:
                String uaID = uri.getPathSegments().get(1);
                count = db.update(provider.UricAcidColumms.TABLE_NAME, values, provider.UricAcidColumms._ID
                        +"=" + uaID
                        +(!TextUtils.isEmpty(selection) ? "AND (" + selection + ')':""), selectionArgs);
                break;
            case BLOOD_PRESSURES:
                count = db.update(provider.BloodPressureColumms.TABLE_NAME, values,selection, selectionArgs);
                break;
            case BLOOD_PRESSURE_ID:
                String bpID = uri.getPathSegments().get(1);
                count = db.update(provider.BloodPressureColumms.TABLE_NAME, values,provider.BloodPressureColumms._ID
                        +"=" + bpID
                        +(!TextUtils.isEmpty(selection) ? "AND (" + selection + ')':""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return count;
    }
}
