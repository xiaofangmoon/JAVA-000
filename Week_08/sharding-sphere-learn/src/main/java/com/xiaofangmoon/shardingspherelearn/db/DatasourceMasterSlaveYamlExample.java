package com.xiaofangmoon.shardingspherelearn.db;

import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;

public class DatasourceMasterSlaveYamlExample {

    public static DataSource getDatasource() throws IOException, SQLException {
        URL resource = DatasourceMasterSlaveYamlExample.class.getResource("/sharding-sphere-master-slave-db.yaml");
        String file = resource.getFile();
        File yamlFile = new File(file);
        System.out.println(file);
        DataSource dataSource = YamlShardingSphereDataSourceFactory.createDataSource(yamlFile);
        return dataSource;
    }

    public static void main(String[] args) throws SQLException, IOException {
        query();

//        insert();

    }

    private static void insert() throws IOException, SQLException {
        DataSource dataSource = getDatasource();
        Connection connection = dataSource.getConnection();

        Statement statement = connection.createStatement();

        boolean execute = statement.execute("INSERT INTO `ds0`.`t_order`(`order_id`, `user_id`, `status`) VALUES (1000, 3, NULL);");
    }

    private static void query1() throws IOException, SQLException {
        DataSource dataSource = getDatasource();
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT o.* FROM t_order o    WHERE o.user_id = 1");
        while (rs.next()) {
            // ...
            System.out.println(rs.getObject(1));

        }

    }

    private static void query() throws IOException, SQLException {
        DataSource dataSource = getDatasource();
        String sql = "SELECT o.* FROM t_order o    WHERE o.user_id=? AND o.order_id= ? ";

        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, 2);
        ps.setInt(2, 1000);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            // ...
            System.out.println(rs.getObject(1));

        }

    }
}
