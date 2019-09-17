package com.jieniuwuliu.jieniu.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.util.GlideUtil;
import com.jieniuwuliu.jieniu.bean.OrderInfo;
import com.jieniuwuliu.jieniu.luntan.LookPicActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDescActivity extends AppCompatActivity {
    @BindView(R.id.layout_img)
    LinearLayout layoutImg;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.img_user)
    ImageView imgUser;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.tv_baojia)
    TextView tvBaojia;
    @BindView(R.id.tv_baojia_money)
    TextView tvBaojiaMoney;
    @BindView(R.id.tv_daishou)
    TextView tvDaiShou;
    @BindView(R.id.tv_yunfei)
    TextView tvYunFei;
    @BindView(R.id.tv_fahuo_name)
    TextView tvFahuoName;
    @BindView(R.id.tv_fahuo_address)
    TextView tvFahuoAddress;
    @BindView(R.id.tv_fahuo_phone)
    TextView tvFahuoPhone;
    @BindView(R.id.tv_fahuo_contact)
    TextView tvFahuoContact;
    @BindView(R.id.tv_shouhuo_name)
    TextView tvShouhuoName;
    @BindView(R.id.tv_shouhuo_address)
    TextView tvShouhuoAddress;
    @BindView(R.id.tv_shouhuo_phone)
    TextView tvShouhuoPhone;
    @BindView(R.id.tv_shouhuo_contact)
    TextView tvShouhuoContact;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    @BindView(R.id.title)
    TextView title;
    private OrderInfo orderWuliuInfo;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_desc);
        ButterKnife.bind(this);
        title.setText("订单信息");
        orderWuliuInfo = (OrderInfo) getIntent().getSerializableExtra("order");
        setData();
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        //签收照片
        if (!orderWuliuInfo.getFinishPhoto().equals("")) {
            layoutImg.setVisibility(View.VISIBLE);
            GlideUtil.setImgUrl(OrderDescActivity.this, orderWuliuInfo.getFinishPhoto(), R.mipmap.loading, img);
        } else {
            layoutImg.setVisibility(View.GONE);
        }
        switch (orderWuliuInfo.getPayType()){
            case 0:
                tvPay.setText("未支付");
                break;
            case 1:
                tvPay.setText("微信支付");
                break;
            case 2:
                tvPay.setText("支付宝支付");
                break;
            case 3:
                tvPay.setText("货到付款");
                break;
        }
        tvNum.setText(orderWuliuInfo.getNumber() + "件");
        tvBaojia.setText("¥ " + (orderWuliuInfo.getBaojiaMoney() / 100));
        tvBaojiaMoney.setText("¥ " + (orderWuliuInfo.getBaojiajine() / 100));
        tvDaiShou.setText("¥ " + (orderWuliuInfo.getDaishouMoney() / 100));
        tvYunFei.setText("¥ " + (orderWuliuInfo.getYunfeiMoney() / 100));
        tvInfo.setText(orderWuliuInfo.getInfo());
        tvFahuoName.setText(orderWuliuInfo.getFromName());
        tvFahuoPhone.setText(orderWuliuInfo.getFromPhone());
        tvFahuoAddress.setText(orderWuliuInfo.getFromAddress().replace("陕西省",""));
        tvShouhuoName.setText(orderWuliuInfo.getToName());
        tvShouhuoAddress.setText(orderWuliuInfo.getToAddress().replace("陕西省",""));
        tvShouhuoPhone.setText(orderWuliuInfo.getToPhone());
        if (orderWuliuInfo.getOrderList() != null) {
            if (orderWuliuInfo.getOrderList().size() != 0) {
                if (!orderWuliuInfo.getOrderList().get(0).getMsg().equals("已发货")) {
                    if (orderWuliuInfo.getOrderList().get(0).getMsg().equals("已签收")) {
                        GlideUtil.setUserImgUrl(OrderDescActivity.this, orderWuliuInfo.getOrderList().get(1).getPhoto(), imgUser);
                        tvName.setText(orderWuliuInfo.getOrderList().get(1).getName());
                        tvPhone.setText(orderWuliuInfo.getOrderList().get(1).getPhone());
                    } else {
                        GlideUtil.setUserImgUrl(OrderDescActivity.this, orderWuliuInfo.getOrderList().get(0).getPhoto(), imgUser);
                        tvName.setText(orderWuliuInfo.getOrderList().get(0).getName());
                        tvPhone.setText(orderWuliuInfo.getOrderList().get(0).getPhone());
                    }
                }
            } else {
                tvName.setText("暂无信息");
                tvPhone.setText("暂无信息");
            }
        } else {
            tvName.setText("暂无信息");
            tvPhone.setText("暂无信息");
        }
    }

    @OnClick({R.id.back,R.id.img, R.id.tv_call_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.img:
                ArrayList<String> list = new ArrayList<>();
                list.add(orderWuliuInfo.getFinishPhoto());
                intent = new Intent();
                intent.setClass(OrderDescActivity.this,LookPicActivity.class);
                intent.putStringArrayListExtra("list", list);
                startActivity(intent);
                break;
            case R.id.tv_call_phone:
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE}, 100);
                        return;
                    }
                }
                String phone = tvPhone.getText().toString();
                if (phone.equals("暂无信息")){
                    return;
                }
                intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                startActivity(intent);
                break;
        }
    }
}
