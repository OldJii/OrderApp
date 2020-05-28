package com.oldjii.ordering.bmob;

import cn.bmob.v3.BmobObject;

/**
 * 商家分类Bmob表
 *
 * 注：此表在后台手动添加
 */
public class ShopCateBean extends BmobObject {
    public String name;//商家分类名称

    public ShopCateBean(String name) {
        this.name = name;
    }
}
