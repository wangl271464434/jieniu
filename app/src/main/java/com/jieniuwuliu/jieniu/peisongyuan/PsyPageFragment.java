package com.jieniuwuliu.jieniu.peisongyuan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.LoginActivity;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseFragment;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.UserBean;
import com.jieniuwuliu.jieniu.mine.ui.ModifyPwdActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PsyPageFragment extends BaseFragment {
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

    @OnClick(R.id.btn_exit)
    public void onViewClicked() {

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

    @OnClick({R.id.layout_pwd, R.id.btn_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
}
