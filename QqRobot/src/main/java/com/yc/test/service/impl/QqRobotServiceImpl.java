package com.yc.test.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yc.test.Util.HttpRequestUtil;
import com.yc.test.service.QqRobotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author yucan
 * @date 2022/11/11 16:53
 */
@Slf4j
@Service
public class QqRobotServiceImpl implements QqRobotService {

    @Override
    public void QqRobotEvenHandle(HttpServletRequest request) {
        //JSONObject
        JSONObject jsonParam = this.getJSONParam(request);
        log.info("接收参数为:{}",jsonParam.toString() !=null ? "SUCCESS" : "FALSE");
        if("message".equals(jsonParam.getString("post_type"))){
//            String message = jsonParam.getString("message");
//            if("你好".equals(message)){
//                // user_id 为QQ好友QQ号
//                String url = "http://127.0.0.1:5700/send_private_msg?user_id=2287360627&message=你好~";
//                String result = HttpRequestUtil.doGet(url);
//                log.info("发送成功:==>{}",result);
//            }
            if (jsonParam.getString("user_id").equals(2287360627L)){

            }
        }
    }

    public JSONObject getJSONParam(HttpServletRequest request){
        JSONObject jsonParam = null;
        try {
            // 获取输入流
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));

            // 数据写入Stringbuilder
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = streamReader.readLine()) != null) {
                sb.append(line);
            }
            jsonParam = JSONObject.parseObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonParam;
    }


}