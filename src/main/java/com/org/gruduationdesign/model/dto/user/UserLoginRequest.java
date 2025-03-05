package com.org.gruduationdesign.model.dto.user;

import cn.hutool.http.server.HttpServerRequest;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Getter
public class UserLoginRequest implements Serializable {


    private static final long serialVersionUID = -266641500925315332L;
    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 请求对象，用于存放session
     */
    private HttpServletRequest httpServletRequest;
}
