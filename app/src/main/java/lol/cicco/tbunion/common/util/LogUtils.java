package lol.cicco.tbunion.common.util;

import android.util.Log;

import lol.cicco.tbunion.BuildConfig;

public class LogUtils {
    private static final boolean DEBUG = BuildConfig.DEBUG;

    public static void info(Class<?> tag, String message) {
        if (DEBUG) {
            Log.i(tag.getSimpleName(), message);
        }
    }

    public static void error(Class<?> tag, String message) {
        if (DEBUG) {
            Log.e(tag.getSimpleName(), message);
        }
    }

    public static void debug(Class<?> tag, String message) {
        if (DEBUG) {
            Log.d(tag.getSimpleName(), message);
        }
    }

    public static void warn(Class<?> tag, String message) {
        if (DEBUG) {
            Log.w(tag.getSimpleName(), message);
        }
    }
}
