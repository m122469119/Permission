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
package com.mix.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

/**
 * Des:       PermissionActivity
 * Create by: m122469119
 * On:        2018/2/21 15:14
 * Email:     122469119@qq.com
 */
public final class PermissionActivity extends Activity {

    private static final String KEY_INPUT_PERMISSIONS = "KEY_INPUT_PERMISSIONS";

    private static final String KEY_INPUT_OPERATION = "KEY_INPUT_OPERATION";

    private static final int VALUE_INPUT_PERMISSION = 1;

    private static final int VALUE_INPUT_INSTALL = 2;

    private static RequestListener sRequestListener;


    public static void requestPermission(Context context, String[] permissions, RequestListener permissionListener) {
        sRequestListener = permissionListener;
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(KEY_INPUT_OPERATION, VALUE_INPUT_PERMISSION);
        intent.putExtra(KEY_INPUT_PERMISSIONS, permissions);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public static void requestInstall(Context context, RequestListener permissionListener) {
        sRequestListener = permissionListener;
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(KEY_INPUT_OPERATION, VALUE_INPUT_INSTALL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   invasionStatusBar(this);
        Intent intent = getIntent();

        int operation = intent.getIntExtra(KEY_INPUT_OPERATION, 0);

        switch (operation) {
            case VALUE_INPUT_PERMISSION:
                String[] permissions = intent.getStringArrayExtra(KEY_INPUT_PERMISSIONS);
                if (permissions != null && sRequestListener != null) {
                    requestPermissions(permissions, VALUE_INPUT_PERMISSION);
                } else {
                    finish();
                }
                break;
            case VALUE_INPUT_INSTALL:
                if (sRequestListener != null) {
                    Intent manageIntent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    manageIntent.setData(Uri.fromParts("package", getPackageName(), null));
                    startActivityForResult(manageIntent, VALUE_INPUT_INSTALL);
                } else {
                    finish();
                }
                break;
            default: {
                throw new AssertionError("This should not be the case.");
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (sRequestListener != null) {
            sRequestListener.onRequestResult();
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (sRequestListener != null) {
            sRequestListener.onRequestResult();
        }
        finish();
    }


    @Override
    public void finish() {
        sRequestListener = null;
        super.finish();
    }


    /**
     * permission callback.
     */
    public interface RequestListener {
        void onRequestResult();
    }


    @Override
    protected void onDestroy() {
        sRequestListener = null;
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
