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

import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.mix.permission.MD5Helper;
import com.mix.permission.PermissionActivity;
import com.mix.permission.checker.PermissionChecker;
import com.mix.permission.checker.StandardChecker;
import com.mix.permission.source.Source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Des:       MRequest
 * Create by: m122469119
 * On:        2018/2/21 15:08
 * Email:     122469119@qq.com
 */

public class MRequest implements PermissionRequest, RequestExecutor, PermissionActivity.RequestListener {


    private static final PermissionChecker CHECKER = new StandardChecker();

    private Action mAction;

    private Source mSource;

    private IRemider mRemider;

    private String[] mPermissions = new String[]{};

    private String mPermissionKey;

    private List<String> mUnAuthorizedPermissionList;


    public String generatePermissionKey(String[] permissions) {
        Arrays.sort(permissions);
        StringBuilder builder = new StringBuilder();
        for (String s : permissions) {
            builder.append(s);
        }
        String md5 = MD5Helper.md5(builder.toString());
        Log.e(MRequest.class.getName(), "拼接结果" + builder.toString());
        Log.e(MRequest.class.getName(), "加密结果" + md5);
        return MD5Helper.md5(builder.toString());

    }

    public boolean getExecuteState() {
        SharedPreferences sharedPreferences = mSource.getContext().getSharedPreferences("permissionhelper", MODE_PRIVATE);
        return sharedPreferences.getBoolean(mPermissionKey, false);

    }

    public void updateExecuteState() {
        SharedPreferences sharedPreferences = mSource.getContext().getSharedPreferences("permissionhelper", MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(mPermissionKey, true).commit();
    }


    public MRequest(Source source) {
        this.mSource = source;
    }

    @NonNull
    @Override
    public PermissionRequest permission(String... permissions) {
        this.mPermissions = permissions;
        return this;
    }

    @NonNull
    @Override
    public PermissionRequest permission(String[]... permissions) {
        mPermissions = concatAll(mPermissions, permissions);

        return this;
    }


    @NonNull
    @Override
    public PermissionRequest reminder(IRemider remider) {
        this.mRemider = remider;
        return this;
    }


    @NonNull
    @Override
    public PermissionRequest onCallback(Action action) {
        this.mAction = action;
        return this;
    }

    @Override
    public void start() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            mPermissionKey = generatePermissionKey(mPermissions);

            mUnAuthorizedPermissionList = getUnAuthorizedPermissions(CHECKER, mSource, mPermissions);

            if (mUnAuthorizedPermissionList.size() > 0) {

                List<String> deniedPermissionsList = getDeniedPermissions(mSource, mUnAuthorizedPermissionList);
                if (deniedPermissionsList.size() > 0 && getExecuteState()) {
                    Log.e(MRequest.class.getName(), "永久拒绝");
                    mRemider.showDenied(mSource.getContext(), mUnAuthorizedPermissionList, this);
                    return;
                }


                List<String> rationalePermissionsList = getRationalePermissions(mSource, mUnAuthorizedPermissionList);
                if (rationalePermissionsList.size() > 0) {
                    Log.e(MRequest.class.getName(), "拒绝");
                    mRemider.showRationale(mSource.getContext(), rationalePermissionsList, this);
                    return;
                }
                Log.e(MRequest.class.getName(), "第一次授权");
                mRemider.showGuide(mSource.getContext(), mUnAuthorizedPermissionList, this);
                return;
            }
        }
        callbackSucceed();

    }


    @Override
    public void onRequestResult() {
        updateExecuteState();
        List<String> unAuthorizedPermissionList = getUnAuthorizedPermissions(CHECKER, mSource, mPermissions);
        if (unAuthorizedPermissionList.isEmpty()) {
            callbackSucceed();
        } else {
            callbackFailed(unAuthorizedPermissionList);
        }
    }


    /**
     * Callback rejected mState.
     */
    private void callbackFailed(@NonNull List<String> unAuthorizedPermissionList) {
        List<String> deniedPermissionsList = getDeniedPermissions(mSource, unAuthorizedPermissionList);
        if (deniedPermissionsList.size() > 0) {
            mRemider.showDenied(mSource.getContext(), unAuthorizedPermissionList, this);
        } else {
            mRemider.showRationale(mSource.getContext(), unAuthorizedPermissionList, this);
        }

    }

    /**
     * 获取未授权的权限
     *
     * @param checker
     * @param source
     * @param permissions
     * @return
     */
    private static List<String> getUnAuthorizedPermissions(PermissionChecker checker, @NonNull Source source, @NonNull String... permissions) {
        List<String> unAuthorizedPermissionsList = new ArrayList();
        for (String permission : permissions) {
            if (!checker.hasPermission(source.getContext(), permission)) {
                unAuthorizedPermissionsList.add(permission);
            }
        }
        Collections.sort(unAuthorizedPermissionsList);
        return unAuthorizedPermissionsList;
    }

    /**
     * 获取拒绝但没有永久拒绝的权限
     *
     * @param source
     * @param unAuthorizedPermissionList
     * @return
     */
    private static List<String> getRationalePermissions(@NonNull Source source, List<String> unAuthorizedPermissionList) {
        List<String> rationalePermissionsList = new ArrayList();
        for (String permission : unAuthorizedPermissionList) {
            if (source.isShowRationalePermission(permission)) {
                rationalePermissionsList.add(permission);
            }
        }
        return rationalePermissionsList;
    }


    /**
     * 获取永久拒绝的权限
     *
     * @param source
     * @param unAuthorizedPermissionList
     * @return
     */
    private static List<String> getDeniedPermissions(@NonNull Source source, List<String> unAuthorizedPermissionList) {
        List<String> deniedPermissionsList = new ArrayList<>(1);
        for (String permission : unAuthorizedPermissionList) {
            if (!source.isShowRationalePermission(permission)) {
                deniedPermissionsList.add(permission);
            }
        }
        return deniedPermissionsList;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void execute() {
        PermissionActivity.requestPermission(mSource.getContext(), mUnAuthorizedPermissionList.toArray(new String[mUnAuthorizedPermissionList.size()]), this);
    }

    @Override
    public void cancel() {
        mAction.onCancel();
    }

    private void callbackSucceed() {
        if (mAction != null) {
            mAction.onSuccess();
        }
    }


    public static <T> T[] concatAll(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
}