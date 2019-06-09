package com.jieniuwuliu.jieniu.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.FileUtil;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.SimpleCallBack;
import com.jieniuwuliu.jieniu.Util.UpLoadFileUtil;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseFragment;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.MenuItem;
import com.jieniuwuliu.jieniu.bean.StoreCerity;
import com.jieniuwuliu.jieniu.bean.UserBean;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.luntan.AppearTextActivity;
import com.jieniuwuliu.jieniu.messageEvent.MessageEvent;
import com.jieniuwuliu.jieniu.mine.ui.AddPicActivity;
import com.jieniuwuliu.jieniu.mine.ui.AddressListActivity;
import com.jieniuwuliu.jieniu.jijian.JiJianSelectActivity;
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

import org.greenrobot.eventbus.EventBus;
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
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class MineFragment extends BaseFragment implements OnItemClickListener{
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
    @Override
    protected int getFragmentLayoutId() {
        return R.layout.mine;
    }

    @Override
    protected void init() {
        dialog = new MyLoading(getActivity(),R.style.CustomDialog);
        list = new ArrayList<>();
        list.add(new MenuItem("我的门店", R.mipmap.ic_center_mendian));
        list.add(new MenuItem("月卡", R.mipmap.ic_center_yueka));
        list.add(new MenuItem("优惠券", R.mipmap.ic_center_youhuiquan));
        list.add(new MenuItem("寄件查询", R.mipmap.ic_center_jijianchaxun));
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
        token = (String) SPUtil.get(getContext(),Constant.TOKEN,Constant.TOKEN,"");
        Log.w("token",token);
        getUserInfo(token);
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
                        switch (user.getPersonType()){
                            case 0:
                                tvType.setVisibility(View.GONE);
                                break;
                            case 1:
                                type = "配件商";
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
                        }
                        tvType.setText(type);
                        isCertify = user.getAuth();
                        //是否认证
                        SPUtil.put(getActivity(),Constant.ISCERTIFY,Constant.ISCERTIFY,isCertify);
                        adapter.notifyItemChanged(0);
                        tvNickName.setText(user.getNickname());
                        if (user.getAddress()!=null){
                            tvAddress.setText(user.getAddress().getAddress());
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
            case 1://月卡
                intent = new Intent();
                intent.setClass(getActivity(),MyCardActivity.class);
                getActivity().startActivity(intent);
                break;
            case 2://优惠券
                intent = new Intent();
                intent.setClass(getActivity(),MyTicketActivity.class);
                getActivity().startActivity(intent);
                break;
            case 3://寄件查询
                intent = new Intent();
                intent.setClass(getActivity(),JiJianSelectActivity.class);
                getActivity().startActivity(intent);
                break;
            case 4://投诉建议
                intent = new Intent();
                intent.setClass(getActivity(),FeedBackActivity.class);
                getActivity().startActivity(intent);
                break;
            case 5://索取发票
                intent = new Intent();
                intent.setClass(getActivity(),SuoQuFaPiaoActivity.class);
                getActivity().startActivity(intent);
                break;
            case 6://我的积分
                intent = new Intent();
                intent.setClass(getActivity(),MyScoreActivity.class);
                intent.putExtra("score",user.getPoint());
                getActivity().startActivity(intent);
                break;
            case 7://在线客服
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

    @OnClick({R.id.setting,R.id.tv_like,R.id.layout_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting://设置
                intent = new Intent();
                intent.setClass(getActivity(),SettingActivity.class);
                getActivity().startActivity(intent);
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
}
