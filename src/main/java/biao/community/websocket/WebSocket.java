package biao.community.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * @author WuJunHui
 * @date 2023/5/5 18:10
 */
@Component
@ServerEndpoint("/websocket/{userId}")
public class WebSocket {


    private String userId;

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId) {
        try {
            System.out.println("【webSocket连接成功】," + userId);
            this.userId=userId;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        try {
            System.out.println("【webSocket退出成功】,"+userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @OnError
    public void onError(Session session,Throwable throwable) {

    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println(userId + "发送消息：" + message);

    }

}
