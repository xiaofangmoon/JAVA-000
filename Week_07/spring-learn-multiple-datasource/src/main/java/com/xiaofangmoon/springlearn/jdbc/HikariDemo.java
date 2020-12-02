package com.xiaofangmoon.springlearn.jdbc;

import java.sql.SQLException;

public class HikariDemo {


    public static void main(String[] args) throws SQLException {
        /*//直接初始化HikariConfig**
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/xiaofang");
        config.setUsername("root");
        config.setPassword("Fang#123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        HikariDataSource ds = new HikariDataSource(config);


        Connection connection = ds.getConnection();

        Statement statement = connection.createStatement();


        ResultSet resultSet = statement.executeQuery("show databases ");
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1));

        }

        System.out.println(connection);
        connection.close();*/




    }
}
