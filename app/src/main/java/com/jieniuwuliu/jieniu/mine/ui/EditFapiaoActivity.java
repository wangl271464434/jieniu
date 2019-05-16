package com.jieniuwuliu.jieniu.mine.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 填写发票
 */
public class EditFapiaoActivity extends BaseActivity {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.et_company)
    EditText etCompany;
    @BindView(R.id.et_shuihao)
    EditText etShuihao;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_bank)
    EditText etBank;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_fapiao;
    }

    @Override
    protected void init() {
        title.setText("填写开票信息");
    }
    @OnClick({R.id.back, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                break;
        }
    }
}
