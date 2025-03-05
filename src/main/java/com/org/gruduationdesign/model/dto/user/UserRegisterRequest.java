package com.org.gruduationdesign.model.dto.user;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 6930910005589573718L;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 确认密码
     */
    private String checkPassword;
}
