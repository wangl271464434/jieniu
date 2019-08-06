package com.jieniuwuliu.jieniu.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.FileUtil;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.UpLoadFileUtil;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.Machine;
import com.jieniuwuliu.jieniu.bean.VinCar;
import com.jieniuwuliu.jieniu.bean.XJImg;
import com.jieniuwuliu.jieniu.home.adapter.XjAddMachieAdapter;
import com.jieniuwuliu.jieniu.mine.ui.AddPicActivity;
import com.jieniuwuliu.jieniu.view.PicDialog;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private AlertDialog dialog;
    private List<Machine> list;
    private XjAddMachieAdapter adapter;
    private EditText etName;
    private String name,type = "";
    private VinCar.Data data;
    private int imgType;
    private XJImg xjImg;
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
        data = (VinCar.Data) getIntent().getSerializableExtra("data");
        if (data!=null){
            GlideUtil.setImgUrl(this,data.getLogos(),img);
            tvName.setText(data.getCartype());
        }
        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new XjAddMachieAdapter(this,list);
        recyclerView.setAdapter(adapter);
    }
    @OnClick({R.id.layout_back,R.id.tv_add, R.id.img1, R.id.img2, R.id.img3, R.id.btn})
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

                addXJOrder();
                break;
        }
    }

    private void uploadImg(String path) {
        COSXMLUploadTask storeTask =  UpLoadFileUtil.getIntance(this).upload("img",new File(path).getName(),path);
        storeTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Log.w("返回结果","Success: " +result.accessUrl);
                switch (imgType){
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.w("返回结果", "Failed: " + (exception == null ? serviceException.getMessage() : exception.toString()));
                MyToast.show(getApplicationContext(),(exception == null ? serviceException.getMessage() : exception.toString()));
            }
        });
    }

    private void showPicDialog() {
         new PicDialog(this,this);
    }

    //生成询价单
    private void addXJOrder() {
    }

    private void showAddDialog() {
        dialog = new AlertDialog.Builder(this).create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.show();
        dialog.setContentView(R.layout.dialog_xj_add);
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        etName = dialog.findViewById(R.id.et_name);
        dialog.findViewById(R.id.yuding_btn).setOnClickListener(this);
        dialog.findViewById(R.id.yuanjian_btn).setOnClickListener(this);
        dialog.findViewById(R.id.fujian_btn).setOnClickListener(this);
        dialog.findViewById(R.id.chaijian_btn).setOnClickListener(this);
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(this);
        dialog.findViewById(R.id.tv_sure).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.yuding_btn:
                type = "接受预订";
                break;
            case R.id.yuanjian_btn:
                type = "原厂件";
                break;
            case R.id.fujian_btn:
                type = "副厂件";
                break;
            case R.id.chaijian_btn:
                type = "拆车件";
                break;
            case R.id.tv_cancel:
                dialog.dismiss();
                break;
            case R.id.tv_sure:
                name = etName.getText().toString();
                if (name.isEmpty()){
                    MyToast.show(getApplicationContext(),"请输入配件名称");
                    return;
                }
                dialog.dismiss();
                Machine machine = new Machine();
                machine.setName(name);
                machine.setType(type);
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
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,permissions,100);
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
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,permissions,100);
                return;
            }
        }
        pictureFile = FileUtil.createImageFile(this);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, pictureFile.getAbsolutePath());
            uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
        }else {
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
                if (data!=null){
                    String imgUrl;
                    Log.w("imgurl", "onActivityResult:相册 " + data.getData().toString());
                    ContentResolver resolver = getContentResolver();
                    try {
                        InputStream inputStream = resolver.openInputStream(data.getData());
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        Cursor cursor = getContentResolver().query(data.getData(),
                                new String[]{MediaStore.Images.ImageColumns.DATA},//
                                null, null, null);
                        if (cursor == null){
                            imgUrl = data.getData().getPath();
                        }else {
                            cursor.moveToFirst();
                            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                            imgUrl = cursor.getString(index);
                            cursor.close();
                        }
                        uploadImg(imgUrl);
                        switch (imgType){
                            case 1:
                                img1.setImageBitmap(bitmap);
                                break;
                            case 2:
                                img2.setImageBitmap(bitmap);
                                break;
                            case 3:
                                img3.setImageBitmap(bitmap);

                                break;
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Constant.CAMERA_CODE://相机
                uploadImg(pictureFile.getPath());
                switch (imgType){
                    case 1:
                        GlideUtil.setLocalImgUrl(XjInfoActivity.this,pictureFile.getPath(),img1);
                        break;
                    case 2:
                        GlideUtil.setLocalImgUrl(XjInfoActivity.this,pictureFile.getPath(),img2);
                        break;
                    case 3:
                        GlideUtil.setLocalImgUrl(XjInfoActivity.this,pictureFile.getPath(),img3);
                        break;
                }
                break;
        }
    }
}
