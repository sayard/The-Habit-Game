package pl.c0.sayard.thehabitgame.widgets;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Karol on 06.05.2017.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListProvider(this.getApplicationContext(), intent);
    }
}
