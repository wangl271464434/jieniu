package com.jieniuwuliu.jieniu.api;


import com.jieniuwuliu.jieniu.bean.AddAdr;
import com.jieniuwuliu.jieniu.bean.AddressList;
import com.jieniuwuliu.jieniu.bean.BJOrder;
import com.jieniuwuliu.jieniu.bean.Coupon;
import com.jieniuwuliu.jieniu.bean.ImgBanner;
import com.jieniuwuliu.jieniu.bean.LoginBean;
import com.jieniuwuliu.jieniu.bean.AliPayResult;
import com.jieniuwuliu.jieniu.bean.Notice;
import com.jieniuwuliu.jieniu.bean.QPType;
import com.jieniuwuliu.jieniu.bean.StoreBean;
import com.jieniuwuliu.jieniu.bean.UserBean;
import com.jieniuwuliu.jieniu.bean.Version;
import com.jieniuwuliu.jieniu.bean.VinCar;
import com.jieniuwuliu.jieniu.bean.XJCarType;
import com.jieniuwuliu.jieniu.bean.XJOrder;
import com.jieniuwuliu.jieniu.bean.XjInfo;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HttpApi {
    /**获取通告*/
    @GET("users/notices")
    Call<Notice> getNotice();
    /**版本更新*/
    @GET("edition")
    Call<Version> checkVersion();
    /**登录*/
    @POST("login")
    Call<LoginBean> login(@Body RequestBody body);
    /**注册*/
    @POST("register")
    Call<ResponseBody> register(@Body RequestBody body);
    /**获取验证码*/
    @POST("sms/{phone}")
    Call<ResponseBody> code(@Path("phone") String phone,@Query("st")String type);
    /**忘记密码*/
    @PUT("password")
    Call<ResponseBody> forgetPwd(@Body RequestBody body);
    /**微信登录*/
    @GET("checkwx")
    Call<LoginBean> weChatLogin(@Query("openid")String openid,@Query("unionid")String unionid);
    /**微信登录*/
    @GET("checkphone")
    Call<LoginBean> weChatBindPhone(@Query("openid")String openid,@Query("unionid")String unionid,@Query("phone")String phone,@Query("code")String code);
    /**获取用户信息*/
    @GET("users")
    Call<UserBean> getUserInfo();
    /**修改用户信息*/
    @PUT("users")
    Call<UserBean> modifyUserInfo(@Body RequestBody body);
     /**获取轮播*/
     @GET("users/getimgsrc")
     Call<ImgBanner> getBanner();
    /**
     * 获取汽配商列表信息
     * */
    @GET("users/store/qp")
    Call<StoreBean> getQPSList(@Query("car")String type,@Query("nickname")String name,@Query("page")int page,@Query("number")int num);
    /**
     * 获取汽修商列表信息
     * */
    @GET("users/store/qx")
    Call<StoreBean> getQXSList(@Query("lat")double lat,@Query("lng")double lng,@Query("page")int page,@Query("number")int num,@Query("region")String region,@Query("distance")String distance,@Query("yewu")String yewu);
   /**
    * 获取汽修工具或汽车用品列表
    * */
    @POST("users/stores")
    Call<StoreBean> getQXORQBList(@Body RequestBody body);
    /**
     * 获取门店详情
     * @path sid
     * */
    @GET("users/stores/{sid}")
    Call<ResponseBody> getStoreInfo(@Path("sid") int id);
    /**
     * 修改门店信息
     * */
    @PUT("users/stores")
    Call<ResponseBody> modifyStoreInfo(@Body RequestBody body);
    /**
     * 积分兑换
     * */
    @PUT("users/points/100/coupons/5/{times}")
    Call<ResponseBody> convertCoupon(@Path("times") int num);
    /**
     * 查询优惠券列表
     * */
    @GET("users/coupons")
    Call<Coupon> getCoupons(@Query("page")int page,@Query("number")int num);
    /**
     * 使用优惠券
     * */
    @PUT("users/coupons/{cid}")
    Call<ResponseBody> useCoupon(@Path("cid")int id);
    /**
     * 获取地址列表
     * */
    @GET("users/addresses")
    Call<AddressList> getAddressList();
    /**
     * 添加地址
     * */
    @POST("users/addresses")
    Call<AddAdr> addAddress(@Body RequestBody body);
    /**
     * 更新地址信息
     * */
    @PUT("users/addresses/{aid}")
    Call<ResponseBody> updateAddress(@Path("aid") int id,@Body RequestBody body);
    /**
     * 删除地址
     * */
    @DELETE("users/addresses/{aid}")
    Call<ResponseBody> deleteAddress(@Path("aid") int id);
    /**
     * 意见反馈
     * */
    @POST("users/feedbacks")
    Call<ResponseBody> feedBack(@Body RequestBody body);
    /**
     * 添加关注
     *  门店id/userid {"fUid":}
     * */
    @POST("users/follows")
    Call<ResponseBody> addFollow(@Body RequestBody body);
    /**
     * 取消关注
     *  门店id  {"fUid":}
     * */
    @DELETE("users/follows/{fid}")
    Call<ResponseBody> deleteFollow(@Path("fid") int id);
    /**
     * 关注列表
     *
     * */
    @GET("users/follows")
    Call<ResponseBody> getFollows(@Query("page")int page,@Query("number")int num);
    /**
     * 关注详情
     *  门店id  {"fUid":}
     * */
    @GET("users/follows/{fid}")
    Call<ResponseBody> getFollowInfo(@Path("fid") int id);
    /**
     * 寄件
     * */
    @POST("users/orders")
    Call<ResponseBody> addOrder(@Body RequestBody body);
    /**
     * 寄件详情
     * */
    @GET("users/orders/{oid}")
    Call<ResponseBody> getOrderInfo(@Path("oid")String orderNo);
    /**
     * 发布论坛
     * */
    @POST("users/forums")
    Call<ResponseBody> addForums(@Body RequestBody body);
    /**
     * 获取论坛列表
     * */
    @GET("users/forums")
    Call<ResponseBody> getForumsList(@Query("page")int page,@Query("number")int num);
   /**
    * 获取单条论坛信息
    * */
    @GET("users/forums/{fid}")
    Call<ResponseBody> getSingleForum(@Path("fid")int id);
    /**
     * 删除论坛
     * */
    @DELETE("users/forums/{fid}")
    Call<ResponseBody> deleteForums(@Path("fid")int id);
    /**
     * 点赞
     *  fid
     * */
    @POST("users/forum/dianzan")
    Call<ResponseBody> addDianZan(@Body RequestBody body);
    /**
     * 取消点赞
     * 论坛id
     * */
    @DELETE("users/forum/dianzan/{fid}")
    Call<ResponseBody> deleteDianZan(@Path("fid")int id);
    /**
     * 添加评论
     * */
    @POST("users/forum/pinglun")
    Call<ResponseBody> addPingLun(@Body RequestBody body);
    /**
     * 删除评论
     * */
    @DELETE("users/forum/pinglum/{pid}")
    Call<ResponseBody> deletePingLun(@Path("pid")int id);

    /**
     * 快递员查看所有他配送的订单
     * @type   daijie paisong  wancheng
     *
     * */
    @GET("users/order/kuaidi/{type}")
    Call<ResponseBody> getPSorderList(@Path("type")String type,@Query("page")int page,@Query("number")int num);
    /**
     * 配送员根据订单号查询订单
     * @type   daijie paisong  wancheng
     * */
    @GET("users/order/kuaidi/{type}")
    Call<ResponseBody> getPSSelectOrder(@Path("type")String type,@Query("order_number")String orderNo,@Query("startdate")String start,@Query("enddate")String end);
    /**
     * 寄件列表
     * type 1寄件 2 收件 3 不限
     * status 1配送中 2已完成
     * */
    @GET("users/order/user")
    Call<ResponseBody> getMyOrderList(@Query("type")int type,@Query("status")int status,@Query("page")int page,@Query("number")int num);
    /**
     * 寄件列表
     * type 1寄件 2 收件 3 不限
     * status 1配送中 2已完成
     * */
    @GET("users/order/user/index")
    Call<ResponseBody> getHomeOrderList(@Query("page")int page,@Query("number")int num);
    /**
     * 根据订单号查询订单
     * */
    @GET("users/orders/{oid}")
    Call<ResponseBody> selectOrder(@Path("oid")String orderNo);
    /**
     * 查询开发票的订单
     * */
    @GET("users/invoice/orders")
    Call<ResponseBody> getFaPiaoList(@Query("page")int page,@Query("number")int num);
    /**
     *  接单 改派
     * */
    @PUT("users/orders/{orderNo}")
    Call<ResponseBody> updateOrder(@Path("orderNo")String orderNo,@Body RequestBody body);
    /**
     *  改派人员列表
     * */
    @GET("users/order/kuaidis")
    Call<ResponseBody> getKuaiDiList(@Query("page")int page,@Query("number")int num);
    /**
     * 拒接订单
     * */
    @PUT("users/order/refuse/{oid}")
    Call<ResponseBody> reFuseOrder(@Path("oid")String orderNo);
    /***
     * 根据门店名搜索门店
     * */
    @GET("users/stores")
    Call<ResponseBody> searchStore(@Query("nickname") String nickname,@Query("page")int page,@Query("number")int num);
    /**
     * 推荐门店
     * */
    @GET("users/recommends")
    Call<ResponseBody> getRecomStoreList(@Query("page")String page,@Query("number")String num);
    /**
     * 获取未读消息列表
     * */
    @GET("users/pinglun/unread")
    Call<ResponseBody> getUnReadList(@Query("page")int page,@Query("number")int num);
   /**
     * 将消息设置为已读
    * cid 论坛评论id
     * */
     @POST("users/reads")
     Call<ResponseBody> getReadMsg(@Body RequestBody body);
     /**
      * 从服务器获取支付宝支付信息
      * */
     @GET("users/alipay")
     Call<AliPayResult> getAliInfo(@Query("total_amount")String amount, @Query("out_trade_no")String tradeNo,@Query("subject")String info);
    /**
     * 获取网点
     * */
    @GET("users/wangdian")
    Call<ResponseBody> getWangDianList();
    /**
     * 取消订单
     * */
    @PUT("users/order/cancel/{oid}")
    Call<ResponseBody> cancelOrder(@Path("oid")String orderNo);
    /**
     *  拨打电话
     * */
    @POST("users/phoneprotect")
    Call<ResponseBody> callPhone(@Query("tophone")String toPhone);
    /**
     * vin码查询
     * */
    @POST("users/frame")
    Call<VinCar> selectVin(@Query("vin")String vin);
    /**
     * 询价车型
     * */
    @GET("users/allcar")
    Call<XJCarType> getXJCarType();
    /**
     * 添加询价单
     * */
    @POST("users/insertoffer")
    Call<ResponseBody> addXJOrder(@Body RequestBody body);
    /**
     * 询价列表
     * */
    @GET("users/getalloffer")
    Call<XJOrder> getXJOrderList(@Query("page")String page,@Query("number")String num);
    /**
     * 询价单详情
     * */
    @GET("users/getofferinform")
    Call<XjInfo> getXJOrderInfo(@Query("Bid")int bid);
    /**
     * 确认购买订单
     * */
    @PUT("users/offerqp")
    Call<ResponseBody> putXJOrderInfo(@Body RequestBody body);
    /**
     * 报价列表
     * */
    @GET("users/getofferlist")
    Call<BJOrder> getBJOrderList(@Query("page")String page,@Query("number")String num);
    /**
     * 配件商报价
     * */
    @POST("users/offerqp")
    Call<ResponseBody> addBJInfo(@Body RequestBody body);
    /**
     * 取消询价
     * */
    @PUT("users/offerstate")
    Call<ResponseBody> cancelBJOrder(@Query("Bid")int bid);
    /**
     * 获取汽配城列表
     * */
    @GET("/users/getqpclist")
    Call<QPType> getQpList();
}
