package com.example.pokerscorekeeper.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.pokerscorekeeper.R;
import com.example.pokerscorekeeper.activities.StandingDetailActivity;
import com.example.pokerscorekeeper.services.StandingWidgetRemoteViewsService;

/**
 * Implementation of App Widget functionality.
 */
public class StandingWidget extends AppWidgetProvider {

    static RemoteViews updateWidget(Context context) {

        Intent intent = new Intent(context, StandingWidgetRemoteViewsService.class);

        // Construct the RemoteViews object
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.standing_widget);
        remoteViews.setRemoteAdapter(R.id.listView, intent);


        Intent clickStandingIntent = new Intent(context, StandingDetailActivity.class);
        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(clickStandingIntent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        remoteViews.setPendingIntentTemplate(R.id.listView, pendingIntent);


        return remoteViews;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = updateWidget(context);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onEnabled(Context context) {
        update(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        update(context);
        super.onReceive(context, intent);
    }

    private void update(Context context)
    {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        ComponentName appWidgetId = new ComponentName(context, StandingWidget.class);

        RemoteViews views = updateWidget(context);
        manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(appWidgetId), R.id.listView);
        manager.updateAppWidget(appWidgetId, views);
    }

    //Used to update the widget
    public static void sendUpdateRequest(Context context)
    {
        //Call to update the widget
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, StandingWidget.class));
        context.sendBroadcast(intent);
    }
}

