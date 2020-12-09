package com.xiaofangmoon.shardingspherelearn.transaction;


import com.mysql.jdbc.jdbc2.optional.MysqlXAConnection;
import com.mysql.jdbc.jdbc2.optional.MysqlXid;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.sql.*;

public class XaExample1 {

    public static void main(String[] args) throws XAException, SQLException, ClassNotFoundException {
        Xa();
    }

    private static void testMysql() throws ClassNotFoundException, SQLException {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/xiaofang";
        String username = "root";
        String password = "Fang#123456";

        //1. 加载驱动
        Class.forName(driver);
        //2. 获取连接
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();


        ResultSet resultSet = statement.executeQuery("update person  set age = 16 where id = 1");

        while (resultSet.next()) {
            String database = resultSet.getString(1);

            System.out.println(database);
        }
    }

    private static void Xa() throws ClassNotFoundException, SQLException, XAException {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/xiaofang?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&autoReconnectForPools=true&useSSL=false&useAffectedRows=true";
        String username = "root";
        String password = "Fang#123456";

        //1. 加载驱动
        Class.forName(driver);
        //2. 获取连接

        Connection conn = DriverManager.getConnection(url, username, password);

        MysqlXAConnection xaConnection = new MysqlXAConnection((com.mysql.jdbc.Connection) conn, true);

        Statement statement = conn.createStatement();
        XAResource xaResource1 = xaConnection.getXAResource();
        int ret1 = 0;
        Xid xid1 = new MysqlXid("g12345".getBytes(), "b00001".getBytes(), 1);
        try {

            xaResource1.start(xid1, XAResource.TMNOFLAGS);
            int update1 = statement.executeUpdate("update person set age = 14 where id = 12;");

            xaResource1.end(xid1, XAResource.TMSUCCESS);
            ret1 = xaResource1.prepare(xid1);
            // 两阶段提交协议第二阶段
            if (XAResource.XA_OK == ret1) {
//                xaResource1.commit(xid1, false);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (XAException e) {
            e.printStackTrace();
            xaResource1.rollback(xid1);
            e.printStackTrace();
        }
    }
}
