package com.yarach.learning;

public class LessonData {
    public String error;
    public String error_code;
    public String course_id;
    public String lesson_id;
    public String lesson_vebinar;
    public String lesson_date;
    public String lesson_about;
    public String hometask_date;
    public String hometask_about;

    @Override
    public String toString() {
        return "courseInfo{" +
                "error=" + error +
                ", error_code=" + error_code +
                ", course_id=" + course_id +
                ", lesson_id=" + lesson_id +
                ", lesson_vebinar=" + lesson_vebinar +
                ", lesson_date=" + lesson_date +
                ", lesson_about=" + lesson_about +
                ", hometask_date=" + hometask_date +
                ", hometask_about=" + hometask_about +
                '}';
    }
}
