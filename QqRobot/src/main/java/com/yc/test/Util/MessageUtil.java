package com.yc.test.Util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yucan
 * @date 2022/12/21 10:22
 */
@Slf4j
public class MessageUtil {
    public static void sendMessageToPerson(String userId, String message) {
        String url = "http://127.0.0.1:5700/send_private_msg?user_id=" + userId + "&message=" + message;
        String result = HttpRequestUtil.doGet(url);
        log.info("发送成功:==>{}", result);
    }

    public static void sendMessageToGroup(String groupId, String message) {
        String url = "http://127.0.0.1:5700/send_group_msg?group_id=" + groupId + "&message=" + message;
        String result = HttpRequestUtil.doGet(url);
        log.info("发送成功:==>{}", result);
    }
}
