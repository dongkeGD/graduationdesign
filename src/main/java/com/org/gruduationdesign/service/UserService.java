package com.org.gruduationdesign.service;

import com.org.gruduationdesign.constant.UserConstant;
import com.org.gruduationdesign.exception.ErrorCode;
import com.org.gruduationdesign.exception.ThrowUtils;
import com.org.gruduationdesign.model.dto.user.UserLoginRequest;
import com.org.gruduationdesign.model.dto.user.UserRegisterRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.org.gruduationdesign.model.entity.LoginUserVO;
import com.org.gruduationdesign.model.entity.User;
import com.org.gruduationdesign.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author dongke
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-02-23 19:05:55
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册功能
     * @param userRegisterRequest 用户注册的请求封装对象
     * @return 用户id
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录功能
     * @param userLoginRequest 用户注册的请求封装对象
     * @return 用户id
     */
    LoginUserVO userLogin(UserLoginRequest userLoginRequest);

    /**
     * 获取当前用户
     * @param httpServletRequest 用户登录请求对象
     * @return 当前用户对象
     */
    User getCurrentUser(HttpServletRequest httpServletRequest);

    /**
     * 密码做MD5 + SALt 加密
     * @param userPassword 用户密码
     * @return 加密后的字符串
     */
    String getEncryptPassword(String userPassword);

    /**
     * 转化为前端的登录对象视图
     * @param user 未脱名的全量信息对象
     * @return 脱敏后的前端登录视图对象
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取当前登录用户
     * @param httpServletRequest http请求对象
     * @return 当前用户信息对象
     */
    User getLoginUser(HttpServletRequest httpServletRequest);

//11

//    UserVO getUserVO(HttpServletRequest httpServletRequest);


    /**
     * 用户登出
     * @param httpServletRequest 请求
     * @return 是否登录
     */
    boolean userLogout(HttpServletRequest httpServletRequest);
}
