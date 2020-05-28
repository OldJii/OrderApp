package com.oldjii.ordering.bmob;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 商家账号信息Bmob表
 */
public class ShopBean extends BmobObject implements Serializable {
    public String name;//店铺名称
    public String tel;//联系方式
    public String username;//店长名称
    public String pass;////密码
    public String createdTime;//创建时间
    public String logo;//商家log
    public String address;//地址
    public String score;//评分
    public String distance;//距离
    public String cateId;//菜品分类id
    public String cateName;//菜品分类名称
    public String lat;//经度
    public String lng;//纬度
    public String hot;//热度
    public boolean isPass = true;//是否审核通过，默认true

    public ShopBean() {
    }

    @Override
    public String toString() {
        return "ShopBean{" +
                "name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", username='" + username + '\'' +
                ", pass='" + pass + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
