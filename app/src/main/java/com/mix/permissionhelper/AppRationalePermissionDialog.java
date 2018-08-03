package com.mix.permissionhelper;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Des:
 * Create by: m122469119
 * On:        2018\08\03 11:30
 * Email:     122469119@qq.com
 */
public class AppRationalePermissionDialog extends BaseAlertDialog {

    protected AppRationalePermissionDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.app_dialog_rationable_permission, null);
        ImageView ivCancel = contentView.findViewById(R.id.iv_cancel);
        FrameLayout flOpen = contentView.findViewById(R.id.fl_open);


        RecyclerView rvGuidePermission = contentView.findViewById(R.id.rv_guide_permission);
        rvGuidePermission.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGuidePermission.setAdapter(new AppRationalePermissionDialog.RecyclerviewAdapter(getContext(), mPermissions));


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


    List<String> mPermissions;

    public void setData(List<String> permissions) {
        mPermissions = permissions;
    }


    public class RecyclerviewAdapter extends RecyclerView.Adapter<AppRationalePermissionDialog.RecyclerviewAdapter.ViewHolder> {

        private Context context;
        private List<String> data;

        public RecyclerviewAdapter(Context context, List<String> data) {
            this.context = context;
            this.data = data;

        }

        @Override
        public AppRationalePermissionDialog.RecyclerviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.app_dialog_guide_permission_item, parent, false);
            return new AppRationalePermissionDialog.RecyclerviewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AppRationalePermissionDialog.RecyclerviewAdapter.ViewHolder holder, final int position) {
            holder.name.setText(data.get(position));


        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView name;

            public ViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.tv_permission_name);

            }
        }
    }

}
