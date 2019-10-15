package com.jieniuwuliu.jieniu.util;

import android.util.Log;

import com.jieniuwuliu.jieniu.api.HttpApi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpUtil {
    private static String TAG = "retrofit";
    private static HttpUtil intance = null;
//    private String baseUrl = "https://api.jieniuwuliu.com/";
    private String baseUrl = "http://192.168.1.105:1323/";
    private static final int READ_TIMEOUT = 60;//读取超时时间,单位秒
    private static final int CONN_TIMEOUT = 50;//连接超时时间,单位秒
    private Retrofit retrofit;
    /**     * 初始化一个client,不然retrofit会自己默认添加一个     */
    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(new MyInterceptor()).connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)//设置连接时间为50s
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取时间为一分钟
            .build();

    public HttpUtil() {
    }

    public static HttpUtil getInstance(){
        if (intance == null){
            intance = new HttpUtil();
        }
        return intance;
    }

    public Retrofit createRetrofit(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().client(httpClient)
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    /**携带token*/
    public Retrofit createRetrofit(final String token){
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Authorization","Bearer "+token);
                return chain.proceed(builder.build());
            }
        }).connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)//设置连接时间为50s
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取时间为一分钟
                .build();
        retrofit = new Retrofit.Builder().client(client)
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
    public HttpApi getApi(String token){
        return createRetrofit(token).create(HttpApi.class);
    }
    /**
     * 拦截器
     * */
    private static class MyInterceptor implements Interceptor{

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Log.i(TAG, "拦截器的请求数据：" + request.toString());
            Request.Builder builder = request.newBuilder();
            builder.addHeader("Content-Type", "application/json");
            Response response = chain.proceed(request);
            Log.i(TAG, "拦截器的返回数据：" + response.toString());
            return response;
        }
    }
}
