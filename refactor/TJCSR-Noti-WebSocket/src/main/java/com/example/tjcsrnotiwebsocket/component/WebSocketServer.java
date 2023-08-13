package com.example.tjcsrnotiwebsocket.component;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

// websocket服务
@ServerEndpoint(value = "/TjcsrWs/{username}")
@Component
public class WebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    // 记录当前在线连接数
    public static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    // 记录各房间内连接
    public static final Map<String, Map<String, Session>> roomSessionMap = new ConcurrentHashMap<>();

    // 连接建立成功调用的方法
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        sessionMap.put(username, session);
        log.info("有新用户加入，username={}, 当前在线人数为：{}", username, sessionMap.size());

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("msgType", "loginUpdate");
        sendAllMessage(jsonObject.toString());  // 后台发送消息给所有的客户端
    }

    // 连接关闭调用的方法
    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        sessionMap.remove(username);
        for (String roomname : roomSessionMap.keySet()) {
            if (roomSessionMap.get(roomname).containsKey(username)) {
                // 移除
                roomSessionMap.get(roomname).remove(username);

                // 更新
                JSONObject jsonObject = new JSONObject();
                jsonObject.set("msgType", "roomOut");
                jsonObject.set("fromObj", roomname);
                jsonObject.set("text", username + " Quit");

                sendRoomMessage(roomname, jsonObject.toString()); // 后台发送消息给房间内所有的客户端
                log.info("发送给房间roomname={}，消息：{}, 当前在线人数为：{}", roomname, jsonObject, roomSessionMap.get(roomname).size());
            }
        }
        log.info("有一连接关闭，移除username={}的用户session, 当前在线人数为：{}", username, sessionMap.size());
    }

    /**
     * 收到客户端消息后调用的方法
     * 后台收到客户端发送过来的消息
     * onMessage 是一个消息的中转站
     * 接受 浏览器端 socket.send 发送过来的 json数据
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("username") String username) {
        log.info("服务端收到用户username={}的消息:{}", username, message);

        JSONObject msg = JSONUtil.parseObj(message);
        String msgType = msg.getStr("msgType");
        String toObj = msg.getStr("toObj");
        String text = msg.getStr("text"); // 发送的消息文本  hello

        if (Objects.equals(msgType, "roomUpdate")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("msgType", "roomUpdate");

            sendAllMessage(jsonObject.toString());  // 后台发送消息给所有的客户端
            log.info("有房间{}，roomname={}，当前在线人数为：{}", text, toObj, sessionMap.size());
        }
        else if (Objects.equals(msgType, "roomIn")) {
            // 加入
            if (!roomSessionMap.containsKey(toObj)) {
                Map<String, Session> tr = new ConcurrentHashMap<String, Session>();
                roomSessionMap.put(toObj, tr);
            }
            roomSessionMap.get(toObj).put(username, session);

            JSONObject jsonObject = new JSONObject();
            jsonObject.set("msgType", msgType);
            jsonObject.set("fromObj", toObj);
            jsonObject.set("text", text);

            sendRoomMessage(toObj, jsonObject.toString()); // 后台发送消息给房间内所有的客户端
            log.info("发送给房间roomname={}，消息：{}，当前在线人数为：{}", toObj, jsonObject, roomSessionMap.get(toObj).size());
        }
        else if (Objects.equals(msgType, "roomOut")) {
            // 退出
            roomSessionMap.get(toObj).remove(username);

            JSONObject jsonObject = new JSONObject();
            jsonObject.set("msgType", msgType);
            jsonObject.set("fromObj", toObj);
            jsonObject.set("text", text);

            sendRoomMessage(toObj, jsonObject.toString()); // 后台发送消息给房间内所有的客户端
            log.info("发送给房间roomname={}，消息：{}，当前在线人数为：{}", toObj, jsonObject, roomSessionMap.get(toObj).size());
        }
        else if (Objects.equals(msgType, "friendUpdate")) {
            // 根据 to用户名来获取 session，再通过session发送消息文本
            Session toSession = sessionMap.get(toObj);
            if (toSession != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.set("msgType", msgType);
                jsonObject.set("fromObj", username);
                jsonObject.set("text", text);

                sendMessage(jsonObject.toString(), toSession);
                log.info("发送给用户username={}，消息：{}", toObj, jsonObject);
            } else {
                log.info("发送失败，未找到用户username={}的session", toObj);
            }
        }
        else if (Objects.equals(msgType, "roomChat")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("msgType", msgType);
            jsonObject.set("fromObj", username);
            jsonObject.set("text", text);

            sendRoomMessage(toObj, jsonObject.toString()); // 后台发送消息给房间内所有的客户端
            log.info("发送给房间={}，消息：{}", toObj, jsonObject);
        }
        else if (Objects.equals(msgType, "friendChat")) {
            // 根据 to用户名来获取 session，再通过session发送消息文本
            Session toSession = sessionMap.get(toObj);
            if (toSession != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.set("msgType", msgType);
                jsonObject.set("fromObj", username);
                jsonObject.set("text", text);

                this.sendMessage(jsonObject.toString(), toSession);
                log.info("发送给用户username={}，消息：{}", toObj, jsonObject);
            } else {
                log.info("发送失败，未找到用户username={}的session", toObj);
            }
        }
        else {
            log.info("发送失败，未找到msgType={}对应的处理操作", msgType);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    // 服务端发送消息给客户端
    private void sendMessage(String message, Session toSession) {
        try {
            log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message);
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }

    // 服务端发送消息给某房间内用户客户端
    private void sendRoomMessage(String roomname, String message) {
        try {
            for (Session session :  roomSessionMap.get(roomname).values()) {
                log.info("服务端给客户端[{}]发送消息{}", session.getId(), message);
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }

    // 服务端发送消息给所有客户端
    private void sendAllMessage(String message) {
        try {
            for (Session session : sessionMap.values()) {
                log.info("服务端给客户端[{}]发送消息{}", session.getId(), message);
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }
}