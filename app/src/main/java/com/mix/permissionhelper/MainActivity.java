package com.mix.permissionhelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mix.permission.Permission;
import com.mix.permission.PermissionHelper;
import com.mix.permission.request.Action;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnRequestPermisssion = findViewById(R.id.btn_request_permisssion);
        btnRequestPermisssion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionHelper
                        .with(MainActivity.this)
                        .permission(Permission.GROUP.LOCATION, Permission.GROUP.RECORD_AUDIO, Permission.GROUP.PHONE)
                        .reminder(new AppRemider())
                        .onCallback(new Action() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onCancel() {

                            }
                        })
                        .start();

            }
        });
//
        //        Button btnRequestInstall = findViewById(R.id.btn_request_install);
        //        btnRequestInstall.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                PermissionHelper
        //                        .with(MainActivity.this)
        //                        .install()
        //
        //                        .onCallback(new Action() {
        //                            @Override
        //                            public void onSuccess() {
        //
        //                            }
        //
        //                            @Override
        //                            public void onCancel() {
        //
        //                            }
        //                        })
        //                        .start();
        //            }
        //        });

    }
}
