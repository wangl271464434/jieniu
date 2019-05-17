package com.jieniuwuliu.jieniu.luntan;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.KeyboardUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.TimeUtil;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.DianZan;
import com.jieniuwuliu.jieniu.bean.Forum;
import com.jieniuwuliu.jieniu.bean.LunTanResult;
import com.jieniuwuliu.jieniu.bean.PingLun;
import com.jieniuwuliu.jieniu.bean.UnReadMsg;
import com.jieniuwuliu.jieniu.bean.UserBean;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.luntan.adapter.LuntanInfoAdater;
import com.jieniuwuliu.jieniu.luntan.adapter.LuntanPicAdapter;
import com.jieniuwuliu.jieniu.messageEvent.LuntanEvent;
import com.jieniuwuliu.jieniu.qipeishang.QPSORQXInfoActivity;
import com.jieniuwuliu.jieniu.view.MyLoading;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 论坛详情
 */
public class LuntanInfoActivity extends BaseActivity implements OnItemClickListener {

    @BindView(R.id.head_img)
    ImageView headImg;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.context)
    TextView context;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.layout_dianzan)
    LinearLayout layoutDianZan;
    @BindView(R.id.rv_msg)
    RecyclerView rvMsg;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.layout_img)
    RelativeLayout layoutImg;
    @BindView(R.id.tv_dianzan)
    TextView tvDianZan;
    @BindView(R.id.layout_msg)
    LinearLayout layoutMsg;
    @BindView(R.id.et_info)
    EditText etInfo;
    private LuntanInfoAdater adapter;
    private Forum data;
    private String token;
    private String time;
    private ArrayList<String> pics;
    private Intent intent;
    private MyLoading loading;
    private UnReadMsg.DataBean msgData;
    private List<PingLun> list;
    private int currentUid;
    private String currentName;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_luntan_info;
    }
    @Override
    protected void init() {
        list = new ArrayList<>();
        loading = new MyLoading(this,R.style.CustomDialog);
        token = (String) SPUtil.get(this, Constant.TOKEN,Constant.TOKEN,"");
        msgData = (UnReadMsg.DataBean) getIntent().getSerializableExtra("data");
        currentName = msgData.getName();
        currentUid = msgData.getUid();
        etInfo.setHint("@"+currentName+":");
        getData();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvMsg.setLayoutManager(manager);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.luntan_divider));
        rvMsg.addItemDecoration(divider);
        adapter = new LuntanInfoAdater(this, list);
        rvMsg.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }
    /**
     * 获取论坛信息
     * */
    private void getData() {
        loading.show();
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).getSingleForum(msgData.getFid());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loading.dismiss();
                try {
                    switch (response.code()){
                        case 200:
                            data = (Forum) GsonUtil.praseJsonToModel(response.body().string(),Forum.class);
                            name.setText(data.getData().getName());
                            if (data.getData().getInfo().equals("")){
                                context.setVisibility(View.GONE);
                            }else{
                                context.setVisibility(View.VISIBLE);
                                context.setText(data.getData().getInfo());
                            }
                            time = data.getData().getCreatedAt();
                            tvTime.setText(TimeUtil.getShowString(TimeUtil.getMiliSecond(time)));
                            if (data.getData().getPhoto()!=null){
                                if (!data.getData().getPhoto().equals("")){
                                    GlideUtil.setUserImgUrl(LuntanInfoActivity.this,data.getData().getPhoto(),headImg);
                                }
                            }
                            pics = new ArrayList<>();
                            switch (data.getData().getType()){
                                case 1:
                                    rv.setVisibility(View.GONE);
                                    layoutImg.setVisibility(View.GONE);
                                    String json = data.getData().getPhotos();
                                    if (!json.equals("")){
                                        try {
                                            JSONArray array = new JSONArray(json);
                                            for (int j = 0;j<array.length();j++){
                                                pics.add(array.get(j).toString());
                                            }
                                            ViewGroup.LayoutParams lp = iv.getLayoutParams();
                                            lp.width = 360;
                                            lp.height = 480;
                                            iv.setLayoutParams(lp);
                                            iv.setMaxHeight(lp.height);
                                            iv.setMaxWidth(lp.width);
                                            GlideUtil.setImgUrl(LuntanInfoActivity.this,pics.get(0),iv);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }else{
                                        iv.setVisibility(View.GONE);
                                    }
                                    break;
                                case 2:
                                    iv.setVisibility(View.GONE);
                                    layoutImg.setVisibility(View.GONE);
                                    String jsonpic = data.getData().getPhotos();
                                    if (!jsonpic.equals("")){
                                        try {
                                            JSONArray array = new JSONArray(jsonpic);
                                            for (int j = 0;j<array.length();j++){
                                                pics.add(array.get(j).toString());
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    GridLayoutManager manager = new GridLayoutManager(LuntanInfoActivity.this, 3);
                                    rv.setLayoutManager(manager);
                                    LuntanPicAdapter adapter = new LuntanPicAdapter(LuntanInfoActivity.this, pics);
                                    rv.setAdapter(adapter);
                                    adapter.setListener(new OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            Intent intent = new Intent();
                                            intent.setClass(LuntanInfoActivity.this,LookPicActivity.class);
                                            intent.putStringArrayListExtra("list", pics);
                                            intent.putExtra("index", position);
                                            startActivity(intent);
                                        }
                                    });
                                    break;
                                case 3:
                                    iv.setVisibility(View.GONE);
                                    rv.setVisibility(View.GONE);
                                    GlideUtil.setVideoImg(LuntanInfoActivity.this,data.getData().getVideoImage(),img);
                                    break;
                            }
                            setDianZan();
                            if (data.getData().getPinglun().size()>0){
                                list.addAll(data.getData().getPinglun());
                                adapter.notifyDataSetChanged();
                            }
                            break;
                        case 400:
                            String error = response.errorBody().string();
                            JSONObject object = new JSONObject(error);
                            MyToast.show(LuntanInfoActivity.this, object.getString("msg"));
                            break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
                Log.i("error","error reason is:"+t.toString());
            }
        });
    }

    private void setDianZan() {
        String s = "";
        if (data.getData().getDianzan().size()>0){
            layoutDianZan.setVisibility(View.VISIBLE);
            for (int j = 0;j<data.getData().getDianzan().size();j++){
                if (j==0){
                    s += data.getData().getDianzan().get(j).getName();
                }else{
                    s += ","+data.getData().getDianzan().get(j).getName();
                }
            }
            tvDianZan.setText(s);
        }else{
            layoutDianZan.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.back, R.id.head_img,R.id.iv,R.id.img,R.id.tv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.head_img:
                intent = new Intent();
                intent.setClass(this,QPSORQXInfoActivity.class);
                intent.putExtra("id",data.getData().getUid());
                startActivity(intent);
                break;
            case R.id.iv:
                intent = new Intent();
                intent.setClass(this,LookPicActivity.class);
                intent.putStringArrayListExtra("list", pics);
                startActivity(intent);
                break;
            case R.id.img:
                intent = new Intent();
                intent.setClass(this,VideoActivity.class);
                intent.putExtra("video",data.getData().getVideo());
                startActivity(intent);
                break;
            case R.id.tv_send://发送消息
                String info = etInfo.getText().toString();
                if (info.isEmpty()){
                    MyToast.show(LuntanInfoActivity.this,"发送内容不能为空");
                    return;
                }
                KeyboardUtil.hideSoftKeyboard(LuntanInfoActivity.this);
                sendMsg(info);
                break;
        }
    }
    /**
     * 发送评论
     * */
    private void sendMsg(final String info) {
        loading.show();
        Map<String,Object> map = new HashMap();
        map.put("fid",data.getData().getId());
        map.put("info",info);
        map.put("ruid",currentUid);
        String json = GsonUtil.mapToJson(map);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).addPingLun(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loading.dismiss();
                try{
                    switch (response.code()){
                        case 200:
                            etInfo.setText("");
                            PingLun  pingLun = new PingLun();
                            pingLun.setInfo(info);
                            pingLun.setName(data.getData().getName());
                            pingLun.setRuid(currentUid);
                            pingLun.setRname(currentName);
                            list.add(pingLun);
                            rv.smoothScrollToPosition(list.size());//将列表移到最后一条
                            adapter.notifyDataSetChanged();
                            break;
                        case 400:
                            String s = response.errorBody().string();
                            JSONObject object = new JSONObject(s);
                            MyToast.show(getApplicationContext(), object.getString("msg"));
                            break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("error","fail reason is:"+t.toString());
                loading.dismiss();
            }
        });
    }


    @Override
    public void onItemClick(View view, int position) {
        if (list.get(position).getUid()==data.getData().getUid()){
            MyToast.show(LuntanInfoActivity.this,"不能自己回复自己");
        }else{
            currentName = list.get(position).getName();
            currentUid = list.get(position).getUid();
            etInfo.setText("");
            etInfo.setHint("@"+currentName+":");
        }

    }
}
