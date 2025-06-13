/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oop.pkgfinal.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/railway_reservation";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static int savePassenger(Passenger p) throws SQLException {
        try (Connection conn = connect()) {
            String query = "INSERT INTO passengers (name, email, phone) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, p.getName());
            stmt.setString(2, p.getEmail());
            stmt.setString(3, p.getPhone());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    public static void saveReservation(int passengerId, int trainNumber) throws SQLException {
        try (Connection conn = connect()) {
            String query = "INSERT INTO reservations (passenger_id, train_number) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, passengerId);
            stmt.setInt(2, trainNumber);
            stmt.executeUpdate();
        }
    }
}
