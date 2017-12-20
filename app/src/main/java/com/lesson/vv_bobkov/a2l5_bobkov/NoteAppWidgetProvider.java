package com.lesson.vv_bobkov.a2l5_bobkov;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.lesson.vv_bobkov.a2l5_bobkov.Exceptions.DBCursorIsEmptyException;
import com.lesson.vv_bobkov.a2l5_bobkov.Exceptions.DBCursorIsNullExceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class NoteAppWidgetProvider extends AppWidgetProvider {

    static final String ACTION_ON_CLICK = "com.lesson.vv_bobkov.a2l5_bobkov.item_on_click";
    static final String ITEM_POSITION = "item_position";
    static List<NoteWithTitle> mNoteWithTitleList;
    static App mApp;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.note_app_widget);
        // Setting the visibility of views
        if (mNoteWithTitleList.size() > 0) {
            remoteViews.setViewVisibility(R.id.tvAppwidget, View.GONE);
            remoteViews.setViewVisibility(R.id.lvAppWidget, View.VISIBLE);
        } else {
            remoteViews.setViewVisibility(R.id.tvAppwidget, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.lvAppWidget, View.GONE);
        }
        mApp = App.getmApp();
        mApp.NOTES_MODE = mApp.EDIT;
        mApp.createmSelectedItems();

        // Opening of NoteActivity for writing of the new note
        Intent newNoteWithTitle = new Intent(context, NoteActivity.class);

        newNoteWithTitle.setAction(AppWidgetManager.ACTION_APPWIDGET_BIND);
        newNoteWithTitle.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, appWidgetId, newNoteWithTitle, 0);
        App.START_FROM = App.WIDGET;
        remoteViews.setOnClickPendingIntent(R.id.btnWidgetTitle, pendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,
                R.id.lvAppWidget);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        mApp = App.getmApp();
        // Reade the data from DB
        if (mApp.getmNoteWithTitleList() == null) {
            try {
                mNoteWithTitleList = NotesTable.createNoteWithTitleArrayListFromBd(mApp.getmDBController().getmSqLiteDatabase());
            } catch (DBCursorIsEmptyException e) {
                mNoteWithTitleList = new ArrayList<>();
            } catch (DBCursorIsNullExceptions e1) {
                e1.printStackTrace();
            }
        } else {
            mNoteWithTitleList = mApp.getmNoteWithTitleList();
        }
        if (mApp.selectedItemsIsEmpty()) {
            mApp.createmSelectedItems();
        } else {
            mApp.getmSelectedItems().clear();
        }

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

