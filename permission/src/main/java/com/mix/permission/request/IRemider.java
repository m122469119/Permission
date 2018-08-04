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

import android.content.Context;

import java.util.List;

/**
 * Des:       IRemider
 * Create by: m122469119
 * On:        2018/2/21 15:08
 * Email:     122469119@qq.com
 */
public interface IRemider {


    /**
     * 引导
     *
     * @param context
     * @param permissions
     * @param executor
     */
    void showGuide(Context context, List<String> permissions, RequestExecutor executor);

    /**
     * 拒绝但不永久拒绝
     *
     * @param context
     * @param permissions
     * @param executor
     */
    void showRationale(Context context, List<String> permissions, RequestExecutor executor);


    /**
     * 永久拒绝
     *
     * @param context
     * @param permissions
     * @param executor
     */
    void showDenied(Context context, List<String> permissions, RequestExecutor executor);


    /**
     * App应用安装授权请求
     * @param context
     * @param executor
     */
    void showInstall(Context context, RequestExecutor executor);
}