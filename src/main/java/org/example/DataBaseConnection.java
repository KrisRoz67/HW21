package org.example;

import lombok.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Data
public final class DataBaseConnection {
    private static DataBaseConnection INSTANCE;
    private Connection connection;

    private DataBaseConnection(){
        this.connection= connect();

    }

    public static DataBaseConnection getINSTANCE(){
        if(INSTANCE ==null){
            INSTANCE = new DataBaseConnection();
        }
        return INSTANCE;
    }

    private Connection connect(){
        try {
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/docker_db",
                    "docker_user","docker_pw");
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void closeConnection(){
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
