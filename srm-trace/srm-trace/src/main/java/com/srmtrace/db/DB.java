package com.srmtrace.db;

import java.sql.*;

public class DB {
    private static final String URL = "jdbc:sqlite:data/srmtrace.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void init() {
        try (Connection c = getConnection();
             Statement s = c.createStatement()) {

            s.execute("PRAGMA foreign_keys = ON;");
            s.execute(
              "CREATE TABLE IF NOT EXISTS users (" +
              "id INTEGER PRIMARY KEY AUTOINCREMENT," +
              "username TEXT UNIQUE NOT NULL," +
              "password TEXT NOT NULL," +
              "display_name TEXT," +
              "reputation INTEGER DEFAULT 0," +
              "is_admin INTEGER DEFAULT 0" +
              ");"
            );

            s.execute(
              "CREATE TABLE IF NOT EXISTS items (" +
              "id INTEGER PRIMARY KEY AUTOINCREMENT," +
              "title TEXT NOT NULL," +
              "description TEXT," +
              "type TEXT NOT NULL," +
              "image_path TEXT," +
              "qr_code TEXT," +
              "location TEXT," +
              "user_id INTEGER," +
              "verified INTEGER DEFAULT 0," +
              "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
              "FOREIGN KEY(user_id) REFERENCES users(id)" +
              ");"
            );

            // create a default admin if not exists
            PreparedStatement ps = c.prepareStatement("SELECT id FROM users WHERE username = ?");
            ps.setString(1, "admin");
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                PreparedStatement ins = c.prepareStatement(
                  "INSERT INTO users(username,password,display_name,is_admin,reputation) VALUES(?,?,?,?,?)"
                );
                ins.setString(1,"admin");
                ins.setString(2,"admin"); // change in production
                ins.setString(3,"SRM Admin");
                ins.setInt(4,1);
                ins.setInt(5,100);
                ins.executeUpdate();
                ins.close();
            }
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
