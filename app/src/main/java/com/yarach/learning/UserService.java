package com.yarach.learning;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
    //requist to Yarach ID API
    @GET("/api/v1/verify.php")
    Call<CurrentData> getCurrentDataCall(@Query("userdata_email") String userEmail,
                                                           @Query("userdata_pwd") String userPassword);
    //requist to Yarach Learning API
    @GET("/api/v1/get_courses.php")
    Call<MyCoursesData> getMyCourses(@Query("userdata_email") String userEmail,
                                     @Query("data_action") String dataActionGetMyCourses);

    //requist to Yarach Learning API
    @GET("/api/v1/get_courses.php")
    Call<MyCoursesData> getCreatedCourses(@Query("userdata_email") String userEmail,
                                        @Query("data_action") String dataActionGetCreatedCourses);
    //requist to Yarach Learning API
    @GET("/api/v1/get_course_info.php")
    Call<AboutCourseData> getCourseInfo(@Query("data_course_id") String courseId,
                                        @Query("userdata_email") String userEmail,
                                          @Query("data_action") String dataActionGetCourseInfo);
    //requist to Yarach Learning API
    @GET("/api/v1/get_user_data.php")
    Call<User> getUserInfo(@Query("userdata_email") String userEmail);
}