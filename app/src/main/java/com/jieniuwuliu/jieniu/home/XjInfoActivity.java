package com.jieniuwuliu.jieniu.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.util.FileUtil;
import com.jieniuwuliu.jieniu.util.GlideUtil;
import com.jieniuwuliu.jieniu.util.GsonUtil;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.util.SimpleCallBack;
import com.jieniuwuliu.jieniu.util.UpLoadFileUtil;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.Machine;
import com.jieniuwuliu.jieniu.bean.VinCar;
import com.jieniuwuliu.jieniu.bean.XJImg;
import com.jieniuwuliu.jieniu.home.adapter.XjAddMachieAdapter;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.jieniuwuliu.jieniu.view.PicDialog;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class XjInfoActivity extends BaseActivity implements View.OnClickListener, PicDialog.CallBack {
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
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.layout_no)
    LinearLayout layoutNo;
    @BindView(R.id.tv_no)
    TextView tvNo;
    private AlertDialog dialog;
    private List<Machine> list;
    private XjAddMachieAdapter adapter;
    private EditText etName;
    private CheckBox ycbz,ycnbz,pp,cc,ccxf;
    private String name,token, type = "",remark = "",carNo="暂无车架号";
    private VinCar.Data data;
    private int imgType;
    private XJImg xjImg;
    private MyLoading loading;

    private String[] permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private File pictureFile;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_xj_info;
    }

    @Override
    protected void init() {
        xjImg = new XJImg();
        loading = new MyLoading(this,R.style.CustomDialog);
        token = (String) SPUtil.get(this,Constant.TOKEN,Constant.TOKEN,"");
        data = (VinCar.Data) getIntent().getSerializableExtra("data");
        carNo = getIntent().getStringExtra("carNo");
        if (data != null) {
            tvName.setText(data.getCartype());
            GlideUtil.setImgUrl(this, data.getLogos(), img);
            if ("暂无车架号".equals(carNo) || carNo == null){
               layoutNo.setVisibility(View.GONE);
            }else{
                layoutNo.setVisibility(View.VISIBLE);
                tvNo.setText(carNo);
            }
        }
        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new XjAddMachieAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }
    @OnLongClick(R.id.tv_no)
    public boolean onLongClicked(View view){
        ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Label", carNo);
        manager.setPrimaryClip(clipData);
        MyToast.show(this, "复制成功");
        return false;
    }
    @OnClick({R.id.layout_back, R.id.tv_add, R.id.img1, R.id.img2, R.id.img3, R.id.btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.tv_add:
                showAddDialog();
                break;
            case R.id.img1:
                imgType = 1;
                showPicDialog();
                break;
            case R.id.img2:
                imgType = 2;
                showPicDialog();
                break;
            case R.id.img3:
                imgType = 3;
                showPicDialog();
                break;
            case R.id.btn:
                if("".equals(xjImg.getCjUrl())){
                    MyToast.show(this,"请您上传车架号的照片");
                    return;
                }
                addXJOrder();
                break;
        }
    }

    private void uploadImg(String path, int imgType) {
        loading.show();
        COSXMLUploadTask storeTask = UpLoadFileUtil.getIntance(this).upload("img", FileUtil.getFileName("xj"), path);
        storeTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Log.w("返回结果", "Success: " + result.accessUrl);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.dismiss();
                        switch (imgType) {
                            case 1:
                                xjImg.setCjUrl(result.accessUrl);
                                GlideUtil.setImgUrl(XjInfoActivity.this,  result.accessUrl, img1);
                                break;
                            case 2:
                                xjImg.setCtUrl(result.accessUrl);
                                GlideUtil.setImgUrl(XjInfoActivity.this,   result.accessUrl, img2);
                                break;
                            case 3:
                                xjImg.setPjUrl(result.accessUrl);
                                GlideUtil.setImgUrl(XjInfoActivity.this,  result.accessUrl, img3);
                                break;
                        }
                    }
                });

            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.w("返回结果", "Failed: " + (exception == null ? serviceException.getMessage() : exception.toString()));
                MyToast.show(getApplicationContext(), (exception == null ? serviceException.getMessage() : exception.toString()));
            }
        });
    }

    private void showPicDialog() {
        new PicDialog(this, this);
    }

    //生成询价单
    private void addXJOrder() {
        try {
            loading.show();
            remark = etRemark.getText().toString();
            if (list.size() == 0) {
                MyToast.show(getApplicationContext(), "请添加配件");
                return;
            }
            String imgStr = GsonUtil.objectToJson(xjImg);
            String pjStr = GsonUtil.listToJson(list);
            JSONObject object = new JSONObject();
            object.put("Partsphoto",imgStr);
            object.put("Remarks",remark);
            object.put("Logos",data.getLogos());
            object.put("Cartype",data.getBrand());
            object.put("Carbrand",data.getCartype());
            object.put("Carvin",carNo);
            object.put("Partslist",pjStr);
            object.put("Stype",1);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
            Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).addXJOrder(body);
            call.enqueue(new SimpleCallBack<ResponseBody>(this) {
                @Override
                public void onSuccess(Response<ResponseBody> response) {
                    loading.dismiss();
                    MyToast.show(getApplicationContext(),"添加成功");
                    finish();
                }

                @Override
                public void onFail(int errorCode, Response<ResponseBody> response) {
                    loading.dismiss();
                    try {
                        String s = response.errorBody().string();
                        Log.w("result",s);
                        JSONObject object = new JSONObject(s);
                        MyToast.show(XjInfoActivity.this, object.getString("msg"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNetError(String s) {
                    loading.dismiss();
                    MyToast.show(getApplicationContext(),s);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
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
        ycbz = dialog.findViewById(R.id.ycbz);
        ycnbz = dialog.findViewById(R.id.ycnbz);
        pp = dialog.findViewById(R.id.pp);
        cc = dialog.findViewById(R.id.cc);
        ccxf = dialog.findViewById(R.id.ccxf);
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(this);
        dialog.findViewById(R.id.tv_sure).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dialog.dismiss();
                break;
            case R.id.tv_sure:
                List<Machine.Type> types = new ArrayList<>();
                name = etName.getText().toString();
                if (name.isEmpty()) {
                    MyToast.show(getApplicationContext(), "请输入配件名称");
                    return;
                }
                if (ycbz.isChecked()){
                    Machine.Type type = new Machine.Type();
                    type.setType("原厂带包装");
                    types.add(type);
                }
                if (ycnbz.isChecked()){
                    Machine.Type type = new Machine.Type();
                    type.setType("原厂不带包装");
                    types.add(type);
                }
                if (pp.isChecked()){
                    Machine.Type type = new Machine.Type();
                    type.setType("品牌");
                    types.add(type);
                }
                if (cc.isChecked()){
                    Machine.Type type = new Machine.Type();
                    type.setType("纯拆件");
                    types.add(type);
                }
                if (ccxf.isChecked()){
                    Machine.Type type = new Machine.Type();
                    type.setType("纯拆修复");
                    types.add(type);
                }
                dialog.dismiss();
                Machine machine = new Machine();
                machine.setName(name);
                machine.setList(types);
                list.add(machine);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    //打开相册
    @Override
    public void openPic() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 100);
                return;
            }
        }
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, Constant.PIC_CODE);
    }

    //打开相机
    @Override
    public void openCamera() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 100);
                return;
            }
        }
        pictureFile = FileUtil.createImageFile(this);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, pictureFile.getAbsolutePath());
            uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        } else {
            uri = Uri.fromFile(pictureFile);
        }
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(cameraIntent, Constant.CAMERA_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.PIC_CODE://相册
                if (data != null) {
                    String imgUrl;
                    Log.w("imgurl", "onActivityResult:相册 " + data.getData().toString());
                    ContentResolver resolver = getContentResolver();
                    try {
                        InputStream inputStream = resolver.openInputStream(data.getData());
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        Cursor cursor = getContentResolver().query(data.getData(),
                                new String[]{MediaStore.Images.ImageColumns.DATA},//
                                null, null, null);
                        if (cursor == null) {
                            imgUrl = data.getData().getPath();
                        } else {
                            cursor.moveToFirst();
                            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                            imgUrl = cursor.getString(index);
                            cursor.close();
                        }
                        uploadImg(imgUrl,imgType);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Constant.CAMERA_CODE://相机
                uploadImg(pictureFile.getPath(),imgType);
                break;
        }
    }

}
