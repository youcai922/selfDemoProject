package com.yc.test.task;

import com.yc.test.Util.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @author yucan
 * @date 2022/12/21 9:52
 */
@Slf4j
@Configuration
@EnableScheduling
public class sendMessageTask {
    //    @Scheduled(cron = "0 0/1 * * * ?")
    public void sendMessageTest() {
        String url = "http://127.0.0.1:5700/send_group_msg?group_id=751133583&message=定时发送消息测试，当前时间戳为：" + System.currentTimeMillis() + "每1分钟一次";
        String result = HttpRequestUtil.doGet(url);
        log.info("发送成功:==>{}", result);
    }
}
