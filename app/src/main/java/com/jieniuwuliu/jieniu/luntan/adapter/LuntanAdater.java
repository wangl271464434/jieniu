package com.jieniuwuliu.jieniu.luntan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.Util.JwtUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.TimeUtil;
import com.jieniuwuliu.jieniu.Util.VideoUtil;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.LunTanResult;
import com.jieniuwuliu.jieniu.bean.UserBean;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.listener.OnItemLongClickListener;
import com.jieniuwuliu.jieniu.luntan.AppearTextActivity;
import com.jieniuwuliu.jieniu.luntan.LookPicActivity;
import com.jieniuwuliu.jieniu.qipeishang.QPSORQXInfoActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class LuntanAdater extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context context;
    private OnItemClickListener listener;
    private List<LunTanResult.DataBean> list;
    private View view;
    private boolean isFlag = false;//是否弹出点赞和评论
    private CallBack callBack;
    private UserBean.DataBean user;
    private ViewHead viewHead;
    private  ViewHolder holder;
    private ViewPicHolder picHolder;
    private VideoViewHolder videoViewHolder;
    private String time;

    public void setUser(UserBean.DataBean user) {
        this.user = user;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public LuntanAdater(Context context,List<LunTanResult.DataBean> list) {
        this.context = context;
        this.list = list;
    }
    public void setData(List<LunTanResult.DataBean> data){
        list.addAll(data);
        notifyDataSetChanged();
    }
    public interface CallBack {
        void dianZan(int position);
        void cancelDianZan(int position);
        void PingLun(int position);
        void playVideo(int position);
        void msgToUser(int msgPosition,int userPosition);
        void deleteLunTan(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            if (list.size()!=0){
                Log.i("length",list.size()+"");
                return list.get(position-1).getType();
            }else {
                return 1;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (i) {
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.luntan_head, viewGroup, false);
                viewHolder = new ViewHead(view);
                break;
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.luntan_item, viewGroup, false);
                viewHolder = new ViewHolder(view);
                view.setOnClickListener(this);
                break;
            case 2:
                view = LayoutInflater.from(context).inflate(R.layout.luntan_pic_item, viewGroup, false);
                viewHolder = new ViewPicHolder(view);
                view.setOnClickListener(this);
                break;
            case 3:
                view = LayoutInflater.from(context).inflate(R.layout.luntan_video_item, viewGroup, false);
                viewHolder = new VideoViewHolder(view);
                view.setOnClickListener(this);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        viewHolder.itemView.setTag(i);
        String token = (String) SPUtil.get(context,Constant.TOKEN,Constant.TOKEN,"");
        String id = JwtUtil.JWTParse(token);
        switch (getItemViewType(i)) {
            case 0:
                if (i==0){
                    viewHead  = (ViewHead) viewHolder;
                    if (user != null) {
                        viewHead.tvName.setText(user.getNickname());
                        GlideUtil.setRoundImg(context, user.getShopPhoto(), viewHead.imgHead);
                    }
                }
                break;
            case 1:
                holder = (ViewHolder) viewHolder;
                if (id.equals(String.valueOf(list.get(i-1).getUid()))){
                    holder.tvDel.setVisibility(View.VISIBLE);
                }else{
                    holder.tvDel.setVisibility(View.INVISIBLE);
                }
                holder.tvDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callBack.deleteLunTan(i-1);
                    }
                });
                holder.name.setText(list.get(i-1).getName());
                GlideUtil.setUserImgUrl(context,list.get(i-1).getPhoto(),holder.headImg);
                time = list.get(i-1).getCreatedAt();
                holder.tvTime.setText(TimeUtil.getShowString(TimeUtil.getMiliSecond(time)));
                holder.headImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(context,QPSORQXInfoActivity.class);
                        intent.putExtra("id",list.get(i-1).getUid());
                        context.startActivity(intent);
                    }
                });
                if (list.get(i-1).getInfo().equals("")){
                    holder.context.setVisibility(View.GONE);
                }else{
                    holder.context.setVisibility(View.VISIBLE);
                    holder.context.setText(list.get(i-1).getInfo());
                    String json = list.get(i-1).getPhotos();
                    final ArrayList<String> pics = new ArrayList<>();
                    if (!json.equals("")){
                        holder.iv.setVisibility(View.VISIBLE);
                        ViewGroup.LayoutParams lp =   holder.iv.getLayoutParams();
                        lp.width = 360;
                        lp.height = 480;
                        holder.iv.setLayoutParams(lp);
                        holder.iv.setMaxHeight(lp.height);
                        holder.iv.setMaxWidth(lp.width);
                        try {
                            JSONArray array = new JSONArray(json);
                            for (int j = 0;j<array.length();j++){
                                pics.add(array.get(j).toString());
                            }
                            GlideUtil.setImgUrl(context,pics.get(0),R.mipmap.loading,holder.iv);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        holder.iv.setVisibility(View.GONE);
                    }
                    holder.iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClass(context,LookPicActivity.class);
                            intent.putStringArrayListExtra("list", pics);
                            context.startActivity(intent);
                        }
                    });
                }
                if (list.get(i-1).isShow()){
                    holder.layoutComment.setVisibility(View.VISIBLE);
                }else{
                    holder.layoutComment.setVisibility(View.INVISIBLE);
                }
                holder.imgIcon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(buttonView.isPressed()){
                            if (isChecked){
                                list.get(i-1).setShow(true);
                                notifyItemChanged(i);
                            }else{
                                list.get(i-1).setShow(false);
                                notifyItemChanged(i);
                            }
                        }

                    }
                });
                if (list.get(i-1).getDianzan().size()>0||list.get(i-1).getPinglun().size()>0){
                    holder.layoutMsg.setVisibility(View.VISIBLE);
                }else{
                    holder.layoutMsg.setVisibility(View.GONE);
                }
                //点赞
                if (list.get(i-1).isZan()){
                    holder.tvLike.setText("取消");
                }else {
                    holder.tvLike.setText("点赞");
                }
                if (list.get(i-1).getDianzan().size()>0){
                    String s = "";
                    for (int j = 0;j<list.get(i-1).getDianzan().size();j++){
                        if (j==0){
                            s += list.get(i-1).getDianzan().get(j).getName();
                        }else{
                            s += ","+list.get(i-1).getDianzan().get(j).getName();
                        }
                    }
                    holder.tvDianZan.setText(s);
                }
                holder.tvLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.get(i-1).setShow(false);
                        if (list.get(i-1).isZan()){
                            list.get(i-1).setZan(false);
                            callBack.cancelDianZan(i-1);
                        }else{
                            list.get(i-1).setZan(true);
                            callBack.dianZan(i-1);
                        }
                    }
                });
                //评论
                if (list.get(i-1).getPinglun().size()>0){
                    if (list.get(i-1).getDianzan().size()==0){
                        holder.layoutDianZan.setVisibility(View.GONE);
                    }else{
                        holder.layoutDianZan.setVisibility(View.VISIBLE);
                    }
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    holder.rvMsg.setLayoutManager(linearLayoutManager);
                    LuntanInfoAdater adater = new LuntanInfoAdater(context,list.get(i-1).getPinglun());
                    holder.rvMsg.setAdapter(adater);
                    adater.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            if (user.getId()!=list.get(i-1).getPinglun().get(position).getUid()){
                                callBack.msgToUser(i-1,position);
                            }else{
                                MyToast.show(context,"不能自己给自己回复");
                            }

                        }
                    });
                }
                holder.tvComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.get(i-1).setShow(false);
                       callBack.PingLun(i-1);
                    }
                });
                break;
            case 2:
                picHolder = (ViewPicHolder) viewHolder;
                if (id.equals(String.valueOf(list.get(i-1).getUid()))){
                    picHolder.tvDel.setVisibility(View.VISIBLE);
                }else{
                    picHolder.tvDel.setVisibility(View.INVISIBLE);
                }
                picHolder.tvDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callBack.deleteLunTan(i-1);
                    }
                });
                picHolder.name.setText(list.get(i-1).getName());
                time = list.get(i-1).getCreatedAt();
                picHolder.tvTime.setText(TimeUtil.getShowString(TimeUtil.getMiliSecond(time)));
                GlideUtil.setUserImgUrl(context,list.get(i-1).getPhoto(),picHolder.headImg);
                picHolder.headImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(context,QPSORQXInfoActivity.class);
                        intent.putExtra("id",list.get(i-1).getUid());
                        context.startActivity(intent);
                    }
                });
                if (list.get(i-1).getInfo().equals("")){
                    picHolder.context.setVisibility(View.GONE);
                }else{
                    picHolder.context.setVisibility(View.VISIBLE);
                    picHolder.context.setText(list.get(i-1).getInfo());
                }
                String jsonpic = list.get(i-1).getPhotos();
                final ArrayList<String> imgs = new ArrayList<>();
                if (!jsonpic.equals("")){
                    try {
                        JSONArray array = new JSONArray(jsonpic);
                        for (int j = 0;j<array.length();j++){
                            imgs.add(array.get(j).toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                GridLayoutManager manager = new GridLayoutManager(context, 3);
                picHolder.rv.setLayoutManager(manager);
                LuntanPicAdapter adapter = new LuntanPicAdapter(context, imgs);
                picHolder.rv.setAdapter(adapter);
                adapter.setListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent();
                        intent.setClass(context,LookPicActivity.class);
                        intent.putStringArrayListExtra("list", imgs);
                        intent.putExtra("index", position);
                        context.startActivity(intent);
                    }
                });
                if (list.get(i-1).isShow()){
                    picHolder.layoutComment.setVisibility(View.VISIBLE);
                }else{
                    picHolder.layoutComment.setVisibility(View.INVISIBLE);
                }
                picHolder.imgIcon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (buttonView.isPressed()){
                            if (isChecked){
                                list.get(i-1).setShow(true);
                                notifyItemChanged(i);
                            }else{
                                list.get(i-1).setShow(false);
                                notifyItemChanged(i);
                            }
                        }
                    }
                });
                if (list.get(i-1).getDianzan().size()>0||list.get(i-1).getPinglun().size()>0){
                    picHolder.layoutMsg.setVisibility(View.VISIBLE);
                }else{
                    picHolder.layoutMsg.setVisibility(View.GONE);
                }
                //点赞
                if (list.get(i-1).isZan()){
                    picHolder.tvLike.setText("取消");
                }else {
                    picHolder.tvLike.setText("点赞");
                }
                if (list.get(i-1).getDianzan().size()>0){
                    String s = "";
                    for (int j = 0;j<list.get(i-1).getDianzan().size();j++){
                        if (j==0){
                            s += list.get(i-1).getDianzan().get(j).getName();
                        }else{
                            s += ","+list.get(i-1).getDianzan().get(j).getName();
                        }
                    }
                    picHolder.tvDianZan.setText(s);
                }
                picHolder.tvLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.get(i-1).setShow(false);
                        if (list.get(i-1).isZan()){
                            list.get(i-1).setZan(false);
                            callBack.cancelDianZan(i-1);
                        }else{
                            list.get(i-1).setZan(true);
                            callBack.dianZan(i-1);
                        }
                    }
                });
                //评论
                if (list.get(i-1).getPinglun().size()>0){
                    if (list.get(i-1).getDianzan().size()==0){
                        picHolder.layoutDianZan.setVisibility(View.GONE);
                    }else{
                        picHolder.layoutDianZan.setVisibility(View.VISIBLE);
                    }
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    picHolder.rvMsg.setLayoutManager(linearLayoutManager);
                    LuntanInfoAdater adater = new LuntanInfoAdater(context,list.get(i-1).getPinglun());
                    picHolder.rvMsg.setAdapter(adater);
                    adater.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            if (user.getId()!=list.get(i-1).getPinglun().get(position).getUid()){
                                callBack.msgToUser(i-1,position);
                            }else{
                                MyToast.show(context,"不能自己给自己回复");
                            }

                        }
                    });
                }
                picHolder.tvComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.get(i-1).setShow(false);
                        callBack.PingLun(i-1);
                    }
                });
                break;
            case 3:
               videoViewHolder = (VideoViewHolder) viewHolder;
                if (id.equals(String.valueOf(list.get(i-1).getUid()))){
                    videoViewHolder.tvDel.setVisibility(View.VISIBLE);
                }else{
                    videoViewHolder.tvDel.setVisibility(View.INVISIBLE);
                }
                videoViewHolder.tvDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callBack.deleteLunTan(i-1);
                    }
                });
                videoViewHolder.name.setText(list.get(i-1).getName());
                time = list.get(i-1).getCreatedAt();
                videoViewHolder.tvTime.setText(TimeUtil.getShowString(TimeUtil.getMiliSecond(time)));
                GlideUtil.setVideoImg(context,list.get(i-1).getVideoImage(),videoViewHolder.img);
                videoViewHolder.layoutImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callBack.playVideo(i-1);
                    }
                });
                GlideUtil.setUserImgUrl(context,list.get(i-1).getPhoto(),videoViewHolder.headImg);
                videoViewHolder.headImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(context,QPSORQXInfoActivity.class);
                        intent.putExtra("id",list.get(i-1).getUid());
                        context.startActivity(intent);
                    }
                });
                if (list.get(i-1).isShow()){
                    videoViewHolder.layoutComment.setVisibility(View.VISIBLE);
                }else{
                    videoViewHolder.layoutComment.setVisibility(View.INVISIBLE);
                }
                if (list.get(i-1).getInfo().equals("")){
                    videoViewHolder.context.setVisibility(View.GONE);
                }else{
                    videoViewHolder.context.setVisibility(View.VISIBLE);
                    videoViewHolder.context.setText(list.get(i-1).getInfo());
                }
                videoViewHolder.imgIcon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (buttonView.isPressed()){
                            if (isChecked){
                                list.get(i-1).setShow(true);
                                notifyItemChanged(i);
                            }else{
                                list.get(i-1).setShow(false);
                                notifyItemChanged(i);
                            }
                        }
                    }
                });
                if (list.get(i-1).getDianzan().size()>0||list.get(i-1).getPinglun().size()>0){
                    videoViewHolder.layoutMsg.setVisibility(View.VISIBLE);
                }else{
                    videoViewHolder.layoutMsg.setVisibility(View.GONE);
                }
                //点赞
                if (list.get(i-1).isZan()){
                    videoViewHolder.tvLike.setText("取消");
                }else {
                    videoViewHolder.tvLike.setText("点赞");
                }
                if (list.get(i-1).getDianzan().size()>0){
                    String s = "";
                    for (int j = 0;j<list.get(i-1).getDianzan().size();j++){
                        if (j==0){
                            s += list.get(i-1).getDianzan().get(j).getName();
                        }else{
                            s += ","+list.get(i-1).getDianzan().get(j).getName();
                        }
                    }
                    videoViewHolder.tvDianZan.setText(s);
                }
                videoViewHolder.tvLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.get(i-1).setShow(false);
                        if (list.get(i-1).isZan()){
                            list.get(i-1).setZan(false);
                            callBack.cancelDianZan(i-1);
                        }else{
                            list.get(i-1).setZan(true);
                            callBack.dianZan(i-1);
                        }
                    }
                });
                //评论
                if (list.get(i-1).getPinglun().size()>0){
                    if (list.get(i-1).getDianzan().size()==0){
                        videoViewHolder.layoutDianZan.setVisibility(View.GONE);
                    }else{
                        videoViewHolder.layoutDianZan.setVisibility(View.VISIBLE);
                    }
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    videoViewHolder.rvMsg.setLayoutManager(linearLayoutManager);
                    LuntanInfoAdater adater = new LuntanInfoAdater(context,list.get(i-1).getPinglun());
                    videoViewHolder.rvMsg.setAdapter(adater);
                    adater.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            if (user.getId()!=list.get(i-1).getPinglun().get(position).getUid()){
                                callBack.msgToUser(i-1,position);
                            }else{
                                MyToast.show(context,"不能自己给自己回复");
                            }
                        }
                    });
                }
                videoViewHolder.tvComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.get(i-1).setShow(false);
                        callBack.PingLun(i-1);
                    }
                });
                break;
        }

    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onItemClick(view, (Integer) view.getTag());
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.head_img)
        ImageView headImg;
        @BindView(R.id.img_icon)
        CheckBox imgIcon;
        @BindView(R.id.iv)
        ImageView iv;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_del)
        TextView tvDel;
        @BindView(R.id.tv_dianzan)
        TextView tvDianZan;
        @BindView(R.id.layout_comment)
        LinearLayout layoutComment;
        @BindView(R.id.layout_msg)
        LinearLayout layoutMsg;
        @BindView(R.id.layout_dianzan)
        LinearLayout layoutDianZan;
        @BindView(R.id.rv_msg)
        RecyclerView rvMsg;
        @BindView(R.id.tv_like)
        TextView tvLike;
        @BindView(R.id.tv_comment)
        TextView tvComment;
        @BindView(R.id.context)
        TextView context;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class ViewPicHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.head_img)
        ImageView headImg;
        @BindView(R.id.img_icon)
        CheckBox imgIcon;
        @BindView(R.id.rv)
        RecyclerView rv;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_del)
        TextView tvDel;
        @BindView(R.id.tv_dianzan)
        TextView tvDianZan;
        @BindView(R.id.layout_comment)
        LinearLayout layoutComment;
        @BindView(R.id.layout_msg)
        LinearLayout layoutMsg;
        @BindView(R.id.layout_dianzan)
        LinearLayout layoutDianZan;
        @BindView(R.id.rv_msg)
        RecyclerView rvMsg;
        @BindView(R.id.tv_like)
        TextView tvLike;
        @BindView(R.id.tv_comment)
        TextView tvComment;
        @BindView(R.id.context)
        TextView context;
        ViewPicHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class ViewHead extends RecyclerView.ViewHolder {
        @BindView(R.id.img_head)
        ImageView imgHead;
        @BindView(R.id.tv_name)
        TextView tvName;
        public ViewHead(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.head_img)
        ImageView headImg;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.context)
        TextView context;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_del)
        TextView tvDel;
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.layout_img)
        RelativeLayout layoutImg;
        @BindView(R.id.tv_like)
        TextView tvLike;
        @BindView(R.id.tv_dianzan)
        TextView tvDianZan;
        @BindView(R.id.layout_msg)
        LinearLayout layoutMsg;
        @BindView(R.id.layout_dianzan)
        LinearLayout layoutDianZan;
        @BindView(R.id.rv_msg)
        RecyclerView rvMsg;
        @BindView(R.id.tv_comment)
        TextView tvComment;
        @BindView(R.id.layout_comment)
        LinearLayout layoutComment;
        @BindView(R.id.img_icon)
        CheckBox imgIcon;

        VideoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
