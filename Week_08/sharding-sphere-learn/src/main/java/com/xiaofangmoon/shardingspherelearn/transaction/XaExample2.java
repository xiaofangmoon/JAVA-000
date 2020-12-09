package com.xiaofangmoon.shardingspherelearn.transaction;


import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlXid;

import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class XaExample2 {

    public static void main(String[] args) throws XAException, SQLException, ClassNotFoundException {
        Xa();
    }


    private static void Xa() throws ClassNotFoundException, SQLException, XAException {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/xiaofang?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&autoReconnectForPools=true&useSSL=false&useAffectedRows=true";
        String username = "root";
        String password = "Fang#123456";

        //1. 加载驱动
        Class.forName(driver);
        //2. 获取连接
        MysqlXADataSource mysqlXADataSource = new MysqlXADataSource();
        mysqlXADataSource.setPassword(password);
        mysqlXADataSource.setUser(username);
        mysqlXADataSource.setUrl(url);

        //XaConnection
        XAConnection xaConnection = mysqlXADataSource.getXAConnection();

        //connection
        Connection connection = xaConnection.getConnection();


       /* Connection connection = DriverManager.getConnection(url, username, password);
        MysqlXAConnection xaConnection = new MysqlXAConnection((com.mysql.jdbc.Connection) connection, true);*/

        Statement statement = connection.createStatement();
        XAResource xaResource1 = xaConnection.getXAResource();
        Xid xid1 = new MysqlXid("x01".getBytes(), "b01".getBytes(), 1);
        try {

            xaResource1.start(xid1, XAResource.TMNOFLAGS);
            int update1 = statement.executeUpdate("update person set age = 11  where id = 100;");

            xaResource1.end(xid1, XAResource.TMSUCCESS);
            int ret1 = xaResource1.prepare(xid1);
            // 两阶段提交协议第二阶段
            if (XAResource.XA_OK == ret1) {
                xaResource1.commit(xid1, false);
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
