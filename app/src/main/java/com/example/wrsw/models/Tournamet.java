package com.example.wrsw.models;

public class Tournamet {
    private String _id;
    private String name;
    private  String banner;
    private  String created;
    private  String status;

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getBanner() {
        return banner;
    }

    public String getDate() {
        return created;
    }

    public String getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
