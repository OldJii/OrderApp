package com.oldjii.ordering.db.bean;

public class Area {
    public int city_id;
    public int area_id;
    public int code;
    public String name;

    public Area(int area_id, int code, String name) {
        this.area_id = area_id;
        this.code = code;
        this.name = name;
    }
}
