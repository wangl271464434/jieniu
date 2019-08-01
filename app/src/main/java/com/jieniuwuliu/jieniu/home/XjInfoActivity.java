package com.jieniuwuliu.jieniu.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Machine;
import com.jieniuwuliu.jieniu.home.adapter.XjAddMachieAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XjInfoActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.img3)
    ImageView img3;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private AlertDialog dialog;
    private List<Machine> list;
    private XjAddMachieAdapter adapter;
    private EditText etName;
    private String name,type = "";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_xj_info;
    }

    @Override
    protected void init() {
        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new XjAddMachieAdapter(this,list);
        recyclerView.setAdapter(adapter);
    }
    @OnClick({R.id.layout_back,R.id.tv_add, R.id.img1, R.id.img2, R.id.img3, R.id.btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.tv_add:
                showAddDialog();
                break;
            case R.id.img1:
                break;
            case R.id.img2:
                break;
            case R.id.img3:
                break;
            case R.id.btn:
                break;
        }
    }

    private void showAddDialog() {
        dialog = new AlertDialog.Builder(this).create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.show();
        dialog.setContentView(R.layout.dialog_xj_add);
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        etName = dialog.findViewById(R.id.et_name);
        dialog.findViewById(R.id.yuding_btn).setOnClickListener(this);
        dialog.findViewById(R.id.yuanjian_btn).setOnClickListener(this);
        dialog.findViewById(R.id.fujian_btn).setOnClickListener(this);
        dialog.findViewById(R.id.chaijian_btn).setOnClickListener(this);
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(this);
        dialog.findViewById(R.id.tv_sure).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.yuding_btn:
                type = "接受预订";
                break;
            case R.id.yuanjian_btn:
                type = "原厂件";
                break;
            case R.id.fujian_btn:
                type = "副厂件";
                break;
            case R.id.chaijian_btn:
                type = "拆车件";
                break;
            case R.id.tv_cancel:
                dialog.dismiss();
                break;
            case R.id.tv_sure:
                name = etName.getText().toString();
                if (name.isEmpty()){
                    MyToast.show(getApplicationContext(),"请输入配件名称");
                    return;
                }
                dialog.dismiss();
                Machine machine = new Machine();
                machine.setName(name);
                machine.setType(type);
                list.add(machine);
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
