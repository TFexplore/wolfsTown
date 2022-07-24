package com.example.wolfstown.easyhttp.model;


import androidx.annotation.NonNull;

import com.hjq.http.config.IRequestServer;
import com.hjq.http.model.BodyType;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2020/10/02
 *    desc   : 服务器配置
 */
public class HttpServer implements IRequestServer {

    final String host="http://table.ethreal.cn/";

    @NonNull
    @Override
    public String getHost() {
        return host;
    }
    @NonNull
    @Override
    public BodyType getBodyType() {
        // 参数以 Json 格式提交（默认是表单）
        return BodyType.JSON;
    }

}