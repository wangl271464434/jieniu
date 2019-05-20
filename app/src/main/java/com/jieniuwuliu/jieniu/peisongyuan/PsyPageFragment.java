package com.jieniuwuliu.jieniu.peisongyuan;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.LoginActivity;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.FileUtil;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.UpLoadFileUtil;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseFragment;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.UserBean;
import com.jieniuwuliu.jieniu.messageEvent.MessageEvent;
import com.jieniuwuliu.jieniu.mine.ui.AddPicActivity;
import com.jieniuwuliu.jieniu.mine.ui.ModifyPwdActivity;
import com.jieniuwuliu.jieniu.view.PicDialog;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PsyPageFragment extends BaseFragment implements PicDialog.CallBack {
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.btn_name)
    Button btnName;
    @BindView(R.id.tv_today_num)
    TextView tvTodayNum;
    @BindView(R.id.tv_total_num)
    TextView tvTotalNum;
    @BindView(R.id.tv_bumen)
    TextView tvBumen;
    private String token;
    private final int PIC_CODE = 1001;//请求系统相册的请求码
    private final int CAMERA_CODE = 1002;//请求相机的请求码
    private File pictureFile;
    private String imgUrl;
    @Override
    protected int getFragmentLayoutId() {
        return R.layout.psy_page;
    }

    @Override
    protected void init() {
        token = (String) SPUtil.get(getActivity(), Constant.TOKEN, Constant.TOKEN, "");
        getUserInfo(token);
    }

    /**
     * 获取用户信息
     *
     * @param token
     */
    private void getUserInfo(String token) {
        Call<UserBean> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).getUserInfo();
        call.enqueue(new Callback<UserBean>() {
            @Override
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                try {
                    switch (response.code()) {
                        case 200://成功
                            UserBean user = response.body();
                            if (!user.getData().getShopPhoto().equals("")){
                                GlideUtil.setUserImgUrl(getActivity(),user.getData().getShopPhoto(),img);
                            }
                            tvBumen.setText(user.getData().getRegion());
                            btnName.setText(user.getData().getNickname());
                            tvTodayNum.setText("" + user.getData().getTodayCount());
                            tvTotalNum.setText("" + user.getData().getTotalCount());
                            if (!user.getData().getShopPhoto().equals("")) {
                                GlideUtil.setUserImgUrl(getActivity(), user.getData().getShopPhoto(), img);
                            }
                            break;
                        case 400://错误
                            String s = response.errorBody().string();
                            JSONObject object = new JSONObject(s);
                            MyToast.show(getActivity(), object.getString("msg"));
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<UserBean> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 退出登录
     */
    private void exit() {
        SPUtil.put(getActivity(), Constant.TOKEN, Constant.TOKEN, "");
        Intent intent = new Intent();
        intent.setClass(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @OnClick({R.id.img,R.id.layout_pwd, R.id.btn_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img://跟换头像
                showPicDialog();
                break;
            case R.id.layout_pwd:
                Intent intent = new Intent();
                intent.setClass(getActivity(),ModifyPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_exit:
                exit();
                break;
        }
    }
    private void showPicDialog() {
        PicDialog picDialog = new PicDialog(getActivity(),this);
    }

    @Override
    public void openPic() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},100);
                return;
            }
            int permission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
                return;
            }
            int read = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
            if (read != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                return;
            }
        }
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, PIC_CODE);
    }

    @Override
    public void openCamera() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},100);
                return;
            }
            int permission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
                return;
            }
            int read = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
            if (read != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                return;
            }
        }
        pictureFile = FileUtil.createImageFile(getActivity());
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, pictureFile.getAbsolutePath());
            uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
        }else {
            uri = Uri.fromFile(pictureFile);
        }
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(cameraIntent, CAMERA_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PIC_CODE://相册
                Log.w("imgurl", "onActivityResult:相册 " + data.getData().toString());
                ContentResolver resolver = getActivity().getContentResolver();
                try {
                    InputStream inputStream = resolver.openInputStream(data.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    Cursor cursor = getActivity().getContentResolver().query(data.getData(),
                            new String[]{MediaStore.Images.ImageColumns.DATA},//
                            null, null, null);
                    if (cursor == null){
                        imgUrl = data.getData().getPath();
                    }else {
                        cursor.moveToFirst();
                        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        imgUrl = cursor.getString(index);
                        cursor.close();
                    }
                    GlideUtil.setLocal0UserImgUrl(getActivity(),imgUrl,img);
                    updateImg(imgUrl);
//                    mIvDispaly.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case CAMERA_CODE://相机
                imgUrl = pictureFile.getPath();
                GlideUtil.setLocal0UserImgUrl(getActivity(),imgUrl,img);
                updateImg(imgUrl);
                break;
        }
    }
    /**
     * 上传图片到腾讯云
     * */
    private void updateImg(String imgUrl) {
        COSXMLUploadTask storeTask =  UpLoadFileUtil.getIntance(getActivity()).upload("img",new File(imgUrl).getName(),imgUrl);
        storeTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Log.w("返回结果","Success: " +result.accessUrl);
               update(result.accessUrl);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.w("返回结果", "Failed: " + (exception == null ? serviceException.getMessage() : exception.toString()));
                MyToast.show(getActivity(),(exception == null ? serviceException.getMessage() : exception.toString()));
            }
        });
    }
    /**
     * 上传图片到服务器
     * */
    private void update(String accessUrl) {
        Map<String,Object> map = new HashMap<>();
        map.put("shopPhoto",accessUrl);
        String json = GsonUtil.mapToJson(map);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),json);
        Call<ResponseBody> observable = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).modifyStoreInfo(body);
        observable.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                switch (response.code()) {
                    case 200:
                        break;
                    case 400:
                        try {
                            String s = response.errorBody().string();
                            Log.w("result",s);
                            JSONObject object = new JSONObject(s);
                            MyToast.show(getActivity(), object.getString("msg"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.w("error", t.toString());
            }
        });
    }
}
