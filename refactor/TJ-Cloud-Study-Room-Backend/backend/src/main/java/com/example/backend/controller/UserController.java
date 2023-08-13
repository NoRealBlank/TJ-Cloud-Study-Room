package com.example.backend.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.Result;
import com.example.backend.entity.User;
import com.example.backend.mapper.UserMapper;
import com.example.backend.service.UserService;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Function;

@RestController
@RequestMapping()

public class UserController {
    @Resource
    UserMapper userMapper;
    @Resource
    UserService userService;

    @PostMapping("/admin/user")          /* 实现用户新增 */
    public Result<?> save(@RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping("/admin/user")                      /* 实现用户更新 */
    public Result<?> updateByAdmin(@RequestBody User user) {
        return userService.updateByAdmin(user);
    }

    @DeleteMapping("/admin/user/{uid}")             /* 实现用户删除 */
    public Result<?> delete(@PathVariable Integer uid) {
        return userService.delete(uid);
    }

    @GetMapping("/admin/user")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "") String search) {
        return userService.findPage(pageNum, pageSize, search);
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody User user) {
        return userService.login(user);
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        return userService.register(user);
    }

    @PutMapping("/userCenter/userProfile")                      /* 实现用户更新 */
    public Result<?> updateByUser(@RequestBody User user) {
        return userService.updateByUser(user);
    }

    @GetMapping("/userCenter/userProfile")
    public Result<?> findUser(@RequestParam(defaultValue = "") String search) {
        return userService.findUser(search);
    }

    @PostMapping ("/checkLoginTime")
    public Result<?> checkTime(@RequestBody User user) {
        return userService.checkTime(user);
    }

    @GetMapping("/checkPhoneNumber")
    public Result<?> checkPhoneNumber(@RequestParam(defaultValue = "") Long phoneNumber) {
        return userService.checkPhoneNumber(phoneNumber);
    }

    @PutMapping("/modifyPwd")
    public Result<?> modifyPwd(@RequestParam(defaultValue = "") Long phoneNumber,
                              @RequestParam(defaultValue = "") String pwd) {
        return userService.modifyPwd(phoneNumber ,pwd);
    }

    // 登出
    @PutMapping("/header/logOut/{uid}")
    public Result<?> userLogOut(@PathVariable Integer uid) {
        return userService.userLogOut(uid);
    }
}
