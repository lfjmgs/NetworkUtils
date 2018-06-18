package com.lfjmgs.networkutils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.lfjmgs.networkutils.net.BaseErrorConsumer;
import com.lfjmgs.networkutils.net.LoadingTransformer;
import com.lfjmgs.networkutils.net.RetrofitUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity implements LoadingTransformer.LoadingCallback {

    public static final String TAG = "MainActivity";

    private ApiService mService;

    private CompositeDisposable mDisposables = new CompositeDisposable();
    private Button mBtnGetIPInfo;
    private Button mBtnApiErr;
    private TextView mTvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnGetIPInfo = findViewById(R.id.btn_get_ip_info);
        mBtnApiErr = findViewById(R.id.btn_api_err);
        mTvInfo = findViewById(R.id.tv_info);

        mService = RetrofitUtils.createService(ApiService.class);
        mBtnGetIPInfo.setOnClickListener(v -> {
            mDisposables.add(mService.getIPInfo()
                    .compose(new LoadingTransformer<>(this)) // 加loading
                    .subscribe(info -> {
                        String str = String.format("IP：%s\n城市：%s\n运营商：%s", info.getIp(),
                                info.getCity(), info.getIsp());
                        mTvInfo.setText(str);
                    }, new BaseErrorConsumer(this))
            );
        });

        mBtnApiErr.setOnClickListener(v -> {
            mDisposables.add(mService.apiErr()
                    .compose(new LoadingTransformer<>(this)) // 加loading
                    .ignoreElements() // 不关心data字段，只需要知道成功与否可以忽略发射的数据
                    .subscribe(() -> {

                    }, new BaseErrorConsumer(this))
            );
        });


    }

    @Override
    public void showLoading(Disposable disposable) {
        LoadingDialog.show(this);
    }

    @Override
    public void dismissLoading() {
        LoadingDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        mDisposables.dispose();
        super.onDestroy();
    }
}
