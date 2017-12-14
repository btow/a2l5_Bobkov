package com.lesson.vv_bobkov.a2l5_bobkov.Exceptions;

/**
 * Created by bobkov-vv on 11.12.2017.
 */

class DBCursorIsEmptyException extends Exception {
    public DBCursorIsEmptyException(final String excMsg) {
        super(excMsg);
    }
}
