package com.yarach.learning;

public class User {
    public String error;
    public String error_code;
    public String name;
    public String id;

    @Override
    public String toString() {
        return "CurrentData{" +
                "error=" + error +
                ", error_code=" + error_code +
                ", name=" + name +
                ", id=" + id +
                '}';
    }
}
