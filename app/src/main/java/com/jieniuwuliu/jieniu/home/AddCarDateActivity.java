package com.jieniuwuliu.jieniu.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.VinCar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddCarDateActivity extends BaseActivity {

    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.edit)
    EditText edit;
    private String time;
    private VinCar.Data data;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_car_date;
    }

    @Override
    protected void init() {
        data = (VinCar.Data) getIntent().getSerializableExtra("data");
        GlideUtil.setImgUrl(this,data.getLogos(),img);
        name.setText(data.getCartype());
    }
    @OnClick({R.id.layout_back, R.id.btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.btn:
                time = edit.getText().toString();
                if (time.isEmpty()){
                    MyToast.show(getApplicationContext(),"请输入年份、款式");
                    return;
                }
                data.setCartype(data.getCartype()+" "+time);
                Intent intent = new Intent();
                intent.setClass(this,XjInfoActivity.class);
                intent.putExtra("data",data);
                startActivity(intent);
                break;
        }
    }
}
