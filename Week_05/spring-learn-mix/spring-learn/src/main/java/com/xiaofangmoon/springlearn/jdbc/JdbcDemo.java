package com.xiaofangmoon.springlearn.jdbc;

import java.sql.*;

public class JdbcDemo {

    /**
     * MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
     */
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/xiaofang?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    /**
     * MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
     */
    static final String JDBC_DRIVER8 = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL8 = "jdbc:mysql://localhost:3306/xiaofang?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    static final String USER = "root";
    static final String PASS = "Fang#123456";
    static Connection connection;

    static {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void getConnection() {

    }

    public void query() throws SQLException {
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("select * from person");
        while (rs.next()) {
            for (int i = 1; i <= 4; i++) {
                System.out.println(rs.getObject(i));
            }
        }
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        JdbcDemo jdbcDemo = new JdbcDemo();
        jdbcDemo.query();

    }
}
