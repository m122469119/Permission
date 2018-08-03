/*
 * Copyright © Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mix.permission.source;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import java.lang.reflect.Method;

/**
 * Des:       Source
 * Create by: m122469119
 * On:        2018/2/21 17:18
 * Email:     122469119@qq.com
 */
public abstract class Source {

    public abstract Context getContext();

    public abstract void startActivity(Intent intent);

    public abstract void startActivityForResult(Intent intent, int requestCode);

    private static final String OPSTR_SYSTEM_ALERT_WINDOW = AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW;
    private static final int OP_REQUEST_INSTALL_PACKAGES = 66;


    private int mTargetSdkVersion;


    private PackageManager mPackageManager;
    private AppOpsManager mAppOpsManager;

    /**
     * Show permissions reminder?
     */
    public final boolean isShowRationalePermission(String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        PackageManager packageManager = getContext().getPackageManager();
        Class<?> pkManagerClass = packageManager.getClass();
        try {
            Method method = pkManagerClass.getMethod("shouldShowRequestPermissionRationale", String.class);
            if (!method.isAccessible())
                method.setAccessible(true);
            return (boolean) method.invoke(packageManager, permission);
        } catch (Exception ignored) {
            return false;
        }
    }


    private int getTargetSdkVersion() {
        if (mTargetSdkVersion < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mTargetSdkVersion = getContext().getApplicationInfo().targetSdkVersion;
        }
        return mTargetSdkVersion;
    }


    public final boolean canRequestPackageInstalls() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (getTargetSdkVersion() < Build.VERSION_CODES.O) {
                Class<AppOpsManager> clazz = AppOpsManager.class;
                try {
                    Method method = clazz.getDeclaredMethod("checkOpNoThrow", int.class, int.class, String.class);
                    int result = (int) method.invoke(getAppOpsManager(), OP_REQUEST_INSTALL_PACKAGES, android.os.Process.myUid(), getContext().getPackageName());
                    return result == AppOpsManager.MODE_ALLOWED;
                } catch (Exception ignored) {
                    // Android P does not allow reflections.
                    return true;
                }
            }
            return getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }


    private AppOpsManager getAppOpsManager() {
        if (mAppOpsManager == null) {
            mAppOpsManager = (AppOpsManager) getContext().getSystemService(Context.APP_OPS_SERVICE);
        }
        return mAppOpsManager;
    }


    private PackageManager getPackageManager() {
        if (mPackageManager == null) {
            mPackageManager = getContext().getPackageManager();
        }
        return mPackageManager;
    }


}
