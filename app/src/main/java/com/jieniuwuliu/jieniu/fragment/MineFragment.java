package com.jieniuwuliu.jieniu.fragment;

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
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.util.FileUtil;
import com.jieniuwuliu.jieniu.util.GlideUtil;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.util.SimpleCallBack;
import com.jieniuwuliu.jieniu.util.UpLoadFileUtil;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseFragment;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.MenuItem;
import com.jieniuwuliu.jieniu.bean.UserBean;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.mine.ui.AddressListActivity;
import com.jieniuwuliu.jieniu.jijian.JiJianSelectActivity;
import com.jieniuwuliu.jieniu.mine.ui.BindWechatActivity;
import com.jieniuwuliu.jieniu.mine.ui.FeedBackActivity;
import com.jieniuwuliu.jieniu.mine.ui.MyFollowActivity;
import com.jieniuwuliu.jieniu.mine.ui.StoreCertifyActivity;
import com.jieniuwuliu.jieniu.mine.ui.StoreInfoActivity;
import com.jieniuwuliu.jieniu.mine.ui.SuoQuFaPiaoActivity;
import com.jieniuwuliu.jieniu.mine.adapter.MineAdater;
import com.jieniuwuliu.jieniu.mine.ui.MyCardActivity;
import com.jieniuwuliu.jieniu.mine.ui.MyScoreActivity;
import com.jieniuwuliu.jieniu.mine.ui.MyTicketActivity;
import com.jieniuwuliu.jieniu.mine.ui.SettingActivity;
import com.jieniuwuliu.jieniu.view.MDGridRvDividerDecoration;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.jieniuwuliu.jieniu.view.PicDialog;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class MineFragment extends BaseFragment implements OnItemClickListener, PicDialog.CallBack {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_nickname)
    TextView tvNickName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.score_no)
    TextView scoreNo;
    @BindView(R.id.ticket_no)
    TextView ticketNo;
    @BindView(R.id.head_img)
    ImageView headImg;
    @BindView(R.id.img_vip)
    ImageView imgVip;
    @BindView(R.id.rv)
    RecyclerView rv;
    private MineAdater adapter;
    private List<MenuItem> list;
    private Intent intent;

    private String token;
    private int isCertify;
    private UserBean.DataBean user;
    private final int PIC_CODE = 1001;//请求系统相册的请求码
    private final int CAMERA_CODE = 1002;//请求相机的请求码
    private MyLoading dialog ;
    private String type="";
    private  File pictureFile;
    private String imgUrl = "";
    private boolean isImg = false;
    private String[] permissions = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    @Override
    protected int getFragmentLayoutId() {
        return R.layout.mine;
    }

    @Override
    protected void init() {
        dialog = new MyLoading(getActivity(),R.style.CustomDialog);
        list = new ArrayList<>();
        list.add(new MenuItem("我的门店", R.mipmap.ic_center_mendian));
        list.add(new MenuItem("绑定微信", R.mipmap.wechat));
        list.add(new MenuItem("我的订单", R.mipmap.ic_center_jijianchaxun));
        list.add(new MenuItem("优惠券", R.mipmap.ic_center_youhuiquan));
        list.add(new MenuItem("月卡", R.mipmap.ic_center_yueka));
        list.add(new MenuItem("意见反馈", R.mipmap.ic_center_tousujianyi));
        list.add(new MenuItem("索取发票", R.mipmap.ic_center_suoqufapiao));
        list.add(new MenuItem("我的积分", R.mipmap.ic_center_wodejifen));
        list.add(new MenuItem("在线客服", R.mipmap.ic_center_kefurexian));
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        rv.setLayoutManager(manager);
        rv.addItemDecoration(new MDGridRvDividerDecoration(getActivity()));
        adapter = new MineAdater(getActivity(), list);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isImg){
            token = (String) SPUtil.get(getContext(),Constant.TOKEN,Constant.TOKEN,"");
            Log.w("token",token);
            getUserInfo(token);
        }
    }

    /**
     * 获取用户信息
     *
     * @param token*/
    private void getUserInfo(String token) {
        Call<UserBean> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).getUserInfo();
        call.enqueue(new SimpleCallBack<UserBean>(getActivity()) {
            @Override
            public void onSuccess(Response<UserBean> response) {
                try{
                    if (response.body().getStatus() == 0){
                        user = response.body().getData();
                        if (user.isVip()){
                            imgVip.setVisibility(View.VISIBLE);
                        }else{
                            imgVip.setVisibility(View.GONE);
                        }
                        GlideUtil.setUserImgUrl(getActivity(),user.getShopPhoto(),headImg);
                        SPUtil.put(getActivity(), Constant.USERTYPE, Constant.USERTYPE,user.getPersonType());
                        switch (user.getPersonType()){
                            case 0:
                                tvType.setVisibility(View.GONE);
                                break;
                            case 1:
                                type = "轿车客车";
                                break;
                            case 2:
                                type = "汽修厂";
                                break;
                            case 3:
                                type = "汽车用品";
                                break;
                            case 4:
                                type = "汽保工具";
                                break;
                            case 8:
                                type = "货车轻卡";
                                break;
                            case 9:
                                type = "单项易损";
                                break;
                        }
                        tvType.setText(type);
                        isCertify = user.getAuth();
                        //是否认证
                        SPUtil.put(getActivity(),Constant.ISCERTIFY,Constant.ISCERTIFY,isCertify);
                        adapter.notifyItemChanged(0);
                        if("".equals(user.getUnionid())||"-1".equals(user.getUnionid())){
                            SPUtil.put(getActivity(),Constant.BIND,Constant.BIND,false);
                        }else{
                            SPUtil.put(getActivity(),Constant.BIND,Constant.BIND,true);
                        }
                        adapter.notifyItemChanged(1);
                        tvNickName.setText(user.getNickname());
                        if (user.getAddress()!=null){
                            tvAddress.setText(user.getAddress().getAddress().replace("陕西省",""));
                            tvName.setText(user.getAddress().getName());
                            tvPhone.setText(user.getAddress().getPhone());
                        }else{
                            tvPhone.setText("");
                            tvName.setText("");
                            tvAddress.setText("");
                        }
                        scoreNo.setText(String.valueOf(user.getPoint()));
                        ticketNo.setText(String.valueOf(user.getCouponNum()));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<UserBean> response) {
                try {
                    String s = response.errorBody().string();
                    JSONObject object = new JSONObject(s);
                    MyToast.show(getActivity(), object.getString("msg"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNetError(String s) {
                MyToast.show(getActivity(),s);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position){
            case 0://我的门店
                if (isCertify == 4){
                    intent = new Intent();
                    intent.setClass(getActivity(),StoreCertifyActivity.class);
                    getActivity().startActivity(intent);
                }else if (isCertify == 1){
                    intent = new Intent();
                    intent.setClass(getActivity(),StoreInfoActivity.class);
                    intent.putExtra("id",user.getId());
                    getActivity().startActivity(intent);
                }else{
                    MyToast.show(getActivity(),"认证中，请耐心等待……");
                }
                break;
            case 1:
                intent = new Intent();
                intent.setClass(getActivity(),BindWechatActivity.class);
                intent.putExtra("wxName",user.getWxName());
                intent.putExtra("unionid",user.getUnionid());
                getActivity().startActivity(intent);
                break;
            case 2://我的订单
                intent = new Intent();
                intent.setClass(getActivity(),JiJianSelectActivity.class);
                getActivity().startActivity(intent);
                break;
            case 3://优惠券
                intent = new Intent();
                intent.setClass(getActivity(),MyTicketActivity.class);
                getActivity().startActivity(intent);
                break;
            case 4://月卡
                intent = new Intent();
                intent.setClass(getActivity(),MyCardActivity.class);
                getActivity().startActivity(intent);
                break;
            case 5://投诉建议
                intent = new Intent();
                intent.setClass(getActivity(),FeedBackActivity.class);
                getActivity().startActivity(intent);
                break;
            case 6://索取发票
                intent = new Intent();
                intent.setClass(getActivity(),SuoQuFaPiaoActivity.class);
                getActivity().startActivity(intent);
                break;
            case 7://我的积分
                intent = new Intent();
                intent.setClass(getActivity(),MyScoreActivity.class);
                intent.putExtra("score",user.getPoint());
                getActivity().startActivity(intent);
                break;
            case 8://在线客服
                setDialog();
                break;
        }
    }
    /**
     * 客服弹框
     * */
    private void setDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(),R.layout.dialog_custom,null);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        TextView tvPhone = view.findViewById(R.id.tv_phone);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},100);
                        return;
                    }
                }
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + "400-029-2282");
                intent.setData(data);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @OnClick({R.id.setting,R.id.head_img,R.id.tv_like,R.id.layout_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting://设置
                intent = new Intent();
                intent.setClass(getActivity(),SettingActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.head_img:
                new PicDialog(getActivity(),this);
                break;
            case R.id.layout_address://地址
                intent = new Intent();
                intent.setClass(getActivity(),AddressListActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.tv_like:
                intent = new Intent();
                intent.setClass(getActivity(),MyFollowActivity.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }
    //相册
    @Override
    public void openPic() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ( ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    ||  ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),permissions,100);
                return;
            }
        }
        isImg = true;
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, PIC_CODE);
    }
    //相机
    @Override
    public void openCamera() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ( ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    ||  ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),permissions,100);
                return;
            }
        }
        isImg = true;
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
                if (data!=null){
                    Log.w("imgurl", "onActivityResult:相册 " + data.getData().toString());
                    ContentResolver resolver = getActivity().getContentResolver();
                    try {
                        InputStream inputStream = resolver.openInputStream(data.getData());
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        Cursor cursor = resolver.query(data.getData(),
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
                        GlideUtil.setLocalUserImgUrl(getActivity(),imgUrl,headImg);
                        upload(imgUrl);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CAMERA_CODE://相机
                Log.w("img", pictureFile.getPath());
                GlideUtil.setLocalUserImgUrl(getActivity(),pictureFile.getPath(),headImg);
                upload(pictureFile.getPath());
                break;
        }
    }
    /**
     * 上传图片
     * */
    private void upload(String path) {
        COSXMLUploadTask storeTask =  UpLoadFileUtil.getIntance(getActivity()).upload("img",FileUtil.getFileName("head"),path);
        storeTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Log.w("返回结果","Success: " +result.accessUrl);
                updata(result.accessUrl);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.w("返回结果", "Failed: " + (exception == null ? serviceException.getMessage() : exception.toString()));
                MyToast.show(getActivity(),(exception == null ? serviceException.getMessage() : exception.toString()));
            }
        });
    }

    /**
     * 修改头像
     * */
    private void updata(final String url) {
        try {
            JSONObject object = new JSONObject();
            object.put("shopPhoto",url);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
            Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).modifyStoreInfo(body);
            call.enqueue(new SimpleCallBack<ResponseBody>(getActivity()) {
                @Override
                public void onSuccess(Response<ResponseBody> response) {
                    isImg = false;
                    try {
                        Log.i("result",response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFail(int errorCode, Response<ResponseBody> response) {
                    isImg = false;
                    try {
                        String s = response.errorBody().string();
                        Log.w("result", s);
                        JSONObject object = new JSONObject(s);
                        MyToast.show(getActivity(), object.getString("msg"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onNetError(String s) {
                    isImg = false;
                    MyToast.show(getActivity(),s);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("异常信息",e.toString());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
