package com.qihan.finalCloudDataSystem;

import java.sql.*;

public class ManipulateTransfersRecord {

    //    rm-2vc9w0ya601ov8r98so.mysql.cn-chengdu.rds.aliyuncs.com
    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://rm-2vc9w0ya601ov8r98so.mysql.cn-chengdu.rds.aliyuncs.com:3306/backup_system?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";


    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "software_development_manager";
    static final String PASS = "Zqh010408";


    public void search(String sql, boolean show_cloudsrcuserid, boolean show_clouddesuserid, boolean show_s3name, boolean show_cloudtime) {
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
                // 通过字段检索 输出数据
                if (show_cloudsrcuserid) {
                    String cloud_srcuserid = rs.getString("cloud_srcuserid");
                    System.out.print("发送方用户id: " + cloud_srcuserid);
                }
                if (show_clouddesuserid) {
                    String cloud_desuserid = rs.getString("cloud_desuserid");
                    System.out.print("接收方用户id: " + cloud_desuserid);
                }
                if (show_s3name) {
                    String s3_name = rs.getString("s3_name");
                    System.out.print("s3存储桶名称: " + s3_name);
                }
                if (show_cloudtime) {
                    String cloud_time = rs.getString("cloud_time");
                    System.out.print("云端拷贝时间: " + cloud_time);
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
