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
package com.mix.permission;

import android.os.Build;

import com.mix.permission.request.InstallRequest;
import com.mix.permission.request.MRequest;
import com.mix.permission.request.NRequest;
import com.mix.permission.request.ORequest;
import com.mix.permission.request.PermissionRequest;
import com.mix.permission.setting.PermissionSetting;
import com.mix.permission.source.Source;


/**
 * Created by YanZhenjie on 2018/4/28.
 */
public class Proxy {

    private static final InstallRequestFactory INSTALL_REQUEST_FACTORY;
    private static final PermissionRequestFactory PERMISSION_REQUEST_FACTORY;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            INSTALL_REQUEST_FACTORY = new ORequestFactory();
        } else {
            INSTALL_REQUEST_FACTORY = new NRequestFactory();
        }

        PERMISSION_REQUEST_FACTORY = new MRequestFactory();


    }

    private Source mSource;

    Proxy(Source source) {
        this.mSource = source;
    }


    private interface PermissionRequestFactory {

        PermissionRequest create(Source source);

    }


    public interface InstallRequestFactory {

        InstallRequest create(Source source);

    }


    private static class MRequestFactory implements PermissionRequestFactory {
        @Override
        public PermissionRequest create(Source source) {
            return new MRequest(source);
        }
    }

    public static class NRequestFactory implements InstallRequestFactory {
        @Override
        public InstallRequest create(Source source) {
            return new NRequest(source);
        }
    }

    public static class ORequestFactory implements InstallRequestFactory {
        @Override
        public InstallRequest create(Source source) {
            return new ORequest(source);
        }
    }


    public PermissionSetting permissionSetting() {
        return new PermissionSetting(mSource);
    }


    public InstallRequest install() {
        return INSTALL_REQUEST_FACTORY.create(mSource);
    }


    public PermissionRequest permission(String... permissions) {
        return PERMISSION_REQUEST_FACTORY.create(mSource).permission(permissions);
    }


    public PermissionRequest permission(String[]... groups) {
        return PERMISSION_REQUEST_FACTORY.create(mSource).permission(groups);
    }

}