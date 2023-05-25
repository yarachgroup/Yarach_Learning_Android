package com.yarach.learning;

public class MyCoursesData {
    public String error;
    public String error_code;
    public String courses_id;
    public String courses_name;

    @Override
    public String toString() {
        return "MyCoursesData{" +
                "error=" + error +
                ", error_code=" + error_code +
                ", courses_id=" + courses_id +
                ", courses_name=" + courses_name +
                '}';
    }
}
