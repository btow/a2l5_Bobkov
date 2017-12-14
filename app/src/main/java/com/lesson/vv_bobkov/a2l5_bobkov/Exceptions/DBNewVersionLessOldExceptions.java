package com.lesson.vv_bobkov.a2l5_bobkov.Exceptions;

/**
 * Created by bobkov-vv on 11.12.2017.
 */

public class DBNewVersionLessOldExceptions extends Exception {

    public DBNewVersionLessOldExceptions(final String excMsg) {
        super(excMsg);
    }
}
