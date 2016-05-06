package com.zaks.ayala.fivethings.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zaks.ayala.fivethings.R;

/**
 * Created by אילה on 4/18/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "FiveThings.db", null, 3);
    }

    SQLiteDatabase SQLdb;

    String createTableSQL =
            "CREATE TABLE " + FactsContract.FactsEntry.TABLE_NAME + "(" +
                    FactsContract.FactsEntry._ID + " INTEGER PRIMARY KEY, " +
                    FactsContract.FactsEntry.COLUMN_ORDER + " INTEGER, " +
                    FactsContract.FactsEntry.COLUMN_TEXT + " TEXT ," +
                    FactsContract.FactsEntry.COLUMN_IMAGE + " INTEGER" + ")";

    String dropTableSQL = "DROP TABLE IF EXISTS " + FactsContract.FactsEntry.TABLE_NAME;


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableSQL);
        initData(db);
        SQLdb = db;
    }

    private void initData(SQLiteDatabase db) {
        insertRow(db, 1, "אנדרואיד היא מערכת הפעלה מבוססת לינוקס המיועדת לסמארטפונים, טאבלטים, טלוויזיות חכמות, שעונים חכמים ומכוניות.", R.drawable.android_devices);
        insertRow(db, 2, "החל משנת 2010 אנדרואיד היא מערכת ההפעלה הנפוצה ביותר לטלפונים חכמים", R.drawable.android_winner);
        insertRow(db, 3, "Google לא המציאה את אנדרואיד. היא קנתה אותה מחברת סטארט-אפ קטנה שרצתה לפתח מערכת הפעלה למצלמות.", R.drawable.google_android);
        insertRow(db, 4, "כל גרסאות מערכת ההפעלה של אנדרואיד קיבלו שמות של קינוחים, ומסודרות לפי סדר ה-ABC", R.drawable.android_versions);
        insertRow(db, 5, "הקוד של אנדרואיד הוא קוד פתוח תחת רישיון ציבורי וזמין להורדה באינטרנט.", R.drawable.android_opensource);
    }

    private void insertRow(SQLiteDatabase db, int order, String text, int image) {
        ContentValues values = new ContentValues();
        values.put(FactsContract.FactsEntry.COLUMN_ORDER, order);
        values.put(FactsContract.FactsEntry.COLUMN_TEXT, text);
        values.put(FactsContract.FactsEntry.COLUMN_IMAGE, image);
        db.insert(FactsContract.FactsEntry.TABLE_NAME, null, values);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropTableSQL);
        SQLdb = null;
        onCreate(db);
    }
}
