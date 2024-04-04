package com.example.cookingrecipesrest.db;

import com.example.cookingrecipesrest.PropertiesUtil;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManagerImpl implements ConnectionManager{

    private static final String PASSWORD_KEY = "db.password";

    private static final String USERNAME_KEY = "db.username";

    private static final String URL_KEY = "db.url";
    private static final HikariDataSource dataSource;

    static {
        loadDriver();
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(PropertiesUtil.get(URL_KEY));
        dataSource.setUsername(PropertiesUtil.get(USERNAME_KEY));
        dataSource.setPassword(PropertiesUtil.get(PASSWORD_KEY));
        dataSource.setMinimumIdle(5);
        dataSource.setMaximumPoolSize(15);
    }

    private static void loadDriver(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public  Connection getConnection() throws SQLException{
        return dataSource.getConnection();
    }
}
