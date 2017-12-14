package com.lesson.vv_bobkov.a2l5_bobkov;

/**
 * Created by samsung on 28.11.2017.
 */

class NoteWithTitle implements java.io.Serializable {

    private long mId;
    private String mTitle, mAddress, mNote;

    public NoteWithTitle(final long id, final String title,
                         final String address, final String note) {
        mId = id;
        mTitle = title;
        mAddress = address;
        mNote = note;
    }

    public NoteWithTitle(final String title,
                         final String address, final String note) {
        mId = -1;
        mTitle = title;
        mAddress = address;
        mNote = note;
    }

    public long getmId() {return mId;}

    public void setmId(final long id) {mId = id;}

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(final String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmNote() {
        return mNote;
    }

    public void setmNote(final String mText) {
        this.mNote = mText;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(final String address) {
        this.mAddress = address;
    }

    public void editNote(final NoteWithTitle noteWithTitle) {
        mId = noteWithTitle.getmId();
        mTitle = noteWithTitle.getmTitle();
        mAddress = noteWithTitle.getmAddress();
        mNote = noteWithTitle.getmNote();
    }
}
