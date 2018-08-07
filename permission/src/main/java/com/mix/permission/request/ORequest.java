/*
 * Copyright 2018 Yan Zhenjie
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
package com.mix.permission.request;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;

import com.mix.permission.PermissionActivity;
import com.mix.permission.PermissionHelper;
import com.mix.permission.source.Source;

import java.io.File;

/**
 * Des:       ORequest
 * Create by: m122469119
 * On:        2018/8/1 09:59
 * Email:     122469119@qq.com
 */
public class ORequest implements InstallRequest, RequestExecutor, PermissionActivity.RequestListener {

    private Action mAction;

    private Source mSource;

    private File mFile;

    private IRemider mRemider;


    public ORequest(Source source) {
        this.mSource = source;
    }

    @Override
    public final InstallRequest file(File file) {
        this.mFile = file;
        return this;
    }

    @NonNull
    @Override
    public ORequest reminder(IRemider remider) {
        mRemider = remider;
        return this;
    }


    @NonNull
    @Override
    public InstallRequest onCallback(Action action) {
        mAction = action;
        return this;
    }

    @Override
    public void start() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (mSource.canRequestPackageInstalls()) {
                install();
                mAction.onSuccess();
            } else {
                mRemider.showInstall(mSource.getContext(), this);
            }
        } else {
            install();
            if (mAction != null) {
                mAction.onSuccess();
            }
        }

    }

    public void install() {
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = PermissionHelper.getFileUri(mSource.getContext(), mFile);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        mSource.startActivity(intent);
    }

    @Override
    public void onRequestResult() {
        if (mSource.canRequestPackageInstalls()) {
            install();
            mAction.onSuccess();
        } else {
            mAction.onCancel();
        }
    }

    @Override
    public void execute() {
        PermissionActivity.requestInstall(mSource.getContext(), this);
    }

    @Override
    public void cancel() {
        if (mAction != null) {
            mAction.onCancel();
        }
    }

}