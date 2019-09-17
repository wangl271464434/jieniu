package com.jieniuwuliu.jieniu.jijian;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.messageEvent.WeightEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 寄件数量
 */
public class JiJianNumActivity extends BaseActivity {

    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.et_num)
    EditText etNum;
    @BindView(R.id.et_info)
    EditText etInfo;
    private  WeightEvent event;
    private int num = 0;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_ji_jian_num;
    }

    @Override
    protected void init() {

    }
    @OnClick({R.id.img_close, R.id.img_ok, R.id.tv_weight,R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.img_ok:
                event = new WeightEvent();
                if (etNum.getText().toString().isEmpty()){
                    event.setNum(num);
                }else {
                    event.setNum(Integer.valueOf(etNum.getText().toString()));
                }
                if (!etInfo.getText().toString().isEmpty()){
                    event.setInfo(etInfo.getText().toString());
                }
                event.setType(tvWeight.getText().toString());
                EventBus.getDefault().post(event);
                finish();
                break;
            case R.id.tv_weight:
                showPopupWindow(tvWeight);
                break;
            case R.id.tv1:
                num = 1;
                break;
            case R.id.tv2:
                num = 2;
                break;
            case R.id.tv3:
                num = 3;
                break;
            case R.id.tv4:
                num = 4;
                break;
        }
    }
    private void showPopupWindow(View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_weight, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
     /*   popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.bg_white_shape));*/
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);
        final TextView tv1 = contentView.findViewById(R.id.tv1);
        final TextView tv2 = contentView.findViewById(R.id.tv2);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                tvWeight.setText(tv1.getText().toString());
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                tvWeight.setText(tv2.getText().toString());
            }
        });
    }
}
