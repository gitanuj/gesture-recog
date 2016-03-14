package com.gesture.recog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.Closeable;
import java.io.IOException;

public class Utils {

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static void launchActivity(Context context, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(context, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }
}
