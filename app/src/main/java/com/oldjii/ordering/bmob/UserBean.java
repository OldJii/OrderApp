package com.oldjii.ordering.bmob;

import cn.bmob.v3.BmobUser;

/**
 * 用户Bmob表
 */
public class UserBean extends BmobUser {
    public String logo;//用户头像
    public String motto;//座右铭
    public String nickname;
    public String plat_type;//第三方登陆类型 wechat qq
    public String openid;//第三方登陆的openid
    public String three_nickname;//第三方登陆的昵称

    //继承自BmobUser，省略了很多字段，比如username、password、mobilePhoneNumber

    public UserBean() {
    }
}
