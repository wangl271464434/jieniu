package com.jieniuwuliu.jieniu;

import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.view.Action;
import com.jieniuwuliu.jieniu.view.CountdownView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 欢迎页
 */
public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.tv_enter)
    CountdownView tvEnter;
    private String token;
    private int userType;
//    private boolean isGuide;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void init() {
        token = (String) SPUtil.get(this, Constant.TOKEN, Constant.TOKEN, "");
        userType = (int) SPUtil.get(this, Constant.USERTYPE, Constant.USERTYPE, 0);
//        isGuide = (boolean) SPUtil.get(this, Constant.GUIDE, Constant.GUIDE, false);
        tvEnter.start();
        tvEnter.setOnFinishAction(new Action() {
            @Override
            public void onAction() {
                enter();
            }
        });
    }

    @OnClick(R.id.tv_enter)
    public void onViewClicked() {
        enter();
    }

    /**
     * 进入具体界面
     */
    private void enter() {
        if (!token.equals("")) {
            if (userType == 5||userType == 6) {
                startAcy(LoginActivity.class);
            } else {
                startAcy(MainActivity.class);
            }
        } else {
            startAcy(LoginActivity.class);
        }
      /*  if (isGuide) {

        } else {
            startAcy(GuideActivity.class);
        }*/
        finish();
    }
}
