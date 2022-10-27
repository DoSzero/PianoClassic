package com.har.pianoclassic.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.har.pianoclassic.model.DBConst.DATABASE_NAME
import com.har.pianoclassic.model.DBConst.DATABASE_VERSION
import com.har.pianoclassic.model.DBConst.KEY_DATETIME
import com.har.pianoclassic.model.DBConst.KEY_ID
import com.har.pianoclassic.model.DBConst.KEY_NAME
import com.har.pianoclassic.model.DBConst.KEY_SCORE
import com.har.pianoclassic.model.DBConst.TABLE_SCORE


class DBHandler(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val CREATE_FOODS_TABLE =
            ("CREATE TABLE " + TABLE_SCORE + "(" + KEY_ID + " INTEGER PRIMARY KEY, "
             + KEY_SCORE + " INTEGER," + KEY_NAME + " TEXT," + KEY_DATETIME + " TEXT)")
        sqLiteDatabase.execSQL(CREATE_FOODS_TABLE)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_SCORE")
        onCreate(sqLiteDatabase)
    }

}