package edu.school21.chat.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Program {
    public static void main(String[] args){
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ex00", "pmaryjo", "");
            System.out.println("Connected to database!");

            ResultSet resultSet = connection.createStatement().executeQuery("select * from chatroom");
            while (resultSet.next()){
                System.out.print(resultSet.getInt("id") + " ");
                System.out.print(resultSet.getString("name") + " ");
                System.out.println(resultSet.getInt("owner"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.print("Error: ");
            System.err.println(e.getMessage());
        }
    }
}
