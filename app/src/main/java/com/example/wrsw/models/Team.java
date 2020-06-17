package com.example.wrsw.models;

public class Team {
    private  String _id;
    private  String name;
    private String logo;
    private  Boolean status;

    public String getId() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }

    public Boolean getStatus() {
        return status;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
