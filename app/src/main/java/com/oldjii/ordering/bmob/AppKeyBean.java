package com.oldjii.ordering.bmob;

import cn.bmob.v3.BmobObject;

/**
 * 支付相关Bmob表
 *
 * 存储微信/支付宝支付uid key值（目前版本已经跳过这一步）
 *
 * 注：此表在后台手动添加
 */
public class AppKeyBean extends BmobObject {
    public String appkey;
}
