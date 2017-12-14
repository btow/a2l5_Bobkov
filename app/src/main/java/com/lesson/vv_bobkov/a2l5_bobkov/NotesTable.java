package com.lesson.vv_bobkov.a2l5_bobkov;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lesson.vv_bobkov.a2l5_bobkov.Exceptions.DBCursorIsNullExceptions;
import com.lesson.vv_bobkov.a2l5_bobkov.Exceptions.DBNewVersionLessOldExceptions;
import com.lesson.vv_bobkov.a2l5_bobkov.Exceptions.DBRecordsDeletingExceptions;

import java.util.ArrayList;

/**
 * Created by bobkov-vv on 11.12.2017.
 */

class NotesTable {

    private static final String
            TABLE_NAME = "notes",
            FIELD_ID = "_id",
            FIELD_TITLE = "title",
            FIELD_ADDRESS = "address",
            FIELD_NOTE = "note";
    private static final String
            SQL_COMMAND_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            "(" +
            FIELD_ID + " INTEGER PRIMARY KEY, " +
            FIELD_TITLE + " TEXT, " +
            FIELD_ADDRESS + " TEXT, " +
            FIELD_NOTE + " TEXT" +
            ")";
    private static final String
            SQL_COMMAND_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(SQL_COMMAND_CREATE_TABLE);
    }

    public static void upgrade(SQLiteDatabase db, final int oldVersion, final int newVersion)
            throws DBNewVersionLessOldExceptions {
        if (oldVersion > newVersion) {
            String excMsg = "The new db version less then there old version";
            throw new DBNewVersionLessOldExceptions(excMsg);
        }
        db.execSQL(SQL_COMMAND_DROP_TABLE);
        db.execSQL(SQL_COMMAND_CREATE_TABLE);
    }

    public static long addRecord(SQLiteDatabase db, final NoteWithTitle record) {
        ContentValues cv = new ContentValues();
        cv.put(FIELD_TITLE, record.getmTitle());
        cv.put(FIELD_ADDRESS, record.getmAddress());
        cv.put(FIELD_NOTE, record.getmNote());
        long addadRecord = 0;
        db.beginTransaction();
        try {
            addadRecord = db.insert(TABLE_NAME, null, cv);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            throw e;
        } finally {
            db.endTransaction();
        }
        return addadRecord;
    }

    public static void updateRecord(SQLiteDatabase db,
                                    final NoteWithTitle newRecord) {
        ContentValues cv = new ContentValues();
        cv.put(FIELD_TITLE, newRecord.getmTitle());
        cv.put(FIELD_ADDRESS, newRecord.getmAddress());
        cv.put(FIELD_NOTE, newRecord.getmNote());
        db.beginTransaction();
        try {
            db.update(TABLE_NAME, cv,
                    FIELD_ID + " = ?",
                    new String[]{String.valueOf(newRecord.getmId())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            throw e;
        } finally {
            db.endTransaction();
        }
    }

    public static void deleteRecord(SQLiteDatabase db, final long[] ides)
            throws DBRecordsDeletingExceptions {
        for (long id :
                ides) {
            if (db.delete(TABLE_NAME, FIELD_ID + " = ?", new String[] {String.valueOf(id)}) == 0) {
                String excMsg = "The records deleting is in error";
                throw new DBRecordsDeletingExceptions(excMsg);
            };
        }
    }

    public static ArrayList<NoteWithTitle> createNoteWithTitleArrayListFromBd(final SQLiteDatabase db)
            throws DBCursorIsEmpty, DBCursorIsNullExceptions {
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        String excMsg = "The cursor is null";
        if (cursor == null) {
            throw new DBCursorIsNullExceptions(excMsg);
        } else if (cursor.getCount() < 1) {
            excMsg = "The cursor is empty";
            throw new DBCursorIsEmpty(excMsg);
        }
        ArrayList<NoteWithTitle> noteWithTitleList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                noteWithTitleList.add(new NoteWithTitle(
                        cursor.getInt(cursor.getColumnIndex(FIELD_ID)),
                        cursor.getString(cursor.getColumnIndex(FIELD_TITLE)),
                        cursor.getString(cursor.getColumnIndex(FIELD_ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(FIELD_NOTE))
                ));
            } while (cursor.moveToNext());
        }
        return noteWithTitleList;
    }
}
