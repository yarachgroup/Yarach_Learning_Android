package com.yarach.learning;

public class AboutCourseData {
    public String error;
    public String error_code;
    public String course_id;
    public String course_title;
    public String course_about;
    public String course_teacher_name;
    public String course_teacher_id;
    public String course_teacher_about;
    public String course_teacher_youtube_link;
    public String course_certificate_about;
    public String course_about_continue;
    public String course_about_category;
    public String course_about_level;
    public String course_about_pay_period;
    public String course_cost;
    public String course_about_lang;
    public String course_lessons_list;
    public String course_lessons_list_withtitles;

    @Override
    public String toString() {
        return "courseInfo{" +
                "error=" + error +
                ", error_code=" + error_code +
                ", course_id=" + course_id +
                ", course_title=" + course_title +
                ", course_about=" + course_about +
                ", course_teacher_id=" + course_teacher_id +
                ", course_teacher_name=" + course_teacher_name +
                ", course_teacher_about=" + course_teacher_about +
                ", course_teacher_youtube_link=" + course_teacher_youtube_link +
                ", course_about_continue=" + course_about_continue +
                ", course_about_category=" + course_about_category +
                ", course_about_level=" + course_about_level +
                ", course_about_lang=" + course_about_lang +
                ", course_about_pay_period=" + course_about_pay_period +
                ", course_cost=" + course_cost +
                ", course_certificate_about=" + course_certificate_about +
                ", course_lessons_list=" + course_lessons_list +
                ", course_lessons_list_withtitles=" + course_lessons_list_withtitles +
                '}';
    }
}
