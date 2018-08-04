package com.mix.permissionhelper;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mix.permission.Permission;
import com.mix.permission.PermissionHelper;
import com.mix.permission.request.Action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
                        .permission(Permission.GROUP.LOCATION, Permission.GROUP.STORAGE, Permission.GROUP.PHONE)
                        .reminder(new AppRemider())
                        .onCallback(new Action() {
                            @Override
                            public void onSuccess() {
                                AppExecutor.getInstance().runWorker(new Runnable() {
                                    @Override
                                    public void run() {
                                        File apkFile = new File(Environment.getExternalStorageDirectory(), "android.apk");

                                        if (!apkFile.exists()) {
                                            AssetManager mAssetManager = getAssets();
                                            InputStream inputStream = null;
                                            BufferedOutputStream outputStream = null;
                                            try {
                                                inputStream = mAssetManager.open("android.apk");
                                                outputStream = new BufferedOutputStream(new FileOutputStream(apkFile));
                                                byte[] buffer = new byte[1024];
                                                int count;
                                                while ((count = inputStream.read(buffer)) > 0) {
                                                    outputStream.write(buffer, 0, count);
                                                }
                                            } catch (IOException e) {
                                                e.printStackTrace();

                                            } finally {
                                                try {
                                                    inputStream.close();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                try {
                                                    outputStream.close();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        }
                                        AppExecutor.getInstance().runUI(new Runnable() {
                                            @Override
                                            public void run() {
                                                installPackage();
                                            }
                                        });
                                    }
                                });
                            }

                            @Override
                            public void onCancel() {
                                Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .start();

            }
        });

    }

    private void installPackage() {
        PermissionHelper
                .with(MainActivity.this)
                .install()
                .file(new File(Environment.getExternalStorageDirectory(), "android.apk"))
                .reminder(new AppRemider())
                .onCallback(new Action() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_SHORT).show();
                    }
                })
                .start();
    }
}
