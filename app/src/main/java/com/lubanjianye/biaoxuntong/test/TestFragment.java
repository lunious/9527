package com.lubanjianye.biaoxuntong.test;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment;
import com.lubanjianye.biaoxuntong.net.RestClient;
import com.lubanjianye.biaoxuntong.net.callback.ISuccess;
import com.lubanjianye.biaoxuntong.util.aes.AesUtil;

import okhttp3.Headers;

/**
 * 项目名:   9527
 * 包名:     com.lubanjianye.biaoxuntong.test
 * 文件名:   TestFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/22  9:05
 * 描述:     TODO
 */

public class TestFragment extends BaseFragment implements View.OnClickListener {

    private Button btn;

    @Override
    public Object setLayout() {
        return R.layout.test_fragment;
    }

    @Override
    public void initView() {
        btn = getView().findViewById(R.id.btn_test);
        btn.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_test:
//                String content = "test";
//                String password = "老子不死你  1";
//                String jiamiresult = AesUtil.aesEncrypt(content, password);
//                Log.i("tag", "加密结果" + jiamiresult);
//
//                String test = "cxLwNyMXvgOuWOCYgDBuWE/O/AP88e5hDdq7GBydLqk8+8Nw8VnbPkGO+8Fn1H8r";
//                String jiemi = AesUtil.aesDecrypt(test, password);
//
//                Log.i("tag", "解密结果" + jiemi);

                RestClient.builder()
                        .url("http://api.lubanjianye.com/bxtajax/Entryajax/getEntryListSer")
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(Headers headers, String response) {
                                String password = "老子不死你1";
                                String jiemi = AesUtil.aesDecrypt(response, password);

                                Log.d("OIHASJIHDBIJHSAIJDB", response);

                                Log.d("OIHASJIHDBIJHSAIJDB", jiemi);
                            }
                        })
                        .build()
                        .post();
                break;
            default:
                break;
        }
    }
}
