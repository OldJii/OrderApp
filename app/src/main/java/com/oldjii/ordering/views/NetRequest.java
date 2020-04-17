package com.oldjii.ordering.views;

import android.os.Build;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.PostRequest;
import com.oldjii.ordering.utils.UIUtils;

import java.io.File;

/**
 * 公共请求头
 */
public class NetRequest {
    public static PostRequest<String> getStringPostRequest(String url) {
        PostRequest<String> request = OkGo.<String>post(url)
                .headers("device-type", "android")
                .headers("x-timestamp", UIUtils.getCurrentSysTimeStamp())
                .headers("system-version", Build.VERSION.RELEASE);
        return request;
    }

    public static PostRequest<File> getFilePostRequest(String url) {
        PostRequest<File> request = OkGo.<File>post(url)
                .headers("device-type", "android")
                .headers("x-timestamp", UIUtils.getCurrentSysTimeStamp())
                .headers("system-version", Build.VERSION.RELEASE);
        return request;
    }
}
