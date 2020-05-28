package com.oldjii.ordering.bmob;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 菜品Bmob表
 */
public class FoodBean extends BmobObject implements Serializable {
    public String shopId;//商家id
    public String cateId;//菜品分类id
    public String cateName;//菜品分类名称
    public String name;//菜品名称
    public String price;//价格
    public String imgs;//图片 字符串数组形式

    public int total;
    public int status = 1;//1上架 0下架

    public FoodBean(String shopId, String cateId, String cateName, String name, String price, String imgs) {
        this.shopId = shopId;
        this.cateId = cateId;
        this.cateName = cateName;
        this.name = name;
        this.price = price;
        this.imgs = imgs;
    }

    public FoodBean() {
    }
}
