package com.mix.permissionhelper;

import android.content.Context;

import com.mix.permission.Permission;
import com.mix.permission.PermissionHelper;
import com.mix.permission.request.IRemider;
import com.mix.permission.request.RequestExecutor;

import java.util.List;

/**
 * Des:
 * Create by: m122469119
 * On:        2018\08\03 10:51
 * Email:     122469119@qq.com
 */
public class AppRemider implements IRemider {


    @Override
    public void showGuide(Context context, List<String> permissions, final RequestExecutor executor) {
        AppGuidePermissionDialog appGuidePermissionDialog = new AppGuidePermissionDialog(context);
        List<String> permissionNames = Permission.transformText(context, permissions);
        appGuidePermissionDialog.setData(permissionNames);
        appGuidePermissionDialog.setCancelClickListener(new AppGuidePermissionDialog.OnBtnClickL() {
            @Override
            public void onBtnClick() {
                executor.cancel();
            }
        });
        appGuidePermissionDialog.setConfirmClickListener(new AppGuidePermissionDialog.OnBtnClickL() {
            @Override
            public void onBtnClick() {
                executor.execute();
            }
        });
        appGuidePermissionDialog.show();
    }

    @Override
    public void showRationale(Context context, List<String> permissions, final RequestExecutor executor) {
        AppRationalePermissionDialog appRationalePermissionDialog = new AppRationalePermissionDialog(context);
        List<String> permissionNames = Permission.transformText(context, permissions);
        appRationalePermissionDialog.setData(permissionNames);
        appRationalePermissionDialog.setCancelClickListener(new BaseAlertDialog.OnBtnClickL() {
            @Override
            public void onBtnClick() {
                executor.cancel();
            }
        });
        appRationalePermissionDialog.setConfirmClickListener(new BaseAlertDialog.OnBtnClickL() {
            @Override
            public void onBtnClick() {
                executor.execute();
            }
        });
        appRationalePermissionDialog.show();
    }


    @Override
    public void showDenied(final Context context, List<String> permissions, final RequestExecutor executor) {


        AppDeniedPermissionDialog appDeniedPermissionDialog = new AppDeniedPermissionDialog(context);
        List<String> permissionNames = Permission.transformText(context, permissions);
        appDeniedPermissionDialog.setData(permissionNames);
        appDeniedPermissionDialog.setCancelClickListener(new BaseAlertDialog.OnBtnClickL() {
            @Override
            public void onBtnClick() {
                executor.cancel();
            }
        });
        appDeniedPermissionDialog.setConfirmClickListener(new BaseAlertDialog.OnBtnClickL() {
            @Override
            public void onBtnClick() {
                PermissionHelper.with(context).permissionSetting().execute();
            }
        });
        appDeniedPermissionDialog.show();
    }


    @Override
    public void showInstall(Context context, final RequestExecutor executor) {
        AppInstallPermissionDialog appInstallPermissionDialog = new AppInstallPermissionDialog(context);
        appInstallPermissionDialog.setCancelClickListener(new BaseAlertDialog.OnBtnClickL() {
            @Override
            public void onBtnClick() {
                executor.cancel();
            }
        });
        appInstallPermissionDialog.setConfirmClickListener(new BaseAlertDialog.OnBtnClickL() {
            @Override
            public void onBtnClick() {
                executor.execute();
            }
        });
        appInstallPermissionDialog.show();
    }
}
