package com.example.backend.controller;

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
import com.example.backend.service.RoomService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping()
public class RoomController {
    @Resource
    RoomMapper roomMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    RoomService roomService;
    //admin add room
//    @PostMapping("/admin/room")
//    public Result<?> adminSaveRoom(@RequestBody Room room){
//        if(room.getRoomName() == null)
//            return Result.error("-1", "未设置房间名");
//        if(room.getMaxNum() == null)
//            room.setMaxNum(20);
//        if(room.getRoomAnnouncement()==null)
//            room.setRoomAnnouncement("无");
//        if(room.getRoomPassword()==null)
//            room.setRoomPassword("-1");
//        room.setStartTime(new Date());
//        room.setFixed(0);           //the room added by user is not a fixed room
//        room.setPreNum(0);
//
//        roomMapper.insert(room);
//        return Result.success();
//    }
    //admin add room 重构代码
    @PostMapping("/admin/room")
    public Result<?> adminSaveRoom(@RequestBody Room room){
//        if(room.getRoomName() == null)
//            return Result.error("-1", "未设置房间名");
//        if(room.getMaxNum() == null)
//            room.setMaxNum(20);
//        if(room.getRoomAnnouncement()==null)
//            room.setRoomAnnouncement("无");
//        if(room.getRoomPassword()==null)
//            room.setRoomPassword("-1");
//        room.setStartTime(new Date());
//        room.setFixed(0);           //the room added by user is not a fixed room
//        room.setPreNum(0);
//
//        roomMapper.insert(room);
        return roomService.adminSaveRoom(room);
    }
    //admin search room
//    @GetMapping("/admin/room")
//    public Result<?> adminFindPage(@RequestParam(defaultValue = "1") Integer pageNum,
//                                   @RequestParam(defaultValue = "10") Integer pageSize,
//                                   @RequestParam(defaultValue = "") String search){
//
//        LambdaQueryWrapper<Room> wrapper = Wrappers.<Room>lambdaQuery();
//        if(StrUtil.isNotBlank(search)) {
//            wrapper.like(Room::getRoomName, search);
//        }
//        Page<Room> roomPage = roomMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
//        return Result.success(roomPage);
//    }
    //admn search room 重构代码
    @GetMapping("/admin/room")
    public Result<?> adminFindPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                   @RequestParam(defaultValue = "") String search){
        return roomService.adminFindPage(pageNum,pageSize,search);
    }

    //admin update room info(edit room info)
//    @PutMapping("/admin/room")
//    public Result<?> adminUpdateRoom(@RequestBody Room room){
//        if(room.getRoomName() == null || room.getRoomName() == ""){
//            return Result.error("-1", "未设置房间名");
//        }
//        roomMapper.updateById(room);
//        return Result.success();
//    }
    //admin update room info 重构代码
    @PutMapping("/admin/room")
    public Result<?> adminUpdateRoom(@RequestBody Room room){
        return roomService.adminUpdateRoom(room);
    }

    //admin delete room
//    @DeleteMapping("/admin/room/{uid}")
//    public Result<?> adminDeleteRoom(@PathVariable Integer uid){
//        roomMapper.deleteById(uid);
//        return Result.success();
//    }
    //admin delete room 重构代码
    @DeleteMapping("/admin/room/{uid}")
    public Result<?> adminDeleteRoom(@PathVariable Integer uid){
        return roomService.adminDeleteRoom(uid);
    }

//    //get user num
//    @GetMapping("/studyHall/getOnlineUserNum")
//    public Result<?> getUserNum(){
//        int userOnlineNum = userMapper.getUserNum();
//        return Result.success(userOnlineNum);
//    }

    //get user num
    @GetMapping("/studyHall/getOnlineUserNum")
    public Result<?> getUserNum(){
        return roomService.getUserNum();
    }

    //user in studyHall add room
    @PostMapping("/studyHall")
    public Result<?> userSaveRoom(@RequestBody Room room){
        return roomService.userSaveRoom(room);
    }

    //user in studyHall search room
    @GetMapping("/studyHall")
    public Result<?> userFindPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam(defaultValue = "") String search){
        return roomService.userFindPage(pageNum, pageSize, search);
    }

    //update user info when user enters room
    @PostMapping("/studyHall/enterRoom/updateUserInfo")
    public Result<?> userEnterRoomUpdateUserInfo(@RequestParam Integer uid,
                                                 @RequestParam Integer rid){
        return roomService.userEnterRoomUpdateUserInfo(uid, rid);
    }

    //update room info when user in studyhall enter room
    @PostMapping("/studyHall/enterRoom/updateRoomInfo")
    public Result<?> userEnterRoomUpdateRoomInfo(@RequestParam Integer uid,
                                                 @RequestParam Integer rid){
        return roomService.userEnterRoomUpdateRoomInfo(uid ,rid);
    }

    //user in studyHall enter room
    @PostMapping("/studyHall/checkpsw/{rid}/{psw}/{uid}")
    public Result<?> userEnterRoom(@PathVariable Integer rid,
                                   @PathVariable String psw,
                                   @PathVariable Integer uid) {
        return roomService.userEnterRoom(rid, psw, uid);
    }

    //get user and room by uid and rid
    @GetMapping("/rooms/getUserInfo/id")
    public Result<?> getUserInfoById(@RequestParam Integer uid){
        return roomService.getUserInfoById(uid);
    }
    @GetMapping("/rooms/getUserInfo/name")
    public Result<?> getUserInfoByName(@RequestParam String userName){
        return roomService.getUserInfoByName(userName);
    }

    //get userList by rid
    //未测试
    @GetMapping("/admin/room/getUserList")
    public Result<?> getUserListByRid(@RequestParam Integer rid){
        return roomService.getUserListByRid(rid);
    }

    //user in study room exit room (王乐宣使用）
    @PutMapping("/rooms/exit")
    public Result<?> userExitRoom(@RequestParam Integer uid,
                                  @RequestParam Integer rid){
        return roomService.userExitRoom(uid, rid);
    }

    //获取房间信息
    @GetMapping("/rooms/getRoomInfo")
    public  Result<?> getRoomInfo(@RequestParam Integer rid)
    {
        return roomService.getRoomInfo(rid);
    }

    @PutMapping("/rooms/changeRoomInfo")
    public Result<?> userEditRoomInfo(@RequestBody Room room)
    {
        return roomService.userEditRoomInfo(room);
    }
    @DeleteMapping("/rooms/deleteRoom/{rid}")
    public Result<?> deleteRoom(@PathVariable Integer rid)
    {
        return roomService.deleteRoom(rid);
    }
}
