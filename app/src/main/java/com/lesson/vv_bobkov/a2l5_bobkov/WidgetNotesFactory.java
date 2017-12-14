package com.lesson.vv_bobkov.a2l5_bobkov;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.lesson.vv_bobkov.a2l5_bobkov.Exceptions.DBCursorIsEmptyException;
import com.lesson.vv_bobkov.a2l5_bobkov.Exceptions.DBCursorIsNullExceptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by samsung on 14.12.2017.
 */

class WidgetNotesFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    App mApp;
    int widgetId;
    List<NoteWithTitle> records;

    public WidgetNotesFactory(Context context, Intent intent) {
        mContext = context;
        mApp = App.getmApp();
        widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        records = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(
                mContext.getPackageName(),
                R.layout.list_view_item);
        remoteViews.setTextViewText(R.id.tvNotesTitle, records.get(position).getmTitle());
        Intent clickIntent = new Intent();
        clickIntent.putExtra(NoteAppWidgetProvider.ITEM_POSITION, position);
        remoteViews.setOnClickFillInIntent(R.id.tvNotesTitle, clickIntent);
        return remoteViews;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        records.clear();
        try {
            NotesTable.createNoteWithTitleArrayListFromBd(mApp.getmDBController().getmSqLiteDatabase());
        } catch (DBCursorIsEmptyException e) {
            e.printStackTrace();
        } catch (DBCursorIsNullExceptions e) {
            e.printStackTrace();
        }
        records = mApp.getmNoteWithTitleList();
    }

    @Override
    public void onDestroy() {

    }
}
