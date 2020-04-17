package com.oldjii.ordering.bmob;

import cn.bmob.v3.BmobObject;

/**
 * 订单评价Bmob表
 */

public class CommentBean extends BmobObject {
    public String shopId, orderId, userId;
    public String content;//内容
    public String imgs;//图片
    public String score;//评分
    public boolean isInonymous;//是否匿名评论

    public String userPortrait;//用户头像
    public String userName;//用户名称

    public boolean isPass;//评论是否通过(用于后台审核操作)
}
