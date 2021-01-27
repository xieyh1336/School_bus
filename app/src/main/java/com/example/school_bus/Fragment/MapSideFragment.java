package com.example.school_bus.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.school_bus.Entity.UserData;
import com.example.school_bus.MyApp;
import com.example.school_bus.NetWork.API_login;
import com.example.school_bus.R;
import com.example.school_bus.Utils.FileUtil;
import com.example.school_bus.Utils.ImageUtil;
import com.example.school_bus.Utils.MyLog;
import com.example.school_bus.View.MyPopupWindow;
import com.example.school_bus.View.SwitchView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-01-13 14:19
 * @类名 MapSideFragment
 * @所在包 com\example\school_bus\Fragment\MapSideFragment.java
 * 主页面侧边栏
 */
public class MapSideFragment extends BaseFragment {

    private static String TAG = "MapSideFragment";
    private final static int PHOTO_REQUEST_CAMERA = 10;//相机权限请求
    private final static int PHOTO_REQUEST_ALBUM = 20;//相册权限请求
    private final static int CAMERA_REQUEST_CODE = 100;//相机跳转code
    private final static int ALBUM_REQUEST_CODE = 200;//相册跳转code
    private final static int TAILOR_REQUEST_CODE = 300;//图片剪裁code
    private final static String IMAGE_FILE_NAME = "headImage.png";
    private final static String SAVE_AVATAR_NAME = "image.png";
    @BindView(R.id.switch1)
    SwitchView switch1;
    @BindView(R.id.ll_login_setting)
    LinearLayout llLoginSetting;
    @BindView(R.id.ll_login_out)
    LinearLayout llLoginOut;
    @BindView(R.id.ll_close)
    LinearLayout llClose;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    MyPopupWindow myPopupWindow;//自定义弹窗

    public static MapSideFragment getInstance() {
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

    private void init() {
        switch1.toggleSwitch(false);
        switch1.setColor(Color.parseColor("#FF1878"), Color.parseColor("#FFFFFF"));
        switch1.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                view.toggleSwitch(true);
                Intent intent = new Intent("Map");
                intent.putExtra("type", 0);
                intent.putExtra("state", true);
                Objects.requireNonNull(getActivity()).sendBroadcast(intent);
            }

            @Override
            public void toggleToOff(SwitchView view) {
                view.toggleSwitch(false);
                Intent intent = new Intent("Map");
                intent.putExtra("type", 0);
                intent.putExtra("state", false);
                Objects.requireNonNull(getActivity()).sendBroadcast(intent);
            }
        });
    }

    /**
     * 显示选择弹窗
     * 如果申请权限则跳转到权限回调{@link MapSideFragment#onRequestPermissionsResult(int, String[], int[])}
     */
    private void showPhotoPopupWindow(){
        if (myPopupWindow == null){
            myPopupWindow = new MyPopupWindow(getContext());
        }
        myPopupWindow.showPhoto();//显示照片弹窗
        myPopupWindow.setOnClickListener(type -> {
            if (getContext() != null){
                switch (type){
                    case MyPopupWindow.TAKE_PHOTO:
                        //拍照
                        MyLog.e(TAG, "点击拍照");
                        if ((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                == PackageManager.PERMISSION_GRANTED)
                                && (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED)
                                && (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED)){
                            //权限都齐的情况下，跳转相机
                            openCamera();
                        }else {
                            if (getActivity() != null){
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
                                == PackageManager.PERMISSION_GRANTED)){
                            //权限都齐的情况下，跳转相册
                            openAlbum();
                        }else {
                            if (getActivity() != null){
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
     * 打开相机
     * 操作完成后回调{@link MapSideFragment#onActivityResult(int, int, Intent)}
     */
    private void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断存储卡是否可用，可用则存储
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME));
            //指定照片保存路径（SD卡），headImage.png为一个临时文件，每次拍照后这个图片都被替换
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        //Android7.0拍照闪退，已在MyApp和文件清单做了处理
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    /**
     * 打开相册
     * 操作完成后回调{@link MapSideFragment#onActivityResult(int, int, Intent)}
     */
    private void openAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, ALBUM_REQUEST_CODE);
    }

    /**
     * 照片剪裁
     * 操作完成后回调{@link MapSideFragment#onActivityResult(int, int, Intent)}
     */
    private void openTailor(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        // 图片格式
        intent.putExtra("outputFormat", "PNG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, TAILOR_REQUEST_CODE);
    }

    //保存裁剪之后的图片数据
    private void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap mPhoto = null;
        if (extras != null) {
            mPhoto = extras.getParcelable("data");
        } else {
            try {
                mPhoto = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(Objects.requireNonNull(data.getData())));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (mPhoto != null) {
            //上传
            upHead(saveMyBitmap(mPhoto));
        }
    }

    /**
     * 将照片保存到本地
     */
    private String saveMyBitmap(Bitmap bitmap) {
        File dir = new File(Environment.getExternalStorageDirectory() + "/head/");
        if (!dir.exists()){
            dir.mkdir();
        }
        File f = new File(dir.getPath() + SAVE_AVATAR_NAME);
        try {
            f.createNewFile();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f));
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, bos);
            bos.flush();
            bos.close();
            MyLog.e(TAG, "照片名：" + f.getName());
            MyLog.e(TAG, "照片绝对路径：" + f.getAbsoluteFile());
            return f.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 更换头像
     */
    private void upHead(String path){
        File file = new File(path);
        if (file.exists()){
            MyLog.e(TAG, "更换头像，图片文件存在，图片名：" + file.getName());
            //以下将图片转化上传
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), requestBody);

            Observable<UserData> observable = API_login.createApi().upHead(body);
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<UserData>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(UserData userData) {
                            if (userData.getCode() == 20000){
                                MyLog.e(TAG, "上传成功，图片地址：" + userData.getData().getHead());
                            }else {
                                MyLog.e(TAG, "上传失败，原因：" + userData.getMessage());
                            }
                            if (getContext() != null){
                                MyLog.e(TAG, "真实地址：" + ImageUtil.getHeadUrl(userData.getData().getHead()));
                                Glide.with(getContext())
                                        .load(ImageUtil.getHeadUrl(userData.getData().getHead()))
                                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                        .into(ivHeader);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    /**
     * 权限缺失提醒
     */
    private void showMissingPermissionDialog(String s){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("当前应用缺少\"" + s + "\"权限。\n\n请点击 \"设置 \"- \"权限 \"打开所需权限。");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("设置", (dialogInterface, i) -> openAppSetting());
        builder.show();
    }

    /**
     * 打开设置
     */
    private void openAppSetting(){
        if (getContext() != null){
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    @OnClick({R.id.ll_login_setting, R.id.ll_login_out, R.id.ll_close, R.id.iv_header})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_login_setting:
                break;
            case R.id.ll_login_out:
                MyApp.clearToken();
                startLogin();
                Objects.requireNonNull(getActivity()).finish();
                break;
            case R.id.ll_close:
                Objects.requireNonNull(getActivity()).finish();
                break;
            case R.id.iv_header:
                //更换头像，选择图片
                MyLog.e(TAG, "点击了头像");
                showPhotoPopupWindow();
                break;
        }
    }

    /**
     * 权限申请回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PHOTO_REQUEST_CAMERA:
                //相机权限请求回调
                MyLog.e(TAG, "相机权限请求回调");
                if (grantResults.length > 0){
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED
                            && grantResults[2] == PackageManager.PERMISSION_GRANTED){
                        //跳转相机
                        openCamera();
                    }else {
                        //无权限提示
                        showMissingPermissionDialog("相机\"或\"存储");
                    }
                }
                break;
            case PHOTO_REQUEST_ALBUM:
                MyLog.e(TAG, "相册权限请求回调");
                if (grantResults.length > 0){
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                        //跳转相册
                        openAlbum();
                    }else {
                        //无权限提示
                        showMissingPermissionDialog("存储");
                    }
                }
        }
    }

    /**
     * 页面回调
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                //相机回调
                MyLog.e(TAG, "相机回调");
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    File file = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
                    openTailor(Uri.fromFile(file));//对照片进行裁剪
                }else {
                    showToast("未找到存储卡，无法");
                }
                break;
            case ALBUM_REQUEST_CODE:
                //相册回调
                MyLog.e(TAG, "相册回调");
                upHead(FileUtil.getFilePathByUri(getContext(), data.getData()));
                break;
            case TAILOR_REQUEST_CODE:
                //图片剪裁回调
                MyLog.e(TAG, "图片剪裁回调");
                if (data != null){
                    setImageToView(data);
                }
        }
    }
}
