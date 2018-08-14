# [UI与交互] 实现和沪江英语/新浪微博一样的运行时权限提示交互
标签（空格分隔）：  Android:UI细节与交互
---
# 摘要
实现和沪江英语/新浪微博一样的运行时权限提示交互
涉及知识点6.0运行时权限,7.0URI,8.0权限组和应用安装授权

# 效果
- 新浪微博首次进入
弹出,新浪微博需要获取手机/电话权限,存储权限提示框

![此处输入图片的描述][1]

- 新浪微博再次进入(拒绝了权限授权,但没有勾选下次不在询问)
弹出,新浪微博需要获取下列(第一次未被允许的权限)才可正常使用

![此处输入图片的描述][2]
- 新浪微博再次进入(拒绝了权限授权,且勾选了下次不在询问)
弹出,让用户前往App设置页手动开启权限提示框

![此处输入图片的描述][3]

# 两个和权限相关的方法


- context.checkPermission(permission, android.os.Process.myPid(), android.os.Process.myUid())
用于获取未授权过的权限
    
    
- shouldShowRequestPermissionRationale
用于判断是否已拒绝或永久拒绝

|场景|状态|
|---|---|
|第一次打开App时|false|
|上次弹出权限授权,点击了拒绝,但没有勾选下次不在询问|true|
|上次弹出权限授权,点击了拒绝,且勾选了下次不在询问  |false|


# 封装6.0权限授权
要实现该效果
新浪微博的场景2和场景3判断肯定要在场景1的判断之前,如下:

申请权限流程

```Java
if (如果未授权权限>0){
    if(如果用户永久拒绝){
      处理场景3;
      return;
    }
    if(如果用户拒绝但不勾选永久拒绝){
      处理场景2;
      return;
    }
    处理场景1;
}else{
    无须授权;
}
```

但是shouldShowRequestPermissionRationale在App第一次启动默认为false(即默认权限已永久拒绝过一次),</br>
所以第一次会走处理场景3的方法,达不到先走处理场景1,拒绝再走处理场景2,永久拒绝走处理场景3的效果.</br>
所以我对权限组申请的权限字符串排序后加密得到唯一key,值默认为false
并通过sharePreference存储,用于做是否申请授权过一次的判断,
(注:App启动申请应用必须的多个权限,App应用内对单个权限进行授权,所以不存在键冲突的问题)

于是授权的逻辑变成如下形式:
```java
if (如果未授权权限>0){
    if(如果用户永久拒绝 && 是否走过一次授权){
      处理场景3;
      return;
    }
    if(如果用户拒绝但不勾选永久拒绝){
      处理场景2;
      return;
    }
    处理场景1;
} else{
    无须授权;
}
```

然后在授权回调更新该权限组的申请状态为已走过一遍授权,即为true;
```Java
public void onRequestPermissionsResult(@NonNull String[] permissions) {
    updateExecuteState();
}
```
授权代码封装后如下,其中AppRemider作为回调提示框自定义接口
```java
PermissionHelper
    .with(MainActivity.this)
    .permission(Permission.GROUP.LOCATION, Permission.GROUP.STORAGE)
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

/**
 * Remider接口
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
     *
     * @param context
     * @param executor
     */
    void showInstall(Context context, RequestExecutor executor);
}
```


# 封装7.0URI
通过工厂创建ORequest操作类
```java
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    return FileProvider.getUriForFile(context,context.getPackageName()+".FileProvider", file);
}
return Uri.fromFile(file);
```
# 封装8.0权限组
授权统一调用自定义的Permission类下的Permission.GROUP,按权限组进行申请
```java
public static final class GROUP {
    public static final String[] CALENDAR = new String[]{
            Permission.READ_CALENDAR,
            Permission.WRITE_CALENDAR};
    
    public static final String[] CAMERA = new String[]{Permission.CAMERA};
    
    public static final String[] CONTACTS = new String[]{
            Permission.READ_CONTACTS,
            Permission.WRITE_CONTACTS,
    };
    .....
}
```

# 封装8.0授权未知应用安装权限
对8.0及以上系统额外弹出申请安装权限对话框引导,并启动授权页面
```java
Intent manageIntent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
manageIntent.setData(Uri.fromParts("package", getPackageName(), null));
startActivityForResult(manageIntent, VALUE_INPUT_INSTALL);
```

# 安装权限申请最终代码如下
```java
PermissionHelper
    .with(MainActivity.this)
    .install()
    .file(new File(Environment.getExternalStorageDirectory(), "android.apk"))
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

```




  [1]: https://raw.githubusercontent.com/m122469119/Screenshots/master/PermissionHelper/1.jpg
  [2]: https://raw.githubusercontent.com/m122469119/Screenshots/master/PermissionHelper/2.jpg
  [3]: https://raw.githubusercontent.com/m122469119/Screenshots/master/PermissionHelper/3.jpg
