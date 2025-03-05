package com.org.gruduationdesign.controller;

import com.org.gruduationdesign.common.BaseResponse;
import com.org.gruduationdesign.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MainController {

    @GetMapping("/health")
    public BaseResponse<String> health() {
        return ResultUtils.sucess("ok");
    }
}
