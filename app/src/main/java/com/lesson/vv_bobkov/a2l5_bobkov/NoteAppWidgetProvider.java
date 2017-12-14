package com.lesson.vv_bobkov.a2l5_bobkov;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class NoteAppWidgetProvider extends AppWidgetProvider {

    static final String ACTION_ON_CLICK = "com.lesson.vv_bobkov.a2l5_bobkov.itemonclick";
    static final String ITEM_POSITION = "item_position";
    private App mApp;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.note_app_widget);

        Intent adapter = new Intent(context, NotesWidgetService.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        Uri data = Uri.parse(adapter.toUri(Intent.URI_INTENT_SCHEME));
        adapter.setData(data);
        views.setRemoteAdapter(R.id.lvAppWidget, adapter);

        Intent listClickIntent = new Intent(context, NoteAppWidgetProvider.class);
        listClickIntent.setAction(ACTION_ON_CLICK);
        PendingIntent listClickPIntent = PendingIntent.getBroadcast(context, 0,
                listClickIntent, 0);
        views.setPendingIntentTemplate(R.id.lvAppWidget, listClickPIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,
                R.id.lvAppWidget);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        mApp = App.getmApp();
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        if (intent.getAction().equalsIgnoreCase(ACTION_ON_CLICK)) {
            int itemPos = intent.getIntExtra(ITEM_POSITION, -1);
            if (itemPos != -1) {
                mApp.getmApp().addSelectedItem(itemPos, mApp.getmApp().getmNoteWithTitleList().get(itemPos));
                Intent noteActivity = new Intent(context, NoteActivity.class);
                mApp.NOTES_MODE = mApp.NOTES_MODE_EDIT;
                context.startActivity(noteActivity);
                return;
            }
        }
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

