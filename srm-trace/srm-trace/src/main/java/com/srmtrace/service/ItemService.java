package com.srmtrace.service;

import com.srmtrace.db.DB;
import com.srmtrace.model.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemService {
    public static boolean add(Item it) {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(
                "INSERT INTO items(title,description,type,image_path,qr_code,location,user_id,verified) VALUES(?,?,?,?,?,?,?,?)"
             )) {
            ps.setString(1, it.title);
            ps.setString(2, it.description);
            ps.setString(3, it.type);
            ps.setString(4, it.imagePath);
            ps.setString(5, it.qrCode);
            ps.setString(6, it.location);
            ps.setInt(7, it.userId);
            ps.setInt(8, it.verified ? 1 : 0);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static List<Item> all() {
        List<Item> list = new ArrayList<>();
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM items ORDER BY created_at DESC")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Item it = new Item();
                it.id = rs.getInt("id");
                it.title = rs.getString("title");
                it.description = rs.getString("description");
                it.type = rs.getString("type");
                it.imagePath = rs.getString("image_path");
                it.qrCode = rs.getString("qr_code");
                it.location = rs.getString("location");
                it.userId = rs.getInt("user_id");
                it.verified = rs.getInt("verified") == 1;
                it.createdAt = rs.getString("created_at");
                list.add(it);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static boolean verify(int itemId, boolean set) {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE items SET verified=? WHERE id=?")) {
            ps.setInt(1, set ? 1 : 0);
            ps.setInt(2, itemId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static boolean delete(int itemId) {
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM items WHERE id=?")) {
            ps.setInt(1, itemId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}
