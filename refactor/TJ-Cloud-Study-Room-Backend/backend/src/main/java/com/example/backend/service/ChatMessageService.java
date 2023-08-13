package com.example.backend.service;

import com.example.backend.common.Result;
import com.example.backend.entity.ChatMessage;
import com.example.backend.mapper.ChatMessageMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
@Service
public class ChatMessageService {
    @Resource
    ChatMessageMapper chatMessageMapper;
    //添加message
    public Result<?> sendMessage(@RequestBody ChatMessage chatMessage){
        chatMessage.setSendDate(new Date());
        chatMessageMapper.insert(chatMessage);
        return Result.success();
    }

    //获取用户的消息
    public Result<?> getMessage(@RequestParam String uname, @RequestParam String fname){
        List<ChatMessage> chatMessageList = chatMessageMapper.getMessageList(uname, fname);
        return Result.success(chatMessageList);
    }
}
