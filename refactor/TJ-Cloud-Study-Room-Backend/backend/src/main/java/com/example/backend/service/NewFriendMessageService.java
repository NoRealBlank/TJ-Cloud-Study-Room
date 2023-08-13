package com.example.backend.service;

import com.example.backend.common.Result;
import com.example.backend.entity.NewFriendMessage;
import com.example.backend.mapper.NewFriendMessageMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Service
public class NewFriendMessageService {
    @Resource
    NewFriendMessageMapper newFriendMessageMapper;

    public Result<?> addFriend(@RequestParam Integer uid, @RequestParam Integer fid){
        NewFriendMessage newFriendMessage = new NewFriendMessage();
        newFriendMessage.setSenderId(uid);
        newFriendMessage.setReceiverId(fid);
        newFriendMessage.setSendTime(new Date());
        if(newFriendMessage.getSenderId() == null)
            return Result.error("-1", "缺少信息发送者id");
        if(newFriendMessage.getReceiverId() == null)
            return Result.error("-1", "缺少信息接受者id");
        newFriendMessage.setState(0);

        newFriendMessageMapper.insert(newFriendMessage);
        return Result.success();
    }
}
