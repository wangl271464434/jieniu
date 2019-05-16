package com.jieniuwuliu.jieniu.luntan;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.LunTanResult;
import com.jieniuwuliu.jieniu.view.CustomJCVideo;

import butterknife.BindView;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * 播放视频
 */
public class VideoActivity extends BaseActivity{
    @BindView(R.id.player)
    CustomJCVideo player;
    private LunTanResult.DataBean video;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_video;
    }
    @Override
    protected void init() {
        video = (LunTanResult.DataBean) getIntent().getSerializableExtra("video");
    }
    @Override
    protected void onResume() {
        super.onResume();
        player.setUp(video.getVideo(),JCVideoPlayer.SCREEN_WINDOW_FULLSCREEN,"没有标题");
        player.startVideo();
    }
    @OnClick(R.id.img_back)
    public void onClick(){
        finish();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
