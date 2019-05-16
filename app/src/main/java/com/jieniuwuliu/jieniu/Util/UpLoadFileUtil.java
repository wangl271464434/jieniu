package com.jieniuwuliu.jieniu.Util;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.LunTanResult;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.DeleteObjectRequest;
import com.tencent.cos.xml.model.object.DeleteObjectResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UpLoadFileUtil {
    private static String imgUrl = "";
    private String ossUrl = "http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/";
    private static UpLoadFileUtil instance = null;
    private String appid = "1254151230";
    private String region = "ap-beijing";
    private String secretId = Constant.SECRETID;
    private String secretKey =Constant.SECRETKEY;
    private Context context;
    private String bucket = "jieniu-1254151230";
    private String cosPath = "对象键"; //即存储到 COS 上的绝对路径, 格式如 cosPath = "test.txt";
    private String srcPath = "本地文件的绝对路径"; // 如 srcPath=Environment.getExternalStorageDirectory().getPath() + "/test.txt";
    private String uploadId = null; //若存在初始化分片上传的 UploadId，则赋值对应uploadId值用于续传，否则，赋值null。
    private CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
            .setAppidAndRegion(appid, region)
            .setDebuggable(true)
            .builder();
    private QCloudCredentialProvider credentialProvider = new ShortTimeCredentialProvider(secretId,
            secretKey, 300);
    private CosXmlSimpleService cosXml ;
    private UpLoadFileUtil(Context context) {
        this.context = context;
        cosXml = new CosXmlSimpleService(context, serviceConfig, credentialProvider);
    }
    public static UpLoadFileUtil getIntance(Context context){
        if (instance == null){
            instance = new UpLoadFileUtil(context);
        }
        return instance;
    }
    /**
     * 文件上传
     * @param type  类型 图片 视频
     * @param path 文件路径
     * */
    public COSXMLUploadTask upload(String type,String name,String path){
        try {
            cosPath = type+"/"+ name;
            // 初始化 TransferConfig
            TransferConfig transferConfig = new TransferConfig.Builder().build();
            //初始化 TransferManager
            TransferManager transferManager = new TransferManager(cosXml, transferConfig);
            //上传文件
            COSXMLUploadTask cosxmlUploadTask = transferManager.upload(bucket, cosPath, path, uploadId);
            return cosxmlUploadTask;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 多图片上传
     * */
    public void upLoadList(String type,List<String> list){
        final List<String> urls = new ArrayList<>();
        for (String s:list) {
            COSXMLUploadTask task =  upload(type,new File(s).getName(),s);
            task.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    Log.w("返回结果","Success: " +result.accessUrl);
                    urls.add(result.accessUrl);
                }
                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                    Log.w("返回结果", "Failed: " + (exception == null ? serviceException.getMessage() : exception.toString()));
                }
            });
        }
    }
    /**
     * 删除文件
     * */
    public void  delete(String key){
        // 指定要删除的 bucket 和路径
        try {
            DeleteObjectRequest request = new DeleteObjectRequest(bucket,key.replace(ossUrl,""));
            DeleteObjectResult result = cosXml.deleteObject(request);
            Log.i("OSS delete result",result.printResult());
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
        }
        // 关闭客户端(关闭后台线程)
        cosXml.cancelAll();
    }
    /**
     * 删除论坛中的 视频 图片
     *
     * @param deleteList*/
    public void deleteFile(List<String> deleteList){
        new Thread(){
            @Override
            public void run() {
                super.run();
                for (String s :deleteList){
                    delete(s);
                }
            }
        }.start();
    }
    /**
     * 删除单张图片
     *
     * @param imgUrl*/
    public void deleteImg(String imgUrl){
        new Thread(){
            @Override
            public void run() {
                super.run();
                delete(imgUrl);
            }
        }.start();
    }
    public void deleteList(LunTanResult.DataBean dataBean){
        List<String> deleteList = new ArrayList<>();
        try {
            switch (dataBean.getType()){
                case 3://视频
                    deleteList.add(dataBean.getVideoImage());
                    deleteList.add(dataBean.getVideo());
                    deleteFile(deleteList);
                    break;
                default://单图片或者多图片
                    if (!dataBean.getPhotos().equals("")){
                        JSONArray array = new JSONArray(dataBean.getPhotos());
                        for (int j = 0;j<array.length();j++){
                            deleteList.add(array.get(j).toString());
                        }
                        deleteFile(deleteList);
                    }
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
