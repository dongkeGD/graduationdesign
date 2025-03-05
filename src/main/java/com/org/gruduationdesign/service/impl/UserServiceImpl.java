package com.org.gruduationdesign.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.org.gruduationdesign.constant.UserConstant;
import com.org.gruduationdesign.exception.BusinessException;
import com.org.gruduationdesign.exception.ErrorCode;
import com.org.gruduationdesign.exception.ThrowUtils;
import com.org.gruduationdesign.model.dto.user.UserLoginRequest;
import com.org.gruduationdesign.model.dto.user.UserRegisterRequest;
import com.org.gruduationdesign.model.entity.LoginUserVO;
import com.org.gruduationdesign.model.entity.User;
import com.org.gruduationdesign.model.enums.UserRoleEnum;
import com.org.gruduationdesign.service.UserService;
import com.org.gruduationdesign.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

/**
* @author dongke
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-02-23 19:05:55
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Override
    public long userRegister(UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        // 1. 参数校验
        ThrowUtils.throwIf(StrUtil.hasBlank(userAccount, userPassword, checkPassword),
                ErrorCode.PARAMS_ERROR, "参数为空");
        ThrowUtils.throwIf(userAccount.length() < 4,
                ErrorCode.PARAMS_ERROR, "用户名过短");
        ThrowUtils.throwIf(userPassword.length() < 8,
                ErrorCode.PARAMS_ERROR, "用户密码过短");
        ThrowUtils.throwIf(!userPassword.equals(checkPassword),
                ErrorCode.PARAMS_ERROR, "两次输入密码不一致");
        // 2.检查用户账号是否与数据库中已有的账号不一致
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.baseMapper.selectCount(queryWrapper);
        ThrowUtils.throwIf(count > 0, ErrorCode.PARAMS_ERROR, "账号重复");
        // 3.密码加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 4.插入数据库中
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName("无名");
        user.setUserRole(UserRoleEnum.USER.getValue());
        try{
            boolean saveResult = this.save(user);
            ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }catch(BusinessException e){
            log.error(e.getMessage(),e);
        }
        // id会自动回填
        return user.getId();
    }

    @Override
    public LoginUserVO userLogin(UserLoginRequest userLoginRequest) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        // 1.参数校验
        ThrowUtils.throwIf(StrUtil.hasBlank(userAccount, userPassword),
                ErrorCode.PARAMS_ERROR, "请求参数为空");
        ThrowUtils.throwIf(userAccount.length() < 4,
                ErrorCode.PARAMS_ERROR, "用户名小于4位");
        ThrowUtils.throwIf(userPassword.length() < 8,
                ErrorCode.PARAMS_ERROR, "用户密码小于8位");
        // 2.密码加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 3.查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", userPassword);
        // 不存在抛出异常
        User user = this.baseMapper.selectOne(queryWrapper);
        ThrowUtils.throwIf(user == null,
                ErrorCode.PARAMS_ERROR, "用户不存在或者密码错误");
        // 4.保存用户登录态
        userLoginRequest.getHttpServletRequest().getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }



    @Override
    public User getCurrentUser(HttpServletRequest httpServletRequest){
        Object objUser = httpServletRequest.getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) objUser;
        ThrowUtils.throwIf(currentUser == null, ErrorCode.NOT_LOGIN_ERROR);
        // 再从数据库查一遍，确保一致性，如果追求性能可以不查
        Long userId = currentUser.getId();
        currentUser = this.getById(userId);
        ThrowUtils.throwIf(currentUser == null, ErrorCode.NOT_LOGIN_ERROR);
        return currentUser;
    }

    /**
     * 获取加密后的密码
     *
     * @param userPassword 用户密码
     * @return 加密后的密码
     */
    @Override
    public String getEncryptPassword(String userPassword) {
        // 加盐，混淆密码
        final String SALT = "dongke";
        return DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    }

    /**
     * 获取脱敏类用户信息
     * @param user 未脱敏的user对象
     * @return 脱敏后的用户信息
     */
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return  null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    /**
     * 获取当前登录用户
     * @param httpServletRequest http请求对象
     * @return 已经登录的用户
     */
    @Override
    public User getLoginUser(HttpServletRequest httpServletRequest) {
        // 判断是否已经登录
        Object objUser = httpServletRequest.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) objUser;
        ThrowUtils.throwIf(currentUser == null || currentUser.getId() == null,
                ErrorCode.NOT_LOGIN_ERROR);
        // 从数据库中查询（追求性能的话可以注释，直接返回上述结果）
        Long userId = currentUser.getId();
        currentUser = this.getById(userId);
        ThrowUtils.throwIf(currentUser == null, ErrorCode.NOT_LOGIN_ERROR);
        return currentUser;
    }

    /**
     * 用户登出
     * @param httpServletRequest 请求
     * @return true / false
     */
    @Override
    public boolean userLogout(HttpServletRequest httpServletRequest) {
        // 判断用户是否登录
        Object user = httpServletRequest.getAttribute(UserConstant.USER_LOGIN_STATE);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_LOGIN_ERROR);
        httpServletRequest.removeAttribute(UserConstant.USER_LOGIN_STATE);
        return true;
    }
}




