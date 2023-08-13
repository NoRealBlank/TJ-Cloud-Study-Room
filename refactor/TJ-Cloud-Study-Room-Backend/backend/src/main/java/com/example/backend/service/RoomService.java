package com.example.backend.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.Result;
import com.example.backend.entity.Room;
import com.example.backend.entity.User;
import com.example.backend.mapper.RoomMapper;
import com.example.backend.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Service
public class RoomService {
    @Resource
    RoomMapper roomMapper;
    @Resource
    UserMapper userMapper;

    public Result<?> adminSaveRoom(Room room){
        if(room.getRoomName() == null)
            return Result.error("-1", "未设置房间名");
        if(room.getMaxNum() == null)
            room.setMaxNum(20);
        if(room.getRoomAnnouncement()==null)
            room.setRoomAnnouncement("无");
        if(room.getRoomPassword()==null)
            room.setRoomPassword("-1");
        room.setStartTime(new Date());
        room.setFixed(0);           //the room added by user is not a fixed room
        room.setPreNum(0);

        roomMapper.insert(room);

        return Result.success();
    }

    public Result<?> adminFindPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                   @RequestParam(defaultValue = "") String search){

        LambdaQueryWrapper<Room> wrapper = Wrappers.<Room>lambdaQuery();
        if(StrUtil.isNotBlank(search)) {
            wrapper.like(Room::getRoomName, search);
        }
        Page<Room> roomPage = roomMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(roomPage);
    }

    public Result<?> adminUpdateRoom(@RequestBody Room room){
        if(room.getRoomName() == null || room.getRoomName() == ""){
            return Result.error("-1", "未设置房间名");
        }

        roomMapper.updateById(room);
        return Result.success();
    }


    public Result<?> adminDeleteRoom(@PathVariable Integer uid){
        roomMapper.deleteById(uid);
        return Result.success();
    }

    //get user num
    public Result<?> getUserNum(){
        int userOnlineNum = userMapper.getUserNum();
        return Result.success(userOnlineNum);
    }

    //user in studyHall add room
    public Result<?> userSaveRoom(@RequestBody Room room){
        if(room.getRoomName() == null)
            return Result.error("-1", "未设置房间名");
        if(room.getMaxNum() == null)
            room.setMaxNum(20);
        if(room.getRoomAnnouncement()==null)
            room.setRoomAnnouncement("");
        if(room.getRoomPassword()==null)
            room.setRoomPassword("-1");
        room.setStartTime(new Date());
        room.setFixed(0);           //the room added by user is not a fixed room
        room.setPreNum(0);

        roomMapper.insert(room);
        return Result.success(room);
    }

    //user in studyHall search room
    public Result<?> userFindPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam(defaultValue = "") String search){

        LambdaQueryWrapper<Room> wrapper = Wrappers.<Room>lambdaQuery();
        if(StrUtil.isNotBlank(search)) {
            wrapper.like(Room::getRoomName, search);
        }
        Page<Room> roomPage = roomMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);

        return Result.success(roomPage);
    }

    //update user info when user enters room
    public Result<?> userEnterRoomUpdateUserInfo(@RequestParam Integer uid,
                                                 @RequestParam Integer rid){
        UpdateWrapper<User> op = new UpdateWrapper<User>();
        op.eq("uid", uid);
        op.set("room_enter_time", new Date());
        op.set("room_id", rid);
        userMapper.update(null, op);

        return Result.success();
    }

    //update room info when user in studyhall enter room
    public Result<?> userEnterRoomUpdateRoomInfo(@RequestParam Integer uid,
                                                 @RequestParam Integer rid){
        UpdateWrapper<Room> op = new UpdateWrapper<Room>();
        op.eq("rid", rid);

        op.setSql("pre_num = pre_num + 1");
        roomMapper.update(null, op);

        return Result.success();
    }

    //user in studyHall enter room
    public Result<?> userEnterRoom(@PathVariable Integer rid,
                                   @PathVariable String psw,
                                   @PathVariable Integer uid) {
        User user=userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUid, uid));
        if(user.getRoomId() != 0)
            return Result.error("-2", "您已在房间中，请先退出房间");

        Room res = roomMapper.selectOne(
                Wrappers.<Room>lambdaQuery().eq(Room::getRid, rid).eq(Room::getRoomPassword, psw)
        );

        if(res == null)
            return Result.error("-1", "房间密码错误");
        else
        {
            userEnterRoomUpdateRoomInfo(uid, rid);
            userEnterRoomUpdateUserInfo(uid, rid);
            res = roomMapper.selectById(rid);

            return Result.success(res);
        }
    }

    //get user and room by uid and rid
    public Result<?> getUserInfoById(@RequestParam Integer uid){
        User user = userMapper.selectById(uid);
        return Result.success(user);
    }
    public Result<?> getUserInfoByName(@RequestParam String userName){
        User user = userMapper.getUserByUname(userName);
        return Result.success(user);
    }

    //get userList by rid
    //未测试
    public Result<?> getUserListByRid(@RequestParam Integer rid){
        List<User> userList = userMapper.selectUidByRid(rid);
        return Result.success(userList);
    }

    //user in study room exit room (王乐宣使用）
    public Result<?> userExitRoom(@RequestParam Integer uid,
                                  @RequestParam Integer rid){
        UpdateWrapper<Room> room = new UpdateWrapper<Room>();
        room.eq("rid", rid);

        if(room!=null){
            room.setSql("pre_num = pre_num - 1");
            roomMapper.update(null, room);
        }

        UpdateWrapper<User> user = new UpdateWrapper<User>();
        user.eq("uid", uid);

        user.set("room_id", 0);
        userMapper.update(null, user);

        return Result.success();
    }

    //获取房间信息
    public  Result<?> getRoomInfo(@RequestParam Integer rid)
    {
        Room room = roomMapper.selectById(rid);

        if(room==null)
            return Result.error("-1", "房间已解散！");

        return Result.success(room);
    }

    public Result<?> userEditRoomInfo(@RequestBody Room room)
    {
        roomMapper.updateById(room);
        return Result.success();
    }

    public Result<?> deleteRoom(@PathVariable Integer rid)
    {
        roomMapper.deleteById(rid);
        return Result.success();
    }
}