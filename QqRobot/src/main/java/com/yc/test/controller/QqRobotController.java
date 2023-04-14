package com.yc.test.controller;

import com.yc.test.Util.HttpRequestUtil;
import com.yc.test.Util.MessageUtil;
import com.yc.test.Util.WeatherUtil;
import com.yc.test.service.QqRobotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author yucan
 * @date 2022/11/11 16:52
 */
@RestController
@Slf4j
public class QqRobotController {
    @Resource
    private QqRobotService robotService;

    @PostMapping
    public void QqRobotEven(HttpServletRequest request) {
        robotService.QqRobotEvenHandle(request);
    }

    @PostMapping("/test")
    public void messageTest() {
        //小情话
        String littleLoveStory = HttpRequestUtil.doGet("https://api.lovelive.tools/api/SweetNothings");
        //未来五天天气
        String weatherMessage = WeatherUtil.getWeather("武汉");
        MessageUtil.sendMessageToPerson("2477282297", "小情话：" + littleLoveStory + "" + weatherMessage);
    }
}