package com.oldjii.ordering.db.bean;

public class Province {
    public int province_id;
    public int code;
    public String name;

    public Province(int province_id, int code, String name) {
        this.province_id = province_id;
        this.code = code;
        this.name = name;
    }
}
