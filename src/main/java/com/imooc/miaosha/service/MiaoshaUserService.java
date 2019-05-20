package com.imooc.miaosha.service;


import com.imooc.miaosha.dao.MiaoshaUserDao;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.exception.GlobalException;
import com.imooc.miaosha.redis.MiaoShaUserKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.util.MD5Util;
import com.imooc.miaosha.util.UUIDUtil;
import com.imooc.miaosha.vo.LoginVo;
import com.sun.tools.corba.se.idl.StringGen;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {

    public static final String COOKI_NAME_TOKEN = "token";

    @Autowired
    private RedisService redisService;

    @Autowired
    private MiaoshaUserDao miaoshaUserDao;

    public MiaoshaUser getById(long id) {

        return miaoshaUserDao.getById(id);
    }


    public MiaoshaUser getByToken(HttpServletResponse response, String token) {

        if (StringUtils.isEmpty(token)) return null;

        MiaoshaUser user = redisService.get(MiaoShaUserKey.token, token, MiaoshaUser.class);

        //延长有效期
        if (user != null) {
            addCookie(response, token, user);
        }

        return user;


    }

    public String login(HttpServletResponse response, LoginVo loginVo) {

        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }

        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();

        //判断手机号是否存在
        MiaoshaUser user = miaoshaUserDao.getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();

        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        //生成cookie
        String token = UUIDUtil.uuid();

        addCookie(response, token, user);


        return token;


    }


    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        redisService.set(MiaoShaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoShaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);

    }


}
