package com.org.gruduationdesign.common;

import com.org.gruduationdesign.model.entity.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResultUtilsMain {
    public static void main(String[] args) {
        User user = new User();
        BaseResponse<User> bsr = ResultUtils.sucess(user);
        System.out.printf(bsr.toString());

    }
}
