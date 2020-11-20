package com.xiaofangmoon.springlearn.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DruidDemo {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/xiaofang?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = "Fang#123456";

    public static void main(String[] args) throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();

        druidDataSource.setUrl(DB_URL);
        druidDataSource.setUsername(USER);
        druidDataSource.setPassword(PASS);

        DruidPooledConnection connection = druidDataSource.getConnection();


        PreparedStatement preparedStatement = connection.prepareStatement("show databases ");

        ResultSet resultSet = preparedStatement.executeQuery();


        while (resultSet.next()) {
            System.out.println(resultSet.getString(1));
        }
    }
}
