package com.jieniuwuliu.jieniu.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.SimpleCallBack;
import com.jieniuwuliu.jieniu.adapter.SortAdapter;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.XJCarType;
import com.jieniuwuliu.jieniu.home.adapter.XJCarTypeAdapter;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.jieniuwuliu.jieniu.view.SideBar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class XjAddCarTypeActivity extends BaseActivity {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.sideBar)
    SideBar sideBar;
    private XJCarTypeAdapter adapter;
    private LinearLayoutManager manager;
    private String token;
    private List<XJCarType> list;
    private MyLoading loading;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_xj_add_car_type;
    }

    @Override
    protected void init() {
        loading = new MyLoading(this,R.style.CustomDialog);
        list = new ArrayList<>();
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new XJCarTypeAdapter(this, list);
        recyclerView.setLayoutManager(manager);
        //设置右侧SideBar触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    manager.scrollToPositionWithOffset(position, 0);
                }
            }
        });
        token = (String) SPUtil.get(this, Constant.TOKEN,Constant.TOKEN,"");
        getData();
    }

    private void getData() {
        loading.show();
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).getXJCarType();
        call.enqueue(new SimpleCallBack<ResponseBody>(this) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                loading.dismiss();
                try {
                   List<Object> objects = GsonUtil.praseJsonToList(response.body().string(),XJCarType.class);
                   for (Object object:objects){
                       XJCarType xjCarType = (XJCarType) object;
                       list.add(xjCarType);
                   }
                   adapter.notifyDataSetChanged();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                loading.dismiss();
                try{
                    String s = response.errorBody().string();
                    JSONObject object = new JSONObject(s);
                    MyToast.show(getApplicationContext(), object.getString("msg"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNetError(String s) {
                loading.dismiss();
                MyToast.show(getApplicationContext(),s);
            }
        });
    }


    @OnClick({R.id.layout_back, R.id.img_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.img_search:
                break;
        }
    }
}
