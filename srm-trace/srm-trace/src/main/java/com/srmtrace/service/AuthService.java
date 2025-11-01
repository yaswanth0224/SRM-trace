package com.srmtrace.service;

import com.srmtrace.db.DB;
import com.srmtrace.model.User;
import java.sql.*;

public class AuthService {
    public static User login(String username, String password) {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM users WHERE username=? AND password=?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.id = rs.getInt("id");
                u.username = rs.getString("username");
                u.password = rs.getString("password");
                u.displayName = rs.getString("display_name");
                u.reputation = rs.getInt("reputation");
                u.isAdmin = rs.getInt("is_admin") == 1;
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean register(String username, String password, String displayName) {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(
                "INSERT INTO users(username,password,display_name) VALUES(?,?,?)")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, displayName);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
