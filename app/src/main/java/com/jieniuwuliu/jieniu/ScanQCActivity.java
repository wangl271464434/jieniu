package com.jieniuwuliu.jieniu;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.JwtUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.util.RegularUtil;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.mylhyl.zxing.scanner.OnScannerCompletionListener;
import com.mylhyl.zxing.scanner.ScannerView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanQCActivity extends BaseActivity implements OnScannerCompletionListener {

    @BindView(R.id.scanner)
    ScannerView scanner;
    private String token;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan_qc;
    }

    @Override
    protected void init() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            }
        }
        token = (String) SPUtil.get(this, Constant.TOKEN, Constant.TOKEN, "");
        scanner.setOnScannerCompletionListener(this);
    }

    @Override
    protected void onResume() {
        scanner.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        scanner.onPause();
        super.onPause();
    }

    @Override
    public void onScannerCompletion(Result rawResult, ParsedResult parsedResult, Bitmap barcode) {
        String s = parsedResult.toString();
        s = s.replace("\r\n", "");
        if (RegularUtil.isNum(s)) {
//            bindOrder(s);
        } else {
            MyToast.show(this, s);
            finish();
        }
    }

    /**
     * 扫码绑定订单
     */
    private void bindOrder(String result) {
        try {
            JSONObject object = new JSONObject();
            object.put("kuaidiStatus", true);
            object.put("kuaidiID", Integer.valueOf(JwtUtil.JWTParse(token)));
            String json = object.toString();
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).updateOrder(result, body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        switch (response.code()) {
                            case 200:
                                MyToast.show(getApplicationContext(), "接单成功");
                                finish();
                                break;
                            case 400:
                                String s = response.errorBody().string();
                                Log.w("result", s);
                                JSONObject object = new JSONObject(s);
                                MyToast.show(getApplicationContext(), object.getString("msg"));
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    MyToast.show(getApplicationContext(), "网络错误，请重试");
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
