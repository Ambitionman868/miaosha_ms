package com.imooc.miaosha.redis;

public class MiaoShaUserKey extends BasePrefix {


    public static final int TOKEN_EXPIRE = 3600*24 * 2;

    public static MiaoShaUserKey token = new MiaoShaUserKey(TOKEN_EXPIRE, "tk");

    public MiaoShaUserKey(String prefix) {
        super(prefix);
    }

    public MiaoShaUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }


}
