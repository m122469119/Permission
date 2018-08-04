package com.mix.permissionhelper;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Des:
 * Create by: m122469119
 * On:        2018\08\04 09:58
 * Email:     122469119@qq.com
 */
public class AppInstallPermissionDialog extends BaseAlertDialog {

    protected AppInstallPermissionDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.app_dialog_install_permission, null);
        ImageView ivCancel = contentView.findViewById(R.id.iv_cancel);
        FrameLayout flOpen = contentView.findViewById(R.id.fl_open);

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnBtnLeftClickL != null) {
                    mOnBtnLeftClickL.onBtnClick();
                }
            }
        });
        flOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnBtnRightClickL != null) {
                    mOnBtnRightClickL.onBtnClick();
                }

            }
        });

        setContentView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

    }
}
