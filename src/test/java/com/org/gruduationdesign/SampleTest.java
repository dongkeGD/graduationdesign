package com.org.gruduationdesign;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.org.gruduationdesign.mapper.UserMapper;
import com.org.gruduationdesign.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class SampleTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", "123456");
        long count = userMapper.selectCount(queryWrapper);
        Assert.isTrue(1 == count, "");
    }

}