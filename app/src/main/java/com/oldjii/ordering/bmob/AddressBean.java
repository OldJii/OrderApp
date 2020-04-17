package com.oldjii.ordering.bmob;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 收货地址Bmob表
 */

public class AddressBean extends BmobObject implements Serializable {
    public String userId;
    public String name, mobile, province, city, area, detailAddress;

}
