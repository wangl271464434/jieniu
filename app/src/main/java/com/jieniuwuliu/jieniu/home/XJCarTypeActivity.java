package com.jieniuwuliu.jieniu.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.KeyboardUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XJCarTypeActivity extends BaseActivity {
    @BindView(R.id.edit)
    EditText edit;
    @BindView(R.id.layout)
    RelativeLayout layout;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.layout_vin)
    LinearLayout layoutVin;
    private String vin;
    private Intent intent;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_xjcar_type;
    }
    @Override
    protected void init() {
    }
    @OnClick({R.id.layout_back, R.id.layout_search,R.id.layout_vin, R.id.tv_shoudong})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_search:
                vin = edit.getText().toString();
                if (vin.isEmpty()){
                    MyToast.show(getApplicationContext(),"请输入vin码");
                    return;
                }
                KeyboardUtil.hideSoftKeyboard(this);
                searchCar(vin);
                break;
            case R.id.layout_vin:
                intent = new Intent();
                intent.setClass(this,XjInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_shoudong:
                intent = new Intent();
                intent.setClass(this,XjAddCarTypeActivity.class);
                startActivity(intent);
                break;
        }
    }
    /**
     * 根据vin搜索
     * */
    private void searchCar(String vin) {
        layoutVin.setVisibility(View.VISIBLE);
    }
}
