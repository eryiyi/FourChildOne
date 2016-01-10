package com.xiaogang.Mine.base;

/**
 * Created by liuzwei on 2015/1/12.
 */
public class InternetURL {
    public static final String INTERNAL = "http://yey.xqb668.com/";

    //1 获得秘钥
    public static final String GET_TOKEN_URL = INTERNAL + "json.php/user.api-authkey";
    //2多媒体文件获取
    public static final String UPLOAD_FILE_URL = INTERNAL + "json.php/user.api-uploadfile/";
    //3注册
    public static final String REG_URL = INTERNAL + "json.php/user.api-regist/";
    //4发送验证码
    public static final String REG_CARD_URL = INTERNAL + "json.php/user.api-sendCode";
    //5登陆
    public static final String LOGIN_URL = INTERNAL + "json.php/user.api-login";
    //6第三方登陆
    public static final String LOGIN_OTHER_URL = INTERNAL + "json.php/user.api-otherlogin";
    //7找回密码
    public static final String FIND_PWR_URL = INTERNAL + "json.php/user.api-findpasswd";
    //8修改密码
    public static final String UPDATE_PWR_URL = INTERNAL + "json.php/member.api-updatepasswd";
    //9获取会员信息
    public static final String GET_MEMBER_URL = INTERNAL + "json.php/member.api-getBaseInfonByUid";
    //10更新会员个人信息
    public static final String UPDATE_MEMBER_URL = INTERNAL + "json.php/member.api-updateBaseInfoByUid";
    //11轮播图
    public static final String GET_AD_URL = INTERNAL + "json.php/community.api-indexad";
    //12分类列表
    public static final String GET_TYPE_URL = INTERNAL + "json.php/product.api-types";
    //13商城主页
    public static final String GET_SHOP_INDEX_URL = INTERNAL + "json.php/shop.api-index";
    //14商品列表
    public static final String GET_SHOP_PRODUCT_URL = INTERNAL + "json.php/product.api-productList";
    //15产品详情
    public static final String GET_SHOP_PRODUCT_DETAIL_URL = INTERNAL + "json.php/product.api-detail";
    //16下订单
    public static final String SET_ORDER_URL = INTERNAL + "json.php/order.api-order";
    //17取消订单
    public static final String CANCEL_ORDER_URL = INTERNAL + "json.php/order.api-ordercancel";
    //18订单列表
    public static final String LIST_ORDER_URL = INTERNAL + "json.php/order.api-orderlist";
    //19我的关注
    public static final String MINE_GUANZHU_URL = INTERNAL + "json.php/member.api-myfollow";
    //
//    public static final String MINE_GUANZHU_URL = INTERNAL + "json.php/member.api-myfollow";


}
