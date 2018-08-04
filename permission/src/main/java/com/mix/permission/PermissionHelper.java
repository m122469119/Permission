
package com.mix.permission;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import com.mix.permission.source.AppActivitySource;
import com.mix.permission.source.ContextSource;
import com.mix.permission.source.SupportFragmentSource;

import java.io.File;


/**
 * Des:       PermissionHelper
 * Create by: m122469119
 * On:        2018/5/4 16:30
 * Email:     122469119@qq.com
 */
public class PermissionHelper {

    @NonNull
    public static Proxy with(@NonNull Activity activity) {
        return new Proxy(new AppActivitySource(activity));
    }


    @NonNull
    public static Proxy with(@NonNull android.support.v4.app.Fragment fragment) {
        return new Proxy(new SupportFragmentSource(fragment));
    }


    @NonNull
    public static Proxy with(@NonNull Context context) {
        return new Proxy(new ContextSource(context));
    }

    private PermissionHelper() {

    }


    public static Uri getFileUri(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, context.getPackageName() + ".FileProvider", file);
        }
        return Uri.fromFile(file);
    }

}