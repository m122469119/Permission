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
package com.mix.permission.request;

import android.support.annotation.NonNull;

/**
 * Des:       Request
 * Create by: m122469119
 * On:        2018/2/21 17:17
 * Email:     122469119@qq.com
 */
public interface PermissionRequest {
    /**
     * One or more permissions.
     */
    @NonNull
    PermissionRequest permission(String... permissions);


    @NonNull
    PermissionRequest permission(String[]... permissions);


    /**
     * Set request reminder.
     */
    @NonNull
    PermissionRequest reminder(IRemider listener);


    /**
     * Action to be taken when all permissions are denied.
     */
    @NonNull
    PermissionRequest onCallback(Action action);

    /**
     * Request permission.
     */
    void start();


}