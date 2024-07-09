package de.appplant.cordova.plugin.notification.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import java.util.Random;

public final class LaunchUtils {

   private static int getIntentFlags() {
        // Switch between FLAG_MUTABLE and FLAG_IMMUTABLE based on Android version
        // FLAG_MUTABLE is used for Android versions below 12 for backwards compatibility

        // https://stackoverflow.com/questions/77275691/targeting-u-version-34-and-above-disallows-creating-or-retrieving-a-pendingin/77691101#77691101
        // 'if you have an implicit intent within a PendingIntent object with FLAG_MUTABLE, your app will crash.'
        // https://developer.android.com/about/versions/14/behavior-changes-14#safer-intents 
        // https://developer.android.com/guide/components/intents-filters#Types

        int FLAG_MUTABLE = 33554432; // Numeric value for FLAG_MUTABLE
        int FLAG_IMMUTABLE = 67108864; // Numeric value for FLAG_IMMUTABLE
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        
        if (android.os.Build.VERSION.SDK_INT >= 31) { // Android 12 and above
            flags |= FLAG_IMMUTABLE;
        } else { // Below Android 12
            flags |= FLAG_MUTABLE;
        }
        
        return flags;
    }

    public static PendingIntent getBroadcastPendingIntent(Context context,
                                                          Intent intent,
                                                          int notificationId) {
        return  PendingIntent.getBroadcast(context, notificationId, intent, getIntentFlags());
    }

    public static PendingIntent getActivityPendingIntent(Context context,
                                                         Intent intent,
                                                         int notificationId) {
        return  PendingIntent.getActivity(context, notificationId, intent, getIntentFlags());
    }

    /***
     * Launch main intent from package.
     */
    public static void launchApp(Context context) {
        String pkgName  = context.getPackageName();

        Intent intent = context
            .getPackageManager()
            .getLaunchIntentForPackage(pkgName);

        if (intent == null)
            return;

        intent.addFlags(
            FLAG_ACTIVITY_REORDER_TO_FRONT
                | FLAG_ACTIVITY_SINGLE_TOP
                | FLAG_ACTIVITY_NEW_TASK
        );

        context.startActivity(intent);
    }
}
