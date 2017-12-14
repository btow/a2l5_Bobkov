package com.lesson.vv_bobkov.a2l5_bobkov;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lesson.vv_bobkov.a2l5_bobkov.Exceptions.DBCursorIsEmptyException;
import com.lesson.vv_bobkov.a2l5_bobkov.Exceptions.DBCursorIsNullExceptions;
import com.lesson.vv_bobkov.a2l5_bobkov.Exceptions.DBNewVersionLessOldExceptions;

/**
 * Created by bobkov-vv on 11.12.2017.
 */

class DBController {

    private SQLiteDatabase mSqLiteDatabase;

    DBController(Context cxt) {
        String DB_NAME = "notes";
        int DB_VERSION = 1;
        DbOpenHelper mDbOpenHelper = new DbOpenHelper(cxt, DB_NAME, null, DB_VERSION);
        mSqLiteDatabase = mDbOpenHelper.getWritableDatabase();
    }

    SQLiteDatabase getmSqLiteDatabase() {
        return mSqLiteDatabase;
    }

    void readeNoteWithTitleArrayListFromBd()
            throws DBCursorIsNullExceptions, DBCursorIsEmptyException {
        App.getmApp().setmNoteWithTitleList(NotesTable.createNoteWithTitleArrayListFromBd(mSqLiteDatabase));
    }

    private class DbOpenHelper extends SQLiteOpenHelper {

        DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            NotesTable.createTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                NotesTable.upgrade(db, oldVersion, newVersion);
            } catch (DBNewVersionLessOldExceptions e) {
                e.printStackTrace();
            }
        }
    }
}
