package com.example.school_bus.Fragment.Main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.school_bus.Activity.StudentActivity;
import com.example.school_bus.Entity.UserData;
import com.example.school_bus.Fragment.BaseFragment;
import com.example.school_bus.Mvp.MapSideMvp;
import com.example.school_bus.MyApp;
import com.example.school_bus.Presenter.MapSidePresenter;
import com.example.school_bus.R;
import com.example.school_bus.Utils.FileUtil;
import com.example.school_bus.Utils.GlideUtils;
import com.example.school_bus.Utils.HttpUtil;
import com.example.school_bus.Utils.ImageUtil;
import com.example.school_bus.Utils.MyLog;
import com.example.school_bus.View.MyPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.content.Context.MODE_PRIVATE;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-01-13 14:19
 * @类名 MapSideFragment
 * @所在包 com\example\school_bus\Fragment\MapSideFragment.java
 * 主页面侧边栏
 */
public class MapSideFragment extends BaseFragment implements MapSideMvp.view {

    private static String TAG = "MapSideFragment";
    private final static int PHOTO_REQUEST_CAMERA = 10;//相机权限请求
    private final static int PHOTO_REQUEST_ALBUM = 20;//相册权限请求
    private final static int CAMERA_REQUEST_CODE = 100;//相机跳转code
    private final static int ALBUM_REQUEST_CODE = 200;//相册跳转code
    private final static int TAILOR_REQUEST_CODE = 300;//图片剪裁code
    private final static String SAVE_AVATAR_NAME = "head.png";//需要上传的图片的文件名
    @BindView(R.id.ll_login_setting)
    LinearLayout llLoginSetting;
    @BindView(R.id.ll_login_out)
    LinearLayout llLoginOut;
    @BindView(R.id.ll_close)
    LinearLayout llClose;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    MyPopupWindow myPopupWindow;//自定义弹窗
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.ll_offline_map)
    LinearLayout llOfflineMap;
    @BindView(R.id.iv_main)
    ImageView ivMain;
    @BindView(R.id.tv_main)
    TextView tvMain;
    @BindView(R.id.iv_offline_map)
    ImageView ivOfflineMap;
    @BindView(R.id.tv_offline_map)
    TextView tvOfflineMap;
    private List<LinearLayout> linearLayouts = new ArrayList<>();
    private List<TextView> textViews = new ArrayList<>();
    private List<ImageView> imageViews = new ArrayList<>();
    private Uri imageUri;//需要上传的图片的Uri
    private File file;//需要上传的图片的文件
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    private MapSidePresenter mapSidePresenter = new MapSidePresenter(this);

    public static MapSideFragment newInstance() {
        return new MapSideFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_side, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        //加载头像
        if (getContext() != null) {
            Glide.with(getContext())
                    .load(ImageUtil.getHeadUrl(MyApp.getHead()))
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .error(R.drawable.ic_header)
                    .into(ivHeader);
        }
        //加载用户名
        tvName.setText(MyApp.getUserName());

        //加载列表选项样式
        linearLayouts.add(llMain);
        linearLayouts.add(llOfflineMap);

        textViews.add(tvMain);
        textViews.add(tvOfflineMap);

        imageViews.add(ivMain);
        imageViews.add(ivOfflineMap);
        initList();
    }

    /**
     * 加载列表选项
     */
    public void initList() {
        if (getActivity() != null && getContext() != null){
            for (int i = 0; i < ((StudentActivity) getActivity()).fragmentList.size(); i++){
                if (((StudentActivity) getActivity()).getCurrentPager() == i){
                    //当前页
                    linearLayouts.get(i).setBackgroundColor(getContext().getResources().getColor(R.color.gray));
                    textViews.get(i).setTextColor(getContext().getResources().getColor(R.color.theme));
                    imageViews.get(i).setImageResource(R.drawable.ic_home_blue);
                } else {
                    //其他页
                    linearLayouts.get(i).setBackgroundColor(getContext().getResources().getColor(R.color.white));
                    textViews.get(i).setTextColor(getContext().getResources().getColor(R.color.text_black));
                    imageViews.get(i).setImageResource(R.drawable.ic_download);
                }
            }
        }
    }

    /**
     * 显示选择弹窗
     * 如果申请权限则跳转到权限回调{@link MapSideFragment#onRequestPermissionsResult(int, String[], int[])}
     */
    private void showPhotoPopupWindow() {
        if (myPopupWindow == null) {
            myPopupWindow = new MyPopupWindow(getContext());
        }
        myPopupWindow.showPhoto();//显示照片弹窗
        myPopupWindow.setOnClickListener(type -> {
            if (getContext() != null) {
                switch (type) {
                    case MyPopupWindow.TAKE_PHOTO:
                        //拍照
                        MyLog.e(TAG, "点击拍照");
                        if ((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                == PackageManager.PERMISSION_GRANTED)
                                && (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED)
                                && (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED)) {
                            //权限都齐的情况下，跳转相机
                            openCamera();
                        } else {
                            if (getActivity() != null) {
                                //请求权限
                                ActivityCompat.requestPermissions(getActivity(), new String[]{
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                }, PHOTO_REQUEST_CAMERA);
                            }
                        }
                        break;
                    case MyPopupWindow.SELECT_PHOTO:
                        //相册
                        MyLog.e(TAG, "点击相册");
                        if ((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED)
                                && (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED)) {
                            //权限都齐的情况下，跳转相册
                            openAlbum();
                        } else {
                            if (getActivity() != null) {
                                //请求权限
                                ActivityCompat.requestPermissions(getActivity(), new String[]{
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                }, PHOTO_REQUEST_ALBUM);
                            }
                        }
                        break;
                }
            }
        });
    }


    /**
     * 转到相机
     * Android10以上返回Uri
     * Android10以下返回文件路径
     * 操作完成后回调{@link MapSideFragment#onActivityResult(int, int, Intent)}
     */
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断是否有相机
        if (getActivity() != null && getContext() != null && intent.resolveActivity(getActivity().getPackageManager()) != null){
            File file;
            Uri uri = null;
            if (isAndroidQ){
                //适配Android10
                uri = createImageUri(getContext());
            } else {
                //Android10以下
                file = createImageFile(getContext());
                if (file != null){
                    //Android10以下
                    //需要上传的图片的保存路径
                    String imagePath = file.getAbsolutePath();
                    MyLog.e(TAG, "相机保存的图片路径：" + imagePath);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        //适配Android7.0文件权限
                        uri = FileProvider.getUriForFile(getContext(), "com.example.camera.fileprovider", file);
                    } else {
                        uri = Uri.fromFile(file);
                    }
                }
            }
            imageUri = uri;
            MyLog.e(TAG, "相机保存的图片Uri：" + imageUri);
            if (uri != null){
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        }
    }

    /**
     * Android10创建图片uri，用来保存拍照后的图片
     * @return uri
     */
    private Uri createImageUri(@NonNull Context context){
        String status = Environment.getExternalStorageState();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, SAVE_AVATAR_NAME);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/0/");
        //判断是否有SD卡
        if (status.equals(Environment.MEDIA_MOUNTED)){
            return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        } else {
            return context.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, contentValues);
        }
    }

    /**
     * Android10以下创建图片file，用来保存拍照后的照片
     * @return file
     */
    private File createImageFile(@NonNull Context context){
        File file = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (file != null && !file.exists()){
            if (file.mkdir()){
                MyLog.e(TAG, "文件夹创建成功");
            } else {
                MyLog.e(TAG, "file为空或者文件夹创建失败");
            }
        }
        File tempFile = new File(file, SAVE_AVATAR_NAME);
        MyLog.e(TAG, "临时文件路径：" + tempFile.getAbsolutePath());
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))){
            return null;
        }
        return tempFile;
    }

    /**
     * 打开相册
     * 操作完成后回调{@link MapSideFragment#onActivityResult(int, int, Intent)}
     */
    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, ALBUM_REQUEST_CODE);
    }

    /**
     * 照片剪裁
     * 操作完成后回调{@link MapSideFragment#onActivityResult(int, int, Intent)}
     */
    private void openTailor(Uri uri) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && getContext() != null){
            file = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/0"), SAVE_AVATAR_NAME);
            MyLog.e(TAG, "裁剪图片存放路径：" + file.getAbsolutePath());
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        //适配Android10，存放图片路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        // 图片格式
        intent.putExtra("outputFormat", "PNG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, TAILOR_REQUEST_CODE);
    }

    /**
     * 更换头像
     */
    private void upHead(File file) {
        if (file.exists()) {
            MyLog.e(TAG, "更换头像，图片文件存在，图片名：" + file.getName());
            //以下将图片转化上传
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), requestBody);
            mapSidePresenter.upHead(body);
        } else {
            showToast("头像更新失败");
            MyLog.e(TAG, "文件不存在");
        }
    }

    /**
     * 更新头像
     */
    public void updateHead(){
        if (getContext() != null){
            //清除缓存和磁盘
            MyLog.e(TAG, "Glide当前的缓存：" + GlideUtils.getInstance().getCacheSize(getContext()));
            MyLog.e(TAG, "清除Glide的缓存");
            GlideUtils.getInstance().clearImageAllCache(getContext());
            Glide.with(getContext())
                    .load(ImageUtil.getHeadUrl(MyApp.getHead()))
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.ic_header)
                    .into(ivHeader);
        }
    }

    /**
     * 权限缺失提醒
     */
    private void showMissingPermissionDialog(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("当前应用缺少\"" + s + "\"权限。\n\n请点击 \"设置 \"- \"权限 \"打开所需权限。");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("设置", (dialogInterface, i) -> openAppSetting());
        builder.show();
    }

    /**
     * 打开设置
     */
    private void openAppSetting() {
        if (getContext() != null) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.ll_login_setting, R.id.ll_login_out, R.id.ll_close, R.id.iv_header, R.id.ll_main, R.id.ll_offline_map})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_main:
                //点击首页
                if (getActivity() != null){
                    ((StudentActivity) getActivity()).closeSideView();//关闭侧边栏
                    new Handler().postDelayed(() -> {
                        ((StudentActivity) getActivity()).selectPager(0);//选择首页分页
                    }, 200);
                }
                break;
            case R.id.ll_offline_map:
                //点击离线地图
                if (getActivity() != null){
                    ((StudentActivity) getActivity()).closeSideView();//关闭侧边栏
                    new Handler().postDelayed(() -> {
                        ((StudentActivity) getActivity()).selectPager(1);//选择离线地图分页
                    }, 200);
                }
                break;
            case R.id.ll_login_setting:
                //设置
                break;
            case R.id.ll_login_out:
                //登出
                MyApp.clearToken();
                startLogin();
                if (getActivity() != null){
                    getActivity().finish();
                }
                break;
            case R.id.ll_close:
                //关闭app
                if (getActivity() != null){
                    getActivity().finish();
                }
                break;
            case R.id.iv_header:
                //更换头像，选择图片
                MyLog.e(TAG, "点击了头像");
                showPhotoPopupWindow();
                break;
        }
    }

    @Override
    public void upHead(UserData userData) {
        if (userData.isSuccess()) {
            MyLog.e(TAG, "上传成功，图片地址：" + userData.getData().getHead());
            showToast("上传成功");
            if (getActivity() != null) {
                MyLog.e(TAG, "真实地址：" + ImageUtil.getHeadUrl(userData.getData().getHead()));
                if (getContext() != null) {
                    SharedPreferences.Editor editor = getContext().getSharedPreferences("user", MODE_PRIVATE).edit();
                    editor.putString("head", userData.getData().getHead());
                    editor.apply();
                    //更新头像
                    ((StudentActivity) getActivity()).updateHead(true);
                }
            }
        } else {
            MyLog.e(TAG, "上传失败，原因：" + userData.getMessage());
            showToast(userData.getMessage());
        }
    }

    @Override
    public void onError(Throwable e) {
        HttpUtil.onError(e);
    }

    /**
     * 权限申请回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PHOTO_REQUEST_CAMERA:
                //相机权限请求回调
                MyLog.e(TAG, "相机权限请求回调");
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED
                            && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                        //跳转相机
                        openCamera();
                    } else {
                        //无权限提示
                        showMissingPermissionDialog("相机\"或\"存储");
                    }
                }
                break;
            case PHOTO_REQUEST_ALBUM:
                MyLog.e(TAG, "相册权限请求回调");
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        //跳转相册
                        openAlbum();
                    } else {
                        //无权限提示
                        showMissingPermissionDialog("存储");
                    }
                }
                break;
        }
    }

    /**
     * 页面回调
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1){
            //回调成功
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:
                    //相机回调
                    MyLog.e(TAG, "相机回调");
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        //照片裁剪
                        openTailor(imageUri);
                    } else {
                        showToast("未找到存储卡");
                    }
                    break;
                case ALBUM_REQUEST_CODE:
                    //相册回调
                    MyLog.e(TAG, "相册回调");
                    if (data != null && data.getData() != null && getContext() != null) {
                        upHead(FileUtil.uriToFile(data.getData(), getContext()));
                    } else {
                        showToast("更新头像失败");
                    }
                    break;
                case TAILOR_REQUEST_CODE:
                    //图片剪裁回调
                    MyLog.e(TAG, "图片剪裁回调");
                    upHead(file);
                    break;
            }
        } else {
            //取消
            showToast("取消");
        }
    }
}
