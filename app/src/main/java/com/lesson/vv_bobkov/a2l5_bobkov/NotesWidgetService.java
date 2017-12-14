package com.lesson.vv_bobkov.a2l5_bobkov;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by samsung on 14.12.2017.
 */

public class NotesWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetNotesFactory(this.getApplicationContext(), intent);
    }
}
