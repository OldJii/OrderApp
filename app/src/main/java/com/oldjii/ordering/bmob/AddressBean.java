package com.oldjii.ordering.bmob;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 收货地址Bmob表
 */

public class AddressBean extends BmobObject implements Serializable {
    public String userId;
    public String name;
    public String mobile;
    public String province;
    public String city;
    public String area;
    public String detailAddress;

}
