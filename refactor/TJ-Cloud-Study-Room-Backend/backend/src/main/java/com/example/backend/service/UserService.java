package com.example.backend.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.Result;
import com.example.backend.entity.User;
import com.example.backend.mapper.RoomMapper;
import com.example.backend.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
@Service
public class UserService {
    @Resource
    RoomMapper roomMapper;
    @Resource
    UserMapper userMapper;
    /* 实现用户新增 */
    public Result<?> save(@RequestBody User user) {

        if (user.getPwd() == null)
            user.setPwd("123456");
        if (user.getEducationLevel() == null)
            user.setEducationLevel("未知");
        if (user.getGrade() == null)
            user.setGrade("未知");
        if (user.getMailAddress() == null)
            user.setMailAddress("未知");
        if (user.getMajor() == null)
            user.setMajor("未知");
        if (user.getSex() == null)
            user.setSex("未知");
        if (user.getPhoneNumber() == null)
            user.setPhoneNumber(0L);
        if (user.getOnlineState() == null)
            user.setOnlineState(0);
        if (user.getRoomId() == null)
            user.setRoomId(0);

        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUserName, user.getUserName()));
        if (res != null) {
            return Result.error("-2", "用户名重复");
        }

        userMapper.insert(user);
        return Result.success();
    }

    /* 实现用户更新 */
    public Result<?> updateByAdmin(@RequestBody User user) {
        userMapper.updateById(user);
        return Result.success();
    }

    /* 实现用户删除 */
    public Result<?> delete(@PathVariable Integer uid) {
        userMapper.deleteById(uid);
        return Result.success();
    }

    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(User::getUserName, search);
        }
        Page<User> userPage = userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(userPage);
    }

    public Result<?> login(@RequestBody User user) {
        User res;

        if(user.getUserName().length() > 0) {
            res = userMapper.selectOne(
                    Wrappers.<User>lambdaQuery().eq(User::getUserName, user.getUserName()).eq(User::getPwd, user.getPwd())
            );

            if (res == null) {
                return Result.error("-1", "用户名或密码错误");
            }
        }
        else {
            res = userMapper.selectOne(
                    Wrappers.<User>lambdaQuery().eq(User::getPhoneNumber, user.getPhoneNumber()).eq(User::getPwd, user.getPwd())
            );

            if (res == null) {
                return Result.error("-1", "手机号或密码错误");
            }
        }

        UpdateWrapper<User> op = new UpdateWrapper<User>();
        op.eq("uid",res.getUid());

        op.set("online_state", 1);
        op.set("login_time", user.getLoginTime());

        userMapper.update(null, op);

        res.setLoginTime(user.getLoginTime());

        return Result.success(res);
    }

    public Result<?> register(@RequestBody User user) {

        if (user.getEducationLevel() == null)
            user.setEducationLevel("未知");
        if (user.getGrade() == null)
            user.setGrade("未知");
        if (user.getMailAddress() == null)
            user.setMailAddress("未知");
        if (user.getMajor() == null)
            user.setMajor("未知");
        if (user.getSex() == null)
            user.setSex("未知");
        if (user.getPhoneNumber() == null)
            user.setPhoneNumber(0L);
        if (user.getOnlineState() == null)
            user.setOnlineState(0);
        if (user.getRoomId() == null)
            user.setRoomId(0);

        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUserName, user.getUserName()));

        if (res != null) {
            return Result.error("-2", "用户名重复");
        }

        userMapper.insert(user);

        return Result.success();
    }

    /* 实现用户更新 */
    public Result<?> updateByUser(@RequestBody User user) {
        userMapper.updateById(user);
        return Result.success();
    }

    public Result<?> findUser(@RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.eq(User::getUserName, search);
        }

        User res = userMapper.selectOne(wrapper);

        if (res == null) {
            return Result.error("-1", "用户名不存在");
        }

        return Result.success(res);
    }

    public Result<?> checkTime(@RequestBody User user) {
        User res = userMapper.selectOne(
                Wrappers.<User>lambdaQuery().eq(User::getUid, user.getUid()).eq(User::getLoginTime, user.getLoginTime())
        );

        if (res == null) {
            return Result.error("-1", "此帐号在另一处登陆，您被强制下线！");
        }

        return Result.success();
    }

    public Result<?> checkPhoneNumber(@RequestParam(defaultValue = "") Long phoneNumber) {
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getPhoneNumber, phoneNumber));

        if (res == null) {
            return Result.error("-1", "此手机号未注册！");
        }

        return Result.success(res);
    }

    public Result<?> modifyPwd(@RequestParam(defaultValue = "") Long phoneNumber,
                               @RequestParam(defaultValue = "") String pwd) {

        UpdateWrapper<User> user = new UpdateWrapper<User>();

        user.eq("phone_number", phoneNumber);
        user.set("pwd", pwd);

        userMapper.update(null, user);

        return Result.success();
    }

    // 登出
    public Result<?> userLogOut(@PathVariable Integer uid) {
        UpdateWrapper<User> user = new UpdateWrapper<User>();
        user.eq("uid", uid);
        user.set("online_state", 0);
        user.set("room_id", 0);
        userMapper.update(null, user);
        return Result.success();
    }

}