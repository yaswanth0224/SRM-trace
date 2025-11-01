package com.srmtrace.model;

public class Item {
    public int id;
    public String title;
    public String description;
    public String type; // "lost" or "found"
    public String imagePath;
    public String qrCode;
    public String location;
    public int userId;
    public boolean verified;
    public String createdAt;

    public Item() {}
}
