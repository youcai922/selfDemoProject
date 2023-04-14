package com.yc.ssh.until;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author yucan
 * @date 2023/4/14 17:13
 */
public class SshUtil {
    private static Connection getConnection(String hostname, String username, String password) throws Exception {
        Connection conn = null;
        try {
            conn = new Connection(hostname);
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(username, password);
            if (!isAuthenticated) {
                throw new IOException("Authentication failed.");
            }
        } catch (Exception e) {
            throw new IOException("username or password error.");
        }
        return conn;
    }

    public static String execRemoteCommand(String hostname, String username, String password, String command, long timeout) throws Exception {
        Connection conn = getConnection(hostname, username, password);
        StringBuilder sb = new StringBuilder();
        Session session = null;
        try {
            session = conn.openSession();
            session.requestPTY("vt100", 80, 24, 640, 480, null);
            session.execCommand(command);
            InputStream stdout = new StreamGobbler(session.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            long start = System.currentTimeMillis();
            char[] arr = new char[512];
            int read;
            while (true) {
                read = br.read(arr, 0, arr.length);
                if (read < 0 || (System.currentTimeMillis() - start) > timeout * 1000) {
                    break;
                }
                sb.append(new String(arr, 0, read));
            }
        } finally {
            if (session != null) {
                session.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return sb.toString();
    }

    public static String execRemoteCommand(String hostname, String username, String password, String[] command, long timeout) throws Exception {
        Connection conn = getConnection(hostname, username, password);
        StringBuilder sb = new StringBuilder();
        Session session = null;
        try {
            for (String s : command) {
                session = conn.openSession();
                session.requestPTY("vt100", 80, 24, 640, 480, null);
                session.execCommand(s);
                InputStream stdout = new StreamGobbler(session.getStdout());
                BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                long start = System.currentTimeMillis();
                char[] arr = new char[512];
                int read;
                while (true) {
                    read = br.read(arr, 0, arr.length);
                    if (read < 0 || (System.currentTimeMillis() - start) > timeout * 1000) {
                        break;
                    }
                    sb.append(new String(arr, 0, read));
                }
                session.close();
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return sb.toString();
    }
}