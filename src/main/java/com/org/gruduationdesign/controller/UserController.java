package com.org.gruduationdesign.controller;

import com.org.gruduationdesign.common.BaseResponse;
import com.org.gruduationdesign.common.ResultUtils;
import com.org.gruduationdesign.constant.UserConstant;
import com.org.gruduationdesign.exception.ErrorCode;
import com.org.gruduationdesign.exception.ThrowUtils;
import com.org.gruduationdesign.model.dto.user.UserLoginRequest;
import com.org.gruduationdesign.model.dto.user.UserRegisterRequest;
import com.org.gruduationdesign.model.entity.LoginUserVO;
import com.org.gruduationdesign.model.entity.User;
import com.org.gruduationdesign.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.SYSTEM_ERROR, "请求对象为空");
        long result = userService.userRegister(userRegisterRequest);
        return ResultUtils.sucess(result);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userRegister(@RequestBody UserLoginRequest userLoginRequest) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.SYSTEM_ERROR, "请求对象为空");
        LoginUserVO loginUserVO = userService.userLogin(userLoginRequest);
        return ResultUtils.sucess(loginUserVO);
    }

    /**
     * 获取当前用户
     */
    @GetMapping("/getCurrentUser")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest httpServletRequest) {
        ThrowUtils.throwIf(httpServletRequest == null, ErrorCode.OPERATION_ERROR);
        User loginUser = userService.getCurrentUser(httpServletRequest);
        return ResultUtils.sucess(userService.getLoginUserVO(loginUser));
    }

    @GetMapping("/userLogout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest httpServletRequest) {
        ThrowUtils.throwIf(httpServletRequest == null, ErrorCode.PARAMS_ERROR);
        boolean logoutResult = userService.userLogout(httpServletRequest);
        return ResultUtils.sucess(logoutResult);
    }
}
