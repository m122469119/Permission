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
import android.support.annotation.NonNull;

import com.mix.permission.PermissionHelper;
import com.mix.permission.source.Source;

import java.io.File;

/**
 * Des:       NRequest
 * Create by: m122469119
 * On:        2018/8/1 09:59
 * Email:     122469119@qq.com
 */
public class NRequest implements InstallRequest {
    private Action mAction;

    private Source mSource;

    private File mFile;

    public NRequest(Source source) {
        this.mSource = source;
    }

    @Override
    public final InstallRequest file(File file) {
        this.mFile = file;
        return this;
    }

    @NonNull
    @Override
    public InstallRequest reminder(IRemider listener) {
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
        install();
        if (mAction != null) {
            mAction.onSuccess();
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

}