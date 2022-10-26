package com.qihan.finalCloudDataSystem;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class ManipulateUsersRecord {
    // MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
//    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
//    static final String DB_URL = "jdbc:mysql://localhost:3306/RUNOOB";

    //    rm-2vc9w0ya601ov8r98so.mysql.cn-chengdu.rds.aliyuncs.com
    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://rm-2vc9w0ya601ov8r98so.mysql.cn-chengdu.rds.aliyuncs.com:3306/backup_system?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";


    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "software_development_manager";
    static final String PASS = "Zqh010408";

    public static String buildMD5(String plainText) {

        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        //System.out.println(md5code);
        return md5code;
    }

    public void search(String sql, boolean show_userid, boolean show_username, boolean show_userencryptedpassword
            , boolean show_useremail) {
        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("Connecting the database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 执行查询
            System.out.println("Instantiating the statement object...");
            stmt = conn.createStatement();


            ResultSet rs = stmt.executeQuery(sql);

            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索
                if (show_userid) {
                    String user_id = rs.getString("user_id");
                    System.out.print("普通用户名id: " + user_id);
                }
                if (show_username) {
                    String user_name = rs.getString("user_name");
                    System.out.print("普通用户姓名: " + user_name);
                }
                if (show_userencryptedpassword) {
                    String user_encryptedpassword = rs.getString("user_encryptedpassword");
                    System.out.print("s3存储桶普通用户加密后的密码: " + user_encryptedpassword);
                }
                if (show_useremail) {
                    String user_email = rs.getString("user_email");
                    System.out.print("s3存储桶普通用户邮箱: " + user_email);
                }


                //单个记录后换行
                System.out.print("\n");
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("The search transaction has ended!");
    }

    public void update(String sql) {
        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("Connecting the database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 执行查询
            System.out.println("Instantiating the statement object...");
            stmt = conn.createStatement();

            stmt.executeUpdate(sql);

            // 完成后关闭
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("The update transaction has ended!");
    }

}
