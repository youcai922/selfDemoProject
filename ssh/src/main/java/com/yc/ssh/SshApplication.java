package com.yc.ssh;

import com.yc.ssh.until.SshUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yucan
 * @date 2023/4/14 17:12
 */
@SpringBootApplication(scanBasePackages = "com.yc.ssh.**")
public class SshApplication {
    public static void main(String[] args) {
        try {
            System.out.println(SshUtil.execRemoteCommand("10.12.137.8", "root", "123456", "docker ps", 1000L));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}