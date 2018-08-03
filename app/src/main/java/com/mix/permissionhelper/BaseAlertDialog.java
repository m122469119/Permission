package com.mix.permissionhelper;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Des:
 * Create by: m122469119
 * On:        2018\08\03 14:04
 * Email:     122469119@qq.com
 */
public class BaseAlertDialog extends Dialog {
    protected BaseAlertDialog(@NonNull Context context) {
        super(context);
    }

    public interface OnBtnClickL {
        void onBtnClick();
    }

    protected OnBtnClickL mOnBtnLeftClickL;

    protected OnBtnClickL mOnBtnRightClickL;


    public void setCancelClickListener(OnBtnClickL onBtnLeftClickL) {
        mOnBtnLeftClickL = onBtnLeftClickL;
    }

    public void setConfirmClickListener(OnBtnClickL onBtnRightClickL) {
        mOnBtnRightClickL = onBtnRightClickL;
    }

}
